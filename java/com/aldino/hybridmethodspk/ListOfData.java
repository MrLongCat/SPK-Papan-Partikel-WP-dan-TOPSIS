package com.aldino.hybridmethodspk;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListOfData extends AppCompatActivity {
    ImageButton addBtn;
    Button hitung;
    RelativeLayout background;
    LinearLayout instruksi;
    RecyclerView listItem;
    View garis;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    ArrayList<HashMap<String, String>> list_data = new ArrayList<HashMap<String, String>>();
    DataAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_data);

        listItem=(RecyclerView)findViewById(R.id.listData);
        addBtn=(ImageButton)findViewById(R.id.add_btn);
        background=(RelativeLayout)findViewById(R.id.bg);
        hitung=(Button)findViewById(R.id.hitung);
        instruksi=(LinearLayout)findViewById(R.id.instruksi);
        garis=(View)findViewById(R.id.view);

        listItem.setHasFixedSize(true);
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        listItem.setLayoutManager(recyclerViewLayoutManager);

        if (getIntent()!=null && getIntent().getExtras()!=null){
            list_data = (ArrayList<HashMap<String, String>>)getIntent().getSerializableExtra("list data");
            adapter = new DataAdapter(ListOfData.this,list_data);
            listItem.setAdapter(adapter);
            if (list_data.size()==0){
                background.setBackground(ContextCompat.getDrawable(this,R.drawable.data_kosong));
                hitung.setBackground(ContextCompat.getDrawable(this,R.drawable.btn_background_unactive));
                hitung.setEnabled(false);
            } else if (list_data.size()==1){
                background.setBackgroundResource(0);
                instruksi.setVisibility(View.VISIBLE);
                garis.setVisibility(View.VISIBLE);
                hitung.setBackground(ContextCompat.getDrawable(this,R.drawable.btn_background_unactive));
                hitung.setEnabled(false);
            }
            else {
                background.setBackgroundResource(0);
                instruksi.setVisibility(View.VISIBLE);
                garis.setVisibility(View.VISIBLE);
                hitung.setBackground(ContextCompat.getDrawable(this,R.drawable.btn_background));
                hitung.setEnabled(true);
            }
            listItem.setVisibility(View.VISIBLE);
        } else {
            background.setBackground(ContextCompat.getDrawable(this,R.drawable.data_kosong));
            listItem.setVisibility(View.GONE);
            garis.setVisibility(View.GONE);
            instruksi.setVisibility(View.GONE);
        }

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListOfData.this, addData.class);
                if (!list_data.isEmpty()){
                    intent.putExtra("list data",list_data);
                }
                startActivity(intent);
                finish();
            }
        });

        hitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list_data.size()>1){
                    Intent intent = new Intent(ListOfData.this, MainActivity.class);
                    intent.putExtra("list data", list_data);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(ListOfData.this, "Data harus lebih dari satu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(positionReceiver, new IntentFilter("position-message"));
        LocalBroadcastManager.getInstance(this).registerReceiver(deleteReceiver, new IntentFilter("delete-message"));
        list_data.size();
    }

    public BroadcastReceiver positionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int getPosition = intent.getIntExtra("position",0);
            if (!list_data.isEmpty() && list_data.size()>=getPosition){
                Intent i = new Intent(ListOfData.this, EditData.class);
                i.putExtra("position",getPosition);
                i.putExtra("list data",list_data);
                startActivity(i);
                finish();
            }
        }
    };

    public BroadcastReceiver deleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final int getPosition = intent.getIntExtra("position",0);
            if (!list_data.isEmpty() && list_data.size()>=getPosition) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListOfData.this);
                builder.setTitle("Hapus Data");
                builder.setMessage("Apakah kamu yakin ingin menghapus data ini?");
                builder.setCancelable(false);
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        list_data.remove(getPosition);
                        adapter.notifyDataSetChanged();
                        if (list_data.size()==0){
                            background.setBackground(ContextCompat.getDrawable(ListOfData.this,R.drawable.data_kosong));
                            instruksi.setVisibility(View.GONE);
                            garis.setVisibility(View.GONE);
                        } else if (list_data.size()==1){
                            hitung.setBackground(ContextCompat.getDrawable(ListOfData.this,R.drawable.btn_background_unactive));
                            hitung.setEnabled(false);
                            instruksi.setVisibility(View.VISIBLE);
                            garis.setVisibility(View.VISIBLE);
                        }
                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                if (!isFinishing()){
                    alertDialog.show();
                }
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (list_data.size()==0){
            Intent intent = new Intent(ListOfData.this, MainMenu.class);
            startActivity(intent);
            finish();
        } else if (list_data.size()>0){
            AlertDialog.Builder builder = new AlertDialog.Builder(ListOfData.this);
            builder.setTitle("Peringatan");
            builder.setMessage("Jika kamu kembali semua data kamu akan terhapus. Yakin ingin kembali?");
            builder.setCancelable(false);
            builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(ListOfData.this, MainMenu.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            if (!isFinishing()){
                alertDialog.show();
            }
        }
    }
}