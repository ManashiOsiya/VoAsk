package com.voaskq.Fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.voaskq.R;
import com.voaskq.adapter.MainAskAdapter;
import com.voaskq.adapter.MainHomeAdapter;
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

import static android.content.Context.MODE_PRIVATE;

public class AskFragment extends Fragment {

    public AskFragment() {}

    RecyclerView recyclerView;
    ArrayList<MainAsk> mainlist;
    View mview;
    Context mContext;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String tag = "ask";
    String Userid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mview =  inflater.inflate(R.layout.fragment_ask, container, false);
        mContext = inflater.getContext();

        intviews();
        getSharedPrefdata();
        getDataList();

        return mview;
    }


    private void getSharedPrefdata() {
        pref = mContext.getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        Userid = pref.getString("Userid", "");
    }

    private void intviews() {

        recyclerView = mview.findViewById(R.id.main_ask_recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void setList() {
        MainAskAdapter main_adapter = new MainAskAdapter(mainlist, mContext);
        recyclerView.setAdapter(main_adapter);
        main_adapter.notifyDataSetChanged();
    }

    private void getDataList() {

        mainlist = new ArrayList<>();

        final Dialog progress_spinner = MyProgressbar.LoadingSpinner(mContext);
        progress_spinner.show();

        Api api = ApiFactory.getClient().create(Api.class);
        Call<ResponseBody> call = api.getAsk(Userid);
        Log.e(tag, Userid + " getHomeData: " + call.request().url());
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
                    progress_spinner.dismiss();
                    if (jsonObject.getString("status").equalsIgnoreCase("SUCCESS")) {

                        JSONArray result_jsonArray = jsonObject.getJSONArray("result");

                        for (int i = 0; i < result_jsonArray.length(); i++) {

                            JSONObject jsonobj = result_jsonArray.getJSONObject(i);

                            String question_id          = jsonobj.getString("question_id");
                            String description          = jsonobj.getString("description");
                            String create_date          = jsonobj.getString("create_date");
                            String user_id              = jsonobj.getString("user_id");
                            String category             = jsonobj.getString("category");
                            String spam_report_count    = jsonobj.getString("spam_report_count");
                            String picture              = jsonobj.getString("picture");
                            String user_name            = jsonobj.getString("user_name");
                            String first_name           = jsonobj.getString("first_name");
                            String last_name            = jsonobj.getString("last_name");
                            String mobile_number        = jsonobj.getString("mobile_number");
                            String email_address        = jsonobj.getString("email_address");
                            String gender               = jsonobj.getString("gender");
                            String create_by            = jsonobj.getString("create_by");
                            String update_date          = jsonobj.getString("update_date");
                            String update_by            = jsonobj.getString("update_by");
                            String is_active            = jsonobj.getString("is_active");
                            String password             = jsonobj.getString("password");
                            String address              = jsonobj.getString("address");
                            String zipcode              = jsonobj.getString("zipcode");
                            String city                 = jsonobj.getString("city");
                            String is_approved          = jsonobj.getString("is_approved");
                            String about                = jsonobj.getString("about");
                            String block_status         = jsonobj.getString("block_status");
                            String ask_picture          = jsonobj.getString("ask_picture");
                            String total_answers        = jsonobj.getString("total_answers");

                          MainAsk obj = new MainAsk(question_id, description, create_date, user_id, category, spam_report_count, picture, user_name, first_name, last_name,
                                  mobile_number,  email_address,  gender,  create_by, update_date,  update_by,  is_active,  password,  address,  zipcode,
                                  city,  is_approved,  about,  block_status, ask_picture, total_answers);

                          mainlist.add(obj);
                        }
                        setList();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("FAILED")) {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
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

}