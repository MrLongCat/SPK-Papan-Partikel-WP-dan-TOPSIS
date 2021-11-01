package com.aldino.hybridmethodspk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class addData extends AppCompatActivity {
    EditText namaPapan, kerapatan, kadarAir, pengembanganTebal, dayaSerap;
    EditText elastisitas, patah, pegangSekrup, rekatInternal;
    EditText partikelKasar, nodaSerbuk, nodaMinyak;
    Spinner spElastis, spPatah, spSekrup, spRekat;
    String sSatPatah, sSatSekrup, sSatRekat, sSatElastis;
    String sNama,sKerapatan,sKadarAir,sTebal,sDayaSerap,sElastis,sPatah,sSekrup,sRekat,sKasar,sSerbuk,sMinyak;
    Button next;
    ScrollView scrollView;
    ArrayList<HashMap<String,String>> listData = new ArrayList<HashMap<String, String>>();

    NumberFormat df = new DecimalFormat("#.######");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        namaPapan = (EditText)findViewById(R.id.namaPapan);
        kerapatan = (EditText)findViewById(R.id.kerapatan);
        kadarAir = (EditText)findViewById(R.id.kadarAir);
        pengembanganTebal = (EditText)findViewById(R.id.pengembanganTebal);
        dayaSerap = (EditText)findViewById(R.id.serap);
        elastisitas = (EditText)findViewById(R.id.elastis);
        patah = (EditText)findViewById(R.id.patah);
        pegangSekrup = (EditText)findViewById(R.id.sekrup);
        rekatInternal = (EditText)findViewById(R.id.rekat);
        partikelKasar = (EditText)findViewById(R.id.kasar);
        nodaSerbuk = (EditText)findViewById(R.id.noda);
        nodaMinyak = (EditText)findViewById(R.id.minyak);
        spElastis = (Spinner)findViewById(R.id.satuanElastis);
        spPatah = (Spinner)findViewById(R.id.satuanPatah);
        spSekrup = (Spinner)findViewById(R.id.satuanSekrup);
        spRekat = (Spinner)findViewById(R.id.satuanRekat);
        next = (Button)findViewById(R.id.next);
        scrollView = (ScrollView)findViewById(R.id.scrollView);

        ArrayList<CharSequence> satuan = new ArrayList<>();
        satuan.add(Html.fromHtml("Kg/cm<sup>2<sup>"));
        satuan.add(Html.fromHtml("N/mm<sup>2<sup>"));

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, R.layout.new_spinner_item,satuan);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spElastis.setAdapter(adapter);
        spPatah.setAdapter(adapter);
        spRekat.setAdapter(adapter);

        ArrayList<String> satuanSekrup = new ArrayList<>();
        satuanSekrup.add("kgf");
        satuanSekrup.add("N");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.new_spinner_item, satuanSekrup);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSekrup.setAdapter(adapter2);

        if (getIntent()!=null && getIntent().getExtras()!=null){
            listData = (ArrayList<HashMap<String, String>>)getIntent().getSerializableExtra("list data");
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double dElastis = 0;
                double dPatah = 0;
                double dSekrup = 0;
                double dRekat = 0;
                boolean checkName = false;
                sNama=namaPapan.getText().toString();
                sKerapatan=kerapatan.getText().toString();
                sKadarAir=kadarAir.getText().toString();
                sTebal=pengembanganTebal.getText().toString();
                sDayaSerap=dayaSerap.getText().toString();
                sElastis=elastisitas.getText().toString();
                sPatah=patah.getText().toString();
                sSekrup=pegangSekrup.getText().toString();
                sRekat=rekatInternal.getText().toString();
                sKasar=partikelKasar.getText().toString();
                sSerbuk=nodaSerbuk.getText().toString();
                sMinyak=nodaMinyak.getText().toString();

                sSatElastis = spElastis.getSelectedItem().toString();
                sSatPatah = spPatah.getSelectedItem().toString();
                sSatRekat = spRekat.getSelectedItem().toString();
                sSatSekrup = spSekrup.getSelectedItem().toString();

                for (int i=0;i<listData.size();i++){
                    for (int j=0;j<listData.get(i).size();j++){
                        if (sNama.equals(listData.get(i).get("nama papan"))){
                            checkName=true;
                        }
                    }
                }

//                if (checkName){
//                    AlertDialog alertDialog = new AlertDialog.Builder(addData.this).create();
//                    alertDialog.setTitle("Ups");
//                    alertDialog.setMessage("Nama papan partikel telah digunakan. Silahkan masukkan nama papan yg baru ya ;)");
//                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            });
//                    alertDialog.show();
//                    namaPapan.setError("Nama papan telah digunakan");
//                    scrollView.smoothScrollTo(0,namaPapan.getBottom());
//                } else if (sNama.length()==0){
//                    namaPapan.setError("Nama papan tidak boleh kosong");
//                    scrollView.smoothScrollTo(0,namaPapan.getBottom());
//                } else if (sKerapatan.length()==0){
//                    kerapatan.setError("Kerapatan tidak boleh kosong");
//                    scrollView.smoothScrollTo(0,kerapatan.getBottom());
//                    kerapatan.requestFocus();
//                } else if (sKadarAir.length()==0){
//                    kadarAir.setError("Kadar air tidak boleh kosong");
//                    scrollView.smoothScrollTo(0,kadarAir.getBottom());
//                } else if (sTebal.length()==0){
//                    pengembanganTebal.setError("Pengembangan tebal tidak boleh kosong");
//                    scrollView.smoothScrollTo(0,pengembanganTebal.getBottom());
//                } else if (sDayaSerap.length()==0){
//                    dayaSerap.setError("Daya serap air tidak boleh kosong");
//                    scrollView.smoothScrollTo(0,dayaSerap.getBottom());
//                } else if (sElastis.length()==0){
//                    elastisitas.setError("Mod. Elastisitas tidak boleh kosong");
//                    elastisitas.requestFocus();
//                } else if (sPatah.length()==0){
//                    patah.setError("Mod. Patah tidak boleh kosong");
//                    patah.requestFocus();
//                } else if (sSekrup.length()==0){
//                    pegangSekrup.setError("Kuat pegang sekrup tidak boleh kosong");
//                    pegangSekrup.requestFocus();
//                } else if (sRekat.length()==0){
//                    rekatInternal.setError("Kuat rekat internal tidak boleh kosong");
//                    rekatInternal.requestFocus();
//                } else if (sKasar.length()==0){
//                    partikelKasar.setError("Partikel kasar perukaan tidak boleh kosong");
//                    partikelKasar.requestFocus();
//                } else if (sSerbuk.length()==0){
//                    nodaSerbuk.setError("Diameter noda serbuk tidak boleh kosong");
//                    nodaSerbuk.requestFocus();
//                } else if (sMinyak.length()==0){
//                    nodaMinyak.setError("Noda minyak tidak boleh kosong");
//                    nodaMinyak.requestFocus();
//                } else {
//                    if (sSatSekrup.contains("N")){
//                        dSekrup=ConvertNtoKgf(Double.parseDouble(pegangSekrup.getText().toString()));
//                    } else {
//                        dSekrup=Double.parseDouble(pegangSekrup.getText().toString());
//                    }
//                    if (sSatElastis.contains("N")){
//                        dElastis=ConvertNmmToKgcm(Double.parseDouble(elastisitas.getText().toString()));
//                    } else {
//                        dElastis=Double.parseDouble(elastisitas.getText().toString());
//                    }
//                    if (sSatRekat.contains("N")){
//                        dRekat=ConvertNmmToKgcm(Double.parseDouble(rekatInternal.getText().toString()));
//                    } else {
//                        dRekat=Double.parseDouble(rekatInternal.getText().toString());
//                    }
//                    if (sSatPatah.contains("N")){
//                        dPatah=ConvertNmmToKgcm(Double.parseDouble(patah.getText().toString()));
//                    } else {
//                        dPatah=Double.parseDouble(patah.getText().toString());
//                    }
//
//                    HashMap<String, String> data = new HashMap<>();
//                    data.put("nama papan",sNama);
//                    data.put("kerapatan",sKerapatan);
//                    data.put("kadar air",sKadarAir);
//                    data.put("pengembangan tebal",sTebal);
//                    data.put("daya serap",sDayaSerap);
//                    data.put("mod elastis",String.valueOf(dElastis));
//                    data.put("mod patah",String.valueOf(dPatah));
//                    data.put("pegang sekrup",String.valueOf(dSekrup));
//                    data.put("rekat internal",String.valueOf(dRekat));
//                    data.put("partikel kasar",sKasar);
//                    data.put("noda serbuk",sSerbuk);
//                    data.put("noda minyak",sMinyak);
//                    listData.add(data);
//
//                    Intent intent = new Intent(addData.this, ListOfData.class);
//                    intent.putExtra("list data",listData);
//                    startActivity(intent);
//                    finish();
//                }
                HashMap<String, String> data = new HashMap<>();
                data.put("nama papan","Daun Mahoni(75%)+Serbuk Sengon(25%)+NH4CL(1%)");
                data.put("kerapatan","0.64");
                data.put("kadar air","7.6");
                data.put("pengembangan tebal","17.6");
                data.put("daya serap","63.3");
                data.put("mod elastis","4614");
                data.put("mod patah","8.15");
                data.put("pegang sekrup","18.9");
                data.put("rekat internal","0.08");
                data.put("partikel kasar","12");
                data.put("noda serbuk","0.5");
                data.put("noda minyak","0.5");
                listData.add(data);

                HashMap<String, String> data2 = new HashMap<>();
                data2.put("nama papan","Daun Mahoni(75%)+Serbuk Sengon(25%)+NH4CL(3%)");
                data2.put("kerapatan","0.64");
                data2.put("kadar air","7.19");
                data2.put("pengembangan tebal","14.9");
                data2.put("daya serap","65.8");
                data2.put("mod elastis","6127");
                data2.put("mod patah","8.18");
                data2.put("pegang sekrup","22.3");
                data2.put("rekat internal","0.08");
                data2.put("partikel kasar","11");
                data2.put("noda serbuk","0.5");
                data2.put("noda minyak","1.3");
                listData.add(data2);

                HashMap<String, String> data3 = new HashMap<>();
                data3.put("nama papan","Daun Mahoni(50%)+Serbuk Sengon(50%)+NH4CL(1%)");
                data3.put("kerapatan","0.65");
                data3.put("kadar air","7.2");
                data3.put("pengembangan tebal","17.6");
                data3.put("daya serap","91.68");
                data3.put("mod elastis","3868");
                data3.put("mod patah","11.14");
                data3.put("pegang sekrup","25.4");
                data3.put("rekat internal","0.12");
                data3.put("partikel kasar","15");
                data3.put("noda serbuk","1");
                data3.put("noda minyak","0.8");
                listData.add(data3);

                HashMap<String, String> data4 = new HashMap<>();
                data4.put("nama papan","Daun Mahoni(50%)+Serbuk Sengon(50%)+NH4CL(2%)");
                data4.put("kerapatan","0.61");
                data4.put("kadar air","9.6");
                data4.put("pengembangan tebal","26");
                data4.put("daya serap","108");
                data4.put("mod elastis","7198");
                data4.put("mod patah","6.5");
                data4.put("pegang sekrup","16.5");
                data4.put("rekat internal","0.07");
                data4.put("partikel kasar","6");
                data4.put("noda serbuk","1");
                data4.put("noda minyak","0.8");
                listData.add(data4);

                HashMap<String, String> data5 = new HashMap<>();
                data5.put("nama papan","Daun Mahoni(25%)+Serbuk Sengon(75%)+NH4CL(1%)");
                data5.put("kerapatan","0.64");
                data5.put("kadar air","8.55");
                data5.put("pengembangan tebal","18.5");
                data5.put("daya serap","98.8");
                data5.put("mod elastis","3287");
                data5.put("mod patah","9.14");
                data5.put("pegang sekrup","27.8");
                data5.put("rekat internal","0.1");
                data5.put("partikel kasar","9");
                data5.put("noda serbuk","0.8");
                data5.put("noda minyak","1");
                listData.add(data5);

                HashMap<String, String> data6 = new HashMap<>();
                data6.put("nama papan","Daun Kelapa(25%)+Serbuk Sengon(75%)+NH4CL(2%)");
                data6.put("kerapatan","0.63");
                data6.put("kadar air","9.9");
                data6.put("pengembangan tebal","16");
                data6.put("daya serap","86.1");
                data6.put("mod elastis","2190");
                data6.put("mod patah","8.13");
                data6.put("pegang sekrup","31.7");
                data6.put("rekat internal","0.1");
                data6.put("partikel kasar","12");
                data6.put("noda serbuk","0.5");
                data6.put("noda minyak","0.5");
                listData.add(data6);

                Intent intent = new Intent(addData.this, ListOfData.class);
                intent.putExtra("list data",listData);
                startActivity(intent);
                finish();
            }
        });
    }

    public double ConvertNtoKgf(double nilai){
        double devider = 9.807;
        nilai = nilai/devider;
        return Double.parseDouble(String.format("%.6f",nilai).replace(",","."));
    }

    public double ConvertNmmToKgcm(double nilai){
        double multiply = 10.197;
        nilai = nilai*multiply;
        return Double.parseDouble(String.format("%.6f",nilai).replace(",","."));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(addData.this, ListOfData.class);
        intent.putExtra("list data",listData);
        startActivity(intent);
        finish();
    }
}