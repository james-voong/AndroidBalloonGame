package uk.ac.reading.sis05kol.mooc;

import android.graphics.BitmapFactory;

/**
 * Created by voongjame on 4/10/2016.
 */

public class ExplodingBalloon extends Balloon {

    public ExplodingBalloon(GameView gameView, int canvasHeight, int canvasWidth) {
        super(BitmapFactory.decodeResource(gameView.getContext().getResources(), R.drawable.red_balloon),
                canvasHeight, canvasWidth);
    }

}
