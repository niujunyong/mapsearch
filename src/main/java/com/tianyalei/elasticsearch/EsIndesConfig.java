package com.tianyalei.elasticsearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import com.tianyalei.elasticsearch.model.Person;

@Configuration
public class EsIndesConfig {
	private static final String PERSON_INDEX_NAME = "elastic_search_project";

	@Autowired
	ElasticsearchTemplate elasticsearchTemplate;
	
	@Bean
	public void EsIndesConfig1() {

		if (!elasticsearchTemplate.indexExists(PERSON_INDEX_NAME)) {
			elasticsearchTemplate.createIndex(PERSON_INDEX_NAME);
		}
		elasticsearchTemplate.putMapping(Person.class);

	}
}
