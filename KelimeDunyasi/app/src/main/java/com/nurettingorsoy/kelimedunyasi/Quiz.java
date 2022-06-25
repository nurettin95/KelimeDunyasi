package com.nurettingorsoy.kelimedunyasi;


import androidx.appcompat.widget.Toolbar;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class Quiz extends BosMenuAktivity{
    private Toolbar toolbar;

    Button btn_answer0,btn_answer1,btn_answer2,btn_answer3;
    TextView tv_sure,tv_istatistik,tv_kelime;
    ImageView imageView,imageView1;

    ArrayList<String> eklenenKelimeArray;
    ArrayList<String> eklenenAnlamArray;

    Random random = new Random();
    int randomSoru;
    String dogruCevap;
    String aSikki; String bSikki; String cSikki;
    int numberCorrect = 0,numberIcorrect = 0;
    CountDownTimer timer;

    static final Uri CONTENT_URI = SozlukProvider.CONTENT_URI;

    //private final int buttonColor = getResources().getColor(R.color.teal_200);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        toolbar = findViewById(R.id.myAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btn_answer0 = findViewById(R.id.btn_answer0);
        btn_answer1 = findViewById(R.id.btn_answer1);
        btn_answer2 = findViewById(R.id.btn_answer2);
        btn_answer3 = findViewById(R.id.btn_answer3);

        tv_sure = findViewById(R.id.tv_sure);
        tv_istatistik = findViewById(R.id.tv_istatistik);
        tv_kelime = findViewById(R.id.tv_kelime);


        eklenenKelimeArray = new ArrayList<>();
        eklenenAnlamArray = new ArrayList<>();

        imageView = findViewById(R.id.neseli);
        imageView.setVisibility(View.INVISIBLE);
        imageView1 = findViewById(R.id.uzgun);
        imageView1.setVisibility(View.INVISIBLE);

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
        if(eklenenKelimeArray.size()>4){
            Soru();
            timer = new CountDownTimer(10000,1000){
                @Override
                public void onTick(long millisUntilFinished) {
                    tv_sure.setText(""+millisUntilFinished/1000);
                }

                @Override
                public void onFinish() {
                    timer.start();
                    Soru();
                }
            }.start();
            tv_istatistik.setText(numberCorrect+" / " + (numberCorrect+numberIcorrect));
            View.OnClickListener answerButtonClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button buttonClicked = (Button) v;
                    String answerSelected = buttonClicked.getText().toString();
                    //Toast.makeText(KelimelerimdenSor.this,"AnswerSelecte= "+ answerSelected,Toast.LENGTH_SHORT).show();
                    if(dogruCevap.equals(answerSelected)){
                        numberCorrect++;
                        tv_istatistik.setText(numberCorrect+" / " + (numberCorrect+numberIcorrect));
                        imageView.setVisibility(View.VISIBLE);

                        AnimatorSet rootSet = new AnimatorSet();
                        ObjectAnimator anim = ObjectAnimator.ofFloat(imageView,"alpha",0f,1f);
                        anim.setDuration(400);

                        AnimatorSet childSet = new AnimatorSet();

                        ObjectAnimator donme = ObjectAnimator.ofFloat(imageView,"rotation",0f,360.0f);
                        donme.setDuration(400);

                        AnimatorSet childSet2 = new AnimatorSet();
                        ObjectAnimator kaybolma = ObjectAnimator.ofFloat(imageView,"alpha",1f,0f);
                        kaybolma.setDuration(400);

                        rootSet.play(anim).before(childSet).before(childSet2);
                        childSet.play(donme).before(kaybolma);
                        childSet.play(kaybolma);
                        rootSet.start();

                       // buttonClicked.setBackgroundTintList(ColorStateList.valueOf(buttonColor));

                    }
                    else{
                        numberIcorrect++;
                        tv_istatistik.setText(numberCorrect +" / " + (numberCorrect+numberIcorrect));
                        Toast.makeText(Quiz.this,"Doğru Cevap: "+ dogruCevap,Toast.LENGTH_SHORT).show();
                        imageView1.setVisibility(View.VISIBLE);
                        AnimatorSet rootSet = new AnimatorSet();
                        ObjectAnimator anim = ObjectAnimator.ofFloat(imageView1,"alpha",0f,1f);
                        anim.setDuration(400);

                        AnimatorSet childSet = new AnimatorSet();
                        ObjectAnimator kaybolma = ObjectAnimator.ofFloat(imageView1,"alpha",1f,0f);
                        kaybolma.setDuration(400);

                        rootSet.play(anim).before(childSet);
                        childSet.play(kaybolma);
                        rootSet.start();

                    }

                    timer.start();
                    Soru();
                }
            };
            btn_answer0.setOnClickListener(answerButtonClickListener);
            btn_answer1.setOnClickListener(answerButtonClickListener);
            btn_answer2.setOnClickListener(answerButtonClickListener);
            btn_answer3.setOnClickListener(answerButtonClickListener);
        }
        else{
            tv_kelime.setText("");
            tv_istatistik.setText("0/0");
            tv_sure.setText("0");
            btn_answer0.setText("A");
            btn_answer1.setText("B");
            btn_answer2.setText("C");
            btn_answer3.setText("D");
            Toast.makeText(Quiz.this,"En az 4 kelime eklemelisiz.", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    public void Soru(){
        int eklenenKelimelerinSayisi = eklenenKelimeArray.size();
        randomSoru = random.nextInt(eklenenKelimelerinSayisi);
        dogruCevap = eklenenAnlamArray.get(randomSoru);


        ArrayList<String> eklenenAnlamArrayCopy = new ArrayList<String>();
        eklenenAnlamArrayCopy = (ArrayList)eklenenAnlamArray.clone();
        eklenenAnlamArrayCopy.remove(randomSoru);
        int yeniSize = eklenenAnlamArrayCopy.size();
        int yanlisCevap = random.nextInt(yeniSize);
        aSikki = eklenenAnlamArrayCopy.get(yanlisCevap);

        ArrayList<String> eklenenAnlamArrayCopy2 = new ArrayList<String>();
        eklenenAnlamArrayCopy2 = (ArrayList)eklenenAnlamArrayCopy.clone();
        eklenenAnlamArrayCopy2.remove(yanlisCevap);
        int yeniSize2 = eklenenAnlamArrayCopy2.size();
        int yanlisCevap2 = random.nextInt(yeniSize2);
        bSikki = eklenenAnlamArrayCopy2.get(yanlisCevap2);

        ArrayList<String> eklenenAnlamArrayCopy3 = new ArrayList<String>();
        eklenenAnlamArrayCopy3 = (ArrayList)eklenenAnlamArrayCopy2.clone();
        eklenenAnlamArrayCopy3.remove(yanlisCevap2);
        int yeniSize3 = eklenenAnlamArrayCopy3.size();
        int yanlisCevap3 = random.nextInt(yeniSize3);
        cSikki = eklenenAnlamArrayCopy3.get(yanlisCevap3);


        int pozisyonCevap = random.nextInt(4);

        switch (pozisyonCevap){
            case 0:
                btn_answer0.setText(aSikki);
                btn_answer1.setText(bSikki);
                btn_answer2.setText(cSikki);
                btn_answer3.setText(dogruCevap);

                btn_answer0.setEnabled(true);
                btn_answer1.setEnabled(true);
                btn_answer2.setEnabled(true);
                btn_answer3.setEnabled(true);

                tv_kelime.setText(eklenenKelimeArray.get(randomSoru));
               // tv_istatistik.setText(numberCorrect + "/"+ (totalKelime));

                break;
            case 1:
                btn_answer0.setText(dogruCevap);
                btn_answer1.setText(bSikki);
                btn_answer2.setText(cSikki);
                btn_answer3.setText(aSikki);

                btn_answer0.setEnabled(true);
                btn_answer1.setEnabled(true);
                btn_answer2.setEnabled(true);
                btn_answer3.setEnabled(true);

                tv_kelime.setText(eklenenKelimeArray.get(randomSoru));
               // tv_bottommessage.setText(numberCorrect + "/"+ (totalQuestions));
                break;
            case 2:

                btn_answer0.setText(cSikki);
                btn_answer1.setText(dogruCevap);
                btn_answer2.setText(aSikki);
                btn_answer3.setText(bSikki);

                btn_answer0.setEnabled(true);
                btn_answer1.setEnabled(true);
                btn_answer2.setEnabled(true);
                btn_answer3.setEnabled(true);

                tv_kelime.setText(eklenenKelimeArray.get(randomSoru));
               // tv_bottommessage.setText(numberCorrect + "/"+ (totalQuestions));

                break;

            case 3:

                btn_answer0.setText(bSikki);
                btn_answer1.setText(cSikki);
                btn_answer2.setText(dogruCevap);
                btn_answer3.setText(aSikki);

                btn_answer0.setEnabled(true);
                btn_answer1.setEnabled(true);
                btn_answer2.setEnabled(true);
                btn_answer3.setEnabled(true);

                tv_kelime.setText(eklenenKelimeArray.get(randomSoru));
                //tv_bottommessage.setText(numberCorrect + "/"+ (totalQuestions));
            default:
        }
    }

}