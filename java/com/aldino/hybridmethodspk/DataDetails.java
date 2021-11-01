package com.aldino.hybridmethodspk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class DataDetails extends AppCompatActivity {
    TextView peringkat,namaRec,namaPP;
    TextView kerapatan,kadarAir,tebal,serap,elastis,patah,sekrup,rekat,kasar,serbuk,minyak;
    String rekomendasi,papan;
    int position;
    View garis;
    ArrayList<HashMap<String,String>> list_data = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_details);

        peringkat=(TextView)findViewById(R.id.peringkat);
        namaRec=(TextView)findViewById(R.id.nama_rec);
        namaPP=(TextView)findViewById(R.id.nama_pp);
        kerapatan=(TextView)findViewById(R.id.nKerapatan);
        kadarAir=(TextView)findViewById(R.id.nKadarAir);
        tebal=(TextView)findViewById(R.id.nTebal);
        serap=(TextView)findViewById(R.id.nSerap);
        elastis=(TextView)findViewById(R.id.nElastis);
        patah=(TextView)findViewById(R.id.nPatah);
        sekrup=(TextView)findViewById(R.id.nSekrup);
        rekat=(TextView)findViewById(R.id.nRekat);
        kasar=(TextView)findViewById(R.id.nKasar);
        serbuk=(TextView)findViewById(R.id.nSerbuk);
        minyak=(TextView)findViewById(R.id.nMinyak);
        garis=(View)findViewById(R.id.garis);

        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            position = extras.getInt("position");
            list_data = (ArrayList<HashMap<String, String>>)getIntent().getSerializableExtra("list data");
            rekomendasi = extras.getString("nama rec");
            papan = extras.getString("nama papan");
            putData(list_data,position);
        }
    }

    private void putData(ArrayList<HashMap<String, String>> list_data,int position) {
        if (rekomendasi.equals("Kosong-")){
            namaRec.setVisibility(View.GONE);
            garis.setVisibility(View.GONE);
            namaPP.setText(papan);
            namaPP.setGravity(Gravity.CENTER);
            namaPP.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
            peringkat.setText("#"+(position));
            for (int i=0;i<list_data.size();i++){
                String getNama = list_data.get(i).get("nama papan");
                if (getNama.equals(papan)){
                    kerapatan.setText(list_data.get(i).get("kerapatan"));
                    kadarAir.setText(list_data.get(i).get("kadar air"));
                    tebal.setText(list_data.get(i).get("pengembangan tebal"));
                    serap.setText(list_data.get(i).get("daya serap"));
                    elastis.setText(list_data.get(i).get("mod elastis"));
                    patah.setText(list_data.get(i).get("mod patah"));
                    sekrup.setText(list_data.get(i).get("pegang sekrup"));
                    rekat.setText(list_data.get(i).get("rekat internal"));
                    kasar.setText(list_data.get(i).get("partikel kasar"));
                    serbuk.setText(list_data.get(i).get("noda serbuk"));
                    minyak.setText(list_data.get(i).get("noda minyak"));
                    break;
                }
            }
        } else {
            namaRec.setText(rekomendasi);
            namaPP.setText(list_data.get(position).get("nama papan"));
            peringkat.setText("#"+list_data.get(position).get("posisi"));
            kerapatan.setText(list_data.get(position).get("kerapatan"));
            kadarAir.setText(list_data.get(position).get("kadar air"));
            tebal.setText(list_data.get(position).get("pengembangan tebal"));
            serap.setText(list_data.get(position).get("daya serap"));
            elastis.setText(list_data.get(position).get("mod elastis"));
            patah.setText(list_data.get(position).get("mod patah"));
            sekrup.setText(list_data.get(position).get("pegang sekrup"));
            rekat.setText(list_data.get(position).get("rekat internal"));
            kasar.setText(list_data.get(position).get("partikel kasar"));
            serbuk.setText(list_data.get(position).get("noda serbuk"));
            minyak.setText(list_data.get(position).get("noda minyak"));
        }
    }
}