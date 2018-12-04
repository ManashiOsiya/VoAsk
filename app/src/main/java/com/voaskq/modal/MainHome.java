package com.voaskq.modal;

import org.json.JSONArray;

public class MainHome {

    String post_id             ;
    String title               ;
    String create_date         ;
    String user_id             ;
    String post_type           ;
    String category            ;
    String spam_report_count   ;
    String votes               ;
    String user_name           ;
    String first_name          ;
    String last_name           ;
    String mobile_number       ;
    String email_address       ;
    String gender              ;
    String create_by           ;
    String update_date         ;
    String update_by           ;
    String is_active           ;
    String password            ;
    String address             ;
    String zipcode             ;
    String city                ;
    String is_approved         ;
    String picture             ;
    String about               ;
    String block_status        ;
    JSONArray images_arr       ;


    public MainHome(String post_id, String title, String create_date, String user_id, String post_type, String category, String spam_report_count,
                    String votes, String user_name, String first_name, String last_name, String mobile_number, String email_address, String gender, String create_by,
                    String update_date, String update_by, String is_active, String password, String address,
                    String zipcode, String city, String is_approved, String picture, String about, String block_status, JSONArray images_arr) {
        this.post_id = post_id;
        this.title = title;
        this.create_date = create_date;
        this.user_id = user_id;
        this.post_type = post_type;
        this.category = category;
        this.spam_report_count = spam_report_count;
        this.votes = votes;
        this.user_name = user_name;
        this.first_name = first_name;
        this.last_name = last_name;
        this.mobile_number = mobile_number;
        this.email_address = email_address;
        this.gender = gender;
        this.create_by = create_by;
        this.update_date = update_date;
        this.update_by = update_by;
        this.is_active = is_active;
        this.password = password;
        this.address = address;
        this.zipcode = zipcode;
        this.city = city;
        this.is_approved = is_approved;
        this.picture = picture;
        this.about = about;
        this.block_status = block_status;
        this.images_arr = images_arr;
    }


    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSpam_report_count() {
        return spam_report_count;
    }

    public void setSpam_report_count(String spam_report_count) {
        this.spam_report_count = spam_report_count;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public String getUpdate_by() {
        return update_by;
    }

    public void setUpdate_by(String update_by) {
        this.update_by = update_by;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIs_approved() {
        return is_approved;
    }

    public void setIs_approved(String is_approved) {
        this.is_approved = is_approved;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getBlock_status() {
        return block_status;
    }

    public void setBlock_status(String block_status) {
        this.block_status = block_status;
    }

    public JSONArray getImages_arr() {
        return images_arr;
    }

    public void setImages_arr(JSONArray images_arr) {
        this.images_arr = images_arr;
    }
}
