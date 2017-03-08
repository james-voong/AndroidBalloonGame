package uk.ac.reading.sis05kol.mooc;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final int MENU_RESUME = 1;
    private static final int MENU_START = 2;
    private static final int MENU_STOP = 3;

    private GameThread mGameThread;
    private GameView mGameView;
    private Bundle temporaryInstanceState;

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.splash_screen);
        splashScreenButtons(savedInstanceState);
        temporaryInstanceState = savedInstanceState;
    }

    private void splashScreenButtons(final Bundle savedInstanceState) {
        Button starter = (Button) findViewById(R.id.startButton);
        starter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
                gameLayout(savedInstanceState);
                temporaryInstanceState = savedInstanceState;
                Button endGameButton = (Button) findViewById(R.id.end);
                endGameButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        endGame(savedInstanceState);
                        temporaryInstanceState = savedInstanceState;
                    }

                });
            }
        });

        Button highScores = (Button) findViewById(R.id.highscore);
        highScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.high_scores);
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                int score = settings.getInt("bestScore", 0);
                setHighScores(score);
                Button back = (Button) findViewById(R.id.menu);

                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setContentView(R.layout.splash_screen);
                        splashScreenButtons(savedInstanceState);
                    }
                });
            }
        });
    }

    private void setHighScores(int score) {
        TextView score1 = (TextView) findViewById(R.id.score1);
        score1.setText(Integer.toString(score));
    }

    private void gameLayout(Bundle savedInstanceState) {
        mGameView = (GameView) findViewById(R.id.gamearea);
        mGameView.setStatusView((TextView) findViewById(R.id.text));
        mGameView.setScoreView((TextView) findViewById(R.id.score));

        this.startGame(mGameView, null, savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        endGame(temporaryInstanceState);
    }

    private void endGame(Bundle savedInstanceState) {
        saveHighScore();
        mGameView = null;
        mGameThread = null;
        setContentView(R.layout.splash_screen);
        splashScreenButtons(savedInstanceState);
    }

    private void saveHighScore() {
        //For saving the current score
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        if (settings.getInt("bestScore", 0) < (int) mGameThread.getScore()) {
            editor.putInt("bestScore", (int) mGameThread.getScore());
        }
        editor.commit();

    }


    private void startGame(GameView gView, GameThread gThread, Bundle savedInstanceState) {

        //Set up a new game, we don't care about previous states
        mGameThread = new TheGame(mGameView);
        mGameView.setThread(mGameThread);
        mGameThread.setState(GameThread.STATE_READY);
        mGameView.startSensor((SensorManager) getSystemService(Context.SENSOR_SERVICE));
    }

	/*
     * Activity state functions
	 */

    @Override
    protected void onPause() {
        super.onPause();

        if (mGameThread.getMode() == GameThread.STATE_RUNNING) {
            mGameThread.setState(GameThread.STATE_PAUSE);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mGameView.cleanup();
        mGameView.removeSensor((SensorManager) getSystemService(Context.SENSOR_SERVICE));
        mGameThread = null;
        mGameView = null;
    }
    
    /*
     * UI Functions
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(0, MENU_START, 0, R.string.menu_start);
        menu.add(0, MENU_STOP, 0, R.string.menu_stop);
        menu.add(0, MENU_RESUME, 0, R.string.menu_resume);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_START:
                mGameThread.doStart();
                return true;
            case MENU_STOP:
                mGameThread.setState(GameThread.STATE_LOSE, getText(R.string.message_stopped));
                return true;
            case MENU_RESUME:
                mGameThread.unpause();
                return true;
        }

        return false;
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // Do nothing if nothing is selected
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
