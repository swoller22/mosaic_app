package com.example.matthew.mosaic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class BaseImageActivity extends AppCompatActivity {

    private Button getImageBtn;
    private Button getLittleImagesBtn;
    private ImageView baseImage;
    private int IMAGE_SELECTOR = 0;
    private int LITTLE_IMAGES_SELECTOR = 1;
    private Bitmap baseImageBitmap;
    BitmapFactory.Options bitmapOptions;
    private int THUMBNAIL_SIZE = 70;
    ArrayList<RGB> littleImagesRGBArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_image);

        getImageBtn = (Button)findViewById(R.id.getImageBtn);
        getLittleImagesBtn = (Button)findViewById(R.id.getLittleImagesBtn);
        baseImage = (ImageView)findViewById(R.id.baseImage);
        littleImagesRGBArrayList = new ArrayList<RGB>();

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
        }
        else if(requestCode == LITTLE_IMAGES_SELECTOR) {
            int count = data.getClipData().getItemCount();
            for(int i = 0; i < count; i++) {
                Uri imageUri = data.getClipData().getItemAt(i).getUri();
                try {
                    //Bitmap miniBitmap =  MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    Bitmap miniBitmap = getMiniBitmap(imageUri);
                    BitmapExtension miniBitmapExtension = new BitmapExtension(miniBitmap);
                    RGB averageRGB = miniBitmapExtension.getAverageRGB();                   //Get the average RGB value for each selected little image
                    littleImagesRGBArrayList.add(averageRGB);                               //Add the most recent average RGB value to the ArrayList
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

    public Bitmap getMiniBitmap(Uri uri) throws FileNotFoundException, IOException{
        InputStream input = this.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither=true;//optional
        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true; //optional
        //bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//
        input = this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }

}

