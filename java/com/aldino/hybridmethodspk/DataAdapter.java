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
import java.util.HashMap;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    Context context;
    ArrayList<HashMap<String, String>> list_data;

    public DataAdapter(ListOfData mainActivity, ArrayList<HashMap<String, String>> list_data) {
        this.context = mainActivity;
        this.list_data = list_data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data_view, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position%2 == 0){
            holder.relativeLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.relativeLayout.setBackgroundColor(Color.parseColor("#F7FAFF"));
        }
        holder.ppName.setText(list_data.get(position).get("nama papan"));
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView ppName;
        Button deleteBtn;
        RelativeLayout relativeLayout;
        int position;
        View rootView;

        public ViewHolder(final View itemView){
            super(itemView);
            rootView = itemView;
            ppName = (TextView)itemView.findViewById(R.id.ppName);
            deleteBtn = (Button)itemView.findViewById(R.id.deleteBtn);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.listBg);

            ppName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    position = getAbsoluteAdapterPosition();
//                    String particleName = list_data.get(position).get("particle name");
                    Intent intent = new Intent("position-message");
                    intent.putExtra("position",position);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    position = getAbsoluteAdapterPosition();
                    Intent intent = new Intent("delete-message");
                    intent.putExtra("position",position);
                    intent.putExtra("new list",list_data);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            });
        }
    }
}
