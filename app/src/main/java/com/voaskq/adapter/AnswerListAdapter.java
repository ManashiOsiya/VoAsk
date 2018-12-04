package com.voaskq.adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.voaskq.R;
import com.voaskq.modal.AnswerList;
import com.voaskq.modal.Search;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnswerListAdapter extends RecyclerView.Adapter<AnswerListAdapter.MyViewHolder> {
    public Context context;
    MyViewHolder myholder;
    private ArrayList<AnswerList> searchlist = null;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String Userid;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView answer;
        CircleImageView user_image;

        public MyViewHolder(View view) {
            super(view);
            user_image=  view.findViewById( R.id.user_image);
            answer      = (TextView) view.findViewById( R.id.answer);
            this.setIsRecyclable(false);
        }
    }

    public AnswerListAdapter(ArrayList<AnswerList> contactslist, Context context) {
        this.searchlist = contactslist;
        this.context = context;
        setHasStableIds(true);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answerlist_adapter, parent, false);


        pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        editor = pref.edit();
        Userid = pref.getString("Userid", null);

        myholder = new MyViewHolder(view);

        return myholder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final AnswerList search = searchlist.get(position);

        holder.answer.setText(search.getAnswer());

        String userimag = context.getResources().getString(R.string.home_userimg_baseurl) + search.getPicture();

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

    }

    @Override
    public int getItemCount() {

        return searchlist.size();

    }
}