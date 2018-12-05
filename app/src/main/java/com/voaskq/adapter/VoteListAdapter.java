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
import com.voaskq.modal.VoteList;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class VoteListAdapter extends RecyclerView.Adapter<VoteListAdapter.MyViewHolder> {

    public Context context;
    VoteListAdapter.MyViewHolder myholder;
    private ArrayList<VoteList> contactslist = null;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String Userid;

    public static VoteListAdapter.OnItemClickListener onItemClickListener;

    public static void setListner(VoteListAdapter.OnItemClickListener listner) {
        onItemClickListener = listner;
    }

    public interface OnItemClickListener {
        public void onItemClick(View v, int Pos);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView fname;
        LinearLayout linear;
        CircleImageView user_image;

        public MyViewHolder(View view) {
            super(view);
            fname   = view.findViewById(R.id. name );
            linear   = view.findViewById(R.id. linear );
            user_image = view.findViewById(R.id. user_image );
            linear.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, getPosition());
            }
        }
    }

    public VoteListAdapter(ArrayList<VoteList> contactslist, Context context) {
        this.contactslist = contactslist;
        this.context = context;
    }

    @Override
    public VoteListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vote_list_item, parent, false);

        myholder = new VoteListAdapter.MyViewHolder(view);
        return myholder;
    }

    @Override
    public void onBindViewHolder(final VoteListAdapter.MyViewHolder holder, final int position) {

        VoteList votelist = contactslist.get(position);

        holder.fname.setText(votelist.getFirst_name()+" "+votelist.getLast_name());

        final String userimag = context.getResources().getString(R.string.home_userimg_baseurl) + votelist.getPicture();

        Picasso.with(context)
                .load(userimag)
                .placeholder(R.drawable.app_icon)
                .error(R.drawable.app_icon)
                .into(holder.user_image);
    }

    @Override
    public int getItemCount() {
        return contactslist.size();
    }
}