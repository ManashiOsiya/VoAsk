package com.voaskq.activity;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.voaskq.R;
import com.voaskq.helper.Constant;
import com.voaskq.helper.MyProgressbar;
import com.voaskq.modal.MainAsk;
import com.voaskq.modal.MainHome;
import com.voaskq.webservices.Api;
import com.voaskq.webservices.ApiFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewCurrentUserData extends AppCompatActivity {

    ImageView back, profile_image;
    TextView firstname, lastname, email, askques, post;
    Context context;
    String Other_userid, tag = "viewOtheruser", AskNo = "", PostNo = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_current_user_data);
        context = this;

        initviews();
        getData();
        getAskList();
        getVoteData();
        clickEvents();

    }


    private void initviews() {

        back = findViewById(R.id.back);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        askques = findViewById(R.id.askques);
        post = findViewById(R.id.post);
        profile_image = findViewById(R.id.profile_image);
    }

    private void clickEvents() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getData() {

        Other_userid = Constant.OTHER_USER_USERID;

        Log.e(tag, "~~~Other_userid~~~~" + Other_userid);
        firstname.setText("First Name: " + Constant.OTHER_USER_FIRSTNAME);
        lastname.setText("Last Name: " + Constant.OTHER_USER_LASTNAME);

        if (Constant.OTHER_USER_EMAIL.equalsIgnoreCase(null))
            email.setText("Email: ");
        else
            email.setText("Email: " + Constant.OTHER_USER_EMAIL);

        String userimag = Constant.OTHER_USER_PROFILEIMAGE;

        if (userimag.equals("") || userimag.equals(null)) {

            Picasso.with(context)
                    .load(R.drawable.app_icon)
                    .error(R.drawable.app_icon)
                    .into(profile_image);

        } else {
            Picasso.with(context)
                    .load(userimag)
                    .placeholder(R.drawable.app_icon)
                    .error(R.drawable.app_icon)
                    .into(profile_image);
        }
    }

    private void getVoteData() {

        Api api = ApiFactory.getClient().create(Api.class);
        Call<ResponseBody> call = api.getProfileVote(Other_userid);
        Log.e(tag, " getHomeData: " + call.request().url());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                String output = null;
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = null;

                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line + "\n");
                    }
                    output = stringBuilder.toString();
                    Log.e(tag, "onResponse: " + output);
                    JSONObject jsonObject = new JSONObject(output);

                    if (jsonObject.getString("status").equalsIgnoreCase("SUCCESS")) {

                        JSONArray result_jsonArray = jsonObject.getJSONArray("result");
                        PostNo = String.valueOf(result_jsonArray.length());
                        post.setText("Posts: " + PostNo);
                    } else if (jsonObject.getString("status").equalsIgnoreCase("FAILED")) {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void getAskList() {
        Api api = ApiFactory.getClient().create(Api.class);
        Call<ResponseBody> call = api.getProfileAsk(Other_userid);
        Log.e(tag, " getHomeData: " + call.request().url());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                String output = null;
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = null;

                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line + "\n");
                    }
                    output = stringBuilder.toString();
                    Log.e(tag, "onResponse: " + output);
                    JSONObject jsonObject = new JSONObject(output);

                    if (jsonObject.getString("status").equalsIgnoreCase("SUCCESS")) {

                        JSONArray result_jsonArray = jsonObject.getJSONArray("result");

                        AskNo = String.valueOf(result_jsonArray.length());
                        askques.setText("Ask-Question: " + AskNo);

                    } else if (jsonObject.getString("status").equalsIgnoreCase("FAILED")) {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }


}

