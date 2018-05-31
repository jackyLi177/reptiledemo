package com.service;

import com.common.SslUtil;
import com.mapper.CompanysMapper;
import com.model.Companys;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URI;
import java.util.Random;

/**
 * @Author : liyongjie
 * @Date : 2018/5/31 0031
 */

@Slf4j
@Service
public class CompanysService {

    private static StringBuffer result = new StringBuffer();

    @Autowired
    private CompanysMapper mapper;

    public int save(Companys record) {
        return mapper.insertSelective(record);
    }

    public void doGet(String url) throws Exception {
        if (url.matches("^https")) {
            SslUtil.ignoreSsl();
        }

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet h = new HttpGet(url);
        h.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        h.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
        h.setHeader("Connection", "keep-alive");
        h.setHeader("Cache-Control", "max-age=0");
        h.setHeader("Cookie", "TYCID=3c3eb4e0630c11e88ad9abef70c80c5b; undefined=3c3eb4e0630c11e88ad9abef70c80c5b; ssuid=3065497743; aliyungf_tc=AQAAABJScyqO+AkAZ6EpOxfKIC4HhYoj; csrfToken=l_TtuuXQ-QAXR9F9qXCkfA4R; __guid=117699678.1112764807559075000.1527576590227.3499; RTYCID=810980aaab2843768d8a98c9a3bf14e6; tyc-user-info=%257B%2522new%2522%253A%25221%2522%252C%2522token%2522%253A%2522eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxODU2NTE2MzE1MiIsImlhdCI6MTUyNzU3OTM2NywiZXhwIjoxNTQzMTMxMzY3fQ._aegBAdfgVHioL_ODWiA_FYXMqzd09sUj9QbQQjXzNJY1l3muR8ikNjUd_aQ7AkTaCVEKGsySrD9DiJhTRrxJA%2522%252C%2522integrity%2522%253A%25220%2525%2522%252C%2522state%2522%253A%25220%2522%252C%2522redPoint%2522%253A%25220%2522%252C%2522vipManager%2522%253A%25220%2522%252C%2522vnum%2522%253A%25220%2522%252C%2522onum%2522%253A%25220%2522%252C%2522mobile%2522%253A%252218565163152%2522%257D; auth_token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxODU2NTE2MzE1MiIsImlhdCI6MTUyNzU3OTM2NywiZXhwIjoxNTQzMTMxMzY3fQ._aegBAdfgVHioL_ODWiA_FYXMqzd09sUj9QbQQjXzNJY1l3muR8ikNjUd_aQ7AkTaCVEKGsySrD9DiJhTRrxJA; bannerFlag=true; monitor_count=11; Hm_lvt_e92c8d65d92d534b0fc290df538b4758=1527576370,1527579145,1527581100; Hm_lpvt_e92c8d65d92d534b0fc290df538b4758=1527581111");
        h.setHeader("Host", "fj.tianyancha.com");
        h.setHeader("Referer", "https://www.tianyancha.com/search?key=%E5%8D%B7%E7%83%9F");
        h.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");

        SslUtil.ignoreSsl();
        Thread.sleep(5000 + new Random().nextInt(10) * 1000);
        HttpResponse response = httpClient.execute(h);
        log.error("请求响应码1：" + response.getStatusLine().getStatusCode());
        log.error("请求体1：" + response.getEntity().getContentLength());
        //log.error(InputStream2String(response.getEntity().getContent(),"UTF-8"));
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream in = entity.getContent();
            String html = InputStream2String(in, "UTF-8");
            Document document = Jsoup.parse(html.toString());
            Elements elements = document.select("div[class=search_result_single search-2017 pb25 pt25 pl30 pr30]");
            StringBuffer sb = new StringBuffer();
            for (Element e : elements) {
                int length = result.length();

                try {
                    Element name = e.select("a[class=query_name sv-search-company f18 in-block vertical-middle]").first();
                    Element owner = e.select("a[class=legalPersonName hover_underline]").first();
                    Element tel = e.select("span[class=overflow-width over-hide vertical-bottom in-block]").first();
                    String href = name.attr("href");
                    result.append(name.text() + "@" + owner.text() + "@" + tel.text() + "@");
                    System.out.println();

                    h.setURI(new URI(href));
                    HttpResponse response1 = httpClient.execute(h);
                    log.error("请求响应码2：" + response1.getStatusLine().getStatusCode());
                    log.error("响应体2：" + response1.getEntity().getContentLength());
                    HttpEntity httpEntity = response1.getEntity();
                    InputStream inputStream = httpEntity.getContent();
                    String details_html = InputStream2String(inputStream, "UTF-8");
                    document = Jsoup.parse(details_html.toString());
                    Element table = document.select("table[class=table companyInfo-table f14]").first();
                    Element address = table.select("td[colspan=4]").first();
                    Element orgCode = table.select("tbody tr td").get(3);
                    Element code = table.select("tbody tr").get(1).child(1);
                    Element business = table.select("span[class=js-full-container ]").first();
                    result.append(address.text() + "@" + orgCode.text() + "@" + code.text() + "@" + business.text() + "#");
                    mapper.insertSelective(new Companys(name.text(), tel.text(), orgCode.text(), code.text(), address.text(), business.text(), owner.text()));
                } catch (Exception e1) {
                    log.error("----->" + e1.getMessage());
                    log.error("该公司资料不全！！！");
                    result.setLength(length);
                    continue;
                }
            }
        }
        log.error("写入文件--------------------》");
        FileOutputStream fos = new FileOutputStream("e:\\result.txt");
        fos.write(result.toString().getBytes());
        fos.close();
        h.reset();
    }

    public String InputStream2String(InputStream in, String charset) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
        StringBuffer sb = new StringBuffer();
        String line = "";
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    public String getSong(String url) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
//        httpGet.setHeader("Accept","*/*");
//        httpGet.setHeader("Accept-Language","zhttpGet-CN,zhttpGet;q=0.9");
//        httpGet.setHeader("chrome-proxy","frfr");
//        httpGet.setHeader("Connection","keep-alive");
//        httpGet.setHeader("CachttpGete-Control","max-age=0");
//        httpGet.setHeader("Cookie","ad_dist=%25B9%25E3%25B6%25AB; Hm_lvt_cdb524f42f0ce19b169a8071123a4797=1527749870,1527750229; Hm_lpvt_cdb524f42f0ce19b169a8071123a4797=1527750874");
//        httpGet.setHeader("Host","win.web.nf03.sycdn.kuwo.cn");
//        httpGet.setHeader("Referer","httpGetttp://www.kuwo.cn/yinyue/40926193?catalog=yueku2016");
//        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) ChttpGetrome/66.0.3359.139 Safari/537.36");
//        httpGet.setHeader("Range","bytes=0-");

