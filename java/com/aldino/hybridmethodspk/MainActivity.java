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
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {
    Button simpanRec;
    TextView progressInfo, infoBtn, runningTime;
    LinearLayout infoText;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    RecommendationAdapter adapter;
    NumberFormat df = new DecimalFormat("#.######");
    ArrayList<String> namaPapanDesc;
    long start, finish;

    private static String URL_BOBOT = "http://192.168.43.146/SPK_ParticleBoard/api/GET_KriteriaAndSub.php"; //"https://particleboardrec.000webhostapp.com/api/GET_KriteriaAndSub.php";
    private static String URL_SIMPAN = "http://192.168.43.146/SPK_ParticleBoard/api/SAVE_Recommendation.php";//"https://particleboardrec.000webhostapp.com/api/SAVE_Recommendation.php";//
    private static String URL_CHECK = "http://192.168.43.146/SPK_ParticleBoard/api/CHECK_PPname.php";//"https://particleboardrec.000webhostapp.com/api/CHECK_PPname.php";//

    ArrayList<HashMap<String, String>> list_data = new ArrayList<HashMap<String, String>>();

    //DATA HASIL REKOMENDASI
    final LinkedHashMap<String,Double> reverseSortedMap = new LinkedHashMap<>();
    //RANK
    List<Integer> rank;

    private String androidID;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.recycleView);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        simpanRec = (Button)findViewById(R.id.save);
        progressInfo = (TextView)findViewById(R.id.progress_text);
        infoBtn = (TextView)findViewById(R.id.info);
        infoText=(LinearLayout)findViewById(R.id.info_text);
        runningTime=(TextView)findViewById(R.id.ms);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        progressInfo.setVisibility(View.VISIBLE);
        infoBtn.setVisibility(View.GONE);
        runningTime.setVisibility(View.GONE);

        recyclerView.setHasFixedSize(true);
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        androidID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        if (getIntent()!=null && getIntent().getExtras()!=null){
            list_data = (ArrayList<HashMap<String, String>>)getIntent().getSerializableExtra("list data");
            convertNilai(list_data);
        }

        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (infoText.getVisibility()==View.GONE){
                    infoText.setVisibility(View.VISIBLE);
                } else {
                    infoText.setVisibility(View.GONE);
                }
            }
        });

        simpanRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Simpan Rekomendasi");
                builder.setView(customLayout);
                builder.setCancelable(false);
                builder.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText editText = customLayout.findViewById(R.id.namaRec);
                        String namaRekomendasi = editText.getText().toString();
                        if (namaRekomendasi.length()==0){
                            editText.setError("Nama Rekomentasi tidak boleh kosong");
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            infoBtn.setVisibility(View.GONE);
                            runningTime.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);
                            progressInfo.setVisibility(View.VISIBLE);
                            simpanRec.setVisibility(View.GONE);
                            progressInfo.setText("Menyimpan");

                            checkNamaPapan(namaRekomendasi);

                        }
                    }
                });
                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
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
        });
        
        getBobot();
    }

    private void checkNamaPapan(String namaRekomendasi) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK+"?id="+androidID+"&nama_rec="+namaRekomendasi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject result = jsonObject.getJSONObject("result");
                    String message = result.getString("message");
                    if (message.equals("KOSONG")){
                        Log.d("NAMA PAPAN KE 0 : ",namaPapanDesc.get(0));
                        simpanRekomendasi(namaRekomendasi,androidID,namaPapanDesc.get(0),1);
                    } else {
                        Toast.makeText(MainActivity.this, "DUPLICATE", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
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
        RequestQueue Queue = Volley.newRequestQueue(MainActivity.this);
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

    private void simpanRekomendasi(String namaRekomendasi, String androidID, String key, final int getLoop) {
        for (int i=0;i<list_data.size();i++){
            if (list_data.get(i).get("nama papan").equals(key)){
                final int position = i;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SIMPAN, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("response",response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject result = jsonObject.getJSONObject("result");
                            String message = result.getString("message");
                            if(message.equals("SUKSES")){ //JIKA PESAN JSON SUCCESS
                                if (getLoop==reverseSortedMap.size()) {
                                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                    alertDialog.setTitle("SUKSES");
                                    alertDialog.setMessage("Rekomendasi telah berhasil tersimpan");
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog.show();
                                    recyclerView.setVisibility(View.VISIBLE);
                                    runningTime.setVisibility(View.VISIBLE);
                                    infoBtn.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    progressInfo.setVisibility(View.GONE);
                                    simpanRec.setVisibility(View.VISIBLE);
                                } else {
                                    simpanRekomendasi(namaRekomendasi,androidID,namaPapanDesc.get(getLoop),getLoop+1);
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "GAGAL", Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //MENAMPILKAN ALERT DIALOG
                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
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
                }) {
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<>();
                        params.put("android_id",androidID);
                        params.put("nama_rec",namaRekomendasi);
                        params.put("nama_papan",list_data.get(position).get("nama papan"));
                        params.put("posisi",String.valueOf(rank.get(getLoop-1)));
                        params.put("kerapatan",list_data.get(position).get("kerapatan"));
                        params.put("kadar_air",list_data.get(position).get("kadar air"));
                        params.put("p_tebal",list_data.get(position).get("pengembangan tebal"));
                        params.put("d_serap",list_data.get(position).get("daya serap"));
                        params.put("mod_elastis",list_data.get(position).get("mod elastis"));
                        params.put("mod_patah",list_data.get(position).get("mod patah"));
                        params.put("sekrup",list_data.get(position).get("pegang sekrup"));
                        params.put("rekat_int",list_data.get(position).get("rekat internal"));
                        params.put("kasar",list_data.get(position).get("partikel kasar"));
                        params.put("serbuk",list_data.get(position).get("noda serbuk"));
                        params.put("minyak",list_data.get(position).get("noda minyak"));
                        return params;
                    }
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("Content-Type","application/x-www-form-urlencoded");
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(this);
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
                requestQueue.add(stringRequest);
            }
        }
    }

    ArrayList<ArrayList<String>> bobot = new ArrayList<ArrayList<String>>();
    private void getBobot() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_BOBOT, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(String response) {
                Log.d("response",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray kategori = jsonObject.getJSONArray("KRITERIA");
                    ArrayList<String> bobotK = new ArrayList<>();
                    for (int i=0;i<kategori.length();i++){
                        JSONObject listData = kategori.getJSONObject(i);
                        String bobot = listData.getString("bobot");
                        bobotK.add(bobot);
                    }
                    bobot.add(bobotK);

                    JSONObject subK1 = jsonObject.getJSONObject("SUB-KRITERIA 1");
                    JSONArray dataSK1 = subK1.getJSONArray("sifat_fisis");
                    ArrayList<String> bobotSF = new ArrayList<>();
                    for (int i=0;i<dataSK1.length();i++){
                        JSONObject listData = dataSK1.getJSONObject(i);
                        String bobot = listData.getString("bobot");
                        bobotSF.add(bobot);
                    }
                    bobot.add(bobotSF);

                    JSONObject subK2 = jsonObject.getJSONObject("SUB-KRITERIA 2");
                    JSONArray dataSK2 = subK2.getJSONArray("sifat_mekanis");
                    ArrayList<String> bobotSM = new ArrayList<>();
                    for (int i=0;i<dataSK2.length();i++){
                        JSONObject listData = dataSK2.getJSONObject(i);
                        String bobot = listData.getString("bobot");
                        bobotSM.add(bobot);
                    }
                    bobot.add(bobotSM);

                    JSONObject subK3 = jsonObject.getJSONObject("SUB-KRITERIA 3");
                    JSONArray dataSK3 = subK3.getJSONArray("mutu_penampilan");
                    ArrayList<String> bobotMP = new ArrayList<>();
                    for (int i=0;i<dataSK3.length();i++){
                        JSONObject listData = dataSK3.getJSONObject(i);
                        String bobot = listData.getString("bobot");
                        bobotMP.add(bobot);
                    }
                    bobot.add(bobotMP);
                    System.out.println("BOBOT :"+bobot);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        normalisasiBobot(bobot);
                    }

                    progressInfo.setText("Memproses Data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
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
        RequestQueue Queue = Volley.newRequestQueue(MainActivity.this);
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

    //------------------------------------------CONVERT NILAI SETIAP ALTERNATIF-------------------------------------------------//
    ArrayList<ArrayList<String>> testAlternatif = new ArrayList<ArrayList<String>>();
    ArrayList<HashMap<String,String>> list_data_converted = new ArrayList<HashMap<String, String>>();
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void convertNilai(ArrayList<HashMap<String,String>> list_data){
        for (int i=0;i<list_data.size();i++){
            HashMap<String,String> hashMap = list_data.get(i);
            HashMap<String,String> newData = new HashMap<String, String>();
            double kerapatan = Double.parseDouble(hashMap.get("kerapatan"));
            if (kerapatan<0.40){
                kerapatan=2;
            } else if (kerapatan>=0.40 && kerapatan<=0.80){
                kerapatan=3;
            } else {
                kerapatan=4;
            }
            newData.put("kerapatan",String.valueOf(kerapatan));
            double kadarAir = Double.parseDouble(hashMap.get("kadar air"));
            if (kadarAir>20.0){
                kadarAir=1;
            } else if (kadarAir>=15 && kadarAir<=20){
                kadarAir=2;
            } else if (kadarAir>=10 && kadarAir<=14.9){
                kadarAir=3;
            } else if (kadarAir>=6 && kadarAir<=9.9){
                kadarAir=4;
            }
            else {
                kadarAir=5;
            }
            newData.put("kadar air",String.valueOf(kadarAir));
            double pengembangan = Double.parseDouble(hashMap.get("pengembangan tebal"));
            if (pengembangan>25){
                pengembangan=1;
            } else if (pengembangan>=20 && pengembangan<=25){
                pengembangan=2;
            } else if (pengembangan>=14 && pengembangan<20){
                pengembangan=3;
            } else if (pengembangan>=10 && pengembangan<14){
                pengembangan=4;
            }
            else {
                pengembangan=5;
            }
            newData.put("pengembangan tebal",String.valueOf(pengembangan));
            double dayaSerap = Double.parseDouble(hashMap.get("daya serap"));
            if (dayaSerap>45){
                dayaSerap=1;
            } else if (dayaSerap>=31 && dayaSerap<=45){
                dayaSerap=2;
            } else if (dayaSerap>=21 && dayaSerap<31){
                dayaSerap=3;
            } else if (dayaSerap>=11 && dayaSerap<21){
                dayaSerap=4;
            } else {
                dayaSerap=5;
            }
            newData.put("daya serap",String.valueOf(dayaSerap));
            double elastis = Double.parseDouble(hashMap.get("mod elastis"));
            if (elastis<10000){
                elastis=1;
            } else if (elastis>=10000 && elastis<20400){
                elastis=2;
            } else if (elastis>=20400 && elastis<30600){
                elastis=3;
            } else if (elastis<=30600 && elastis<=40500){
                elastis=4;
            } else {
                elastis=5;
            }
            newData.put("mod elastis",String.valueOf(elastis));
            double patah = Double.parseDouble(hashMap.get("mod patah"));
            if (patah<50){
                patah=1;
            } else if (patah>=50 && patah<80){
                patah=2;
            } else if (patah>=80 && patah<=85){
                patah=3;
            } else if (patah>85 && patah<=95){
                patah=4;
            } else {
                patah=5;
            }
            newData.put("mod patah",String.valueOf(patah));
            double sekrup = Double.parseDouble(hashMap.get("pegang sekrup"));
            if (sekrup<20){
                sekrup=1;
            } else if (sekrup>=20 && sekrup<30){
                sekrup=2;
            } else if (sekrup>=30 && sekrup<=40){
                sekrup=3;
            } else if (sekrup>40 && sekrup<=50){
                sekrup=4;
            } else {
                sekrup=5;
            }
            newData.put("pegang sekrup",String.valueOf(sekrup));
            double rekat = Double.parseDouble(hashMap.get("rekat internal"));
            if (rekat<1){
                rekat=1;
            } else if (rekat>=1 && rekat<1.5){
                rekat=2;
            } else if (rekat>=1.5 && rekat<=2){
                rekat=3;
            } else if (rekat>2.0 && rekat<=3){
                rekat=4;
            } else {
                rekat=5;
            }
            newData.put("rekat internal",String.valueOf(rekat));
            double kasar =Double.parseDouble(hashMap.get("partikel kasar"));
            if (kasar>20){
                kasar=2;
            } else if (kasar>=11 && kasar<=20){
                kasar=3;
            } else {
                kasar=4;
            }
            newData.put("partikel kasar",String.valueOf(kasar));
            double serbuk = Double.parseDouble(hashMap.get("noda serbuk"));
            if (serbuk>4){
                serbuk=2;
            } else if (serbuk>=1 && serbuk<=4){
                serbuk=3;
            } else {
                serbuk=4;
            }
            newData.put("noda serbuk",String.valueOf(serbuk));
            double minyak = Double.parseDouble(hashMap.get("noda minyak"));
            if (minyak>2){
                minyak=2;
            } else if (minyak>=1 && minyak<=2){
                minyak=3;
            } else {
                minyak=4;
            }
            newData.put("noda minyak",String.valueOf(minyak));

            list_data_converted.add(newData);
        }
        System.out.println("LIST DATA CONVERTED "+list_data_converted);
    }

    //--------------------------------------------------------TAHAP 1 : NORMALISASI BOBOT KRITERIA & SUB KRITERIA------------------------------------------------//
    ArrayList<Double> nBobotK = new ArrayList<>(); //BOBOT KRITERIA SETELAH NORMALISASSI
    ArrayList<Double> nBobotSubK = new ArrayList<>(); //BOBOT SUB KRITERIA SETELAH NORMALISASI
    double subKtotal = 0;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void normalisasiBobot(ArrayList<ArrayList<String>> bobot) {
        for (int i=0;i<bobot.size();i++){
            if (i==0){
                double total=0;
                for (int j=0;j<bobot.get(i).size();j++){
                    total=total+Double.parseDouble(bobot.get(i).get(j));
                    df.format(total);
                }
                for (int j=0;j<bobot.get(i).size();j++){
                    double normalisasi = Double.parseDouble(bobot.get(i).get(j))/total;
                    nBobotK.add(Double.parseDouble(String.format("%.6f",normalisasi).replace(",",".")));
                }
            } else {
                for (int j=0;j<bobot.get(i).size();j++){
                    subKtotal = subKtotal+Double.parseDouble(bobot.get(i).get(j));
                }
            }
        }
        System.out.println("TOTAL : "+subKtotal);
        for (int i=1;i<bobot.size();i++){
            for (int j=0;j<bobot.get(i).size();j++){
                double normalisasi = Double.parseDouble(bobot.get(i).get(j))/subKtotal;
                nBobotSubK.add(Double.parseDouble(String.format("%.6f",normalisasi).replace(",",".")));
            }
        }
        System.out.println("NORMALISASI BOBOT :"+nBobotK);
        System.out.println("NORMALISASI BOBOT SUB K : "+nBobotSubK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Recommendation(list_data_converted);
        }
    }

    ArrayList<ArrayList<Double>> alternatif = new ArrayList<ArrayList<Double>>(); //NILAI ALTERNATIF
    ArrayList<ArrayList<Double>> nMatrix = new ArrayList<ArrayList<Double>>(); //MATRIKS KEPUTUSAN TERNORMALISASI
    ArrayList<ArrayList<Double>> nMatrixBobot = new ArrayList<ArrayList<Double>>(); //MATRIKS KEPUTUSAN NORMALISASI TERBOBOT
    ArrayList<Double> idealPositif = new ArrayList<Double>(); //SOLUSI IDEAL POSITIF
    ArrayList<Double> idealNegatif = new ArrayList<Double>(); //SOLUSI IDEAL NEGATIF
    ArrayList<ArrayList<Double>> jarakPositif = new ArrayList<ArrayList<Double>>(); //JARAK SOLUSI IDEAL POSITIF
    ArrayList<ArrayList<Double>> jarakNegatif = new ArrayList<ArrayList<Double>>(); //JARAK SOLUSI IDEAL NEGATIF
    ArrayList<ArrayList<Double>> prefensi = new ArrayList<ArrayList<Double>>(); //PREFENSI ALTERNATIF
    ArrayList<Double> vektorS = new ArrayList<Double>(); //VEKTOR V
    ArrayList<Double> nilaiV = new ArrayList<>(); //NILAI VEKTOR V
    boolean zeroValue = false;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void Recommendation(ArrayList<HashMap<String, String>> list_data_converted) {
        start=System.currentTimeMillis();
//        //-----------------------------------------------------------------TAHAP 2---------------------------------------------------------------------------//
//        //PENGISIAN NILAI MASING2 ALTERNATIF
        for (int i=0; i<list_data_converted.size(); i++){
            ArrayList<Double> nilai = new ArrayList<>();
            double vKrptn = Double.parseDouble(list_data_converted.get(i).get("kerapatan"));
            nilai.add(vKrptn);
            double vKdrAir = Double.parseDouble(list_data_converted.get(i).get("kadar air"));
            nilai.add(vKdrAir);
            double vTbl = Double.parseDouble(list_data_converted.get(i).get("pengembangan tebal"));
            nilai.add(vTbl);
            double vSrp = Double.parseDouble(list_data_converted.get(i).get("daya serap"));
            nilai.add(vSrp);
            double vEls = Double.parseDouble(list_data_converted.get(i).get("mod elastis"));
            nilai.add(vEls);
            double vPth = Double.parseDouble(list_data_converted.get(i).get("mod patah"));
            nilai.add(vPth);
            double vSkrp = Double.parseDouble(list_data_converted.get(i).get("pegang sekrup"));
            nilai.add(vSkrp);
            double vRkt = Double.parseDouble(list_data_converted.get(i).get("rekat internal"));
            nilai.add(vRkt);
            double vPrtkl = Double.parseDouble(list_data_converted.get(i).get("partikel kasar"));
            nilai.add(vPrtkl);
            double vSrbk = Double.parseDouble(list_data_converted.get(i).get("noda serbuk"));
            nilai.add(vSrbk);
            double vMnyk = Double.parseDouble(list_data_converted.get(i).get("noda minyak"));
            nilai.add(vMnyk);

            alternatif.add(nilai);
        }
        System.out.println("ALTERNATIF "+alternatif);

        //---------------------------------------------------------------TAHAP 3---------------------------------------------------------------------------//
        //MATRIKS KEPUTUSAN TERNORMALISASI
        for (int i=0;i<alternatif.get(0).size();i++){
            double jPangkat = 0;
            ArrayList<Double> nilai = new ArrayList<>();
            for (int j=0;j<alternatif.size();j++){
                double getVal = alternatif.get(j).get(i);
                nilai.add(getVal);
                jPangkat = jPangkat+(getVal*getVal);
            }
            ArrayList<Double> getNorm = new ArrayList<>();
            for (int k=0;k<nilai.size();k++){
                double normalisasi = alternatif.get(k).get(i)/Math.sqrt(jPangkat);
                getNorm.add(Double.parseDouble(String.format("%.6f",normalisasi).replace(",",".")));
            }
            nMatrix.add(getNorm);
        }
        System.out.println("N MATRIX "+nMatrix);
        //---------------------------------------------------------TAHAP 4------------------------------------------------------------//
        //MATRIKS TERNORMALISASI TERBOBOT
        for(int i=0;i<alternatif.size();i++){
            double value;
            ArrayList<Double> altValue = new ArrayList<>();
            for (int j=0;j<alternatif.get(i).size();j++){
                value = nMatrix.get(j).get(i)*nBobotSubK.get(j);
                altValue.add(Double.parseDouble(String.format("%.6f",value).replace(",",".")));
            }
            nMatrixBobot.add(altValue);
        }
        System.out.println("N MATRIX BOBOT :"+nMatrixBobot);
        //----------------------------------------------------------TAHAP 5--------------------------------------------------------------//
        //SOLUSI IDEAL POSITIF DAN SOLUSI IDEAL NEGATIF
        for (int i=0;i<alternatif.get(0).size();i++){
            double max=nMatrixBobot.get(0).get(i);
            double min=nMatrixBobot.get(0).get(i);
            for (int j=0;j<alternatif.size();j++){
                if (nMatrixBobot.get(j).get(i)>=max){
                    max=nMatrixBobot.get(j).get(i);
                }
                if (nMatrixBobot.get(j).get(i)<=min){
                    min=nMatrixBobot.get(j).get(i);
                }
            }
            idealPositif.add(max);
            idealNegatif.add(min);
        }
        System.out.println("IDEAL POSITIF"+idealPositif);
        System.out.println("IDEAL NEGATIF"+idealNegatif);

        //------------------------------------------------------TAHAP 6---------------------------------------------//
        //JARAK SOLUSI IDEAL POSITIF DAN SOLUSI IDEAL NEGATIF
        for (int i=0;i<alternatif.size();i++){
            double DposF=0, DposM=0, DposP=0;
            double DnegF =0, DnegM=0, DnegP=0;
            ArrayList<Double> DposVal = new ArrayList<>();
            ArrayList<Double> DnegVal = new ArrayList<>();
            //SIFAT FISIS
            for (int j=0;j<4;j++){
                DposF = DposF+(Math.pow((nMatrixBobot.get(i).get(j)-idealPositif.get(j)),2));
                DnegF = DnegF+Math.pow(nMatrixBobot.get(i).get(j)-idealNegatif.get(j),2);
            }
            DposF=Math.sqrt(DposF);
            DnegF=Math.sqrt(DnegF);
            DposVal.add(Double.parseDouble(String.format("%.6f",DposF).replace(",",".")));
            DnegVal.add(Double.parseDouble(String.format("%.6f",DnegF).replace(",",".")));
            //SIFAT MEKANIS
            for (int k=0;k<4;k++) {
                DposM = DposM+Math.pow(nMatrixBobot.get(i).get(k+4)-idealPositif.get(k+4),2);
                DnegM = DnegM+Math.pow(nMatrixBobot.get(i).get(k+4)-idealNegatif.get(k+4),2);
            }
            DposM=Math.sqrt(DposM);
            DnegM=Math.sqrt(DnegM);
            DposVal.add(Double.parseDouble(String.format("%.6f",DposM).replace(",",".")));
            DnegVal.add(Double.parseDouble(String.format("%.6f",DnegM).replace(",",".")));
            //MUTU PENAMPILAN
            for (int l=0;l<3;l++){
                DposP = DposP+Math.pow(nMatrixBobot.get(i).get(l+8)-idealPositif.get(l+8),2);
                DnegP = DnegP+Math.pow(nMatrixBobot.get(i).get(l+8)-idealNegatif.get(l+8),2);
            }
            DposP=Math.sqrt(DposP);
            DnegP=Math.sqrt(DnegP);
            DposVal.add(Double.parseDouble(String.format("%.6f",DposP).replace(",",".")));
            DnegVal.add(Double.parseDouble(String.format("%.6f",DnegP).replace(",",".")));
            jarakPositif.add(DposVal);
            jarakNegatif.add(DnegVal);
        }
        System.out.println("JARAK POSITIF"+jarakPositif);
        System.out.println("JARAK NEGATIF"+jarakNegatif);
        //------------------------------------------------TAHAP 7-------------------------------------------------------//
        //PREFENSI SETIAP ALTERNATIF
        for (int i=0;i<alternatif.size();i++){
            ArrayList<Double> prefVal = new ArrayList<>();
            double pref;
            for (int j=0;j<jarakPositif.get(i).size();j++){
                pref= jarakNegatif.get(i).get(j)/(jarakNegatif.get(i).get(j)+jarakPositif.get(i).get(j));
                prefVal.add(Double.parseDouble(String.format("%.6f",pref).replace(",",".")));
            }
            prefensi.add(prefVal);
            if (prefensi.get(i).contains(0.0) && !zeroValue){
                zeroValue=true;
                System.out.println("DITAMBAH");
            } else {
                System.out.println("DIKALI");
            }
        }
        System.out.println("PREFENSI "+prefensi);
        //----------------------------------------TAHAP 8---------------------------------------------------//
        //VEKTOR S
        for (int i=0; i<alternatif.size();i++){
            double flagV;
            if (zeroValue){
                flagV = 0;
            } else {
                flagV = 1;
            }
            for (int j=0;j<prefensi.get(i).size();j++){
                if (zeroValue){
                    flagV=flagV+Math.pow(prefensi.get(i).get(j),nBobotK.get(j));
                } else {
                    flagV=flagV*Math.pow(prefensi.get(i).get(j),nBobotK.get(j));
                }
            }
            vektorS.add(Double.parseDouble(String.format("%.6f",flagV).replace(",",".")));
        }
        System.out.println("VEKTOR S "+vektorS);
        //---------------------------------------------TAHAP 9--------------------------------------------------//
        //Nilai Vektor V
        double totalVector=0;
        double Vfinal;
        for (int i=0;i<alternatif.size();i++){
            totalVector=totalVector+vektorS.get(i);
        }
        for (int i=0;i<alternatif.size();i++){
            Vfinal=vektorS.get(i)/totalVector;
            nilaiV.add(Vfinal);
        }
        System.out.println("NILAI AKHIR: "+nilaiV);

        finish=System.currentTimeMillis();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SortData(nilaiV);
        }
    }


    HashMap<String,Double> rekomendasi = new HashMap<String, Double>();
    List<Double> nilaiRank;
    int flagRank=1;
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void SortData(ArrayList<Double> nilaiV) {
        for (int i = 0; i < list_data.size(); i++) {
            String namaPapan=list_data.get(i).get("nama papan");
            double vVal=nilaiV.get(i);
            rekomendasi.put(namaPapan,vVal);
        }
        rekomendasi.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(new Consumer<Map.Entry<String, Double>>() {
                    @Override
                    public void accept(Map.Entry<String, Double> x) {
                        reverseSortedMap.put(x.getKey(), x.getValue());
                    }
                });
        progressBar.setVisibility(View.GONE);
        progressInfo.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        runningTime.setVisibility(View.VISIBLE);
        infoBtn.setVisibility(View.VISIBLE);
        nilaiRank = new ArrayList<>(reverseSortedMap.values());
        rank = new ArrayList<Integer>();
        rank.add(flagRank);
        for (int i=1;i<nilaiRank.size();i++){
            if (nilaiRank.get(i).equals(nilaiRank.get(i-1))){
                rank.add(flagRank);
            } else {
                flagRank++;
                rank.add(flagRank);
            }
        }
        System.out.println("RANK : "+rank);
        System.out.println("RANK nilai : "+nilaiRank);
        adapter = new RecommendationAdapter(MainActivity.this,reverseSortedMap,nilaiRank);
        recyclerView.setAdapter(adapter);
        long duration=(finish-start);
        System.out.println("RUNNING TIME MILI"+duration);
        runningTime.setText("Running Time : "+duration+"ms");
        namaPapanDesc = new ArrayList<>(reverseSortedMap.keySet());
        System.out.println("REVERSE SORTED MAP :"+reverseSortedMap);

        LocalBroadcastManager.getInstance(this).registerReceiver(getData, new IntentFilter("get-rec"));
    }

    public BroadcastReceiver getData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int position = intent.getIntExtra("position",0);
            Intent i = new Intent(MainActivity.this,DataDetails.class);
            i.putExtra("list data",list_data);
            i.putExtra("nama rec","Kosong-");
            i.putExtra("position",rank.get(position));
            i.putExtra("nama papan",namaPapanDesc.get(position));
            startActivity(i);
        }
    };
}