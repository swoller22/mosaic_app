package com.example.matthew.mosaic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class BaseImageActivity extends AppCompatActivity {

    private Button getImageBtn;
    private Button getLittleImagesBtn;
    private ImageView baseImage;
    private int IMAGE_SELECTOR = 0;
    private int LITTLE_IMAGES_SELECTOR = 1;
    private Bitmap baseImageBitmap;

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
                    BitmapExtension bitmapExtension = new BitmapExtension(baseImageBitmap);
                    RGB averageRGB = bitmapExtension.getAverageRGB();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(BaseImageActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else if(requestCode == LITTLE_IMAGES_SELECTOR) {
            int count = data.getClipData().getItemCount();
            for(int i = 0; i < count; i++) {
                Uri imageUri = data.getClipData().getItemAt(i).getUri();
                try {
                    Bitmap miniBitmap =  MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    Log.d("RGBVal", "onActivityResult: "+miniBitmap.getPixel(300,300));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void chooseImage(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Log.d("TAG", "Starting gallery activity!");
        startActivityForResult(galleryIntent, IMAGE_SELECTOR);
    }

    public void chooseLittleImages() {
        Intent littleImageSelectorIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        littleImageSelectorIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        Log.d("TAG", "Starting little images selector selector activity!");
        startActivityForResult(littleImageSelectorIntent, LITTLE_IMAGES_SELECTOR);
    }

    public int[] getAverageRGBValueOfImage(Bitmap imageBitmap){
        int baseImageBitmapWidth = imageBitmap.getWidth();
        int baseImageBitmapHeight = imageBitmap.getHeight();
        int[] baseImagePixels = new int[baseImageBitmapWidth*baseImageBitmapHeight];
        int R = 0,G = 0,B = 0;
        int R_average = 0, G_average = 0, B_average = 0;
        int cnt = 0;

        for(int y = 0; y < baseImageBitmapHeight; y++) {
            for(int x = 0; x < baseImageBitmapWidth; x++) {
                int index = y * baseImageBitmapWidth + x;
                R = (baseImagePixels[index] >> 16) & 0xff;     //bitwise shifting
                G = (baseImagePixels[index] >> 8) & 0xff;
                B = baseImagePixels[index] & 0xff;

                Log.d("Average R, G, B: ", ""+R);
                R_average = R_average + R;
                G_average += G;
                B_average += B;
                cnt++;
            }
        }
        return new int[] {R_average, G_average, B_average};
    }
}

