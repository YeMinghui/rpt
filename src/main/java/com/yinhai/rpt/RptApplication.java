package com.yinhai.rpt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yinhai.rpt.mapper")
public class RptApplication {

    public static void main(String[] args) {
        SpringApplication.run(RptApplication.class, args);
    }

}
