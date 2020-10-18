package com.nero.spring.log.vo;

import java.util.Date;

public class ExampleVO {
    private String str;
    private Date time;

    public ExampleVO(String str, Date time) {
        this.str = str;
        this.time = time;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
