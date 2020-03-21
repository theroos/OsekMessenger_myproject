package com.example.theroos.OsekApp;

public class UserObject {

    public String name, phone, status;

    public UserObject(String s, String name){};

    public UserObject(String s, String name, String phone, String status){
        this.name = name;
        this.phone = phone;
        this.status = status;
    }

    public UserObject(){

    }

    public UserObject(String name, String phone, String status) {

    }


    public String getName(){
        return name;
    }

    public void setName(String name) { this.name = name; }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) { this.phone = phone; }

    public String getStatus(){return status;}

    public void setStatus(String status){this.status = status; }

}
