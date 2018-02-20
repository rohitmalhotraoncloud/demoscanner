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
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class TaskService implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Async
    //@Scheduled(cron = "0 15 10 15 * ?")
    @Scheduled(fixedDelay = 20000)
    public void scheduleFixedDelayTask() {
        parseSite("https://site1.com");
        parseSite("https://site2.com");
    }

    private void parseSite(String url) {
        int totalrecords = 0;
        List<Future<UrlWrapper>> handles = new ArrayList<>();

        try {
            SchedulerTask task = null;
            List<UrlWrapper> urlWrapperList = new ArrayList<>();
            Document document = Jsoup.connect(url + "/sitemap.xml").get();

            List<String> urls = document.select("url loc").stream().map(e -> e.childNode(0).outerHtml().trim()).collect(Collectors.toList());
            totalrecords = urls.size();
            int index = 0;
            for (String str : urls) {
                index++;

                if (index > 50) {
                    break;
                }

                task = applicationContext.getBean("schedulerTask", SchedulerTask.class);
                task.setUrl(str);
                urlWrapperList.add(taskScheduler.submit(task).get());
            }
            System.err.println(urlWrapperList);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        System.err.println("It is done");
    }
}