package com.voaskq.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.voaskq.R;
import com.voaskq.modal.CommentList;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.MyViewHolder> {

    public Context context;
    CommentListAdapter.MyViewHolder myholder;
    private ArrayList<CommentList> contactslist = null;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String Userid;

    public static CommentListAdapter.OnItemClickListener onItemClickListener;

    public static void setListner(CommentListAdapter.OnItemClickListener listner) {
        onItemClickListener = listner;
    }

    public interface OnItemClickListener {
        public void onItemClick(View v, int Pos);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView fname,mcommenttext;
        LinearLayout linear;
        CircleImageView user_image;

        public MyViewHolder(View view) {
            super(view);
            fname   = view.findViewById(R.id. name );
            linear   = view.findViewById(R.id. linear );
            user_image = view.findViewById(R.id. user_image );
            mcommenttext = view.findViewById(R.id.mcommenttext );
            linear.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, getPosition());
            }
        }
    }

    public CommentListAdapter(ArrayList<CommentList> contactslist, Context context) {
        this.contactslist = contactslist;
        this.context = context;
    }

    @Override
    public CommentListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item, parent, false);

        myholder = new CommentListAdapter.MyViewHolder(view);
        return myholder;
    }

    @Override
    public void onBindViewHolder(final CommentListAdapter.MyViewHolder holder, final int position) {

        CommentList votelist = contactslist.get(position);

        holder.fname.setText(votelist.getFirst_name()+" "+votelist.getLast_name());

        holder.mcommenttext.setText(votelist.getComment()+"");

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