package com.voaskq.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.voaskq.Fragment.NewVideoFragment;
import com.voaskq.R;
import com.voaskq.activity.ViewBroadcastItem;
import com.voaskq.modal.BroadcastList;

import java.util.ArrayList;

public class NewListAdapter extends RecyclerView.Adapter<NewListAdapter.MyViewHolder> {

    public Context context;
    MyViewHolder myholder;
    ArrayList<BroadcastList> mainList = null;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView status;
        public MyViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.img);
            status = view.findViewById(R.id.status);
            this.setIsRecyclable(false);
        }
    }

    public NewListAdapter(ArrayList<BroadcastList> mainList, Context context) {
        this.mainList = mainList;
        this.context = context;
        setHasStableIds(true);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);

        myholder = new MyViewHolder(view);

        return myholder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final BroadcastList mainobj = mainList.get(position);
        holder.status.setText(mainobj.getType());

        String userimag = mainobj.getPreview();

        if (userimag.equals("")) {
            Picasso.with(context)
                    .load(R.drawable.app_icon)
                    .error(R.drawable.app_icon)
                    .into(holder.img);
        } else {
            Picasso.with(context)
                    .load(userimag)
                    .placeholder(R.drawable.app_icon)
                    .error(R.drawable.app_icon)
                    .into(holder.img);
        }

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NewVideoFragment.SELECTED_VIDEO = mainobj.getResourceuri();
                Intent in =new Intent(context,ViewBroadcastItem.class);
                context.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mainList.size();
    }
}