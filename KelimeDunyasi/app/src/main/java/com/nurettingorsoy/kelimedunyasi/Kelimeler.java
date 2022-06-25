package com.nurettingorsoy.kelimedunyasi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.transition.Explode;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class Kelimeler extends AppCompatActivity {
    EditText editText1, editText2, editText3;
    TextView textView1, textView2, textView3,timeText;
    ArrayList<String> kelimeArray;
    ArrayList<String> anlamArray;
    ArrayList<Integer> idArray;
    int skor;
    Button button4;
    String birinciKelime,ikinciKelime,ucuncuKelime;
    Random random = new Random();
    static final Uri CONTENT_URI2 = SozlukProvider.CONTENT_URI2;

    int i =random.nextInt(191);
    int j =random.nextInt(191);
    int k= random.nextInt(191);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //animasyon kullanmak için yazılan kod :
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelimeler);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        timeText = findViewById(R.id.timeText);
        button4 = findViewById(R.id.button4);

        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText10);
        editText3 = findViewById(R.id.editText11);

        kelimeArray = new ArrayList<>();
        anlamArray = new ArrayList<>();
        idArray = new ArrayList<>();

        getIntent();
        Fade fade = new Fade();
        fade.setDuration(1000);
        // fade.setInterpolator(new AnticipateInterpolator());
        getWindow().setEnterTransition(fade);
        getWindow().setAllowEnterTransitionOverlap(false);

        String[] projection = {"dahilkelime", "dahilanlam"};

        Cursor cursor = getContentResolver().query(CONTENT_URI2, projection, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int kelimeIx = cursor.getColumnIndex("dahilkelime");
                int anlamIx = cursor.getColumnIndex("dahilanlam");

                kelimeArray.add(cursor.getString(kelimeIx));
                anlamArray.add(cursor.getString(anlamIx));
            } while (cursor.moveToNext());
        }
        skor = 0;
        if(i != j && i!=k && k!=j){
            textView1.setText(kelimeArray.get(i)+" Kelimesinin \nAnlamı : "+anlamArray.get(i));
            textView2.setText(kelimeArray.get(j)+" Kelimesinin \nAnlamı : "+anlamArray.get(j));
            textView3.setText(kelimeArray.get(k)+" Kelimesinin \nAnlamı : "+anlamArray.get(k));
        }
        else{
            Intent intent = getIntent();
            startActivity(intent);
        }
        new CountDownTimer(11500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeText.setText("Süre: "+millisUntilFinished/1000);
                editText1.setVisibility(View.GONE);
                editText2.setVisibility(View.GONE);
                editText3.setVisibility(View.GONE);
                button4.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onFinish() {
                timeText.setText("Süre Bitti.");
                editText1.setVisibility(View.VISIBLE);
                editText2.setVisibility(View.VISIBLE);
                editText3.setVisibility(View.VISIBLE);
                button4.setVisibility(View.VISIBLE);

                textView1.setText(kelimeArray.get(k)+" Kelimesinin Anlamı Nedir ? ");
                textView2.setText(kelimeArray.get(i)+" Kelimesinin Anlamı Nedir ?");
                textView3.setText(kelimeArray.get(j)+" Kelimesinin Anlamı Nedir ?");

                Toast.makeText(Kelimeler.this,"Kelimelerin Anlamlarını Giriniz.",Toast.LENGTH_SHORT).show();

            }
        }.start();
    }

    public void kaydet(View view){
        if(editText1.getText().toString().trim().length() == 0 || editText2.getText().toString().trim().length()==0 || editText3.getText().toString().trim().length()==0){

            Toast.makeText(Kelimeler.this, "Kelimelerin anlamlarını giriniz", Toast.LENGTH_SHORT).show();
        }
        else {
            birinciKelime = editText1.getText().toString();
            ikinciKelime = editText2.getText().toString();
            ucuncuKelime = editText3.getText().toString();

            Intent intent = new Intent(Kelimeler.this, Sonuc.class);
            intent.putExtra("kelime", birinciKelime);
            intent.putExtra("kelimeiki", ikinciKelime);
            intent.putExtra("kelimeuc",ucuncuKelime);
            intent.putExtra("anlam", anlamArray.get(k));
            intent.putExtra("anlamiki", anlamArray.get(i));
            intent.putExtra("anlamuc", anlamArray.get(j));
            startActivity(intent);
            finish();

        }
    }
}
