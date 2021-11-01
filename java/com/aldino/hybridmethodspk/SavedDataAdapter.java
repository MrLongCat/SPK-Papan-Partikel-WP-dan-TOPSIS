package com.aldino.hybridmethodspk;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

public class SavedDataAdapter extends RecyclerView.Adapter<SavedDataAdapter.ViewHolder> {
    Context context;
    ArrayList<HashMap<String,String>> list_data;

    public SavedDataAdapter(SavedData mainActivity, ArrayList<HashMap<String,String>> list_data){
        this.context=mainActivity;
        this.list_data=list_data;
    }

    @Override
    public SavedDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_saved_data_view,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedDataAdapter.ViewHolder holder, int position) {
        holder.ppName.setText(list_data.get(position).get("nama papan"));
        String peringkat = list_data.get(position).get("posisi");
        holder.rank.setText(peringkat);
        if (peringkat.equals("1")){
            holder.border.setBackground(ContextCompat.getDrawable(context,R.drawable.blue_color));
            holder.rank.setBackground(ContextCompat.getDrawable(context,R.drawable.blue_circle));
        } else if (peringkat.equals("2")){
            holder.border.setBackground(ContextCompat.getDrawable(context,R.drawable.green_color));
            holder.rank.setBackground(ContextCompat.getDrawable(context,R.drawable.green_circle));
        } else if (peringkat.equals("3")){
            holder.border.setBackground(ContextCompat.getDrawable(context,R.drawable.orange_color));
            holder.rank.setBackground(ContextCompat.getDrawable(context,R.drawable.orange_circle));
        } else {
            holder.border.setBackground(ContextCompat.getDrawable(context,R.drawable.grey_color));
            holder.rank.setBackground(ContextCompat.getDrawable(context,R.drawable.gret_circle));
        }
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        int position;
        View rootView;
        TextView ppName,rank;
        View border;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            ppName = (TextView)itemView.findViewById(R.id.ppName);
            rank = (TextView)itemView.findViewById(R.id.ranking);
            border =(View)itemView.findViewById(R.id.garis);

            ppName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    position = getAbsoluteAdapterPosition();
                    Intent intent = new Intent("selectedData-message");
                    intent.putExtra("position",position);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            });
        }
    }
}
