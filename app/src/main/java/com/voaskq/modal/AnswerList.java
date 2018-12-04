package com.voaskq.modal;

public class AnswerList {

    String  answer_id;
    String  question_id;
    String  answer;
    String  user_id;
    String  create_date;
    String picture;


    public AnswerList(String answer_id, String question_id, String answer, String user_id, String create_date,String picture) {
        this.answer_id = answer_id;
        this.question_id = question_id;
        this.answer = answer;
        this.user_id = user_id;
        this.create_date = create_date;
        this.picture=picture;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(String answer_id) {
        this.answer_id = answer_id;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }
}
