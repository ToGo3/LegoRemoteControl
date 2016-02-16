package com.example.togo.legoremotecontrol;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Main extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    Image lego, wheel;
    ImageArrow across, up, back, front;
    int eX, eY;
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;

    //TODO почистить код от станадртных вложений
    //TODO стандартизировать ontouchlister для всех классов IMAGE
    // TODO программно отрисовать 3 блок (найти картинку-связку)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);


        lego = new Image((ImageView) findViewById(R.id.lego));
        wheel = new Image((ImageView) findViewById(R.id.wheel));
        up = new ImageArrow((ImageView) findViewById(R.id.move_up));
        across = new ImageArrow((ImageView) findViewById(R.id.move_across));
        back = new ImageArrow((ImageView) findViewById(R.id.back));
        front = new ImageArrow((ImageView) findViewById(R.id.front));


        final RelativeLayout relativeLayout = (RelativeLayout) up.getImageView().getParent();
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) up.getImageView().getLayoutParams();
        final RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) across.getImageView().getLayoutParams();


        Log.d("A", params.toString());



        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);


        lego.getImageView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        up.show();
                        across.show();

                        up.setIsTouched(false);
                        across.setIsTouched(false);

                        up.coordinates();
                        lego.coordinates();
                        across.coordinates();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        eX = (int) event.getX() + lego.getLeftX();
                        eY = (int) event.getY() + lego.getTopY();

                        if (up.isHit(eX, eY)) {

                            up.showBig();
                            params.setMargins(0, 0, 0, 12);
                            relativeLayout.updateViewLayout(up.getImageView(), params);
                            up.setIsTouched(true);
                        } else {
                            up.showOriginal();
                            params.setMargins(5, 0, 0, 12);
                            relativeLayout.updateViewLayout(up.getImageView(), params);
                            up.setIsTouched(false);

                            if (across.isHit(eX, eY)) {
                                across.showBig();
                                params1.setMargins(0, 0, -78, 12);
                                relativeLayout.updateViewLayout(across.getImageView(), params1);
                                across.setIsTouched(true);
                            } else {
                                across.showOriginal();
                                params1.setMargins(0, 0, -52, 12);
                                relativeLayout.updateViewLayout(across.getImageView(), params1);
                                across.setIsTouched(false);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (up.isTouched()) Log.d("A", "Move up");
                        else up.hide();
                        if (across.isTouched()) Log.d("A", "Move across");
                        else across.hide();
                        break;
                    default:
                        break;

                }
                return true;
            }
        });

        wheel.getImageView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        back.show();
                        front.show();

                        back.setIsTouched(false);
                        front.setIsTouched(false);

                        back.coordinates();
                        wheel.coordinates();
                        front.coordinates();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        eX = (int) event.getX() + wheel.getLeftX();
                        eY = (int) event.getY() + wheel.getTopY();

                        if (back.isHit(eX, eY)) {
                            back.showBig();
                            back.setIsTouched(true);
                        } else {
                            back.showOriginal();
                            back.setIsTouched(false);

                            if (front.isHit(eX, eY)) {
                                front.showBig();
                                front.setIsTouched(true);
                            } else {
                                front.showOriginal();
                                front.setIsTouched(false);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (back.isTouched()) Log.d("A", "Move back");
                        else back.hide();
                        if (front.isTouched()) Log.d("A", "Move front");
                        else front.hide();
                        //Log.d("A","Bingo");
                        break;
                    default:
                        break;

                }
                return true;
            }
        });


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //mControlsView.setVisibility(View.GONE);
        //mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        //mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

}
