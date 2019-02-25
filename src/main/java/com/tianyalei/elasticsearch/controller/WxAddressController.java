package com.tianyalei.elasticsearch.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tianyalei.elasticsearch.utils.GpsBdMapUtils;
import com.tianyalei.elasticsearch.utils.MapPoint;

@RestController
@RequestMapping("wxAddress")
public class WxAddressController {
	@GetMapping("/add")
    public Map<String, Object> add(double latitude, double longitude) {
		Map<String, Object> map = new HashMap<>();
		
		map.put("statusCode", 200);
		System.out.println(longitude);
		System.out.println(latitude);
		MapPoint mPoint = new MapPoint();
		mPoint.latitude = latitude;
		mPoint.longitude = longitude;
		mPoint = GpsBdMapUtils.getBdPoint(mPoint);
		System.out.println(mPoint.longitude);
		System.out.println(mPoint.latitude);
		return map;
	}
}
