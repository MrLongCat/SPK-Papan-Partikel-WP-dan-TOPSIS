package com.aldino.hybridmethodspk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.transition.Slide;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    LinearLayout dotsLayout;
    SliderAdapter adapter;
    ViewPager2 pager2;
    int list[] = new int[]{R.drawable.memulai_aplikasi,R.drawable.input_data,R.drawable.hasil_rekomendasi};
    TextView[] dots;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        dotsLayout = (LinearLayout)findViewById(R.id.dots_container);
        pager2 = (ViewPager2)findViewById(R.id.view_pager);

        adapter = new SliderAdapter(list);
        pager2.setAdapter(adapter);

        dots = new TextView[3];
        dotsIndicator();

        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                selectedIndocator(position);
                super.onPageSelected(position);
            }
        });
    }

    private void selectedIndocator(int position) {
        for (int i=0;i<dots.length;i++){
            if (i==position){
                dots[i].setTextColor(Color.parseColor("#EFEFEF"));
            } else {
                dots[i].setTextColor(Color.parseColor("#B3B3B3"));
            }
        }
    }

    private void dotsIndicator() {
        for (int i=0;i<dots.length;i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#9679;"));
            dots[i].setTextSize(18);
            dotsLayout.addView(dots[i]);
        }
    }
}