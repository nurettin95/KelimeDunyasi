package com.nurettingorsoy.kelimedunyasi;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SozlukProvider extends ContentProvider {
    SQLiteDatabase db;

    //CONTENT PROVIDER ILE ILGILI KISIM  --once manifestte belirttik

    static final String CONTENT_AUTHORITY = "com.nurettingorsoy.kelimedunyasi.SozlukProvider";
    static final String PATH_EKLENEN_KELIMELER = "eklenenkelimeler"; //tablo adına ulaşacağız
    static final String PATH_DAHIL_KELIMELER = "dahilkelimeler";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_EKLENEN_KELIMELER);
    static final Uri CONTENT_URI2 = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_DAHIL_KELIMELER);

    static final UriMatcher matcher;

    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(CONTENT_AUTHORITY, PATH_EKLENEN_KELIMELER, 1);
        matcher.addURI(CONTENT_AUTHORITY, PATH_DAHIL_KELIMELER, 2);//kişiler tabloma ulaşma linkimi 1 verdim
        //başka bir tablo olsaydı başka numara verecektik
    }
    //CONTENT PROVIDER ILE ILGILI KISIM


    //DATABASE VE TABLOLAR ILE ILGILI KISIM
    private final static String DATABASE_NAME = "Kelimeler";
    private final static int DATABASE_VERSION = 9;
    private final static String EKLENEN_KELIMELER_TABLE_NAME = "eklenenkelimeler";
    private final static String DAHIL_KELIMELER_TABLE_NAME = "dahilkelimeler";
    private final static String CREATE_EKLENEN_KELIMELER_TABLE = " CREATE TABLE IF NOT EXISTS " + EKLENEN_KELIMELER_TABLE_NAME
            + " (eklenenid INTEGER PRIMARY KEY AUTOINCREMENT, " + " eklenenkelime TEXT,"
            + "  eklenenanlam TEXT)";
    private final static String CREATE_DAHIL_KELIMELER_TABLE = " CREATE TABLE IF NOT EXISTS " + DAHIL_KELIMELER_TABLE_NAME
            + "(dahilid INTEGER PRIMARY KEY AUTOINCREMENT, " + " dahilkelime TEXT,"
            + " dahilanlam TEXT)";

    //DATABASE VE TABLOLAR ILE ILGILI KISIM

    @Override
    public boolean onCreate() {
        DatabaseHelper helper = new DatabaseHelper(getContext());

        db = helper.getWritableDatabase();

        return db != null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        switch (matcher.match(uri)) {

            case 1:
                Cursor cursor = db.query(EKLENEN_KELIMELER_TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                return cursor;
            case 2:
                Cursor cursor2 = db.query(DAHIL_KELIMELER_TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                return cursor2;
            default:
                 throw new IllegalArgumentException("Bilinmeyen uri" + uri);
        }
    }



    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (matcher.match(uri)) {

            case 1:
                long eklenenSatirID = db.insert(EKLENEN_KELIMELER_TABLE_NAME, null, values);
                if (eklenenSatirID > 0) {
                    Uri _uri = ContentUris.withAppendedId(CONTENT_URI, eklenenSatirID);
                    getContext().getContentResolver().notifyChange(_uri,null);
                    return _uri;
                }
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (matcher.match(uri)) {

            case 1:
                return kayitSil(uri, selection, selectionArgs, EKLENEN_KELIMELER_TABLE_NAME);
            case 2:
                return kayitSil(uri, selection, selectionArgs, DAHIL_KELIMELER_TABLE_NAME);
            default:
                throw new IllegalArgumentException("BILINMEYEN DELETE URI"+uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowCount = 0 ;
        switch (matcher.match(uri)){
            case 1:
                rowCount = db.update(EKLENEN_KELIMELER_TABLE_NAME,values,selection,selectionArgs);
            break;
            case 2:
                rowCount = db.update(CREATE_DAHIL_KELIMELER_TABLE,values,selection,selectionArgs);
                break;
            default:

                throw new IllegalArgumentException("Failed URI"+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return rowCount;
    }
    public class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(@Nullable Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_EKLENEN_KELIMELER_TABLE);
            db.execSQL(CREATE_DAHIL_KELIMELER_TABLE);
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('able','yapabilen')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('about','hakkında')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('above','üstünde')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('across','karşısında')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('action','aksiyon')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('actually','aslında')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('add','eklemek')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('adjective','sıfat')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('afraid','korkmuş')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('after','sonra')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('age','yaş')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('ago','önce')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('ahead',' önünde')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam)values('air','hava')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('all','hepsi')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('almost','neredeyse')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('animal','hayvan')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('another','diğer')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('alone','yalnız')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('along','boyunca')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('already','zaten')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam)values('also','ayrıca')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('although','rağmen')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('always','her zaman')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam)values('among','arasında')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('amount','miktar')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam)values('and','ve')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('angle','açı')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('answer','cevap')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('any','herhangi')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('blood','kan')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('blow','patlamak')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('blue','mavi')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('board','tablo')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('boat','bot')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('body','vücut')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('bone','kemik')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('book','kitap')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('born','doğmuş')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('bottom','dip')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('box','kutu')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('branch','dal')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('break','kırmak')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('bright','parlak')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('bring','getirmek')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('broken','kırık')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('brought','getirilmiş')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('brown','kahverengi')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('building','bina')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('burn','yanmak')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('business','işletme')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('but','fakat')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('by','tarafından')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('call','aramak')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('came','geldi')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('can','yapabilmek')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('capital','başkent')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('captain','kaptan')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('car','araba')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('care','önemsemek')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('carefully','dikkatlice')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('carry','taşımak')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('cat','kedi')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('catch','yakalamak')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('cattle','inek')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('caught','yakalamak')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('cause','sebep')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('cell','hücre')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('center','merkez')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('century','yüzyıl')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('certain','kesin')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('chance','şans')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('change','değişmek')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('chart','tablo')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('chief','şef')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('child','çocuk')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('children','çocuklar')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('choose','seçmek')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('church','kilise')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('circle','çember')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('city','şehir')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('class','sınıf')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('clean','temiz')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('clear','temizlemek')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('climb','tırmanmak')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('close','kapatmak')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('cloth','elbise')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('cloud','bulut')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('coast','kıyı')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('cold','soğuk')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('color','renk')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('column','kolon')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('come','gelmek')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('common','ortak')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('company','şirket')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('compare','karşılaştırmak')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('complete','tamamlanmış')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('compound','madde')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('condition','durum')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('consider','düşünmek')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('contain','içermek')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('cook','pişirmek')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('copy','kopyalamak')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('corn','mısır')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('corner','köşe')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('correct','doğru')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('cost','masraf')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('cotton','pamuk')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('count','saymak')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('cover','kapsamak')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('cow','inek')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('create','oluşturmak')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('cry','ağlamak')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('crop','ekin')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('cross','çapraz')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('crowd','kalabalık')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('current','mevcut')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('cut','kesmek')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('dark','karanlık')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('dead','ölü')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('deal','anlaşma')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('decimal','ondalık')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('deap','derin')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('describe','açıklama')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('desert','çöl')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('desing','tasarım')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('details','ayrıntılar')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('determine','belirlemek')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('believe','inanmak')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('bed','yatak')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('bell','zill')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('belong','ait olmak')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('beside','yanında')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('between','arasında')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('big','büyük')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('bill','fatura')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('birds','kuşlar')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('black','siyah')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('bit','parça')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('block','engellemek')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('anything','hiçbir şey')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('appear','gözükmek')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('area','bölge')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('arms','kollar')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('army','ordu')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('around','etraf')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('art','sanat')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('as','iken')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('ask','sormak')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('away','uzak')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('back','arka')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('bad','kötü')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('ball','top')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('bank','banka')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('base','temel')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('be','olmak')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('bear','ayı')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('beat','yemek')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('beautiful','güzel')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('because','çünkü')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('become','olmak')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('below','altında')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('befor','önce')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('begin','başlamak')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('behind','arka')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('develope','geliştirmek')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('dictionary','sözlük')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('did','yaptı')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('difference','fark')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('different','farklı')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('difficult','zor')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('direction','yön')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('discover','keşfetmek')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('distance','mesafe')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('divided','bölünmüş')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('division','bölüm')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('dog','köpek')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('door','kapı')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('down','aşağı')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('draw','çizmek')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('dress','elbise')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('drop','düşmek')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam)values('dry','kuru')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('during','boyunca')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('early','erken')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('ear','kulak')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('earth','dünya')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('east','doğu')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('easy','kolay')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('eat','yemek')");
            db.execSQL("INSERT INTO dahilkelimeler (dahilkelime,dahilanlam) values('end','son')");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + EKLENEN_KELIMELER_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DAHIL_KELIMELER_TABLE_NAME);

            onCreate(db);

        }
    }

    private int kayitSil(Uri uri,String selection,String[] selectionArgs,String tabloAdi){

        int id = db.delete(tabloAdi,selection,selectionArgs);
        if(id==0)//herhangi bir kayıt guncellenmemişse
        {
            Log.e("HATA","BIR SEY SILINEMEMISTIR"+uri);
            return -1;
        }
        return id;
    }


}
