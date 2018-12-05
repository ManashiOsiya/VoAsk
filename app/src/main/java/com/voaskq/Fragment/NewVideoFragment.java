package com.voaskq.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.voaskq.R;
import com.voaskq.activity.CameraActivity;
import com.voaskq.adapter.NewListAdapter;
import com.voaskq.helper.Constant;
import com.voaskq.helper.MyProgressbar;
import com.voaskq.modal.BroadcastList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewVideoFragment extends Fragment {

    View mview;
    Context mContext;
    Button dobroadcast;
    RecyclerView recyclerView;
    ArrayList<BroadcastList> mainlist;
    public static String SELECTED_VIDEO = "";

    private static final String API_KEY = Constant.BAMBUSER_API_KEY;


    public NewVideoFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mview = inflater.inflate(R.layout.fragment_new_video, container, false);
        mContext = inflater.getContext();
        initviews();
        clickEvents();
        return mview;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataList();
    }

    private void initviews() {
        dobroadcast = mview.findViewById(R.id.dobroadcast);
        recyclerView = mview.findViewById(R.id.main_home_recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void clickEvents() {

        dobroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, CameraActivity.class);
                mContext.startActivity(in);
            }
        });
    }

    private void getDataList() {

        mainlist = new ArrayList<>();
        final OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.bambuser.com/broadcasts")
                .addHeader("Accept", "application/vnd.bambuser.v1+json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + API_KEY)
                .get()
                .build();
        Log.e("one", "~~~~~url~~~~~~~" + request.url());
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                Log.e("one", "~~~~~~~~onFailure~~~~~");
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject json = new JSONObject(body);
                    JSONArray results = json.optJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject latestBroadcast = results.optJSONObject(i);

                        Log.e("one","latestBroadcast~~~~"+latestBroadcast);

//                     {"author":"","created":1543999309,"height":1280,"id":"b5d6ef42-55ab-41c1-a193-d2a4d4be68fe","ingestChannel":"594eb79b-004e-d1d3-cfde-c19d6eabc7d4","length":8,
//                              "preview":"https:\/\/preview.bambuser.io\/live\/eyJyZXNvdXJjZVVyaSI6Imh0dHBzOlwvXC9jZG4uYmFtYnVzZXIubmV0XC9icm9hZGNhc3RzXC9iNWQ2ZWY0Mi01NWFiLTQxYzEtYTE5My1kMmE0ZDRiZTY4ZmUifQ==\/preview.jpg",
//                              "resourceUri":"https:\/\/cdn.bambuser.net\/broadcasts\/b5d6ef42-55ab-41c1-a193-d2a4d4be68fe?da_signature_method=HMAC-SHA256&da_id=9e1b1e83-657d-7c83-b8e7-0b782ac9543a&da_timestamp=1543999324&da_static=1&da_ttl=0&da_signature=c426a9c68179ad84643cab6bbb68ff400b574fbd4883b52f6f6ebcc4d6a2ea28","tags":[],"title":"VoAsk","type":"archived","width":720}

                        String preview = "", type = "";

                        try {
                            preview = latestBroadcast.optString("preview");
                        } catch (Exception e) {
                        }
                        try {
                            type = latestBroadcast.optString("type");
                        } catch (Exception e) {
                        }

                        String resourceuri  = latestBroadcast.optString("resourceUri");
                        String created      = latestBroadcast.getString("created");
                        String author       = latestBroadcast.getString("author");
                        String id           = latestBroadcast.getString("id");

                 //     String customdata   = latestBroadcast.getString("customData");
                 //     String height          = latestBroadcast.getString("height");
                 //     String ingestChannel   = latestBroadcast.getString("ingestChannel");
                 //     String length   = latestBroadcast.getString("length");

                        boolean isValid = calculateTime(created);

                        if (isValid) {
                            BroadcastList obj = new BroadcastList(preview, resourceuri, type, created,author,id);
                            mainlist.add(obj);
                        }
                    }
                    refreshMyRecyclerView();
                } catch (Exception ignored) {
                    Log.e("one", "~~~ignored~~~~~Exception~~~~~" + ignored);
                }
            }
        });
    }


    private boolean calculateTime(String created) {
        try {
            long unixSeconds = Long.parseLong(created);
            Date date = new java.util.Date(unixSeconds * 1000L);
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
//        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-5"));
            String formattedDate = sdf.format(date);
            String currentdate = getCurrentDate();

            Date apiDate = sdf.parse(formattedDate);
            Date currentDate = sdf.parse(currentdate);

            long diff_hr = printDifference(apiDate, currentDate);

            if (diff_hr > 0) {
                return false;
            } else {
                return true;
            }


        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
    public String getCurrentDate() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        String Date = dateFormat.format(new Date());
        Log.e("one", "getCurrentDate    ---" + Date);
        return Date;
    }
    public long printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;


        Log.e("one", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        Log.e("one", "elapsedDays=" + elapsedDays);
        Log.e("one", "elapsedHours=" + elapsedHours);
        Log.e("one", "elapsedMinutes=" + elapsedMinutes);
        Log.e("one", "elapsedSeconds=" + elapsedSeconds);

        Log.e("one", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        return elapsedDays;

    }

    public void refreshMyRecyclerView() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NewListAdapter main_adapter = new NewListAdapter(mainlist, mContext);
                        recyclerView.setAdapter(main_adapter);
                        main_adapter.notifyDataSetChanged();

                    }
                });
            }
        }).start();
    }

}
