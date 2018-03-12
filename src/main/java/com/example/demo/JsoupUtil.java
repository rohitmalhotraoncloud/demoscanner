package com.example.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class JsoupUtil {

    private static String USER_AGENT = "mozilla";
    private static int TIMEOUT = 30 * 1000;
    private static boolean IGNORE_HTTP_REQUESTS = true;
    private static boolean FOLLOW_REDIRECTS = true;

    public static Document parseUrl(String url) throws IOException {
        return Jsoup.connect(url).
                followRedirects(FOLLOW_REDIRECTS).
                timeout(TIMEOUT).
                userAgent(USER_AGENT).
                ignoreHttpErrors(IGNORE_HTTP_REQUESTS).
                get();
    }
}
