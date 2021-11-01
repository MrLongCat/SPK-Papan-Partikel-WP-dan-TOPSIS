package com.aldino.hybridmethodspk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainMenu extends AppCompatActivity {

    LinearLayout addNewData,savedRec,help,about;
    private String androidID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        addNewData = (LinearLayout) findViewById(R.id.add);
        help = (LinearLayout)findViewById(R.id.help);
        savedRec = (LinearLayout)findViewById(R.id.savedRec);
        about = (LinearLayout)findViewById(R.id.about);

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, HelpActivity.class);
                startActivity(intent);
            }
        });

        savedRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this,SavedRecommendation.class);
                startActivity(intent);
            }
        });

        addNewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, ListOfData.class);
                startActivity(intent);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, TentangAplikasi.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }
}