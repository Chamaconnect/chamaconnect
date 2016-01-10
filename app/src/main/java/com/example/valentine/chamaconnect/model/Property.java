package com.example.valentine.chamaconnect.model;

/**
 * Created by cliff on 10/01/16.
 */
public class Property {

    String location;
    String description;
    int photoId;

    public Property(String location, String description, int photoId) {
        this.location = location;
        this.description = description;
        this.photoId = photoId;
    }

    public String getLocation(){
        return location;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public String getDescription(){
        return  description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public int getPhotoId(){
        return photoId;
    }

    public void setPhotoId(int photoId){
        this.photoId = photoId;
    }

}