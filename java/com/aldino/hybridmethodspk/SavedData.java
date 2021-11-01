package com.aldino.hybridmethodspk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class SavedData extends AppCompatActivity {

    RecyclerView rv;
    ProgressBar progressBar;
    TextView loading,info;
    LinearLayout textInfo;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    SavedDataAdapter adapter;
    ArrayList<HashMap<String,String>> list_data = new ArrayList<HashMap<String, String>>();
    private String androidID;
    String namaRec;

    private static String URL_GET_DATA = "http://192.168.43.146/SPK_ParticleBoard/api/GET_SavedData.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_data);

        rv = (RecyclerView)findViewById(R.id.data);
        progressBar=(ProgressBar)findViewById(R.id.progress);
        loading=(TextView)findViewById(R.id.memuat);
        info=(TextView)findViewById(R.id.info);
        textInfo=(LinearLayout)findViewById(R.id.info_text);

        rv.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        loading.setVisibility(View.VISIBLE);

        rv.setHasFixedSize(true);
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(recyclerViewLayoutManager);

        androidID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textInfo.getVisibility()==View.GONE){
                    textInfo.setVisibility(View.VISIBLE);
                } else {
                    textInfo.setVisibility(View.GONE);
                }
            }
        });

        if (getIntent()!=null && getIntent().getExtras()!=null){
            namaRec=getIntent().getExtras().getString("rec");
            getSavedData(namaRec);
        }

    }

    public BroadcastReceiver selectedData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int position = intent.getIntExtra("position",0);
            Intent i = new Intent(SavedData.this,DataDetails.class);
            i.putExtra("list data",list_data);
            i.putExtra("nama rec",namaRec);
            i.putExtra("nama papan", list_data.get(position).get("nama papan"));
            i.putExtra("position", position);
            startActivity(i);
        }
    };

    private void getSavedData(String namaRec) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_GET_DATA+"?id="+androidID+"&namaRec="+namaRec, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONArray("result");
                    if (data.length()>0){
                        list_data.clear();
                        for(int i=0; i<data.length(); i++) {
                            JSONObject listData = data.getJSONObject(i);
                            HashMap<String,String> map = new HashMap<String, String>();
                            String namePP = listData.getString("nama_papan");
                            map.put("nama papan",namePP);
                            String posisi = listData.getString("posisi");
                            map.put("posisi",posisi);
                            String kerapatan = listData.getString("kerapatan");
                            map.put("kerapatan",kerapatan);
                            String kdrAir = listData.getString("kadar_air");
                            map.put("kadar air",kdrAir);
                            String pTebal = listData.getString("pengembangan_tebal");
                            map.put("pengembangan tebal",pTebal);
                            String dSerap = listData.getString("daya_serap_air");
                            map.put("daya serap",dSerap);
                            String elastis = listData.getString("modulus_elastisitas");
                            map.put("mod elastis",elastis);
                            String patah = listData.getString("modulus_patah");
                            map.put("mod patah",patah);
                            String sekrup = listData.getString("kuat_pegang_sekrup");
                            map.put("pegang sekrup",sekrup);
                            String rekatInt = listData.getString("kuat_rekat_internal");
                            map.put("rekat internal",rekatInt);
                            String kasar = listData.getString("partikel_kasar_permukaan");
                            map.put("partikel kasar",kasar);
                            String serbuk = listData.getString("diameter_noda_serbuk");
                            map.put("noda serbuk",serbuk);
                            String minyak = listData.getString("diameter_noda_minyak");
                            map.put("noda minyak",minyak);

                            list_data.add(map);
                        }
                    } else {
                        Toast.makeText(SavedData.this, "Data Empty", Toast.LENGTH_SHORT).show();
                    }

                    if (list_data.size()>0){
                        adapter = new SavedDataAdapter(SavedData.this,list_data);
                        rv.setAdapter(adapter);
                        rv.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);
                    } else {
                        rv.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        loading.setVisibility(View.VISIBLE);
                        loading.setText("Data Kosong");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog alertDialog = new AlertDialog.Builder(SavedData.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Check your connection and try again");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
        RequestQueue Queue = Volley.newRequestQueue(SavedData.this);
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


    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(selectedData, new IntentFilter("selectedData-message"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(selectedData);
    }
}