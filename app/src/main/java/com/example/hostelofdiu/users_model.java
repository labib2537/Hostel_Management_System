package com.example.hostelofdiu;

public class users_model {
    String name,phone,profile_img,student_id;

    public users_model() {
    }

    public users_model(String name, String phone, String profile_img, String student_id) {
        this.name = name;
        this.phone = phone;
        this.profile_img = profile_img;
        this.student_id = student_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }
}
