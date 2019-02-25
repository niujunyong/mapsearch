package com.tianyalei.elasticsearch.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class GpsBdMapUtils {
	private static String connectURL(String dest_url, String commString) {
		String rec_string = "";
		URL url = null;
		HttpURLConnection urlconn = null;
		OutputStream out = null;
		BufferedReader rd = null;
		try {
			url = new URL(dest_url);
			urlconn = (HttpURLConnection) url.openConnection();
			urlconn.setReadTimeout(1000 * 30);
			//urlconn.setRequestProperty("content-type", "text/html;charset=UTF-8");
			urlconn.setRequestMethod("POST");
			urlconn.setDoInput(true); 
			urlconn.setDoOutput(true);
			out = urlconn.getOutputStream();
			out.write(commString.getBytes("UTF-8"));
			out.flush(); 
			out.close();
			rd = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
			StringBuffer sb = new StringBuffer();
			int ch;
			while ((ch = rd.read()) > -1)
				sb.append((char) ch);
			rec_string = sb.toString();
		} catch (Exception e) {
			return "";
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (urlconn != null) {
					urlconn.disconnect();
				}
				if (rd != null) {
					rd.close();
				}
			} catch (Exception e) {
			}
		}
		return rec_string;
	}
 
	public static void main(String[] args) {
		String coords = "116.334822,40.006025";
		String result =connectURL("http://api.map.baidu.com/geoconv/v1/?coords="+coords+"&from=1&to=5&output=json&ak=GtX8z7fuxhKd1nhfO11ENq3vrMZV88vY","");
		System.out.println(result);
		MapPoint mPoint = new MapPoint();
		mPoint.latitude = 40.006025;
		mPoint.longitude = 116.334822;
		mPoint = getBdPoint(mPoint);
		System.out.println(mPoint.longitude);
		System.out.println(mPoint.latitude);
	}
	
	public static MapPoint getBdPoint(MapPoint mPoint){
//		String coords = "116.334822,40.006025";
		String coords = mPoint.longitude + "," + mPoint.latitude;
		String result =connectURL("http://api.map.baidu.com/geoconv/v1/?coords="+coords+"&from=1&to=5&output=json&ak=GtX8z7fuxhKd1nhfO11ENq3vrMZV88vY","");
//		System.out.println(result);
		//{"status":0,"result":[{"x":116.34762079748623,"y":40.01301329842685}]}
		
		JSONObject jsonObject = JSONObject.fromObject(result);
		if(jsonObject.containsKey("status") && jsonObject.getInt("status") == 0 && jsonObject.containsKey("result")){
			JSONArray jarry = JSONArray.fromObject(jsonObject.get("result"));
			if(jarry.size()>=1){
				JSONObject json = JSONObject.fromObject(jarry.get(0));
				if(json.containsKey("x") && json.containsKey("y")){
					mPoint.longitude = json.getDouble("x");
					mPoint.latitude = json.getDouble("y");
				}
			}
		}
		
		return mPoint;
	}
}
