package com.tianyalei.elasticsearch.controller;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tianyalei.elasticsearch.model.Person;
import com.tianyalei.elasticsearch.service.PersonService;
 
@RestController
public class PersonController {
    @Autowired
    PersonService personService;
 
    @GetMapping("/add")
    public Object add() {
        double lat = 36.331654;
        double lon = 113.299258;
        long st = System.currentTimeMillis();
        List<Person> personList = new ArrayList<>(900000);
        for (int i = 1000000; i < 1200000; i++) {
            double max = 0.001;
            double min = 0.000001;
            Random random = new Random();
            double s1 = random.nextDouble() % (max - min + 1) + max;
            double s2 = random.nextDouble() % (max - min + 1) + max;
            DecimalFormat df = new DecimalFormat("######0.000000");
            // System.out.println(s);
            String lons = df.format(s1 + lon);
            String lats = df.format(s2 + lat);
            Double dlon = Double.valueOf(lons);
            Double dlat = Double.valueOf(lats);
 
            Person person = new Person();
            person.setId(i);
            person.setName("名字" + i);
            person.setPhone("电话" + i);
            person.setAddress(dlat + "," + dlon);
 
            personList.add(person);
        }
        personService.bulkIndex(personList);
        
        //用下面的算法，操作方便，可很慢
//        personService.addMore(personList);
 
//        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.queryStringQuery("spring boot OR 书籍")).build();
//        List<Article> articles = elas、ticsearchTemplate.queryForList(se、archQuery, Article.class);
//        for (Article article : articles) {
//            System.out.println(article.toString());
//        }
 
        long et = System.currentTimeMillis();
        return "添加数据，所用时间=" + (et - st);
    }
 
    /**
     *
     geo_distance: 查找距离某个中心点距离在一定范围内的位置
     geo_bounding_box: 查找某个长方形区域内的位置
     geo_distance_range: 查找距离某个中心的距离在min和max之间的位置
     geo_polygon: 查找位于多边形内的地点。
     sort可以用来排序
     */
    @GetMapping("/query")
    public Map<String, Object> query() {
        double lat = 36.346014;
        double lon = 113.307417;
    	//36.337509,113.305632
//        double lat = 36.331654;
//        double lon = 113.299258;
        int range = 1000;
 
        Long nowTime = System.currentTimeMillis();
        List<Person> personList = personService.personList(lat, lon, range);
        String hs = "耗时：" + (System.currentTimeMillis() - nowTime);
        System.out.println(hs + "; 数据=" + personList.size());
        Map<String, Object> map = new HashMap<>();
        map.put("hs", hs);
        map.put("data", personList);
        return map;
    }
    
    @GetMapping("/queryByXy")
    public Map<String, Object> query(double lat, double lng) {
//        double lat = 36.346014;
//        double lon = 113.307417;
    	//36.337509,113.305632
//        double lat = 36.331654;
//        double lon = 113.299258;
        int range = 1000;
 
        Long nowTime = System.currentTimeMillis();
        List<Person> personList = personService.personList(lat, lng, range);
        String hs = "耗时：" + (System.currentTimeMillis() - nowTime);
        System.out.println(hs + "; 数据=" + personList.size());
        Map<String, Object> map = new HashMap<>();
        map.put("hs", hs);
        map.put("data", personList);
        return map;
    }
    
    @GetMapping("/update")
    public Map<String, Object> update() {
    	 Map<String, Object> map = new HashMap<>();
         map.put("hs", "aaa");
         
         double lat = 39.92991;
         double lon = 116.39561;
         DecimalFormat df = new DecimalFormat("######0.000000");
         // System.out.println(s);
         String lats = df.format(lat);
         String lons = df.format(lon);
         Double dlat = Double.valueOf(lats);
         Double dlon = Double.valueOf(lons);
         
         Person person = new Person();
         person.setId(255849);
         person.setAddress(dlat + "," + dlon);
         person.setName("名字138848");
         person.setPhone("电话138848");
         
//         personService.addOrUpdate(person);
         personService.personUpdate(person);
         
//         List<Person> pList = new ArrayList<>();
//         pList.add(person);
//         personService.insertOrUpdateTaskInfo(pList);
         
         return map;
    }
}
