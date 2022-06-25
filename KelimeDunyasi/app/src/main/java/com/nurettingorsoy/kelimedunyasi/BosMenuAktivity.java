package com.nurettingorsoy.kelimedunyasi;

import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.app.AppCompatActivity;

public class BosMenuAktivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.bos_menu,menu);
        return true;
    }
}
