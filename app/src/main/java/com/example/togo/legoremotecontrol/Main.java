package com.example.togo.legoremotecontrol;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
    // TODO программно отрисовать 3 блок (найти картинку-связку)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);


        wheel = new Image(new ImageView(this));
        wheel.getImageView().setImageDrawable(getResources().getDrawable(R.drawable.ic_wheel));
        RelativeLayout.LayoutParams wheelparams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        wheel.getImageView().setId(relativeLayout.getId() + 1);
        wheelparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        wheelparams.addRule(RelativeLayout.ALIGN_PARENT_START);
        wheelparams.setMargins(dpToPx(70), 0, 0, 0);
        relativeLayout.addView(wheel.getImageView(), wheelparams);


        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        lego = new Image(new ImageView(this));
        lego.getImageView().setImageDrawable(getResources().getDrawable(R.drawable.ic_lego));

        //params.addRule(RelativeLayout.ALIGN_PARENT_START);
        params.addRule(RelativeLayout.ABOVE, wheel.getImageView().getId());
        params.addRule(RelativeLayout.ALIGN_LEFT, wheel.getImageView().getId());
        params.setMargins(dpToPx(-12), 0, 0, 0);
        relativeLayout.addView(lego.getImageView(), params);
        //lego.getImageView().setId(relativeLayout.getId() + 1);

        //Log.d("A", "" + lego.getImageView().getId());



        /*wheel = new Image((ImageView) findViewById(R.id.wheel));
        up = new ImageArrow((ImageView) findViewById(R.id.move_up));
        across = new ImageArrow((ImageView) findViewById(R.id.move_across));
        back = new ImageArrow((ImageView) findViewById(R.id.back));
        front = new ImageArrow((ImageView) findViewById(R.id.front));*/

        /*
            <ImageView
                android:id="@+id/lego"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_above="@+id/wheel"
                android:layout_alignParentLeft="true"
                android:layout_alignParentStart="true"
                android:layout_marginLeft="70dp"
                android:src="@drawable/ic_lego" />

            <ImageView
                android:id="@+id/move_up"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_above="@id/lego"
                android:layout_alignStart="@id/lego"
                android:layout_marginBottom="12dp"
                android:layout_marginLeft="5dp"
                android:src="@drawable/move_up" />

            <ImageView
                android:id="@+id/move_across"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_above="@+id/lego"
                android:layout_alignEnd="@+id/lego"
                android:layout_marginBottom="12dp"
                android:layout_marginRight="-35dp"
                android:src="@drawable/move_across" />

            <ImageView
                android:id="@+id/wheel"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_alignLeft="@+id/lego"
                android:layout_alignParentBottom="true"
                android:layout_alignStart="@+id/lego"
                android:layout_marginLeft="12dp"
                android:src="@drawable/ic_wheel" />

            <ImageView
                android:id="@+id/back"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_alignTop="@id/wheel"
                android:layout_marginTop="15dp"
                android:layout_toLeftOf="@id/wheel"
                android:src="@drawable/move_back" />

            <ImageView
                android:id="@+id/front"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_alignTop="@id/wheel"
                android:layout_marginLeft="12dp"
                android:layout_marginTop="15dp"
                android:layout_toRightOf="@id/wheel"
                android:src="@drawable/move_front" />
                */






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

        //lego.setOnTouchListener(up,across);

        //wheel.setOnTouchListener(back,front);



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

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

}
