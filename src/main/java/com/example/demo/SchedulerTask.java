package com.example.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.Callable;

@Component
public class SchedulerTask implements Callable<UrlWrapper> {
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
            Document doc = Jsoup.connect(url).userAgent("Mozilla").ignoreHttpErrors(true).get();
            if (doc.title().startsWith("Page not found")) {
                statusCode = StatusCode.ERROR_404;
            } else if (doc.title().startsWith("Unexpected error")) {
                statusCode = StatusCode.ERROR_500;
            }
            Elements elems = doc.getElementsByClass("page-results-counter");
            if (!CollectionUtils.isEmpty(elems)) {
                if (elems.get(0).text().startsWith("(0 - 0")) {
                    statusCode = StatusCode.NO_CONTENT;
                }
            }
            if (statusCode == StatusCode.PENDING) {
                statusCode = StatusCode.SUCCESS;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("Current Thread : "+Thread.currentThread().getName());
        return new UrlWrapper(url, statusCode);
    }

}