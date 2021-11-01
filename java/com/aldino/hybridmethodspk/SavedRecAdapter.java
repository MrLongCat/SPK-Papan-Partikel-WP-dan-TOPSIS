package com.aldino.hybridmethodspk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

public class SavedRecAdapter extends RecyclerView.Adapter<SavedRecAdapter.ViewHolder> {
    Context context;
    ArrayList<String> list_data;

    public SavedRecAdapter(SavedRecommendation mainActivity, ArrayList<String> list_data){
        this.context=mainActivity;
        this.list_data=list_data;
    }

    @Override
    public SavedRecAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_saved_rec_view,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedRecAdapter.ViewHolder holder, int position) {
        holder.recName.setText(list_data.get(position));
        if (position%2==0){
            holder.background.setBackground(ContextCompat.getDrawable(context,R.drawable.form_border));
            holder.recName.setTextColor(Color.parseColor("#2D7DD2"));
            holder.forward.setBackground(ContextCompat.getDrawable(context,R.drawable.ic_arrow_forward));
        } else {
            holder.background.setBackground(ContextCompat.getDrawable(context,R.drawable.blue_color));
            holder.recName.setTextColor(Color.parseColor("#FFFFFF"));
            holder.forward.setBackground(ContextCompat.getDrawable(context,R.drawable.ic_arrow_forward_white));
        }
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        int position;
        View rootView;
        RelativeLayout background;
        Button forward;
        TextView recName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            recName = (TextView)itemView.findViewById(R.id.ppName);
            background = (RelativeLayout)itemView.findViewById(R.id.background);
            forward = (Button)itemView.findViewById(R.id.forward);

            recName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    position = getAbsoluteAdapterPosition();
                    Intent intent = new Intent("savedRec-message");
                    intent.putExtra("rec",list_data.get(position));
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            });

            recName.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    position = getAbsoluteAdapterPosition();
                    Intent i = new Intent("deleteRec-message");
                    i.putExtra("position",position);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(i);
                    return true;
                }
            });
        }
    }
}
