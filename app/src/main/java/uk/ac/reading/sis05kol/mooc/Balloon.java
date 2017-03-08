package uk.ac.reading.sis05kol.mooc;

import android.graphics.Bitmap;

import java.util.Random;

/**
 * Created by voongjame on 4/10/2016.
 */

public abstract class Balloon {

    int size;
    float xCoordinate;
    float yCoordinate;
    float dx;
    float dy;
    Bitmap image;

    public Balloon(Bitmap image, int canvasHeight, int canvasWidth) {
        this.size = (int) (Math.random() * 300 + 100);
        this.xCoordinate = ((float) Math.random()) * canvasWidth;
        this.yCoordinate = ((float) Math.random()) * canvasHeight;
        if (Math.random() >= 0.5) {
            this.dx = (float) (Math.random() * 200 + 100);
        } else {
            this.dx = -(float) (Math.random() * 200 + 100);
        }
        if (Math.random() >= 0.5) {
            this.dy = (float) (Math.random() * 200 + 100);
        } else {
            this.dy = -(float) (Math.random() * 200 + 100);
        }
        this.image = Bitmap.createScaledBitmap(image, this.size, this.size, true);
    }
}