package com.example.btl_ltuddd_electricstore.object;

import java.io.Serializable;

public class Chat implements Serializable {
    private long id;
    private String ten;
    private String comment;
    private String anh;

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public Chat() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Chat(long id, String ten, String comment, String anh) {
        this.id = id;
        this.ten = ten;
        this.comment = comment;
        this.anh = anh;
    }
}
