package com.tools.commonservice.util;

import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HttpRequestUtil {

    public static <T> T doPost(String urlParam, String jsonStr, Class<T> tClass) {
        try {
            String result = doRequest(urlParam, jsonStr, HttpMethod.POST);
            return JsonUtils.read(result, tClass);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    public static <T> T doGet(String urlParam, Map<String, Object> param, Class<T> tClass) {
        try {
            String urlAdditional = getUrlAdditional(param);
            String result = doRequest(urlParam + urlAdditional, null, HttpMethod.GET);
            return JsonUtils.read(result, tClass);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    private static String doRequest(String urlParam, String jsonStr, HttpMethod httpMethod) {
        HttpURLConnection con = null;

        BufferedReader buffer = null;
        StringBuilder stringBuilder = null;

        try {
            URL url = new URL(urlParam);
            System.out.println(urlParam);
            //得到连接对象
            con = (HttpURLConnection) url.openConnection();
            //设置请求类型
            con.setRequestMethod(httpMethod.name());
            //设置请求需要返回的数据类型和字符集类型
            if(httpMethod == HttpMethod.POST) {
                con.setRequestProperty("Content-Type", "application/json;charset=GBK");
            }


            //允许写出
            con.setDoOutput(true);
            //允许读入
            con.setDoInput(true);
            //不使用缓存
            con.setUseCaches(false);

            if (jsonStr != null) {
                OutputStream outputStream = con.getOutputStream();
                outputStream.write(jsonStr.getBytes());
            }
            //得到响应码
            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                //得到响应流
                InputStream inputStream = con.getInputStream();
                //将响应流转换成字符串
                stringBuilder = new StringBuilder();
                String line;
                buffer = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
                while ((line = buffer.readLine()) != null) {
                    stringBuilder.append(line);
                }
                return stringBuilder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    private static String getUrlAdditional(Map<String, Object> map) {
        if (map == null || CollectionUtils.isEmpty(map)) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder("?");
        Set<String> keySet = map.keySet();
        try {
            for (String key : keySet) {
                Object obj = map.get(key);
                if(obj instanceof Collection) {
                    Collection collection = (Collection)obj;
                    Iterator iterator = collection.iterator();
                    while (iterator.hasNext()) {
                        stringBuilder.append(key).append("=")
                                .append(URLEncoder.encode(iterator.next().toString(), "utf-8"))
                                .append("&");
                    }
                }
                // 基础类型
                else if(isPrimitive(obj) || obj.getClass() == String.class) {
                    stringBuilder.append(key).append("=")
                            .append(URLEncoder.encode(map.get(key).toString(), "utf-8"))
                            .append("&");
                }
                else {
                    stringBuilder.append(key).append("=")
                            .append(URLEncoder.encode(JsonUtils.write(map.get(key)), "utf-8"))
                            .append("&");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
        return stringBuilder.substring(0, stringBuilder.lastIndexOf("&"));
    }


    private static boolean isPrimitive(Object obj) {
        try {
            return ((Class<?>) obj.getClass().getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

}

