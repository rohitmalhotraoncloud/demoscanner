package com.example.demo;

import javax.persistence.*;

@Entity
public class Sitedetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "sourcedata")
    private String sourcedata;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public String getSourcedata() {
        return sourcedata;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSourcedata(String sourcedata) {
        this.sourcedata = sourcedata;
    }
}
