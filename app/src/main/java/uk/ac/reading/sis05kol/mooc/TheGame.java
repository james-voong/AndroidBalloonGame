package uk.ac.reading.sis05kol.mooc;

//Other parts of the android libraries that we use

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class TheGame extends GameThread {

    private int balloonsInARow;
    GameView gameView;
    long score = 0;

    //ArrayList to save the balloons in
    private ArrayList<Balloon> balloons = new ArrayList<Balloon>();

    //This is run before anything else, so we can prepare things here
    public TheGame(GameView gameView) {
        //House keeping
        super(gameView);
        this.gameView = gameView;

    }

    //This is run before a new game (also after an old game)
    @Override
    public void setupBeginning() {
        createBalloons(20);
    }

    protected void createBalloons(int number) {
        //creating x number of random balloons and adding them to arrayList
        Balloon b;
        for (int i = 0; i < number; i++) {
            int randomiser = (int) (Math.random() * 300);

            if (randomiser <= 200) {
                b = new RegularBalloon(gameView, mCanvasHeight, mCanvasWidth);
                balloons.add(b);

            } else if (randomiser > 200 && randomiser <= 225) {
                b = new ExplodingBalloon(gameView, mCanvasHeight, mCanvasWidth);
                balloons.add(b);

            } else if (randomiser > 225) {
                b = new ArmouredBalloon(gameView, mCanvasHeight, mCanvasWidth);
                balloons.add(b);
            }
        }
    }

    @Override
    protected void doDraw(Canvas canvas) {
        //If there isn't a canvas to draw on do nothing
        //It is ok not understanding what is happening here
        if (canvas == null) return;

        super.doDraw(canvas);

        for (int i = 0; i < balloons.size(); i++) {

            //draw the image
            canvas.drawBitmap(balloons.get(i).image, balloons.get(i).xCoordinate - (float) (balloons.get(i).size / 2),
                    balloons.get(i).yCoordinate - (float) (balloons.get(i).size / 2), null);
        }
    }

    //This is run whenever the phone is touched by the user
    @Override
    protected void actionOnTouch(float x, float y) {

        for (int i = balloons.size() - 1; i >= 0; i--) {
            //These 4 statements check if the touch is within the area of a balloon
            if (x <= balloons.get(i).xCoordinate + balloons.get(i).size / 2
                    && x >= balloons.get(i).xCoordinate - balloons.get(i).size / 2
                    && y <= balloons.get(i).yCoordinate + balloons.get(i).size / 2
                    && y >= balloons.get(i).yCoordinate - balloons.get(i).size / 2) {

                //If the balloon is an exploding balloon, blow up something
                if (balloons.get(i).getClass() == ExplodingBalloon.class) {
                    balloons.remove(i);
                    score++;
                    if (balloons.size() >= 1) {
                        balloons.remove((int) (Math.random() * (balloons.size() - 1)));
                        score++;
                        balloonsInARow=0;
                    }
                    return;
                }
                //Pops the balloon and updates score for ArmouredBalloon
                if (balloons.get(i).getClass() == ArmouredBalloon.class) {
                    ArmouredBalloon a = (ArmouredBalloon) balloons.get(i);
                    a.reduceArmour();
                    balloonsInARow=0;
                    if (a.size == 0) {
                        balloons.remove(i);
                        score = score + 3;
                        return;
                    } else return;
                }
                //Pops the balloon and updates score for RegularBalloon
                if (balloons.get(i).getClass() == RegularBalloon.class) {
                    balloons.remove(i);
                    score++;
                    balloonsInARow++;
                    if(balloonsInARow == 10){
                        score++;
                    }
                    return;
                } else return;
            }
        }
    }

    //This is run just before the game "scenario" is printed on the screen
    @Override
    protected void updateGame(float secondsElapsed) {
        setScore(score);
        if (balloons.size() < 10) {
            createBalloons((int) (Math.random() * 20 + 10));
        }

        //These statements prevent the balloons from going off the screen
        for (int i = 0; i < balloons.size(); i++) {
            if (balloons.get(i).xCoordinate + balloons.get(i).size / 2 >= mCanvasWidth) {
                balloons.get(i).dx = -(Math.abs(balloons.get(i).dx));
            }
            if (balloons.get(i).xCoordinate - balloons.get(i).size / 2 <= 0) {
                balloons.get(i).dx = Math.abs(balloons.get(i).dx);
            }
            if (balloons.get(i).yCoordinate + balloons.get(i).size / 2 >= mCanvasHeight) {
                balloons.get(i).dy = -(Math.abs(balloons.get(i).dy));
            }
            if (balloons.get(i).yCoordinate - balloons.get(i).size / 2 <= 0) {
                balloons.get(i).dy = Math.abs(balloons.get(i).dy);
            }

            //Move the balloon's X and Y using the speed (pixel/sec)
            balloons.get(i).xCoordinate = balloons.get(i).xCoordinate + secondsElapsed * balloons.get(i).dx;
            balloons.get(i).yCoordinate = balloons.get(i).yCoordinate + secondsElapsed * balloons.get(i).dy;
        }

    }

}

// This file is part of the course "Begin Programming: Build your first mobile game" from futurelearn.com
// Copyright: University of Reading and Karsten Lundqvist
// It is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// It is is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// 
// You should have received a copy of the GNU General Public License
// along with it.  If not, see <http://www.gnu.org/licenses/>.
