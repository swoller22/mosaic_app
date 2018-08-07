package com.example.matthew.mosaic;

import android.graphics.Bitmap;

public class BitmapExtension {
    Bitmap imageBitmap;
    RGB rgb;

    public BitmapExtension(Bitmap bitmap) {
        this.imageBitmap = bitmap;
    }

    public RGB getAverageRGB() {
        // calculate average RGB values
        int averageR = 0;
        int averageG = 0;
        int averageB = 0;

        return new RGB(averageR, averageG, averageB);
    }

}

