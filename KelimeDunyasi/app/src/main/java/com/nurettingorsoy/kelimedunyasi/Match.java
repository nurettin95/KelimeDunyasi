package com.nurettingorsoy.kelimedunyasi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Match extends BosMenuAktivity {

    private Toolbar toolbar;
    private Context context;
    private int widthButton,heightButton;
    private LinearLayout containerLeft, containerRight;
    private int x,y;

    private int silinenButonSayisi = 0;
    private static final int DURATION=400;
    private static final int DURATION_KAYBOL=300;
    private static final int DURATION_ADD_BUTTON=700;
    private Button selectButtonLeft=null;
    private Button selectButtonRight=null;

    private boolean buttonClickEnable = true;

    ArrayList<String> eklenenKelimeArray;
    ArrayList<String> eklenenAnlamArray;
    private List<Model> modelListLeft,modelListRight;
    Dialog dialog;
    LayoutInflater layoutInflater;
    static final Uri CONTENT_URI = SozlukProvider.CONTENT_URI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        toolbar = findViewById(R.id.myAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        dialog = new Dialog(this, R.style.CustomDialog);
        layoutInflater = LayoutInflater.from(this);
        eklenenKelimeArray = new ArrayList<>();
        eklenenAnlamArray = new  ArrayList<>();
        modelListLeft = new ArrayList<>();
        modelListRight = new ArrayList<>();
        containerLeft = findViewById(R.id.containerLeft);
        containerRight = findViewById(R.id.containerRigth);

        String[] projection={"eklenenkelime","eklenenanlam"};
        Cursor cursor = getContentResolver().query(CONTENT_URI,projection,null,null,null);
        if(cursor.moveToFirst()){
            do {
                int kelimeIx = cursor.getColumnIndex("eklenenkelime");
                int anlamIx = cursor.getColumnIndex("eklenenanlam");
                eklenenKelimeArray.add(cursor.getString(kelimeIx));
                eklenenAnlamArray.add(cursor.getString(anlamIx));
            } while (cursor.moveToNext());
        }
        //button genişlikk ve yüksekliğinin hesaplanması
        Display display = getWindowManager().getDefaultDisplay(); // ekran genişliğini yükselekliğini bulmak için yazıyoruz
        Point point = new Point();
        display.getSize(point);

        widthButton = (int) ((point.x) / (2+.4));
        heightButton = (int) ((point.y) / (6+1.5));

        for(int j = 0; j<eklenenKelimeArray.size(); j++){
            modelListLeft.add(new Model(eklenenKelimeArray.get(j),eklenenAnlamArray.get(j),j));
        }
        //model Listemizi klonluyoruz
        for (Model model : modelListLeft){
            modelListRight.add(model);
        }
        //karıştıralım
        Collections.shuffle(modelListLeft);
        Collections.shuffle(modelListRight);

        if(eklenenKelimeArray.size()<4){
            Toast.makeText(this,"En az 6 kelime eklemiş olmanız gerekiyor",Toast.LENGTH_SHORT).show();
        }else{




        //soldaki butonlar konteynira dolduruluyor.
        for(int i=0; i<6; i++)
        {
            Button button = new Button(this);
            button.setWidth(widthButton);
            button.setHeight(heightButton);
            button.setTextColor(getResources().getColor(R.color.black));
            button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorButton)));
            button.setAllCaps(false);//küçük harflerle yazsın
            button.setTextSize(heightButton/15);
            button.setId(modelListLeft.get(0).getId());
            button.setText(modelListLeft.get(0).getKelime());
            button.setOnTouchListener(touchListener);//dokundugumuz yerin koordinatlarını almamız için
            button.setOnClickListener(clickListenerLeft);
            containerLeft.addView(button);
            modelListLeft.remove(0);

        }

        //sağdaki butonlar konteynira dolduruluyor.
        for(int i=0; i<6; i++)
        {
            Button button = new Button(this);
            button.setWidth(widthButton);
            button.setHeight(heightButton);
            button.setTextColor(getResources().getColor(R.color.black));
            button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorButton)));
            button.setAllCaps(false);//küçük harflerle yazsın
            button.setTextSize(heightButton/15);
            button.setId(modelListRight.get(0).getId());
            button.setText(modelListRight.get(0).getAnlami());
            button.setOnTouchListener(touchListener);//dokundugumuz yerin koordinatlarını almamız için
            button.setOnClickListener(clickListenerRight);
            containerRight.addView(button);
            modelListRight.remove(0);

        }
        girisAnimasyonu();
        }
    }
    private View.OnTouchListener touchListener = new View.OnTouchListener(){
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            x = (int)event.getX();
            y = (int)event.getY();

            return false;
        }
    };
    private View.OnClickListener clickListenerLeft = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!buttonClickEnable)
                return;

            if(selectButtonRight != null){
                //eşleştirme kontrolü yapılacak
                selectButtonLeft = (Button) v;
                buttonClickEnable=false;

                if(selectButtonLeft.getId()==selectButtonRight.getId()){
                     revealEffect();
                }else {
                    revealEffectRed();
                }
            }else{
                if(selectButtonLeft != null)
                selectButtonLeft.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorButton)));

                selectButtonLeft = (Button) v;
                selectButtonLeft.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorButtonSelect)));

            }
        }
    };
    private View.OnClickListener clickListenerRight = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!buttonClickEnable)
                return;
            if(selectButtonLeft != null){
                //eşleştirme

                selectButtonRight = (Button) v;
                buttonClickEnable=false;

                if(selectButtonLeft.getId()==selectButtonRight.getId()){
                    revealEffect();
                }else {
                    revealEffectRed();
                }
            }
            else{
                if(selectButtonRight != null)
                 selectButtonRight.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorButton)));

                selectButtonRight = (Button) v;
                selectButtonRight.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorButtonSelect)));

            }
        }
    };
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    private void girisAnimasyonu() {

        for (int i=0; i<containerLeft.getChildCount(); i++){
            Button button = (Button)containerLeft.getChildAt(5-i);
            ObjectAnimator animator = ObjectAnimator.ofFloat(button,"translationX",-widthButton*1.5f,0);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());//dururken yavaşlasın
            animator.setDuration(DURATION + i*70);
            animator.start();
        }

        for (int i = 0; i< containerRight.getChildCount(); i++){
            Button button = (Button) containerRight.getChildAt(5-i);
            ObjectAnimator animator = ObjectAnimator.ofFloat(button,"translationX",widthButton*1.5f,0);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());//dururken yavaşlasın
            animator.setDuration(DURATION + i*70);
            animator.start();
        }
    }
    private void revealEffectRed() {
        int finalRadius = Math.max(widthButton,heightButton);

        Animator animRight =
                ViewAnimationUtils.createCircularReveal(selectButtonRight,x,y,0,finalRadius);
        Animator animLeft =
                ViewAnimationUtils.createCircularReveal(selectButtonLeft,x,y,0,finalRadius);

        selectButtonLeft.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorButtonRed)));
        selectButtonRight.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorButtonRed)));

        animLeft.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) { //renkler eski haline gelecek
                super.onAnimationEnd(animation);

                selectButtonLeft.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorButton)));
                selectButtonRight.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorButton)));

                selectButtonLeft=null;
                selectButtonRight=null;

                buttonClickEnable=true;
            }
        });

        animLeft.setDuration(DURATION);
        animRight.setDuration(DURATION);
        animLeft.start();
        animRight.start();
    }
    private void revealEffect() {
        int finalRadius = Math.max(widthButton,heightButton);

        Animator animRight =
                ViewAnimationUtils.createCircularReveal(selectButtonRight,x,y,0,finalRadius);
        Animator animLeft =
                ViewAnimationUtils.createCircularReveal(selectButtonLeft,x,y,0,finalRadius);

        selectButtonLeft.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorButtonGreen)));
        selectButtonRight.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorButtonGreen)));

        animLeft.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) { //renkler eski haline gelecek
                super.onAnimationEnd(animation);

                kaybol();
            }

        });
        animLeft.setDuration(DURATION);
        animRight.setDuration(DURATION);
        animLeft.start();
        animRight.start();
    }
    private void kaybol() {

        ObjectAnimator animatorLeft = ObjectAnimator.ofFloat(selectButtonLeft,"TranslationX",0,-widthButton*1.5f);
        ObjectAnimator animatorRight = ObjectAnimator.ofFloat(selectButtonRight,"TranslationX",0,widthButton*1.5f);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(animatorLeft,animatorRight);
        set.setDuration(DURATION_KAYBOL);
        set.setInterpolator(new LinearInterpolator());

        set.addListener(new AnimatorListenerAdapter(){
            @Override
            public void onAnimationEnd(Animator animation) { //renkler eski haline gelecek
                super.onAnimationEnd(animation);

                selectButtonLeft.setVisibility(View.GONE);//boşlugu doldurması saglanacak
                selectButtonRight.setVisibility(View.GONE);
                selectButtonLeft = null;
                selectButtonRight = null;

                buttonClickEnable = true;

                silinenButonSayisi++;

                if(modelListLeft.size() ==0 && modelListRight.size()==0){
                    //eklenecek buton kalmadı
                    if(silinenButonSayisi == eklenenKelimeArray.size()){

                        CardView view = (CardView) layoutInflater.inflate(R.layout.dialog,null);
                        dialog.setContentView(view);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();

                    }
                }
                else{
                    //eklenecek eleman kalmadıysa
                    addButon();
                }
            }
        });
        set.start();

    }
    private void addButon() {
        Button button = new Button(this);
        button.setWidth(widthButton);
        button.setHeight(heightButton);
        button.setTextColor(getResources().getColor(R.color.black));
        button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorButton)));
        button.setAllCaps(false);//küçük harflerle yazsın
        button.setTextSize(heightButton/15);
        button.setId(modelListLeft.get(0).getId());
        button.setText(modelListLeft.get(0).getKelime());
        button.setOnTouchListener(touchListener);//dokundugumuz yerin koordinatlarını almamız için
        button.setOnClickListener(clickListenerLeft);
        containerLeft.addView(button,0);//üstten eklememiz için
        modelListLeft.remove(0);

        ObjectAnimator animatorLeft = ObjectAnimator.ofFloat(button,"TranslationX",-widthButton*1.5f,0);
        animatorLeft.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorLeft.setDuration(DURATION_ADD_BUTTON);
        animatorLeft.start();


        Button buttonR = new Button(this);
        buttonR.setWidth(widthButton);
        buttonR.setHeight(heightButton);
        buttonR.setTextColor(getResources().getColor(R.color.black));
        buttonR.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorButton)));
        buttonR.setAllCaps(false);//küçük harflerle yazsın
        buttonR.setTextSize(heightButton/15);
        buttonR.setId(modelListRight.get(0).getId());
        buttonR.setText(modelListRight.get(0).getAnlami());
        buttonR.setOnTouchListener(touchListener);//dokundugumuz yerin koordinatlarını almamız için
        buttonR.setOnClickListener(clickListenerRight);
        containerRight.addView(buttonR,0);//üstten eklememiz için
        modelListRight.remove(0);

        ObjectAnimator animatorRight = ObjectAnimator.ofFloat(buttonR,"TranslationX",widthButton*1.5f,0);
        animatorRight.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorRight.setDuration(DURATION_ADD_BUTTON);
        animatorRight.start();

    }
}