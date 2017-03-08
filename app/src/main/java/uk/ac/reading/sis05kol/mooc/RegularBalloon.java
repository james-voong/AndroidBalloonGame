package uk.ac.reading.sis05kol.mooc;

import android.graphics.BitmapFactory;

/**
 * Created by voongjame on 4/10/2016.
 */

public class RegularBalloon extends Balloon {

    public RegularBalloon(GameView gameView, int canvasHeight, int canvasWidth) {
        super(BitmapFactory.decodeResource(gameView.getContext().getResources(), R.drawable.blue_balloon),
                canvasHeight, canvasWidth);

    }

}