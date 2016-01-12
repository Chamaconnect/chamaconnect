package com.example.valentine.chamaconnect.model;

import java.io.Serializable;

/**
 * Created by cliff on 10/01/16.
 */
public class Property implements Serializable {
    private static final long serialVersionUID = 1L;

    String location;
    String description;
    String photoId;

    public Property(){
        super();
    }

    public Property(String location, String description, String photoId) {
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

    public String getPhotoId(){
        return photoId;
    }

    public void setPhotoId(String photoId){
        this.photoId = photoId;
    }

}