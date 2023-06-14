package com.example.hostelofdiu;

public class complain_box_model {
    String phone,complain_text,time;

    public complain_box_model() {
    }

    public complain_box_model(String phone, String complain_text,String time) {
        this.phone = phone;
        this.complain_text = complain_text;
        this.time=time;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getComplain_text() {
        return complain_text;
    }

    public void setComplain_text(String complain_text) {
        this.complain_text = complain_text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
