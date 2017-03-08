package uk.ac.reading.sis05kol.mooc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by voongjame on 4/10/2016.
 */

public class ArmouredBalloon extends Balloon {

    private int armour;

    public ArmouredBalloon(GameView gameView, int canvasHeight, int canvasWidth) {
        super(BitmapFactory.decodeResource(gameView.getContext().getResources(), R.drawable.orange_balloon),
                canvasHeight, canvasWidth);
        armour = 2;
    }

    public void reduceArmour() {
        this.armour--;
        if (armour == 1) {
            size = size / 2;
            updateSize();
        }
        if (this.armour == 0) {
            size = 0;
        }
    }
    public void updateSize(){
        this.image = Bitmap.createScaledBitmap(image, this.size, this.size, true);
    }
}
