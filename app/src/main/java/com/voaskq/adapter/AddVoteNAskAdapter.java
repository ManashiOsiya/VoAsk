package com.voaskq.adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.voaskq.R;
import com.voaskq.activity.RegistrationActivity;
import com.voaskq.modal.AddVoteNAsk;

import java.io.File;
import java.util.ArrayList;

public class AddVoteNAskAdapter extends RecyclerView.Adapter<AddVoteNAskAdapter.MyViewHolder> {
    public Context context;
    MyViewHolder myholder;
    private ArrayList<File> list = null;


    public static OnItemClickListener onItemClickListener;

    public static void setListner(OnItemClickListener listner) {
        onItemClickListener = listner;
    }

    public interface OnItemClickListener {
        public void onItemClick(View v, int Pos);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView previewImage,delete;

        public MyViewHolder(View view) {
            super(view);
            previewImage= (ImageView) view.findViewById( R.id.previewImage);
            delete = (ImageView) view.findViewById( R.id.delete);
          //  this.setIsRecyclable(false);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, getPosition());
            }
        }
    }

    public AddVoteNAskAdapter(ArrayList<File> contactslist, Context context) {
        this.list = contactslist;
        this.context = context;
     //   setHasStableIds(true);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_vote_n_ask_adapter, parent, false);
        myholder = new MyViewHolder(view);
        return myholder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        File mFile  = list.get(position);

        if(!mFile.equals(null)){

            Picasso.with(context)
                    .load(mFile)
                    .error(R.mipmap.ic_launcher)
                    .fit()
                    .into(holder.previewImage);
        }





    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
