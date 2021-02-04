package com.yinhai.rpt.controller;

import com.yinhai.rpt.mapper.MapperDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yinhai.rpt.util.Process;

@RestController
public class IndexController {

    @Autowired
    MapperDAO dao;

    @Autowired
    Process process;

    @GetMapping("/test")
    public void test() {
//        System.out.println(dao.version());
//        List<Map> hi1 = dao.select("HI1", "2020");
//        System.out.println(hi1);
        process.ydjy();
    }

}
