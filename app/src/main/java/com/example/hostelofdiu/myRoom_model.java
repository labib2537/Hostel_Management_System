package com.example.hostelofdiu;

public class myRoom_model {
    String user_name, uer_phone, building_name, floor_number, room_number, application_time,
            requeste_for, application_condition, occupation_date, leaving_date, id;

    public myRoom_model() {
    }


    public myRoom_model(String user_name, String uer_phone, String building_name,
                        String floor_number, String room_number, String application_time,
                        String requeste_for,String application_condition, String occupation_date,
                        String leaving_date, String id) {
        this.user_name = user_name;
        this.uer_phone = uer_phone;
        this.building_name = building_name;
        this.floor_number = floor_number;
        this.room_number = room_number;
        this.application_time = application_time;
        this.requeste_for=requeste_for;
        this.application_condition = application_condition;
        this.occupation_date = occupation_date;
        this.leaving_date = leaving_date;
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUer_phone() {
        return uer_phone;
    }

    public void setUer_phone(String uer_phone) {
        this.uer_phone = uer_phone;
    }

    public String getBuilding_name() {
        return building_name;
    }

    public void setBuilding_name(String building_name) {
        this.building_name = building_name;
    }

    public String getFloor_number() {
        return floor_number;
    }

    public void setFloor_number(String floor_number) {
        this.floor_number = floor_number;
    }

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    public String getRequeste_for() {
        return requeste_for;
    }

    public void setRequeste_for(String requeste_for) {
        this.requeste_for = requeste_for;
    }

    public String getApplication_time() {
        return application_time;
    }

    public void setApplication_time(String application_time) {
        this.application_time = application_time;
    }

    public String getApplication_condition() {
        return application_condition;
    }

    public void setApplication_condition(String application_condition) {
        this.application_condition = application_condition;
    }

    public String getOccupation_date() {
        return occupation_date;
    }

    public void setOccupation_date(String occupation_date) {
        this.occupation_date = occupation_date;
    }

    public String getLeaving_date() {
        return leaving_date;
    }

    public void setLeaving_date(String leaving_date) {
        this.leaving_date = leaving_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