//        httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//        httpGet.setHeader("Accept-Language", "zh-CN,zhttpGet;q=0.9");
//        httpGet.setHeader("Connection", "keep-alive");
//        httpGet.setHeader("Cache-Control", "max-age=0");
//        httpGet.setHeader("Cookie", "__guid=112476674.2483117269951210500.1524118386849.2087; gtoken=9fkI50WwZNAr; flag=true; ad_dist=%25B9%25E3%25B6%25AB; JSESSIONID=1ozh50leqraek1ck0vegs9cgp4; monitor_count=15; reqid=987a4ee1X4de4X483bXb31bX7c6c273e2588; gid=aef64f8c-3781-48eb-a9e7-fc3f67969de8; Hm_lvt_cdb524f42f0ce19b169a8071123a4797=1527749870,1527750229; Hm_lpvt_cdb524f42f0ce19b169a8071123a4797=1527753819");
//        httpGet.setHeader("Host", "www.kuwo.cn");
//        httpGet.setHeader("Referer", "http://www.kuwo.cn/bang/index");
//        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) ChttpGetrome/66.0.3359.139 Safari/537.36");
//        httpGet.setHeader("Upgrade-Insecure-Requests", "1");

        httpGet.setHeader("Accept", "*/*");
        httpGet.setHeader("Accept-Language", "zh-CN,zhttpGet;q=0.9");
        httpGet.setHeader("chrome-proxy","frfr");
        httpGet.setHeader("Connection", "keep-alive");
        httpGet.setHeader("Cache-Control", "max-age=0");
        httpGet.setHeader("Cookie", "ad_dist=%25B9%25E3%25B6%25AB; Hm_lvt_cdb524f42f0ce19b169a8071123a4797=1527749870,1527750229; Hm_lpvt_cdb524f42f0ce19b169a8071123a4797=1527755076");
        httpGet.setHeader("Host", "antiserver.kuwo.cn");
        httpGet.setHeader("Referer", "http://www.kuwo.cn/yinyue/41378649?catalog=yueku2016");
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) ChttpGetrome/66.0.3359.139 Safari/537.36");
        httpGet.setHeader("Range","bytes=0-");

        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream in = entity.getContent();
            String html = InputStream2String(in, "UTF-8");
            System.out.println("---->" + html);
//            File f = new File("E:\\a.mp3");
//            OutputStream out = new FileOutputStream(f);
//            byte[] song = new byte[1024];
//            int len = 0;
//            while((len = in.read(song))!=-1){
//                out.write(song,0,len);
//            }
        }
        return "song";
    }

}
