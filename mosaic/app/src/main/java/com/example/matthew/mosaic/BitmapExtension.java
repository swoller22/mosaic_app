package com.example.matthew.mosaic;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class BitmapExtension {
    Bitmap imageBitmap;
    RGB rgb;

    public BitmapExtension(Bitmap bitmap) {
        this.imageBitmap = bitmap;
    }

    public RGB getAverageRGB() {
        // calculate average RGB values

        int baseImageBitmapWidth = imageBitmap.getWidth();
        int baseImageBitmapHeight = imageBitmap.getHeight();
        int[] baseImagePixels = new int[baseImageBitmapWidth*baseImageBitmapHeight];
        imageBitmap.getPixels(baseImagePixels, 0, baseImageBitmapWidth, 0, 0,
                baseImageBitmapWidth, baseImageBitmapHeight);

        int R = 0,G = 0,B = 0;
        float averageR = 0, averageG = 0, averageB = 0;
        int cnt = 0;

        for(int y = 0; y < baseImageBitmapHeight; y++) {
            for(int x = 0; x < baseImageBitmapWidth; x++) {
                int index = y * baseImageBitmapWidth + x;

                R = Color.red(baseImagePixels[index]);
                G = Color.green(baseImagePixels[index]);
                B = Color.blue(baseImagePixels[index]);

                averageR += R;
                averageG += G;
                averageB += B;
                cnt++;
            }
        }

        return new RGB(averageR/cnt, averageG/cnt, averageB/cnt);

    }

}

