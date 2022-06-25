package com.nurettingorsoy.kelimedunyasi;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class SecondMenuAktivity extends AppCompatActivity{
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.second_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent(SecondMenuAktivity.this, KelimeEkle.class);
                intent.putExtra("info","new");
                startActivity(intent);
                break;
             case R.id.search:
                Toast.makeText(this, "Second Item Clıcked", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }


}
