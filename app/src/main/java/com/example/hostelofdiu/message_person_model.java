package com.example.hostelofdiu;

public class message_person_model {
    String time,phone,massage,massage_id;

    public message_person_model() {
    }

    public message_person_model(String time, String phone, String massage) {
        this.time = time;
        this.phone = phone;
        this.massage = massage;
    }
    public message_person_model(String time, String phone, String massage, String massage_id) {
        this.time = time;
        this.phone = phone;
        this.massage = massage;
        this.massage_id=massage_id;
    }

    public String getMassage_id() {
        return massage_id;
    }

    public void setMassage_id(String massage_id) {
        this.massage_id = massage_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }
}
