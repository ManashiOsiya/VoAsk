package com.voaskq.webservices;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface Api {


    /* 1 =====================================  FOR REGISTRATION  ====================================================*/

    //  https://apps.konnectapp.co.nz/voask/index.php/api/user/register

    @Multipart
    @POST("user/register")
    Call<ResponseBody> getRegistration(@Part("user_name") RequestBody user_name,
                                       @Part("password") RequestBody password,
                                       @Part("first_name") RequestBody first_name,
                                       @Part("last_name") RequestBody last_name,
                                       @Part MultipartBody.Part image);

    /* 2 =====================================  FOR LOGIN  ====================================================*/

//    https://apps.konnectapp.co.nz/voask/index.php/api/user/login

    @POST("user/login")
    @FormUrlEncoded
    Call<ResponseBody> getLogin(@Field("username") String user_name,
                                @Field("password") String password);

    /* 3 =====================================  @ home fragment  ====================================================*/


//    https://apps.konnectapp.co.nz/voask/index.php/api/post/list_votes

    @GET("post/list_votes")
    Call<ResponseBody> getHomeData(@Header("token") String token);


    /* 4 =====================================  @ Profile fragment  to view userdata ====================================================*/


//  https://apps.konnectapp.co.nz/voask/index.php/api/user/detail?user_id=4D9568DADD98ECF4D3BABC5D1CD18D05

    @GET("user/detail")
    Call<ResponseBody> getUserDetail(@Query("user_id") String user_id);

    /* 5 =====================================  @ Profile fragment update user data  ====================================================*/

//    https://apps.konnectapp.co.nz/voask/index.php/api/user/editprofile

    @Multipart
    @POST("user/editprofile")
    Call<ResponseBody> getUpdateProfile(@Header("token") String token,
                                        @Part("user_name") RequestBody user_name,
                                        @Part("first_name") RequestBody first_name,
                                        @Part("last_name") RequestBody last_name,
                                        @Part("password") RequestBody password,
                                        @Part("about") RequestBody about,
                                        @Part("email_address") RequestBody email_address,
                                        @Part MultipartBody.Part images);

    /* 6 =====================================  @ vote in bottom navigation  ====================================================*/

//    https://apps.konnectapp.co.nz/voask/index.php/api/post/list

    @GET("post/list")
    Call<ResponseBody> getVote(@Header("token") String token);

    /* 7 =====================================  @ vote add in top header  ====================================================*/

//    https://apps.konnectapp.co.nz/voask/index.php/api/post/add

    @Multipart
    @POST("post/add")
    Call<ResponseBody> addVote(@Header("token") String token,
                               @Part("post_type") RequestBody post_type,
                               @Part("title") RequestBody title,
                               @Part("category") RequestBody category,
                               @Part MultipartBody.Part images[]);

    /* 8 =====================================  @ Ask add in top header  ====================================================*/

    @Multipart
    @POST("question/add")
    Call<ResponseBody> addAsk(@Header("token") String token,
                              @Part("post_type") RequestBody post_type,
                              @Part("description") RequestBody title,
                              @Part("category") RequestBody category,
                              @Part MultipartBody.Part images[]);

    /* 9 =====================================  @ Ask fragment  ====================================================*/

//    https://apps.konnectapp.co.nz/voask/index.php/api/question/list

    @GET("question/list")
    Call<ResponseBody> getAsk(@Header("token") String token);

    /* 10 =====================================  @ vote in profile section  ====================================================*/

//    https://apps.konnectapp.co.nz/voask/index.php/api/post/myposts

    @GET("post/myposts")
    Call<ResponseBody> getProfileVote(@Header("token") String token);

    /* 11 =====================================  @ ask in profile section  ====================================================*/

//    https://apps.konnectapp.co.nz/voask/index.php/api/question/myquestions

    @GET("question/myquestions")
    Call<ResponseBody> getProfileAsk(@Header("token") String token);

    /* 12 =====================================  @ to live vote  ====================================================*/

//    https://apps.konnectapp.co.nz/voask/index.php/api/post/vote?imageid=

    @GET("post/vote")
    Call<ResponseBody> getFavorite(@Header("token") String token,
                                   @Query("imageid") String imageid);

    /* 13 =====================================  @ to live vote  ====================================================*/

//    https://apps.konnectapp.co.nz/voask/index.php/api/post/share?post_id=E03A084006B497D32B7C391F865BD43F

    @GET("post/share")
    Call<ResponseBody> getShare(@Header("token") String token,
                                   @Query("post_id") String post_id);


    /* 14 =====================================  @ report spam  ====================================================*/

//    https://apps.konnectapp.co.nz/voask/index.php/api/post/spam

    @GET("post/spam")
    Call<ResponseBody> reportSpam(@Header("token") String token,
                                @Query("post_id") String post_id,
                                @Query("spamType") String spamType);


    /* 15 =====================================  @ search  ====================================================*/

//    https://apps.konnectapp.co.nz/voask/index.php/api/user/searchuser?searchuserkey=

    @GET("user/searchuser")
    Call<ResponseBody> getSearchData(@Header("token") String token,
                                     @Query("searchuserkey") String searchuserkey);


    /* 16 =====================================  @ answerlist  ====================================================*/

//    https://apps.konnectapp.co.nz/voask/index.php/api/question/answer?question_id=1850C6CFB38021FA0E38C48C0FE4C8C0


    @GET("question/answer")
    Call<ResponseBody> getAnswerList(@Header("token") String token,
                                     @Query("question_id") String question_id);

    /* 17 =====================================  @ add new ans  ====================================================*/


//    https://apps.konnectapp.co.nz/voask/index.php/api/question/addanswer
//    question_id, answer


    @POST("question/addanswer")
    @FormUrlEncoded
    Call<ResponseBody> addNewAnswer(@Header("token") String token,
                                     @Field("question_id") String question_id,
                                     @Field("answer") String answer);


    /* 18 =====================================  @ follow  ====================================================*/

//    https://apps.konnectapp.co.nz/voask/index.php/api/user/follow?tofollow=


    @GET("user/follow")
    Call<ResponseBody> getFollow(@Header("token") String token,
                                 @Query("tofollow") String tofollow);

    /* 19 =====================================  @ unfollow  ====================================================*/

//    https://apps.konnectapp.co.nz/voask/index.php/api/user/unfollow?followid=

    @GET("user/unfollow")
    Call<ResponseBody> getUnFollow(@Header("token") String token,
                                 @Query("followid") String followid);

    /* 20 =====================================  @ delete post  ====================================================*/

//    https://apps.konnectapp.co.nz/voask/index.php/api/post/deletepost

    @GET("post/deletepost")
    Call<ResponseBody> deleteVote(@Header("token") String token,
                                   @Query("post") String post);



    /* 21 =====================================  @ delete ques  ====================================================*/

//    https://apps.konnectapp.co.nz/voask/index.php/api/question/deletequestion?question=

    @GET("question/deletequestion")
    Call<ResponseBody> deleteAsk(@Header("token") String token,
                                  @Query("question") String question);


    /* 22 =====================================  @ show vote list  ====================================================*/

//    https://apps.konnectapp.co.nz/voask/index.php/api/post/votedList?post_image_id=


    @GET("post/votedList")
    Call<ResponseBody> showVoteList(@Header("token") String token,
                                    @Query("post_image_id") String post_image_id);


}