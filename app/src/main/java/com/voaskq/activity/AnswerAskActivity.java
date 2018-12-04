package com.voaskq.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.voaskq.R;
import com.voaskq.adapter.AnswerListAdapter;
import com.voaskq.helper.Constant;
import com.voaskq.helper.MyProgressbar;
import com.voaskq.modal.AnswerList;
import com.voaskq.webservices.Api;
import com.voaskq.webservices.ApiFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.senab.photoview.PhotoViewAttacher;

public class AnswerAskActivity extends AppCompatActivity {

    ImageView back;
    RecyclerView recyclerView;
    ArrayList<AnswerList> anslist;
    AnswerListAdapter adapter;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String Userid, QuesId;
    TextView submit;
    EditText ans;
    Context context;

    CircleImageView user_image;
    TextView Username, questitle, ans_no;
    ImageView main_img;
    String ask_imag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_ask);
        context = this;

        getSharedPrefdata();
        initviews();
        clickEvents();
        setData();
        getAnsList();
    }

    private void setData() {

        Username.setText(Constant.ASK_USERNAME);
        questitle.setText(Constant.ASK_QUES);
        final String userimag = Constant.ASK_USERIMAGE;
        ans_no.setText(Constant.ASK_NO);
         ask_imag = Constant.ASK_IMAGE;

        if (userimag.equals("")) {

            Picasso.with(context)
                    .load(R.drawable.app_icon)
                    .error(R.drawable.app_icon)
                    .into(user_image);

        } else {
            Picasso.with(context)
                    .load(userimag)
                    .placeholder(R.drawable.app_icon)
                    .error(R.drawable.app_icon)
                    .into(user_image);
        }

        if (userimag.equals("")) {

            Picasso.with(context)
                    .load(R.drawable.app_icon)
                    .error(R.drawable.app_icon)
                    .into(main_img);

        } else {
            Picasso.with(context)
                    .load(ask_imag)
                    .placeholder(R.drawable.app_icon)
                    .error(R.drawable.app_icon)
                    .into(main_img);
        }


    }

    private void getSharedPrefdata() {
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        Userid = pref.getString("Userid", null);
        QuesId = Constant.ASK_QUESTION_ID;
    }

    private void initviews() {
        back = findViewById(R.id.back);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setNestedScrollingEnabled(false);
        submit = findViewById(R.id.submit);
        ans = findViewById(R.id.ans);

        user_image = findViewById(R.id.user_image);
        Username = findViewById(R.id.Username);
        questitle = findViewById(R.id.questitle);
        main_img = findViewById(R.id.main_img);
        ans_no = findViewById(R.id.ans_no);

    }

    private void clickEvents() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Ans = ans.getText().toString();

                if (Ans.equalsIgnoreCase("")) {
                    Toast.makeText(AnswerAskActivity.this, "answer can not be empty", Toast.LENGTH_SHORT).show();
                } else {
                    addNewAnswer(Ans);
                }

            }
        });

        main_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showZoomDialog(ask_imag);
            }
        });


    }

    private void showZoomDialog(String image_url) {

        final Dialog zoom_dialog = new Dialog(context);
        zoom_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        zoom_dialog.setCancelable(true);
        zoom_dialog.setCanceledOnTouchOutside(true);
        zoom_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        zoom_dialog.setContentView(R.layout.zoom_dialog);

        ImageView zoomimg = zoom_dialog.findViewById(R.id.zoomimg);
        ImageView close  = zoom_dialog.findViewById(R.id.close);
        final PhotoViewAttacher mAttacher = new PhotoViewAttacher(zoomimg);
        Glide.with(context)
                .load(image_url)
                .placeholder(R.drawable.app_icon)
                .error(R.drawable.app_icon)
                .into(zoomimg);

        mAttacher.update();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoom_dialog.dismiss();
            }
        });

        zoom_dialog.show();
    }


    private void addNewAnswer(String Ans) {

        final Dialog progress_spinner = MyProgressbar.LoadingSpinner(AnswerAskActivity.this);
        progress_spinner.show();

        Api api = ApiFactory.getClient().create(Api.class);
        Call<ResponseBody> call = api.addNewAnswer(Userid, QuesId, Ans);
        Log.e("url   .......", "getSearch: " + call.request().url());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // progressDialog.hide();
                String output = null;
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = null;

                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line + "\n");
                    }
                    output = stringBuilder.toString();
                    Log.e("new api==>>>>>>", "onResponse: getSearch" + output);
                    JSONObject jsonObject = new JSONObject(output);

                    progress_spinner.dismiss();

                    if (jsonObject.getString("status").equalsIgnoreCase("SUCCESS")) {
                        JSONObject result_json = jsonObject.getJSONObject("result");

                        String answer_id = result_json.getString("answer_id");
                        String question_id = result_json.getString("question_id");
                        String answer = result_json.getString("answer");
                        String user_id = result_json.getString("user_id");
                        String create_date = result_json.getString("create_date");
                        String picture  = result_json.getString("picture");

                        AnswerList obj = new AnswerList(answer_id, question_id, answer, user_id, create_date, picture);
                        anslist.add(obj);
                        setList();

                    } else if (jsonObject.getString("status").equalsIgnoreCase("FAILED")) {
                        String msg = jsonObject.getString("msg");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    progress_spinner.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progress_spinner.dismiss();
            }
        });
    }


    private void getAnsList() {

        final Dialog progress_spinner = MyProgressbar.LoadingSpinner(AnswerAskActivity.this);
        progress_spinner.show();

        anslist = new ArrayList<>();
        Api api = ApiFactory.getClient().create(Api.class);
        Call<ResponseBody> call = api.getAnswerList(Userid, QuesId);
        Log.e("url   .......", "getSearch: " + call.request().url());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // progressDialog.hide();
                String output = null;
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = null;

                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line + "\n");
                    }
                    output = stringBuilder.toString();
                    Log.e("new api==>>>>>>", "onResponse: getSearch" + output);
                    JSONObject jsonObject = new JSONObject(output);

                    progress_spinner.dismiss();

                    if (jsonObject.getString("status").equalsIgnoreCase("SUCCESS")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject result_json = jsonArray.getJSONObject(i);

                            String answer_id = result_json.getString("answer_id");
                            String question_id = result_json.getString("question_id");
                            String answer = result_json.getString("answer");
                            String user_id = result_json.getString("user_id");
                            String create_date = result_json.getString("create_date");
                            String picture = result_json.getString("picture");

                            AnswerList obj = new AnswerList(answer_id, question_id, answer, user_id, create_date, picture);
                            anslist.add(obj);
                        }
                        setList();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("FAILED")) {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    progress_spinner.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progress_spinner.dismiss();
            }
        });
    }

    private void setList() {
        adapter = new AnswerListAdapter(anslist, getApplicationContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


}
