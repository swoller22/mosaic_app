package com.example.matthew.mosaic;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class BaseImageActivity extends AppCompatActivity {

    private Button getImageBtn;
    private Button getLittleImagesBtn;
    private ImageView baseImage;
    private int IMAGE_SELECTOR = 0;
    private int LITTLE_IMAGES_SELECTOR = 1;
    private static final String IMAGE_DIRECTORY = "/Download";
    private Bitmap baseImageBitmap;
    private static final int PICKFILE_REQUEST_CODE = 8778;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_image);

        getImageBtn = (Button)findViewById(R.id.getImageBtn);
        getLittleImagesBtn = (Button)findViewById(R.id.getLittleImagesBtn);
        baseImage = (ImageView)findViewById(R.id.baseImage);

        getImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        getLittleImagesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseLittleImages();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("TAG", "Made it back!");

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == IMAGE_SELECTOR) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    baseImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    baseImage.setImageBitmap(baseImageBitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(BaseImageActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
            beginImageProcessing();
        }
        else if(requestCode == LITTLE_IMAGES_SELECTOR) {
            int count = data.getClipData().getItemCount();
            for(int i = 0; i < count; i++) {
                Uri imageUri = data.getClipData().getItemAt(i).getUri();
                try {
                    Bitmap miniBitmap =  MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    baseImage.setImageBitmap(miniBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void beginImageProcessing() {

/*
        int littleImageSize = 10;
        int baseImageBitmapWidth = baseImageBitmap.getWidth();
        int baseImageBitmapHeight = baseImageBitmap.getHeight();
        int[] baseImagePixels = new int[baseImageBitmapWidth*baseImageBitmapHeight];

        baseImageBitmap.getPixels(baseImagePixels, 0, baseImageBitmapWidth, 0, 0,
                baseImageBitmapWidth, baseImageBitmapHeight);


        int R,G,B;

        for(int y = 0; y < baseImageBitmapHeight; y++) {
            for(int x = 0; x < baseImageBitmapWidth; x++) {
                int index = y * baseImageBitmapWidth + x;
                R = (baseImagePixels[index] >> 16) & 0xff;     //bitwise shifting
                G = (baseImagePixels[index] >> 8) & 0xff;
                B = baseImagePixels[index] & 0xff;
                baseImagePixels[index] = (R << 16) | (G << 8) | B;
            }
        }
        Log.d("first pixel value: ", String.valueOf(baseImagePixels[105800]));
        */
    }


    public void chooseImage(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Log.d("TAG", "Starting gallery activity!");
        startActivityForResult(galleryIntent, IMAGE_SELECTOR);
    }

    public void chooseLittleImages() {
        Intent folderSelectorIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        folderSelectorIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        Log.d("TAG", "Starting little images selector selector activity!");
        startActivityForResult(folderSelectorIntent, LITTLE_IMAGES_SELECTOR);
    }

    public void getAverageRGBValueOfImage(){

    }
}

