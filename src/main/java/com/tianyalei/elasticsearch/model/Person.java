package com.tianyalei.elasticsearch.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
 
import java.io.Serializable;
 
/**
 * model类
 */
@Document(indexName="elastic_search_project",type="person",indexStoreType="fs",shards=5,replicas=1,refreshInterval="-1")
public class Person implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3971438321456377333L;

	@Id
    private int id;
 
    private String name;
 
    private String phone;
 
    /**
     * 地理位置经纬度
     * lat纬度，lon经度 "40.715,-74.011"
     * 如果用数组则相反[-73.983, 40.719]
     */
    @GeoPointField
    private String address;
 
    public int getId() {
        return id;
    }
 
    public void setId(int id) {
        this.id = id;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public String getPhone() {
        return phone;
    }
 
    public void setPhone(String phone) {
        this.phone = phone;
    }
 
    public String getAddress() {
        return address;
    }
 
    public void setAddress(String address) {
        this.address = address;
    }
}

