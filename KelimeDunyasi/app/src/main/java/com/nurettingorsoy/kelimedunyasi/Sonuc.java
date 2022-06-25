package com.nurettingorsoy.kelimedunyasi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Sonuc extends AppCompatActivity {
    TextView textView,textView3,textView4,textView5;
    int a,b,c,d=0;
    int sonuc;

    Animation havai_anim;
    ImageView havai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sonuc);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        textView = findViewById(R.id.textView);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);

        havai_anim = AnimationUtils.loadAnimation(this,R.anim.havai_anim);
        havai = findViewById(R.id.havaiView);
        havai.setVisibility(View.INVISIBLE);

        //havai.setAnimation(havai_anim);

        Intent intent = getIntent();
        String birincikelime = intent.getStringExtra("kelime");
        String ikincikelime = intent.getStringExtra("kelimeiki");
        String ucuncukelime = intent.getStringExtra("kelimeuc");
        String birincianlam = intent.getStringExtra("anlam");
        String ikincianlam = intent.getStringExtra("anlamiki");
        String ucuncuanlam = intent.getStringExtra("anlamuc");



        if (birincikelime.equals(birincianlam)) {
            textView3.setText("1.soru doğru ");
            a++;
        }
        if (ikincikelime.equals(ikincianlam)) {
            textView4.setText("2.soru doğru ");
            b++;
        }
        if (ucuncukelime.equals(ucuncuanlam)) {
            textView.setText("3.soru doğru ");
            c++;
        } else {
            d = 0;
        }
        sonuc=a+b+c+d;
        idegeri();
    }
    public void idegeri(){

        if(sonuc==0 || sonuc==1 || sonuc==2){
            textView5.setText("Toplamda " + sonuc + "  doğrun var.");
        }
        else{
            textView5.setText("Vay Canına Tebrikler Toplamda " + sonuc + "  doğrun var.");
                havai.setVisibility(View.VISIBLE);
                havai.setAnimation(havai_anim);
                havai_anim.start();

        }
    }
}