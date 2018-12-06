package com.voaskq.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.voaskq.R;
import com.voaskq.activity.AnswerAskActivity;
import com.voaskq.helper.Constant;
import com.voaskq.helper.MyProgressbar;
import com.voaskq.modal.MainAsk;
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

public class MainAskAdapter extends RecyclerView.Adapter<MainAskAdapter.MyViewHolder> {

    public Context context;
    MyViewHolder myholder;
    ArrayList<MainAsk> mainList = null;
    String tag = "haomeadapter";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String Userid;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView user_image;
        TextView Username,  questitle,ans_no;
        ImageView main_img,deleteask;
        LinearLayout mainLinearimg,clickLinear;

        public MyViewHolder(View view) {
            super(view);

            user_image = view.findViewById(R.id.user_image);
            Username = view.findViewById(R.id.Username);
            mainLinearimg = view.findViewById(R.id.mainLinearimg);
            questitle = view.findViewById(R.id.questitle);
            main_img = view.findViewById(R.id.main_img);
            ans_no  = view.findViewById(R.id.ans_no);
            deleteask  = view.findViewById(R.id.deleteask);
            clickLinear  = view.findViewById(R.id.clickLinear);
        //    this.setIsRecyclable(false);

           if (Constant.ASK_CURRENT_PAGE_SELECTED.equalsIgnoreCase(Constant.ASK_PROFILE_PAGE_SELECTED)) {
               deleteask.setVisibility(View.VISIBLE);
           }
        }

    }

    public MainAskAdapter(ArrayList<MainAsk> mainList, Context context) {
        this.mainList = mainList;
        this.context = context;
      //  setHasStableIds(true);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ask_adapteritem, parent, false);
        myholder = new MyViewHolder(view);

        pref    = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        editor  = pref.edit();
        Userid  = pref.getString("Userid", null);


        return myholder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final MainAsk mainobj = mainList.get(position);

        holder.Username.setText(mainobj.getFirst_name()+" "+mainobj.getLast_name());
        holder.questitle.setText(mainobj.getDescription());
        final String userimag = context.getResources().getString(R.string.home_userimg_baseurl) + mainobj.getPicture();     //        https://apps.konnectapp.co.nz/voask/assets/profile/2018-11-05-02-33-04_5894_profile_image.jpg
        holder.ans_no.setText(mainobj.getTotal_answers());

        if (userimag.equals("")) {

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

        final String ask_imag = context.getResources().getString(R.string.home_adapter_postimg_baseurl) + mainobj.getAsk_picture();

        Log.e(tag," mainobj.getAsk_picture()~~"+ mainobj.getAsk_picture());

        if(mainobj.getAsk_picture().equalsIgnoreCase(null) || mainobj.getAsk_picture().equalsIgnoreCase("null")){
            holder.mainLinearimg.setVisibility(View.GONE);
        }

        if (userimag.equals("")) {

            Picasso.with(context)
                    .load(R.drawable.app_icon)
                    .error(R.drawable.app_icon)
                    .into(holder.main_img);

        } else {
            Picasso.with(context)
                    .load(ask_imag)
                    .placeholder(R.drawable.app_icon)
                    .error(R.drawable.app_icon)
                    .into(holder.main_img);
        }
        holder.clickLinear .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constant.ASK_QUESTION_ID = mainobj.getQuestion_id();

                Constant.ASK_USERIMAGE = userimag;
                Constant.ASK_USERNAME = holder.Username.getText().toString();
                Constant.ASK_QUES =  holder.questitle.getText().toString();
                Constant.ASK_NO = holder.ans_no.getText().toString();
                Constant.ASK_IMAGE = ask_imag;

                Intent in = new Intent(context, AnswerAskActivity.class);
                context.startActivity(in);
            }
        });


        holder.deleteask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeletePopup(mainobj.getQuestion_id(),position);
            }
        });

    }


    private void showDeletePopup(final  String question_id,final int position) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.message);

        final TextView tvmsg = (TextView) dialog.findViewById(R.id.msg);
        TextView done = dialog.findViewById(R.id.btn_done);
        TextView cancel = dialog.findViewById(R.id.btn_cancel);

        done.setText("Yes");
        cancel.setText("No");

        tvmsg.setText("Do You Want Delete Post");
        tvmsg.setGravity(Gravity.CENTER);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteASk(question_id,position);
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void deleteASk(String question_id, final int position) {

        final Dialog progress_spinner = MyProgressbar.LoadingSpinner(context);
        progress_spinner.show();
        Api api = ApiFactory.getClient().create(Api.class);
        Call<ResponseBody> call = api.deleteAsk(Userid,question_id);
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

                        mainList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mainList.size());

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