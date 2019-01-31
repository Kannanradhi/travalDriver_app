package com.cabily.cabilydriver.Pojo;

/**
 * Created by user144 on 5/18/2018.
 */

public class DriverDocumentPojo {
    String name = "";
    String short_name = "";
    String title = "";
    String description = "";
    String is_req = "";
    String is_exp = "";
    String id = "";
    String categoryImage="";

    public String getDocument_avail() {
        return document_avail;
    }

    public void setDocument_avail(String document_avail) {
        this.document_avail = document_avail;
    }

    String document_avail;
    String document_path;
    String document_status,Imageadded;
    public String getDocument_path() {
        return document_path;
    }

    public void setDocument_path(String document_path) {
        this.document_path = document_path;
    }



    public String getDocument_status() {
        return document_status;
    }

    public void setDocument_status(String document_status) {
        this.document_status = document_status;
    }


    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getImageadded() {
        return Imageadded;
    }

    public void setImageadded(String Imageadded) {
        this.Imageadded = Imageadded;
    }


    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getIs_req() {
        return is_req;
    }

    public void setIs_req(String is_req) {
        this.is_req = is_req;
    }


    public String getIs_exp() {
        return is_exp;
    }

    public void setIs_exp(String is_exp) {
        this.is_exp = is_exp;
    }


}
