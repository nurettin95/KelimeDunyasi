package com.nurettingorsoy.kelimedunyasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.List;

public class MetinTara extends BosMenuAktivity{

    private Button goruntu_yakalama_button, metin_yakala_button, translateButton;
    private ImageView imageView;
    private TextView textView2, translatedTv;
    private EditText inputToTranslate;
    String originalText;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metin_tara);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        goruntu_yakalama_button = findViewById(R.id.goruntu_yakala_button);
        metin_yakala_button = findViewById(R.id.metin_yakala_button);
        imageView = findViewById(R.id.imageView);
        textView2 = findViewById(R.id.textView2);
        inputToTranslate = findViewById(R.id.inputToTranslate);
        translatedTv = findViewById(R.id.translatedTv);
        translateButton = findViewById(R.id.translateButton);

        goruntu_yakalama_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dispatchTakePictureIntent();
                textView2.setText("");

            }
        });
        metin_yakala_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detectTextFromImage();

            }
        });
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                originalText=inputToTranslate.getText().toString();
                translateTextOfLanguage(originalText);
            }
        });
    }

    private void dispatchTakePictureIntent() { //Kamerayı açıyor
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bundle bundle = data.getExtras();
            imageBitmap = (Bitmap) bundle.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }
    private void detectTextFromImage() { //resimden metin algılama methodu

        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = FirebaseVision.getInstance().getCloudTextRecognizer();


        Task<FirebaseVisionText> result = firebaseVisionTextRecognizer.processImage(firebaseVisionImage);
        result.addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                resimdekiMetniGoster(firebaseVisionText);
            }
        }).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MetinTara.this, "Hata: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                        Log.d("Hata: ", e.getMessage());
                    }
                });
    }
    private void resimdekiMetniGoster(FirebaseVisionText firebaseVisionText) {

        List<FirebaseVisionText.TextBlock> blockList = firebaseVisionText.getTextBlocks();
        if (blockList.size() == 0) {
            Toast.makeText(this, "Fotoğrafta metin bulunamadı.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {
                String text = block.getText();
                textView2.setText(text);
            }
        }
    }
    private void translateTextOfLanguage(final String sourceText) {
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.TURKISH)
                .build();
        final Translator englishTurkishTranslator = Translation.getClient(options);

        DownloadConditions conditions = new DownloadConditions.Builder().requireWifi()
                .build();
        //download model first
        englishTurkishTranslator.downloadModelIfNeeded().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void v) {
                englishTurkishTranslator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {

                        translatedTv.setText(s);
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MetinTara.this, "e 2 "+e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MetinTara.this, "e 1 "+e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    }
