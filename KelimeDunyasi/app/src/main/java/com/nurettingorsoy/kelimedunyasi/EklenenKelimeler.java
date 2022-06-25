package com.nurettingorsoy.kelimedunyasi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class EklenenKelimeler extends SecondMenuAktivity {
    //implements LoaderManager.LoaderCallbacks<Cursor>
    private Toolbar toolbar;

    RecyclerView dataList;
    List<String> eklenenKelime;
    List<String> eklenenAnlam;

    KelimelerimAdapter adapter;
    static final Uri CONTENT_URI=SozlukProvider.CONTENT_URI;
    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //animasyon kullanmak için yazılan kod :
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eklenen_kelimeler);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        toolbar = findViewById(R.id.myAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getIntent();
        Slide slide = new Slide();
        slide.setDuration(750);
        slide.setSlideEdge(Gravity.LEFT);//SOLDAN GELSİN
        getWindow().setEnterTransition(slide);
        getWindow().setAllowEnterTransitionOverlap(false);

        dataList = findViewById(R.id.dataList);

        eklenenKelime = new ArrayList<>();
        eklenenAnlam = new ArrayList<>();

           //getSupportLoaderManager().initLoader(100,null,this);
           getData();

        ItemClickSupport.addTo(dataList)
        .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
        @Override
        public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        Intent intent = new Intent(getApplicationContext(),KelimeEkle.class);
        intent.putExtra("info","old");
        intent.putExtra("eklenenkelime",eklenenKelime.get(position));//kaçıncı positionda buradan alacaz
        intent.putExtra("eklenenanlam",eklenenAnlam.get(position));
        startActivity(intent);
        finish();
        }
        });
}
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    public void getData(){
        String[] projection = {"eklenenkelime","eklenenanlam"};
        cursor = getContentResolver().query(CONTENT_URI,projection,null,null,null);
        //int idIx = cursor.getColumnIndex("id");

        while (cursor.moveToNext()){

            int kelimeIx = cursor.getColumnIndex("eklenenkelime");
            int anlamIx = cursor.getColumnIndex("eklenenanlam");
            eklenenKelime.add(cursor.getString(kelimeIx));
            eklenenAnlam.add(cursor.getString(anlamIx));
        }
        adapter = new KelimelerimAdapter(this,cursor);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        dataList.setLayoutManager(gridLayoutManager);
        dataList.setHasFixedSize(true); //performansı arttırıyormuş.
        dataList.setAdapter(adapter);
    }


}


