package com.example.theroos.OsekApp;

public class UserObject {

    public String name, phone, id;

    public UserObject(String name, String phone) {
    }

    public UserObject(String key, String name, String phone, String id){}


    public UserObject(String name, String phone, String id){
        this.name = name;
        this.phone = phone;
        this.id = id;
    }

    public UserObject(){

    }


    public String getName(){
        return name;
    }

    public void setName(String name) { this.name = name; }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) { this.phone = phone; }

    public String getId() { return id; }

    public void setId(String id){this.id = id;}


}
