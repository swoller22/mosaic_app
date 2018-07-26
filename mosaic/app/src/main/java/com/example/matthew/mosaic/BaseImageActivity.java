package com.example.matthew.mosaic;

import android.content.Intent;
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
    private ImageView baseImage;
    private int GALLERY = 1;
    private static final String IMAGE_DIRECTORY = "/Download";
    private Bitmap baseImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_image);

        getImageBtn = (Button)findViewById(R.id.getImageBtn);
        baseImage = (ImageView)findViewById(R.id.baseImage);

        getImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
    }

    public void chooseImage(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Log.d("TAG", "Starting gallery activity!");
        startActivityForResult(galleryIntent, GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("TAG", "Made it back!");

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    baseImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    Toast.makeText(BaseImageActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    baseImage.setImageBitmap(baseImageBitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(BaseImageActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        beginImageProcessing();
    }

    public void beginImageProcessing() {

        int baseImageBitmapWidth = baseImageBitmap.getWidth();
        int baseImageBitmapHeight = baseImageBitmap.getHeight();
        int[] baseImagePixels = new int[baseImageBitmapWidth*baseImageBitmapHeight];

        baseImageBitmap.getPixels(baseImagePixels, 0, baseImageBitmapWidth, 0, 0,
                baseImageBitmapWidth, baseImageBitmapHeight);


        int R,G,B,Y;

        for(int y = 0; y < baseImageBitmapHeight; y++) {
            for(int x = 0; x < baseImageBitmapWidth; x++) {
                int index = y * baseImageBitmapWidth + x;
                R = (baseImagePixels[index] >> 16) & 0xff;     //bitwise shifting
                G = (baseImagePixels[index] >> 8) & 0xff;
                B = baseImagePixels[index] & 0xff;
                baseImagePixels[index] = 0x00000000 | (R << 16) | (G << 8) | B;
            }
        }
        Log.d("first pixel value: ", String.valueOf(baseImagePixels[105800]));
    }
}
