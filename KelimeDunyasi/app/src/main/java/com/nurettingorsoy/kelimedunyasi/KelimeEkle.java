package com.nurettingorsoy.kelimedunyasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

public class KelimeEkle extends BosMenuAktivity{
    private Toolbar toolbar;
    EditText editTextKelime,editTextAnlam;
    String eklenenKelime,eklenenAnlam;
    ImageButton imageButton3,imageButton4,imageButton;
    static final Uri CONTENT_URI=SozlukProvider.CONTENT_URI;
    String birincikelime, birincianlam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelime_ekle);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        toolbar = findViewById(R.id.myAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editTextAnlam = findViewById(R.id.editTextAnlam);
        editTextKelime = findViewById(R.id.editTextKelime);
       // imageButton2 = findViewById(R.id.imageButton2);
        imageButton3 = findViewById(R.id.imageButton3);
        imageButton4 = findViewById(R.id.imageButton4);
        imageButton = findViewById(R.id.imageButton);


        Intent intent = getIntent();
        String info = intent.getStringExtra("info");

        if (info.matches("new")) {
            editTextKelime.setText("");
            editTextAnlam.setText("");
            imageButton.setVisibility(View.VISIBLE);//savebutonu
            imageButton3.setVisibility(View.INVISIBLE);//update
            imageButton4.setVisibility(View.INVISIBLE);//delete

        }
        else{
            String eklenenkelimeid = intent.getStringExtra("eklenenkelime");//buradan database'ten kelimeyi alıyor
            editTextKelime.setText(eklenenkelimeid);//buradan positionunu
            birincikelime = eklenenkelimeid;

            int position = intent.getIntExtra("position",0);

            String eklenenanlamid = intent.getStringExtra("eklenenanlam");
            editTextAnlam.setText(eklenenanlamid);
            birincianlam = eklenenanlamid;


            imageButton.setVisibility(View.INVISIBLE);
            imageButton3.setVisibility(View.VISIBLE);
            imageButton4.setVisibility(View.VISIBLE);

        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    public void done(View view){

        if(editTextKelime.getText().toString().trim().length()==0 || editTextAnlam.getText().toString().trim().length()==0){
            return;
        }
        eklenenKelime = editTextKelime.getText().toString();
        eklenenAnlam  =  editTextAnlam.getText().toString();

        ContentValues values = new ContentValues();
        values.put("eklenenkelime",eklenenKelime);
        values.put("eklenenanlam",eklenenAnlam);
        Uri _uri = getContentResolver().insert(CONTENT_URI,values);


        Intent intent = new Intent(KelimeEkle.this,EklenenKelimeler.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

   /* @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
        case R.id.add:
        break;
        }
        return super.onOptionsItemSelected(item);
    }*/

    public void update(View view){
        String eklenenKelime = editTextKelime.getText().toString();
        String eklenenAnlam  =  editTextAnlam.getText().toString();

        String[] selectionbirincikelime ={birincikelime};
        String[] selectionbirincianlam = {birincianlam};

        ContentValues contentValues = new ContentValues();
        contentValues.put("eklenenkelime",eklenenKelime);
        contentValues.put("eklenenanlam",eklenenAnlam);


        getContentResolver().update(CONTENT_URI,contentValues,"eklenenkelime=?",selectionbirincikelime);
        getContentResolver().update(CONTENT_URI,contentValues,"eklenenanlam=?",selectionbirincianlam);

        Intent intent = new Intent(getApplicationContext(),EklenenKelimeler.class);
        startActivity(intent);
        finish();

    }

    public void delete(View view){
        String eklenenkelime = editTextKelime.getText().toString();
        String eklenenanlam = editTextAnlam.getText().toString();

        String[] selectionArgs = {eklenenkelime};
        String[] selectionArgs2 = {eklenenanlam};

        getContentResolver().delete(CONTENT_URI,"eklenenkelime=?",selectionArgs);
        getContentResolver().delete(CONTENT_URI,"eklenenanlam=?",selectionArgs2);

        Intent intent = new Intent(getApplicationContext(),EklenenKelimeler.class);
        startActivity(intent);
        finish();

    }
}
