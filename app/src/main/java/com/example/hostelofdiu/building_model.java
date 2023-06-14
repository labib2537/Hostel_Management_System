package com.example.hostelofdiu;

public class building_model {
    String building_name;
    String total_flor;
    String total_room_inflor;
    String building_id;
    String available_rooms;
    String building_details;
    String building_for;
    String first_image;


    public building_model() {
    }

    public building_model(String building_name, String total_flor, String total_room_inflor,
                          String building_id, String available_rooms, String building_details,
                          String building_for,String first_image) {
        this.building_name = building_name;
        this.total_flor = total_flor;
        this.total_room_inflor = total_room_inflor;
        this.building_id = building_id;
        this.available_rooms = available_rooms;
        this.building_details = building_details;
        this.building_for=building_for;
        this.first_image=first_image;
    }

    public String getBuilding_name() {
        return building_name;
    }

    public void setBuilding_name(String building_name) {
        this.building_name = building_name;
    }

    public String getTotal_flor() {
        return total_flor;
    }

    public void setTotal_flor(String total_flor) {
        this.total_flor = total_flor;
    }

    public String getTotal_room_inflor() {
        return total_room_inflor;
    }

    public void setTotal_room_inflor(String total_room_inflor) {
        this.total_room_inflor = total_room_inflor;
    }

    public String getBuilding_id() {
        return building_id;
    }

    public void setBuilding_id(String building_id) {
        this.building_id = building_id;
    }

    public String getAvailable_rooms() {
        return available_rooms;
    }

    public void setAvailable_rooms(String available_rooms) {
        this.available_rooms = available_rooms;
    }

    public String getBuilding_details() {
        return building_details;
    }

    public void setBuilding_details(String building_details) {
        this.building_details = building_details;
    }
    public String getBuilding_for() {
        return building_for;
    }
    public void setBuilding_for(String building_for) {
        this.building_for = building_for;
    }

    public String getFirst_image() {
        return first_image;
    }
    public void setFirst_image(String first_image) {
        this.first_image = first_image;
    }

}
