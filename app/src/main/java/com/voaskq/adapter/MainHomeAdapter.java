package com.voaskq.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.voaskq.R;
import com.voaskq.helper.Constant;
import com.voaskq.helper.MyProgressbar;
import com.voaskq.modal.CommentList;
import com.voaskq.modal.MainHome;
import com.voaskq.modal.SubHome;
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

public class MainHomeAdapter extends RecyclerView.Adapter<MainHomeAdapter.MyViewHolder> {

    public Context context;
    MyViewHolder myholder;
    ArrayList<MainHome> mainList = null;
    String tag = "haomeadapter";
    ArrayList<SubHome> subList = null;

    ArrayList<CommentList> comment_List = null;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String Userid;


    public static OnItemClickListener onItemClickListener;
    public static void setListner(OnItemClickListener listner) {
        onItemClickListener = listner;
    }

    public interface OnItemClickListener {
        public void onItemClick(View v, int Pos);


    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView user_image ,user_image_comm;
        TextView Username, report, questitle,allcomment;
        ImageView  deletevote,submitComment;
        RecyclerView home_adapter_recyclerView;
        EditText newComment;

        public MyViewHolder(View view) {
            super(view);

            user_image = view.findViewById(R.id.user_image);
            Username = view.findViewById(R.id.Username);
            report = view.findViewById(R.id.report);
            questitle = view.findViewById(R.id.questitle);

            deletevote = view.findViewById(R.id.deletevote);
            allcomment = view.findViewById(R.id.allcomment);
            user_image_comm = view.findViewById(R.id.user_image_comm);
            newComment = view.findViewById(R.id.newComment);
            submitComment  = view.findViewById(R.id.submitComment);
            deletevote.setOnClickListener(this);

            home_adapter_recyclerView = view.findViewById(R.id.home_adapter_recyclerView);
            home_adapter_recyclerView.setHasFixedSize(true);
            home_adapter_recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

            if (Constant.VOTE_CURRENT_PAGE_SELECTED.equalsIgnoreCase(Constant.VOTE_PROFILE_PAGE_SELECTED)) {
                report.setVisibility(View.GONE);
                deletevote.setVisibility(View.VISIBLE);
            }
        //    this.setIsRecyclable(false);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, getPosition());
            }
        }
    }

    public MainHomeAdapter(ArrayList<MainHome> mainList, Context context) {
        this.mainList = mainList;
        this.context = context;
      //  setHasStableIds(true);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_main_adapteritem, parent, false);

        pref    = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        editor  = pref.edit();
        Userid  = pref.getString("Userid", null);

        myholder = new MyViewHolder(view);
        return myholder;
    }

    SubHomeAdapter sub_adapter = null;

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final MainHome mainobj = mainList.get(position);

        holder.Username.setText(mainobj.getFirst_name() + " " + mainobj.getLast_name());
        holder.questitle.setText(mainobj.getTitle());
        String userimag = context.getResources().getString(R.string.home_userimg_baseurl) + mainobj.getPicture();     //        https://apps.konnectapp.co.nz/voask/assets/profile/2018-11-05-02-33-04_5894_profile_image.jpg

        Picasso.with(context)
                    .load(userimag)
                    .placeholder(R.drawable.app_icon)
                    .error(R.drawable.app_icon)
                    .into(holder.user_image);

        Picasso.with(context)
                .load(userimag)
                .placeholder(R.drawable.app_icon)
                .error(R.drawable.app_icon)
                .into(holder.user_image_comm);

        try {
            JSONArray images_arr = mainobj.getImages_arr();

            Log.e(tag, "images arr= " + images_arr);
            subList = new ArrayList<>();

            for (int k = 0; k < images_arr.length(); k++) {

                JSONObject jobj = images_arr.getJSONObject(k);
                String post_image_id = jobj.getString("post_image_id");
                String post_id = jobj.getString("post_id");
                String post_image = jobj.getString("post_image");
                String total_votes = jobj.getString("total_votes");
                String total_comments = jobj.getString("total_comments");
                String vote_id = jobj.getString("vote_id");

                SubHome sobj = new SubHome(post_image_id, post_id, post_image, total_votes, total_comments, vote_id, mainobj.getTitle());
                subList.add(sobj);
            }

            sub_adapter = new SubHomeAdapter(subList, context);
            holder.home_adapter_recyclerView.setAdapter(sub_adapter);
            sub_adapter.notifyDataSetChanged();

        } catch (Exception e) {

        }

        holder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.report_dialog);

                ImageView cancle;
                RelativeLayout spamtype_relative;
                final TextView spamtype, submit;

                cancle = dialog.findViewById(R.id.cancle);
                spamtype_relative = dialog.findViewById(R.id.spamtype_relative);
                spamtype = dialog.findViewById(R.id.spamtype);
                submit = dialog.findViewById(R.id.submit);

                cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                spamtype_relative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Dialog dialog_spam = new Dialog(context);
                        dialog_spam.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog_spam.setCancelable(true);
                        dialog_spam.setCanceledOnTouchOutside(true);
                        dialog_spam.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog_spam.setContentView(R.layout.report_spam);

                        final TextView markSpam, reportUser;

                        markSpam = dialog_spam.findViewById(R.id.markSpam);
                        reportUser = dialog_spam.findViewById(R.id.reportUser);

                        markSpam.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                spamtype.setText(markSpam.getText().toString());

                                dialog_spam.dismiss();
                            }
                        });

                        reportUser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                spamtype.setText(reportUser.getText().toString());

                                dialog_spam.dismiss();
                            }
                        });

                        dialog_spam.show();

                    }
                });


                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reportSpam(mainobj.getPost_id(), spamtype.getText().toString(),dialog);
                    }
                });

                dialog.show();
            }
        });


        holder.submitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mytext = holder.newComment.getText().toString();

                if(!mytext.equalsIgnoreCase("") || mytext.equalsIgnoreCase(null)  ){

                    addNewComment(holder,mytext,mainobj.getPost_id());
                }else{
                    Toast.makeText(context, "Please enter valid comment", Toast.LENGTH_SHORT).show();
                }
            }
        });


        holder.allcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getCommentList(mainobj.getPost_id());
            }
        });

    }

    private void getCommentList(String post_id) {


        comment_List = new ArrayList<>();

        final Dialog progress_spinner = MyProgressbar.LoadingSpinner(context);
        progress_spinner.show();

        Api api = ApiFactory.getClient().create(Api.class);
        Call<ResponseBody> call = api.getCommentList(Userid,post_id);

        Log.e(tag, "new url : " + call.request().url());

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

                    Log.e(tag,"response>>>>>>>>>"+output);

                    if (jsonObject.getString("status").equalsIgnoreCase("SUCCESS")) {


                        JSONArray result = jsonObject.getJSONArray("result");

                        for(int i=0;i<result.length();i++){

                            JSONObject jobj = result.getJSONObject(i);


                            String id              = jobj.getString("id");
                            String post_id         = jobj.getString("post_id");
                            String comment         = jobj.getString("comment");
                            String user_id         = jobj.getString("user_id");
                            String date_time       = jobj.getString("date_time");
                            String user_name       = jobj.getString("user_name");
                            String first_name      = jobj.getString("first_name");
                            String last_name       = jobj.getString("last_name");
                            String mobile_number   = jobj.getString("mobile_number");
                            String email_address   = jobj.getString("email_address");
                            String gender          = jobj.getString("gender");
                            String create_date     = jobj.getString("create_date");
                            String create_by       = jobj.getString("create_by");
                            String update_date     = jobj.getString("update_date");
                            String update_by       = jobj.getString("update_by");
                            String is_active       = jobj.getString("is_active");
                            String password        = jobj.getString("password");
                            String address         = jobj.getString("address");
                            String zipcode         = jobj.getString("zipcode");
                            String city            = jobj.getString("city");
                            String is_approved     = jobj.getString("is_approved");
                            String picture         = jobj.getString("picture");
                            String about           = jobj.getString("about");
                            String block_status    = jobj.getString("block_status");

                            CommentList commlist = new CommentList(id, post_id, comment, user_id, date_time, user_name, first_name, last_name, mobile_number, email_address, gender, create_date, create_by, update_date, update_by, is_active, password, address, zipcode, city, is_approved, picture, about, block_status);
                            comment_List.add(commlist);

                        }

                        showCommetList(comment_List);

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


    void showCommetList(ArrayList<CommentList> commentlist){

        final Dialog alphadialog = new Dialog(context);
        alphadialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alphadialog.setCancelable(true);
        alphadialog.setCanceledOnTouchOutside(true);
        alphadialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alphadialog.setContentView(R.layout.listview_popup);

        // WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        // lp.copyFrom(alphadialog.getWindow().getAttributes());
        // lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        // lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        // alphadialog.show();
        // alphadialog.getWindow().setAttributes(lp);

        RecyclerView   recyclerView = (RecyclerView) alphadialog.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        CommentListAdapter mylistAdapter = new CommentListAdapter(commentlist, context);
        recyclerView.setAdapter(mylistAdapter);
        mylistAdapter.notifyDataSetChanged();


        mylistAdapter.setListner(new CommentListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int Pos) {
                switch (v.getId()) {
                    case R.id.linear:


                 //       alphadialog.dismiss();
                        break;
                }
            }
        });

        alphadialog.show();
    }


    private void reportSpam(String postid, final String spamtype, final Dialog dialog) {

        final Dialog progress_spinner = MyProgressbar.LoadingSpinner(context);
        progress_spinner.show();

        Api api = ApiFactory.getClient().create(Api.class);
        Call<ResponseBody> call = api.reportSpam(Userid,postid,spamtype);

        Log.e(tag, "new url : " + call.request().url());

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


                        dialog.dismiss();


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

    private void addNewComment(final MyViewHolder holder, String mytext, String post_id) {

        final Dialog progress_spinner = MyProgressbar.LoadingSpinner(context);
        progress_spinner.show();

        Api api = ApiFactory.getClient().create(Api.class);
        Call<ResponseBody> call = api.addNewComment(Userid,mytext,post_id);

        Log.e(tag, "new url : " + call.request().url());

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


                        holder.newComment.setText("");


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




    @Override
    public int getItemCount() {
        return mainList.size();
    }
}