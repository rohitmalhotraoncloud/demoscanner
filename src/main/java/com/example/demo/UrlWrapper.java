package com.example.demo;

public class UrlWrapper {
    private String url;
    private StatusCode status;

    public UrlWrapper(){

    }

    public UrlWrapper(String url,StatusCode status){
        this.url=url;
        this.status=status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public StatusCode getStatus() {
        return status;
    }

    public void setStatusCode(StatusCode status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "url='" + url + '\'' +", status=" + status;
    }
}
