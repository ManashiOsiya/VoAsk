package com.voaskq.adapter;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.voaskq.R;
import com.voaskq.activity.ViewCurrentUserData;
import com.voaskq.helper.Constant;
import com.voaskq.helper.MyProgressbar;
import com.voaskq.modal.Search;
import com.voaskq.webservices.Api;
import com.voaskq.webservices.ApiFactory;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    public Context context;
    MyViewHolder myholder;
    private ArrayList<Search> searchlist = null;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String Userid;
    String tag="SearchAdapter";
    View mView;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView username,followingtext,follow_btn,unfollow_btn;
        CircleImageView user_image;
        LinearLayout unfollow_linear;

        public MyViewHolder(View view) {
            super(view);

            mView =view;

            user_image=  view.findViewById( R.id.user_image);
            username      = (TextView) view.findViewById( R.id.username);
            followingtext     = (TextView) view.findViewById( R.id.followingtext);
            follow_btn      = (TextView) view.findViewById( R.id.follow_btn);
            unfollow_btn =  view.findViewById( R.id.unfollow_btn);
            unfollow_linear =  view.findViewById( R.id.unfollow_linear);
            this.setIsRecyclable(false);
        }
    }

    public SearchAdapter(ArrayList<Search> contactslist, Context context) {
        this.searchlist = contactslist;
        this.context = context;
        setHasStableIds(true);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_adapter, parent, false);


        pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        editor = pref.edit();
        Userid = pref.getString("Userid", null);

        myholder = new MyViewHolder(view);

        return myholder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Search search = searchlist.get(position);

        holder.username.setText( search.getFirst_name() +" "+search.getLast_name() );

        final String userimag = context.getResources().getString(R.string.home_userimg_baseurl) + search.getPicture();

        String follow= search.getFollow_id();

        if(follow.equalsIgnoreCase("")){

            holder.follow_btn.setVisibility(View.VISIBLE);
            holder.unfollow_linear.setVisibility(View.GONE);
        }else{          // to unfollow
            holder.follow_btn.setVisibility(View.GONE);
            holder.unfollow_linear.setVisibility(View.VISIBLE);
        }

        if (userimag.equals("")|| userimag.equals(null)) {

            Picasso.with(context)
                    .load(R.drawable.app_icon)
                    .error(R.drawable.app_icon)
                    .into(holder.user_image);

        } else {
            Picasso.with(context)
                    .load(userimag)
                    .placeholder(R.drawable.app_icon)
                    .error(R.drawable.app_icon)
                    .into(holder.user_image);
        }

        holder.follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFollow(search.getUser_id(), holder,search);
            }
        });

        holder.unfollow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getUnFollow(search.getFollow_id(), holder,search);

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constant.OTHER_USER_USERID =   search.getUser_id()   ;
                Constant.OTHER_USER_FIRSTNAME =  search.getFirst_name() ;
                Constant.OTHER_USER_LASTNAME =  search.getLast_name()  ;
                Constant.OTHER_USER_EMAIL =  search.getEmail_address()     ;
                Constant.OTHER_USER_PROFILEIMAGE = userimag    ;

                Intent in = new Intent(context, ViewCurrentUserData.class);
               context.startActivity(in);
            }
        });


    }

    private void getFollow(String tofollow, final MyViewHolder holder,  final Search search) {

     //   final Dialog progress_spinner = MyProgressbar.LoadingSpinner(context);
     //   progress_spinner.show();

        Log.e(tag,"~~~~~~~~~~~~Userid~~~~~~~~~~~~~~"+Userid);

        Api api = ApiFactory.getClient().create(Api.class);
        Call<ResponseBody> call = api.getFollow(Userid,tofollow);
        Log.e(tag, Userid+" getHomeData: " + call.request().url());
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
//                    progress_spinner.dismiss();
                    if (jsonObject.getString("status").equalsIgnoreCase("SUCCESS")) {

                        String result = jsonObject.getString("result");

                        holder.follow_btn.setVisibility(View.GONE);
                        holder.unfollow_linear.setVisibility(View.VISIBLE);

                        Toast.makeText(context, "successfully done", Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("FAILED")) {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
//                    progress_spinner.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                progress_spinner.dismiss();
            }
        });
    }

    private void getUnFollow(String followid, final MyViewHolder holder, final Search search) {

      //  final Dialog progress_spinner = MyProgressbar.LoadingSpinner(context);
      //  progress_spinner.show();

        Api api = ApiFactory.getClient().create(Api.class);
        Call<ResponseBody> call = api.getUnFollow(Userid,followid);
        Log.e(tag, Userid+" getHomeData: " + call.request().url());
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
//                    progress_spinner.dismiss();
                    if (jsonObject.getString("status").equalsIgnoreCase("SUCCESS")) {

                        String result = jsonObject.getString("result");

                        holder.follow_btn.setVisibility(View.VISIBLE);
                        holder.unfollow_linear.setVisibility(View.GONE);

                        Toast.makeText(context, "successfully done", Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("FAILED")) {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
//                    progress_spinner.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                progress_spinner.dismiss();
            }
        });
    }


    @Override
    public int getItemCount() {

        return searchlist.size();

    }
}