package com.tools.commonservice;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonServiceApplication.class)
@AutoConfigureMockMvc
//@WebAppConfiguration
@TestPropertySource(locations = "classpath:application.yml")
public class BasicTest {


}
