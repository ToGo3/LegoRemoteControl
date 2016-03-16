package com.example.togo.legoremotecontrol;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Vector;

public class Main extends AppCompatActivity {

    public static Handler handler;
    public static Context context;
    public static ImageView imageView;
    private static Vector<ImageRobot> robots;
    private static int relativeLayoutId;
    private Thread thread;

    //TODO speed and angle spinner
    //TODO add fullscreen setIp activity


    public static int dpToPx(int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private static void paintBlock(int i, boolean back, boolean front, boolean up, boolean across, boolean allBack, boolean allFront) {

        robots.add(new ImageRobot(context, relativeLayoutId + 1 + i * 2, dpToPx(75 + (i * 150)), "block" + i));
        robots.lastElement().initWheelArrows(front, back);
        robots.lastElement().initBlockArrows(up, across, allBack, allFront);
        if (!across) {
            robots.lastElement().initConnection();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        context = this;

        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        relativeLayoutId = relativeLayout.getId();
        robots = new Vector<ImageRobot>();

        new updateFromSmart().execute();

        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                relativeLayout.removeAllViewsInLayout();
                new updateFromSmart().execute();
            }
        };

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SmartM3.subscribe();
                do {
                    if (Thread.interrupted()) {
                        SmartM3.leave();
                    }

                }
                while (true);

            }
        });
        thread.start();

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

                thread.interrupt();
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
            PD.showDialog(Main.context, "Updating...");
        }


        @Override
        protected Integer doInBackground(Void... params) {
            return SmartM3.update();
        }

        protected void onPostExecute(Integer status) {
            if (status != 0) {
                Log.d("Status= ", "" + status);
                robots.clear();

                int count = Character.getNumericValue(String.valueOf(status).charAt(0)) - 1;
                for (int i = 0; i <= count; i++) {
                    boolean back = false, front = false, up = false, across = false, allBack = false, allFront = false;
                    if (status % 10 == 1) {
                        back = true;
                        front = true;
                    }
                    status /= 10;
                    if (status % 10 == 1) {
                        up = true;
                    }
                    status /= 10;
                    if (status == count + 1) {
                        across = true;
                        allFront = true;
                    }
                    allBack = (i == 0);
                    paintBlock(i, back, front, up, across, allBack, allFront);

                }

            }
            PD.hideDialog();

        }
    }


}
