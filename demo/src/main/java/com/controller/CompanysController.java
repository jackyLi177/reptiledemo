package com.controller;

import com.service.CompanysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

/**
 * @Author : liyongjie
 * @Date : 2018/5/31 0031
 */
@Controller
public class CompanysController {

    @Autowired
    private CompanysService service;

    @GetMapping("/start")
    public String doCraw() throws Exception {
        StringBuffer url = new StringBuffer();
        for(int i=1;i<100;i++){
            url.setLength(0);
            url.append("https://fj.tianyancha.com/search/p"+i+"?key=%E5%8D%B7%E7%83%9F");
            service.doGet(url.toString());
        }
        return "success";
    }


    @GetMapping("/getSong")
    public String getSong() throws IOException {
        //String url = "http://win.web.nf03.sycdn.kuwo.cn/8d6ec3bd7ba53f059a23141e0fede6f7/5b0fa155/resource/a1/71/0/474540545.aac";
        String url = "http://antiserver.kuwo.cn/anti.s?format=mp3&rid=MUSIC_41378649&type=convert_url&response=res";
        //String url = "http://www.kuwo.cn/yinyue/41378649?catalog=yueku2016";
        service.getSong(url);
        return "";
    }
}
