package com.nurettingorsoy.kelimedunyasi;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class KelimelerimAdapter extends RecyclerView.Adapter<KelimelerimAdapter.KelimelerimViewHolder> {

    private Context mContext; //layoutinflatere ulaşmak için
    private Cursor  mCursor;
    static final Uri CONTENT_URI=SozlukProvider.CONTENT_URI;



    public KelimelerimAdapter(Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;

    }

    public class  KelimelerimViewHolder extends RecyclerView.ViewHolder{
        //ViewYertutucu
        TextView eklenenKelime;
        TextView eklenenAnlam;
        public KelimelerimViewHolder(@NonNull View itemView) {
            super(itemView);

            eklenenKelime = itemView.findViewById(R.id.textView);
            eklenenAnlam = itemView.findViewById(R.id.textView2);

        }
    }

    @NonNull
    @Override
    public KelimelerimViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.custom_grim_layout,parent,false);
        //xml dosyalarını java dosyalarına çeviren method inflater
        return new KelimelerimViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KelimelerimViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position)){
            return;
        }
        String eklenenKelime = mCursor.getString(mCursor.getColumnIndex("eklenenkelime"));
        String eklenenAnlam  = mCursor.getString(mCursor.getColumnIndex("eklenenanlam"));
        //long id = mCursor.getLong(mCursor.getColumnIndex("eklenenid"));



        holder.eklenenKelime.setText(eklenenKelime);
        holder.eklenenAnlam.setText(eklenenAnlam);
        //holder.itemView.setTag(id);

       /*holder.constraintLayout.setTag(holder);
       holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KelimelerimViewHolder holder = (KelimelerimViewHolder)v.getTag();
                int position = holder.getPosition();
                mCursor.getColumnIndex("eklenenkelime");
                Toast.makeText(mContext, ""+position, Toast.LENGTH_SHORT).show();
            }
        });*/

    }
    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
    public void swapCursor(Cursor newCursor){
        if(mCursor !=null){
            mCursor.close();
        }
        mCursor = newCursor;
        if(newCursor != null){
            notifyDataSetChanged();
        }
    }
    public void deleteItem(int positon){
        String a = mCursor.getString(mCursor.getColumnIndex("eklenenkelime"));
        String[] args ={a};

        mContext.getContentResolver().delete(CONTENT_URI,"eklenenkelime=?",args);
    }
}
