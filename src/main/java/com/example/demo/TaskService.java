package com.example.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class TaskService {//implements ApplicationContextAware {

    //private ApplicationContext applicationContext;

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    private SchedulerTask task;


//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        this.applicationContext = applicationContext;
//    }

    @Async
    //@Scheduled(cron = "0 15 10 15 * ?")
    @Scheduled(fixedDelay = 50000)
    public void scheduleFixedDelayTask() {
        parseSite("https://site1.com");
        parseSite("https://site2.com");
        parseSite("https://site3.com");
        parseSite("https://site4.com");
    }

    private void parseSite(String url) {
        List<Future<UrlWrapper>> handles = new ArrayList<>();

        try {
            List<UrlWrapper> urlWrapperList = new ArrayList<>();
            for (String str : getUrls(url)) {
                task.setUrl(str);
                urlWrapperList.add(taskScheduler.submit(task).get());
            }
            System.err.println(urlWrapperList);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        System.err.println("It is done");
    }

    private List<String> getUrls(String site) throws Exception {
        Set<String> urls = new HashSet<>();
        Document docs = JsoupUtil.parseUrl(site + "/search?q=a&pageSize=100");
        int pagesToScan = Integer.parseInt(docs.select(".pagination li:nth-last-child(2) span").text());
        //pagesToScan = 1;

        for (int page = 1; page <= pagesToScan; page++) {
            docs = JsoupUtil.parseUrl(site + "/search?q=a&pageSize=100&page=" + page);
            urls.addAll(docs.select(".list-image-wrapper").stream().map(e -> site + e.attr("href")).collect(Collectors.toSet()));
        }

        urls.addAll(JsoupUtil.parseUrl(site + "/sitemap.xml").select("url loc").stream().map(e -> e.childNode(0).outerHtml().trim()).collect(Collectors.toSet()));
        return new ArrayList(urls);
    }
}