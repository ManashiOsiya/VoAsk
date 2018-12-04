package com.voaskq.modal;

public class SubHome {

        String post_image_id       ;
        String post_id             ;
        String post_image          ;
        String total_votes         ;
        String total_comments      ;
        String vote_id             ;
        String main_title           ;

    public SubHome(String post_image_id, String post_id, String post_image, String total_votes, String total_comments, String vote_id,String main_title) {
        this.post_image_id = post_image_id;
        this.post_id = post_id;
        this.post_image = post_image;
        this.total_votes = total_votes;
        this.total_comments = total_comments;
        this.vote_id = vote_id;
        this.main_title = main_title;
    }

    public String getMain_title() {
        return main_title;
    }

    public void setMain_title(String main_title) {
        this.main_title = main_title;
    }

    public String getPost_image_id() {
        return post_image_id;
    }

    public void setPost_image_id(String post_image_id) {
        this.post_image_id = post_image_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPost_image() {
        return post_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }

    public String getTotal_votes() {
        return total_votes;
    }

    public void setTotal_votes(String total_votes) {
        this.total_votes = total_votes;
    }

    public String getTotal_comments() {
        return total_comments;
    }

    public void setTotal_comments(String total_comments) {
        this.total_comments = total_comments;
    }

    public String getVote_id() {
        return vote_id;
    }

    public void setVote_id(String vote_id) {
        this.vote_id = vote_id;
    }
}


