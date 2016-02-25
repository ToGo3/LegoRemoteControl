package com.example.togo.legoremotecontrol;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.Vector;

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
    private static Vector<ImageRobot> robots;
    private static Context context;
    private static int relativeLayoutId;
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
    //TODO почистить код от станадртных вложений
    // TODO программно отрисовать 3 блок (найти картинку-связку)
    //TODO слушать инфу из смарта
    Image lego, wheel;
    ImageArrow across, up, back, front;
    int eX, eY;
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
    private boolean mVisible;

    private static void paintBlock(int i, boolean back, boolean front, boolean up, boolean across) {

        robots.add(new ImageRobot(context, relativeLayoutId + 1 + i * 2, dpToPx(70 + (i * 100)), "block" + i));
        //relativeLayout.addView(robots.get(i).getWheel().getImageView(), robots.get(i).getWheel().getParams());
        //relativeLayout.addView(robots.get(i).getBlock().getImageView(), robots.get(i).getBlock().getParams());
        //robots.lastElement().setName("forwardBlock");
        robots.lastElement().initWheelArrows(front, back);
        robots.lastElement().initBlockArrows(up, across);

    }

    public static int dpToPx(int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        relativeLayoutId = relativeLayout.getId();
        robots = new Vector<ImageRobot>();

        new updateFromSmart().execute();
        //TODO SmartM3.subscribe();
        //TODO SmartM3.leave();

        /*for (int i = 0; i < 3; i++) {
            robots.add(new ImageRobot(this, relativeLayout.getId() + 1 + i * 2, dpToPx(70 + (i * 100)), "block" + i));
            //relativeLayout.addView(robots.get(i).getWheel().getImageView(), robots.get(i).getWheel().getParams());
            //relativeLayout.addView(robots.get(i).getBlock().getImageView(), robots.get(i).getBlock().getParams());
        }
        robots.lastElement().setName("forwardBlock");
        robots.lastElement().initWheelArrows(true, false);

        robots.lastElement().initBlockArrows(true, false);*/
        //relativeLayout.addView(robots.lastElement().getFront().getImageView(),robots.lastElement().getFront().getParams());

        /*ImageRobot imageRobot1=new ImageRobot(this,relativeLayout.getId()+1,dpToPx(350));
        ImageRobot imageRobot2=new ImageRobot(this,relativeLayout.getId()+3,dpToPx(70));

        relativeLayout.addView(imageRobot1.getWheel().getImageView(),imageRobot1.getWheel().getParams());
        relativeLayout.addView(imageRobot1.getBlock().getImageView(),imageRobot1.getBlock().getParams());

        relativeLayout.addView(imageRobot2.getWheel().getImageView(),imageRobot2.getWheel().getParams());
        relativeLayout.addView(imageRobot2.getBlock().getImageView(), imageRobot2.getBlock().getParams());

        imageRobot1.initWheelArrows(this, true, false);
        relativeLayout.addView(imageRobot1.getFront().getImageView(),imageRobot1.getFront().getParams());

        imageRobot2.initWheelArrows(this,false,true);
        relativeLayout.addView(imageRobot2.getBack().getImageView(),imageRobot2.getBack().getParams());*/


        /*wheel = new Image(new ImageView(this),relativeLayout.getId() + 1,getResources().getDrawable(R.drawable.ic_wheel));
        wheel.getParams().addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        wheel.getParams().addRule(RelativeLayout.ALIGN_PARENT_START);
        wheel.getParams().setMargins(dpToPx(70), 0, 0, 0);
        relativeLayout.addView(wheel.getImageView(), wheel.getParams());


        lego = new Image(new ImageView(this),wheel.getId()+1,getResources().getDrawable(R.drawable.ic_lego));
        lego.getParams().addRule(RelativeLayout.ABOVE, wheel.getImageView().getId());
        lego.getParams().addRule(RelativeLayout.ALIGN_LEFT, wheel.getImageView().getId());
        lego.getParams().setMargins(dpToPx(-10), 0, 0, 0);
        relativeLayout.addView(lego.getImageView(), lego.getParams());

        up=new ImageArrow(new ImageView(this),lego.getId()+1,getResources().getDrawable(R.drawable.move_up));
        up.getParams().addRule(RelativeLayout.ABOVE, lego.getId());
        up.getParams().addRule(RelativeLayout.ALIGN_START, lego.getId());
        up.getParams().setMargins(dpToPx(5), 0, 0, dpToPx(12));
        relativeLayout.addView(up.getImageView(), up.getParams());

        across=new ImageArrow(new ImageView(this),up.getId()+1,getResources().getDrawable(R.drawable.move_across));
        across.getParams().addRule(RelativeLayout.ABOVE,lego.getId());
        across.getParams().addRule(RelativeLayout.ALIGN_END, lego.getId());
        across.getParams().setMargins(0, 0, dpToPx(-35), dpToPx(12));
        relativeLayout.addView(across.getImageView(), across.getParams());

        back=new ImageArrow(new ImageView(this),across.getId()+1,getResources().getDrawable(R.drawable.move_back));
        back.getParams().addRule(RelativeLayout.ALIGN_TOP,wheel.getId());
        back.getParams().addRule(RelativeLayout.LEFT_OF, wheel.getId());
        back.getParams().setMargins(0,dpToPx(15),dpToPx(12),0);
        relativeLayout.addView(back.getImageView(),back.getParams());

        front=new ImageArrow(new ImageView(this),back.getId()+1,getResources().getDrawable(R.drawable.move_front));
        front.getParams().addRule(RelativeLayout.ALIGN_TOP,wheel.getId());
        front.getParams().addRule(RelativeLayout.RIGHT_OF, wheel.getId());
        front.getParams().setMargins(dpToPx(12),dpToPx(15),0,0);
        relativeLayout.addView(front.getImageView(),front.getParams());*/


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

//        lego.setOnTouchListener(up,across);

        // wheel.setOnTouchListener(back,front);



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

    @Override
    public void onBackPressed() {
        openQuitDialog();
    }

    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                context);
        quitDialog.setTitle("Выход: Вы уверены?");

        quitDialog.setPositiveButton("Да!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SmartM3.leave();
                finish();
            }
        });

        quitDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        quitDialog.show();
    }

    public static class updateFromSmart extends AsyncTask<Void, Void, Integer> {

        protected void onPreExecute() {
            PD.showDialog(Main.context, "Inserting...");
        }


        @Override
        protected Integer doInBackground(Void... params) {
            return SmartM3.update();
        }

        protected void onPostExecute(Integer status) {
            if (status != 0) {
                robots.clear();

                int count = Character.getNumericValue(String.valueOf(status).charAt(0));
                for (int i = count; i >= 1; i++) {
                    boolean back = false, front = false, up = false, across = false;
                    if (status % 10 == 1) {
                        back = true;
                        front = true;
                    }
                    status %= 10;
                    if (status % 10 == 1) {
                        up = true;
                    }
                    status %= 10;
                    if (status == count)
                        across = true;
                    paintBlock(i - 1, back, front, up, across);

                }

            }
            PD.hideDialog();

        }
    }


}
