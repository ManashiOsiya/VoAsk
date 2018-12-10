package com.voaskq.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.voaskq.R;
import com.voaskq.helper.MyProgressbar;
import com.voaskq.modal.SubHome;
import com.voaskq.modal.VoteList;
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
import uk.co.senab.photoview.PhotoViewAttacher;


public class SubHomeAdapter extends RecyclerView.Adapter<SubHomeAdapter.MyViewHolder> {
    public Context context;
    MyViewHolder myholder;
    private ArrayList<SubHome> sublist = null;
    private ArrayList<VoteList> votelist = null;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String tag = "subhome";

    String Userid;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView vote_text, asktext;
        ImageView mainimg, vote_btn, share_btn;

        public MyViewHolder(View view) {
            super(view);

            mainimg = view.findViewById(R.id.mainimg);
            vote_btn = view.findViewById(R.id.vote_btn);
            share_btn = view.findViewById(R.id.share_btn);
            vote_text = view.findViewById(R.id.vote_text);
            asktext = view.findViewById(R.id.asktext);
            this.setIsRecyclable(false);
        }
    }

    public SubHomeAdapter(ArrayList<SubHome> contactslist, Context context) {
        this.sublist = contactslist;
        this.context = context;
        setHasStableIds(true);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_home_adapter_item, parent, false);

        pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        editor = pref.edit();
        Userid = pref.getString("Userid", null);

        ImageView imgView = view.findViewById(R.id.mainimg);
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        int widthInDP = /*Math.round*/(dm.widthPixels);
        int width = (int) ((widthInDP * 0.8f));
        int height = (int) ((width * 0.66f));
        imgView.getLayoutParams().width = width;
        imgView.getLayoutParams().height = height;

        myholder = new MyViewHolder(view);
        return myholder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final SubHome subobj = sublist.get(position);

        holder.vote_text.setText(subobj.getTotal_votes() + " Votes");
        holder.asktext.setText(subobj.getTotal_comments() + " Shares");

        final String userimag = context.getResources().getString(R.string.home_adapter_postimg_baseurl) + subobj.getPost_image();

//        if(subobj.getPost_image().equalsIgnoreCase(null)){
//            holder.itemView.setVisibility(View.GONE);
//        }

        if (userimag.equals("")) {

            Picasso.with(context)
                    .load(R.drawable.app_icon)
                    .error(R.drawable.app_icon)
                    .into(holder.mainimg);

        } else {
            Picasso.with(context)
                    .load(userimag)
                    .placeholder(R.drawable.app_icon)
                    .error(R.drawable.app_icon)
                    .into(holder.mainimg);
        }

        holder.vote_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getLike(subobj.getPost_image_id(), holder);

            }
        });


        holder.share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getShare(subobj.getPost_id(), subobj.getMain_title(), holder);
            }
        });

        holder.mainimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showZoomDialog(userimag);
            }
        });


        holder.vote_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getVoteList(subobj.getPost_image_id());
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

//        zoom_dialog.getWindow().setGravity(Gravity.BOTTOM);

        ImageView zoomimg = zoom_dialog.findViewById(R.id.zoomimg);
        ImageView close = zoom_dialog.findViewById(R.id.close);
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

    private void getLike(String imageid, final MyViewHolder holder) {

        final Dialog progress_spinner = MyProgressbar.LoadingSpinner(context);
        progress_spinner.show();

        Api api = ApiFactory.getClient().create(Api.class);
        Call<ResponseBody> call = api.getFavorite(Userid, imageid);
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

                        JSONObject jresult = jsonObject.getJSONObject("result");

                        String result = jresult.getString("total_votes");
                        holder.vote_text.setText(result + " Votes");
                        //   notifyDataSetChanged();

                        Toast.makeText(context, "successfully done", Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("FAILED")) {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
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

    private void getShare(String postid, final String title, final MyViewHolder holder) {

        final Dialog progress_spinner = MyProgressbar.LoadingSpinner(context);
        progress_spinner.show();

        Api api = ApiFactory.getClient().create(Api.class);
        Call<ResponseBody> call = api.getShare(Userid, postid);
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


                        String msg = title + "\n" + "Download Voask app from the below link- \n" +
                                " https://play.google.com/store/apps/details?id=com.voask";

                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
                        sendIntent.setType("text/plain");
                        context.startActivity(sendIntent);

                        JSONObject jresult = jsonObject.getJSONObject("result");
                        String result = jresult.getString("total_shares");
                        holder.asktext.setText(result + " Shares");

                        Toast.makeText(context, "successfully done", Toast.LENGTH_SHORT).show();


                    } else if (jsonObject.getString("status").equalsIgnoreCase("FAILED")) {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
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

    void getVoteList(String voteid) {
        votelist = new ArrayList<>();
        final Dialog progress_spinner = MyProgressbar.LoadingSpinner(context);
        progress_spinner.show();

        Api api = ApiFactory.getClient().create(Api.class);
        Call<ResponseBody> call = api.showVoteList(Userid, voteid);
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
                        JSONArray jresult = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jresult.length(); i++) {
                            JSONObject obj = jresult.getJSONObject(i);

                            String user_id = obj.getString("user_id");
                            String user_name = obj.getString("user_name");
                            String first_name = obj.getString("first_name");
                            String last_name = obj.getString("last_name");
                            String mobile_number = obj.getString("mobile_number");
                            String email_address = obj.getString("email_address");
                            String gender = obj.getString("gender");
                            String create_date = obj.getString("create_date");
                            String create_by = obj.getString("create_by");
                            String update_date = obj.getString("update_date");
                            String update_by = obj.getString("update_by");
                            String is_active = obj.getString("is_active");
                            String password = obj.getString("password");
                            String address = obj.getString("address");
                            String zipcode = obj.getString("zipcode");
                            String city = obj.getString("city");
                            String is_approved = obj.getString("is_approved");
                            String picture = obj.getString("picture");
                            String about = obj.getString("about");
                            String block_status = obj.getString("block_status");

                            VoteList mvotelist = new VoteList(user_id, user_name, first_name, last_name, mobile_number, email_address, gender, create_date, create_by, update_date, update_by, is_active, password, address, zipcode, city, is_approved, picture, about, block_status);
                            votelist.add(mvotelist);
                        }
                        showVoteList(votelist);
                    } else if (jsonObject.getString("status").equalsIgnoreCase("FAILED")) {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    progress_spinner.dismiss();
                    Log.e("one", "~~~~Exception at sub home adapter in vote list~~~~~~~~~~" + e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progress_spinner.dismiss();
            }
        });
    }


    void showVoteList(ArrayList<VoteList> votelist) {

        final Dialog alphadialog = new Dialog(context);
        alphadialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alphadialog.setCancelable(true);
        alphadialog.setCanceledOnTouchOutside(true);
        alphadialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alphadialog.setContentView(R.layout.listview_popup);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alphadialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
    //  lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alphadialog.show();
        alphadialog.getWindow().setAttributes(lp);

        alphadialog.getWindow().setGravity(Gravity.BOTTOM);

        RecyclerView recyclerView = (RecyclerView) alphadialog.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        VoteListAdapter mylistAdapter = new VoteListAdapter(votelist, context);
        recyclerView.setAdapter(mylistAdapter);
        mylistAdapter.notifyDataSetChanged();


        mylistAdapter.setListner(new VoteListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int Pos) {
                switch (v.getId()) {
                    case R.id.linear:


                        //  alphadialog.dismiss();
                        break;
                }
            }
        });

        alphadialog.show();
    }


    @Override
    public int getItemCount() {
        return sublist.size();
    }
}