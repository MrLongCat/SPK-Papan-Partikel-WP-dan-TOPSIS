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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditData extends AppCompatActivity {
    EditText namaPapan, kerapatan, kadarAir, pengembanganTebal, dayaSerap;
    EditText elastisitas, patah, pegangSekrup, rekatInternal;
    EditText partikelKasar, nodaSerbuk, nodaMinyak;
    Spinner spElastis, spPatah, spSekrup, spRekat;
    String sSatPatah, sSatSekrup, sSatRekat, sSatElastis;
    String sNama,sKerapatan,sKadarAir,sTebal,sDayaSerap,sElastis,sPatah,sSekrup,sRekat,sKasar,sSerbuk,sMinyak;
    Button next;
    int position;
    ScrollView scrollView;
    ArrayList<HashMap<String,String>> listData = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

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
            position = getIntent().getExtras().getInt("position");

            namaPapan.setText(listData.get(position).get("nama papan"));
            kerapatan.setText(listData.get(position).get("kerapatan"));
            kadarAir.setText(listData.get(position).get("kadar air"));
            pengembanganTebal.setText(listData.get(position).get("pengembangan tebal"));
            dayaSerap.setText(listData.get(position).get("daya serap"));
            elastisitas.setText(listData.get(position).get("mod elastis"));
            patah.setText(listData.get(position).get("mod patah"));
            pegangSekrup.setText(listData.get(position).get("pegang sekrup"));
            rekatInternal.setText(listData.get(position).get("rekat internal"));
            partikelKasar.setText(listData.get(position).get("partikel kasar"));
            nodaSerbuk.setText(listData.get(position).get("noda serbuk"));
            nodaMinyak.setText(listData.get(position).get("noda minyak"));
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
                        if (sNama.equals(listData.get(i).get("nama papan")) && i!=position){
                            checkName=true;
                        }
                    }
                }

                if (checkName){
                    AlertDialog alertDialog = new AlertDialog.Builder(EditData.this).create();
                    alertDialog.setTitle("Ups");
                    alertDialog.setMessage("Nama papan partikel telah digunakan. Silahkan masukkan nama papan yg baru ya ;)");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    namaPapan.setError("Nama papan telah digunakan");
                    scrollView.smoothScrollTo(0,namaPapan.getBottom());
                } else if (sNama.length()==0){
                    namaPapan.setError("Nama papan tidak boleh kosong");
                    scrollView.smoothScrollTo(0,namaPapan.getBottom());
                } else if (sKerapatan.length()==0){
                    kerapatan.setError("Kerapatan tidak boleh kosong");
                    scrollView.smoothScrollTo(0,kerapatan.getBottom());
                } else if (sKadarAir.length()==0){
                    kadarAir.setError("Kadar air tidak boleh kosong");
                    scrollView.smoothScrollTo(0,kadarAir.getBottom());
                } else if (sTebal.length()==0){
                    pengembanganTebal.setError("Pengembangan tebal tidak boleh kosong");
                    scrollView.smoothScrollTo(0,pengembanganTebal.getBottom());
                } else if (sDayaSerap.length()==0){
                    dayaSerap.setError("Daya serap air tidak boleh kosong");
                    scrollView.smoothScrollTo(0,dayaSerap.getBottom());
                } else if (sElastis.length()==0){
                    elastisitas.setError("Mod. Elastisitas tidak boleh kosong");
                    elastisitas.requestFocus();
                } else if (sPatah.length()==0){
                    patah.setError("Mod. Patah tidak boleh kosong");
                    patah.requestFocus();
                } else if (sSekrup.length()==0){
                    pegangSekrup.setError("Kuat pegang sekrup tidak boleh kosong");
                    pegangSekrup.requestFocus();
                } else if (sRekat.length()==0){
                    rekatInternal.setError("Kuat rekat internal tidak boleh kosong");
                    rekatInternal.requestFocus();
                } else if (sKasar.length()==0){
                    partikelKasar.setError("Partikel kasar perukaan tidak boleh kosong");
                    partikelKasar.requestFocus();
                } else if (sSerbuk.length()==0){
                    nodaSerbuk.setError("Diameter noda serbuk tidak boleh kosong");
                    nodaSerbuk.requestFocus();
                } else if (sMinyak.length()==0){
                    nodaMinyak.setError("Noda minyak tidak boleh kosong");
                    nodaMinyak.requestFocus();
                } else {
                    if (sSatSekrup.contains("N")){
                        dSekrup=ConvertNtoKgf(Double.parseDouble(pegangSekrup.getText().toString()));
                    } else {
                        dSekrup=Double.parseDouble(pegangSekrup.getText().toString());
                    }
                    if (sSatElastis.contains("N")){
                        dElastis=ConvertNmmToKgcm(Double.parseDouble(elastisitas.getText().toString()));
                    } else {
                        dElastis=Double.parseDouble(elastisitas.getText().toString());
                    }
                    if (sSatRekat.contains("N")){
                        dRekat=ConvertNmmToKgcm(Double.parseDouble(rekatInternal.getText().toString()));
                    } else {
                        dRekat=Double.parseDouble(rekatInternal.getText().toString());
                    }
                    if (sSatPatah.contains("N")){
                        dPatah=ConvertNmmToKgcm(Double.parseDouble(patah.getText().toString()));
                    } else {
                        dPatah=Double.parseDouble(patah.getText().toString());
                    }

                    Map<String,String> map = listData.get(position);

                    map.put("nama papan",sNama);
                    map.put("kerapatan",sKerapatan);
                    map.put("kadar air",sKadarAir);
                    map.put("pengembangan tebal",sTebal);
                    map.put("daya serap",sDayaSerap);
                    map.put("mod elastis",String.valueOf(dElastis));
                    map.put("mod patah",String.valueOf(dPatah));
                    map.put("pegang sekrup",String.valueOf(dSekrup));
                    map.put("rekat internal",String.valueOf(dRekat));
                    map.put("partikel kasar",sKasar);
                    map.put("noda serbuk",sSerbuk);
                    map.put("noda minyak",sMinyak);

                    Intent intent = new Intent(EditData.this, ListOfData.class);
                    intent.putExtra("list data",listData);
                    startActivity(intent);
                    finish();
                }
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
        Intent intent = new Intent(EditData.this, ListOfData.class);
        intent.putExtra("list data",listData);
        startActivity(intent);
        finish();
    }
}