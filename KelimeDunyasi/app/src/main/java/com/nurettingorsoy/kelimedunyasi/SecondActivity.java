package com.nurettingorsoy.kelimedunyasi;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.airbnb.lottie.LottieAnimationView;

public class SecondActivity extends BosMenuAktivity implements View.OnClickListener {
    private Toolbar toolbar;
    Button kelime_ezberle,kelimelerim,kelimelerimdensor,metin_yakala_buton;

    LottieAnimationView lottieAnimationView;
    boolean isCheckedDone = false;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_second);

        //Toolbar logo mogo
        toolbar = findViewById(R.id.myAppBar);
        //toolbar.setTitle("Başlık");
        //toolbar.setSubtitle("Alt başlık");
        //toolbar.setNavigationIcon(R.drawable.done);
        toolbar.setLogo(R.drawable.done);
        //Toolbar logo mogo
        setSupportActionBar(toolbar);

        ilkleme();

        //aktivite geçiş animasyonları
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.RIGHT);//sağdan gel
        slide.setDuration(750);

        Slide slide2 = new Slide();
        slide2.setSlideEdge(Gravity.RIGHT);//sağdan çık demişsin
        slide2.setDuration(750);

        getWindow().setReenterTransition(slide);
        getWindow().setAllowReturnTransitionOverlap(false);//Animsyonları sırasıyla çalıştırıyor.

        getWindow().setExitTransition(slide2);
        //aktivite geçiş animasyonları


        lottieAnimationView = findViewById(R.id.animation_view);
        lottieAnimationView.setOnClickListener(this);


    }
    public void ilkleme(){
        kelime_ezberle= findViewById(R.id.kelime_ezberle);
        kelimelerim = findViewById(R.id.kelimelerim);
        kelimelerimdensor = findViewById(R.id.kelimelerimdensor);
        metin_yakala_buton = findViewById(R.id.metin_yakala_buton);

        kelime_ezberle.setOnClickListener(this);
        kelimelerim.setOnClickListener(this);
        kelimelerimdensor.setOnClickListener(this);
        metin_yakala_buton.setOnClickListener(this);



        textView = findViewById(R.id.textView);


        textView.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.kelimelerim:
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
                Intent intent = new Intent(SecondActivity.this,EklenenKelimeler.class);
                startActivity(intent,options.toBundle());
                break;
            case R.id.kelime_ezberle:
                ActivityOptions options2 = ActivityOptions.makeSceneTransitionAnimation(this);
                Intent intent2 = new Intent(SecondActivity.this, Kelimeler.class);
                startActivity(intent2,options2.toBundle());
                break;
            case R.id.metin_yakala_buton:
                Intent intent3 = new Intent(SecondActivity.this,MetinTara.class);
                startActivity(intent3);
                break;

            case R.id.textView:
                AnimatorSet rootSet = new AnimatorSet();
                ObjectAnimator rotate = ObjectAnimator.ofFloat(textView,"rotation",0f,360f);
                rotate.setDuration(500);
                rootSet.play(rotate);
                rootSet.start();
                break;
            case R.id.animation_view:
                if(isCheckedDone){
                    //lottieAnimationView.setSpeed(-1);
                    lottieAnimationView.playAnimation();
                    isCheckedDone = false;

                }else{
                    //lottieAnimationView.setSpeed(1);
                    lottieAnimationView.pauseAnimation();
                    isCheckedDone=true;
                }
                break;
            case R.id.kelimelerimdensor:
                Intent intent4 = new Intent(this,KelimelerimdenSor.class);
                startActivity(intent4);
                break;
            default:
        }
    }
}