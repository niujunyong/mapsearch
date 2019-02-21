package com.tianyalei.elasticsearch.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQueryBuilder;
import org.springframework.stereotype.Service;

import com.tianyalei.elasticsearch.model.Person;
import com.tianyalei.elasticsearch.repository.PersonRepository;
 
@Service
public class PersonService {
	
	
	
    @Autowired
    PersonRepository personRepository;
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;
 
    private static final String PERSON_INDEX_NAME = "elastic_search_project";
    private static final String PERSON_INDEX_TYPE = "person";
 
    
//    {
//    	if (!elasticsearchTemplate.indexExists(PERSON_INDEX_NAME)) {
//			elasticsearchTemplate.createIndex(PERSON_INDEX_NAME);
//		}
//    	elasticsearchTemplate.putMapping(Person.class);
////    	elasticsearchTemplate.putMapping(NewsInfo.class);
//    }
    
    public Person addOrUpdate(Person person) {
        return personRepository.save(person);
    }
    
    public void addMore(List<Person> personList){
    	for(Person person : personList){
    		personRepository.save(person);
    	}
    }
 
    public void bulkIndex(List<Person> personList) {
        int counter = 0;
        try {
//            if (!elasticsearchTemplate.indexExists(PERSON_INDEX_NAME)) {
//                elasticsearchTemplate.createIndex(PERSON_INDEX_NAME);
//            }
            List<IndexQuery> queries = new ArrayList<>();
            for (Person person : personList) {
                IndexQuery indexQuery = new IndexQuery();
                indexQuery.setId(person.getId() + "");
                indexQuery.setObject(person);
                indexQuery.setIndexName(PERSON_INDEX_NAME);
                indexQuery.setType(PERSON_INDEX_TYPE);
 
                //上面的那几步也可以使用IndexQueryBuilder来构建
                //IndexQuery index = new IndexQueryBuilder().withId(person.getId() + "").withObject(person).build();
 
                queries.add(indexQuery);
                if (counter % 500 == 0) {
                    elasticsearchTemplate.bulkIndex(queries);
                    queries.clear();
                    System.out.println("bulkIndex counter : " + counter);
                }
                counter++;
            }
            if (queries.size() > 0) {
                elasticsearchTemplate.bulkIndex(queries);
            }
            System.out.println("bulkIndex completed.");
        } catch (Exception e) {
            System.out.println("IndexerService.bulkIndex e;" + e.getMessage());
            throw e;
        }
    }


    /**
     * 查询
     * @param lat 坐标
     * @param lon 坐标
     * @param range 范围
     * @return
     */
    @SuppressWarnings("deprecation")
	public List<Person> personList( double lat ,double lon ,int range){
    	List<Person> list = new ArrayList<>();
    	
    	 //查询某经纬度100米范围内
        GeoDistanceQueryBuilder builder = QueryBuilders.geoDistanceQuery("address").point(lat, lon)
                .distance(range, DistanceUnit.METERS);
 
//        GeoDistanceSortBuilder sortBuilder = SortBuilders.fieldSort("address");
        GeoDistanceSortBuilder  sortBuilder = SortBuilders.geoDistanceSort("address", lat, lon).unit(DistanceUnit.METERS)
                .order(SortOrder.ASC);
 
        Pageable pageable = new PageRequest(0, 50);
 
        NativeSearchQueryBuilder builder1 = new NativeSearchQueryBuilder().withFilter(builder).withSort(sortBuilder).withPageable(pageable);
        SearchQuery searchQuery = builder1.build();
 
        //queryForList默认是分页，走的是queryForPage，默认10个
        list = elasticsearchTemplate.queryForList(searchQuery, Person.class);
    	
    	return list;
    }
    
    public void personUpdate(Person person) {
    	UpdateRequest updateRequest = new UpdateRequest();
        try {
            updateRequest.doc(XContentFactory.jsonBuilder()
                            .startObject()
                            .field("address", person.getAddress())//指定更新的字段和更新的值，可指定多个字段
                            .endObject());
            updateRequest.setRefreshPolicy(RefreshPolicy.IMMEDIATE);
        } catch (IOException e) {
//            logger.error(e.getMessage());
        	e.printStackTrace();
        }
        UpdateQuery updateQuery = new UpdateQueryBuilder()
                .withIndexName(PERSON_INDEX_NAME).withType(PERSON_INDEX_TYPE)//声明索引名和索引类型
                .withClass(Person.class)
                .withId(String.valueOf(person.getId()))//声明文档id
                .withUpdateRequest(updateRequest)
                .build();
    	
    	elasticsearchTemplate.update(updateQuery);
    	//下面更新能用，只是生效太慢
//    	List<IndexQuery> queries = new ArrayList<>();
//    	 IndexQuery indexQuery = new IndexQuery();
//         indexQuery.setId(person.getId() + "");
//         indexQuery.setObject(person);
//         indexQuery.setIndexName(PERSON_INDEX_NAME);
//         indexQuery.setType(PERSON_INDEX_TYPE);
//         queries.add(indexQuery);
//         elasticsearchTemplate.bulkIndex(queries);
	}
    
    /**
     * 没调试通过
     * @param taskInfoList
     * @return
     */
    public boolean insertOrUpdateTaskInfo(List<Person> taskInfoList) {
        List<IndexQuery> queries = new ArrayList<IndexQuery>();
        for (Person taskInfo : taskInfoList) {
            IndexQuery indexQuery = new IndexQueryBuilder().withId(taskInfo.getId()+"").withObject(taskInfo).build();
            indexQuery.setIndexName(PERSON_INDEX_NAME);
            indexQuery.setType(PERSON_INDEX_TYPE);
            queries.add(indexQuery);
        }
        elasticsearchTemplate.bulkIndex(queries);
        return true;
    }
}
