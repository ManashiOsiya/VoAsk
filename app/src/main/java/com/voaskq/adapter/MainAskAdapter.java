package com.voaskq.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.voaskq.R;
import com.voaskq.activity.AnswerAskActivity;
import com.voaskq.helper.Constant;
import com.voaskq.modal.MainAsk;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class MainAskAdapter extends RecyclerView.Adapter<MainAskAdapter.MyViewHolder> {

    public Context context;
    MyViewHolder myholder;
    ArrayList<MainAsk> mainList = null;
    String tag = "haomeadapter";

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView user_image;
        TextView Username,  questitle,ans_no;
        ImageView main_img;
        LinearLayout mainLinearimg;

        public MyViewHolder(View view) {
            super(view);

            user_image = view.findViewById(R.id.user_image);
            Username = view.findViewById(R.id.Username);
            mainLinearimg = view.findViewById(R.id.mainLinearimg);
            questitle = view.findViewById(R.id.questitle);
            main_img = view.findViewById(R.id.main_img);
            ans_no  = view.findViewById(R.id.ans_no);
            this.setIsRecyclable(false);
        }
    }

    public MainAskAdapter(ArrayList<MainAsk> mainList, Context context) {
        this.mainList = mainList;
        this.context = context;
        setHasStableIds(true);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ask_adapteritem, parent, false);
        myholder = new MyViewHolder(view);
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
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





    }




    @Override
    public int getItemCount() {
        return mainList.size();
    }
}