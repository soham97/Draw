package com.developers.sd.drawline;

public class Line {

    private String title;
    private int status;

    public Line(String title, int status) {
        this.title = title;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public int getStatus() {
        return status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
