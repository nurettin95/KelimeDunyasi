package com.nurettingorsoy.kelimedunyasi;


import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class KelimelerimdenSor extends BosMenuAktivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelimelerimden_sor);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        toolbar = findViewById(R.id.myAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    public void quiz(View view){

        Intent intent = new Intent(KelimelerimdenSor.this,Quiz.class);
        startActivity(intent);
    }
    public void match(View view){
        Intent intent2 = new Intent(KelimelerimdenSor.this,Match.class);
        startActivity(intent2);
    }
}