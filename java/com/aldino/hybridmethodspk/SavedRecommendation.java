package com.aldino.hybridmethodspk;

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
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SavedRecommendation extends AppCompatActivity {
    RecyclerView savedRec;
    ProgressBar progressBar;
    RelativeLayout background;
    LinearLayout instruksi;
    View garis;
    TextView loading;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    ArrayList<String> ListRec = new ArrayList<String>();
    SavedRecAdapter adapter;

    private String androidID;

    private static String URL_GET_RECOMMENDATION = "http://192.168.43.146/SPK_ParticleBoard/api/GET_SavedRecommendation.php";
    private static String URL_DELETE_RECOMMENDATION = "http://192.168.43.146/SPK_ParticleBoard/api/DELETE_SavedRecommendation.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_recommendation);

        savedRec = (RecyclerView)findViewById(R.id.rv);
        progressBar=(ProgressBar)findViewById(R.id.progress);
        loading=(TextView)findViewById(R.id.memuat);
        background=(RelativeLayout)findViewById(R.id.background);
        instruksi=(LinearLayout)findViewById(R.id.instruksi);
        garis=(View)findViewById(R.id.line);
        garis.setVisibility(View.GONE);
        instruksi.setVisibility(View.GONE);
        savedRec.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        loading.setVisibility(View.VISIBLE);

        savedRec.setHasFixedSize(true);
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        savedRec.setLayoutManager(recyclerViewLayoutManager);

        androidID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        getSavedRec();

        LocalBroadcastManager.getInstance(this).registerReceiver(selectedRec, new IntentFilter("savedRec-message"));
        LocalBroadcastManager.getInstance(this).registerReceiver(deleteRec, new IntentFilter("deleteRec-message"));
    }

    private void getSavedRec() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_GET_RECOMMENDATION+"?id="+androidID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONArray("result");
                    if (data.length()>0){
                        for(int i=0; i<data.length(); i++) {  // MENYIMPAN DATA MAKANAN DARI JSON KEDALAM LIST_DATA
                            JSONObject listData = data.getJSONObject(i);
                            String name = listData.getString("nama_rec");
                            ListRec.add(name);
                        }
                    } else {
                        background.setBackground(ContextCompat.getDrawable(SavedRecommendation.this,R.drawable.data_rec_kosong));
                        garis.setVisibility(View.GONE);
                        instruksi.setVisibility(View.GONE);
                        savedRec.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);
                    }

                    if (ListRec.size()>0){
                        adapter = new SavedRecAdapter(SavedRecommendation.this,ListRec);
                        background.setBackgroundResource(0);
                        savedRec.setAdapter(adapter);
                        garis.setVisibility(View.VISIBLE);
                        instruksi.setVisibility(View.VISIBLE);
                        savedRec.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog alertDialog = new AlertDialog.Builder(SavedRecommendation.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Check your connection and try again");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
//                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        RequestQueue Queue = Volley.newRequestQueue(SavedRecommendation.this);
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 0;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        Queue.add(stringRequest);
    }

    public BroadcastReceiver selectedRec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String nama = intent.getStringExtra("rec");
            Intent i = new Intent(SavedRecommendation.this,SavedData.class);
            i.putExtra("rec",nama);
            startActivity(i);
        }
    };

    public BroadcastReceiver deleteRec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int getPosition = intent.getIntExtra("position",0);
            if (!ListRec.isEmpty() && ListRec.size()>=getPosition) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SavedRecommendation.this);
                builder.setTitle("Hapus Data");
                builder.setMessage("Apakah kamu yakin ingin menghapus data ini?");
                builder.setCancelable(false);
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteSavedRec(getPosition);
                        garis.setVisibility(View.GONE);
                        instruksi.setVisibility(View.GONE);
                        savedRec.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.VISIBLE);
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

    private void deleteSavedRec(int position) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DELETE_RECOMMENDATION+"?id="+androidID+"&namaRec="+ListRec.get(position), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String getResult = jsonObject.getString("result");
                    if (getResult.equals("SUKSES")){
                        ListRec.remove(position);
                        adapter.notifyDataSetChanged();
                        if (ListRec.size()==0){
                            background.setBackground(ContextCompat.getDrawable(SavedRecommendation.this,R.drawable.data_rec_kosong));
                            garis.setVisibility(View.GONE);
                            instruksi.setVisibility(View.GONE);
                            savedRec.setVisibility(View.GONE);
                        } else {
                            background.setBackgroundResource(0);
                            garis.setVisibility(View.VISIBLE);
                            instruksi.setVisibility(View.VISIBLE);
                            savedRec.setVisibility(View.VISIBLE);
                        }
                        progressBar.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(SavedRecommendation.this, "Terjadi Kesalahan, Harap coba lagi nanti", Toast.LENGTH_SHORT).show();
                        savedRec.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog alertDialog = new AlertDialog.Builder(SavedRecommendation.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Check your connection and try again");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
//                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        RequestQueue Queue = Volley.newRequestQueue(SavedRecommendation.this);
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 0;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        Queue.add(stringRequest);
    }

}