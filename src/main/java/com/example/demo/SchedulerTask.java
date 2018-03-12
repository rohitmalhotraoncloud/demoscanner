package com.example.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.Callable;

@Component
public class SchedulerTask implements Callable<UrlWrapper> {

    @Autowired
    SitedetailRepository siteDetailRepository;

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public UrlWrapper call() {
        StatusCode statusCode = StatusCode.PENDING;
        try {
            Document doc = JsoupUtil.parseUrl(url);
            Sitedetail sd = new Sitedetail();
            sd.setSourcedata(doc.html());
            sd.setUrl(url);
            siteDetailRepository.save(sd);
        } catch (Exception ex) {
            System.out.println(url + " : " + ex.getMessage());
        }
        //System.out.println("Current Thread : " + Thread.currentThread().getName());
        return new UrlWrapper(url, statusCode);
    }

}