package com.aldino.hybridmethodspk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.ViewHolder> {
    Context context;
    LinkedHashMap<String,Double> list_data;
    List<Double> list_rank;
    int rank=1;

    public RecommendationAdapter(MainActivity mainActivity, LinkedHashMap<String,Double> list_data, List<Double> list_rank){
        this.context=mainActivity;
        this.list_data=list_data;
        this.list_rank=list_rank;
    }


    @NonNull
    @Override
    public RecommendationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_rec_view, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendationAdapter.ViewHolder holder, int position) {
        holder.ppName.setText(String.valueOf(list_data.keySet().toArray()[position]));
        if (position>0){
            if (!list_rank.get(position).equals(list_rank.get(position-1))){
                rank++;
                holder.ranking.setText(String.valueOf(rank));
            } else {
                holder.ranking.setText(String.valueOf(rank));
            }
        }
        if (rank==1){
            holder.border.setBackground(ContextCompat.getDrawable(context,R.drawable.blue_color));
            holder.ranking.setBackground(ContextCompat.getDrawable(context,R.drawable.blue_circle));
        } else if (rank==2){
            holder.border.setBackground(ContextCompat.getDrawable(context,R.drawable.green_color));
            holder.ranking.setBackground(ContextCompat.getDrawable(context,R.drawable.green_circle));
        } else if (rank==3){
            holder.border.setBackground(ContextCompat.getDrawable(context,R.drawable.orange_color));
            holder.ranking.setBackground(ContextCompat.getDrawable(context,R.drawable.orange_circle));
        } else {
            holder.border.setBackground(ContextCompat.getDrawable(context,R.drawable.grey_color));
            holder.ranking.setBackground(ContextCompat.getDrawable(context,R.drawable.gret_circle));
        }
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        int position;
        View rootView;
        View border;
        TextView ppName;
        TextView ranking;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            ppName = (TextView)itemView.findViewById(R.id.ppName);
            ranking =(TextView)itemView.findViewById(R.id.ranking);
            border =(View)itemView.findViewById(R.id.garis);

            ppName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    position = getAbsoluteAdapterPosition();
                    Intent intent = new Intent("get-rec");
                    intent.putExtra("position",position);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            });

        }
    }
}
