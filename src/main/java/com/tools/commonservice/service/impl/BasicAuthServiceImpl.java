package com.tools.commonservice.service.impl;

import com.tools.commonservice.config.AccessConfig;
import com.tools.commonservice.data.constant.ConstantsKey;
import com.tools.commonservice.service.BasicAuthService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Service
public class BasicAuthServiceImpl implements BasicAuthService {


    @Resource
    private AccessConfig accessConfig;

    @Resource
    private RestTemplate restTemplate;


    @Override
    public void sendBasicRequest() {

        HttpHeaders headers = new HttpHeaders();
        headers.add(ConstantsKey.HTTP_REQUEST_HEADER_AUTH, getBasicAuth());
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<String> httpEntity = new HttpEntity(null, headers);


        String url = "http://localhost:8080/job/getAllJobs";

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
            System.out.println("-------------" + response.getBody());
        } catch (Exception ignored) {

        }
    }

    private String getBasicAuth() {
        String auth = accessConfig.getAuthInfo().getUsername() + ":" + accessConfig.getAuthInfo().getPassword();
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encodedAuth);
    }


}
