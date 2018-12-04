package com.voaskq.modal;

import org.json.JSONArray;

public class MainAsk {

    String question_id;
    String description;
    String create_date;
    String user_id;
    String category;
    String spam_report_count;
    String picture;
    String user_name;
    String first_name;
    String last_name;
    String mobile_number;
    String email_address;
    String gender;
    String create_by;
    String update_date;
    String update_by;
    String is_active;
    String password;
    String address;
    String zipcode;
    String city;
    String is_approved;
    String about;
    String block_status;
    String total_answers;
    String ask_picture;


    public MainAsk(String question_id, String description, String create_date, String user_id, String category, String spam_report_count, String picture, String user_name, String first_name, String last_name, String mobile_number, String email_address, String gender, String create_by, String update_date, String update_by, String is_active, String password, String address, String zipcode, String city, String is_approved, String about, String block_status,String ask_picture ,String total_answers) {
        this.question_id = question_id;
        this.description = description;
        this.create_date = create_date;
        this.user_id = user_id;
        this.category = category;
        this.spam_report_count = spam_report_count;
        this.picture = picture;
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
        this.about = about;
        this.block_status = block_status;
        this.ask_picture = ask_picture;
        this.total_answers = total_answers;
    }

    public String getAsk_picture() {
        return ask_picture;
    }

    public void setAsk_picture(String ask_picture) {
        this.ask_picture = ask_picture;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public String getTotal_answers() {
        return total_answers;
    }

    public void setTotal_answers(String total_answers) {
        this.total_answers = total_answers;
    }
}
