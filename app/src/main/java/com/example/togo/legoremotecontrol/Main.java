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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Vector;

public class Main extends AppCompatActivity {

    public static Handler handler;
    public static Context context;
    public static ImageView stop;
    public static ImageView record;
    public static boolean recordFlag;
    private static Vector<ImageRobot> robots;
    private static int relativeLayoutId;
    private Thread thread;

    //TODO speed and angle spinner


    public static int dpToPx(int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * displayMetrics.density);
    }

    private static void paintBlock(int i, boolean moveEngine, boolean liftEngine, boolean headBlock, boolean tailBlock) {

        robots.add(new ImageRobot(context, relativeLayoutId + 1 + i * 2, dpToPx(75 + (i * 150)), "block" + i));
        robots.lastElement().initWheelArrows(moveEngine);
        robots.lastElement().initBlockArrows(liftEngine, headBlock, tailBlock);
        if (!headBlock) {
            robots.lastElement().initConnectionLine();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        stop = (ImageView) findViewById(R.id.imageView);
        record=(ImageView)findViewById(R.id.startRecord);
        recordFlag=false;
        context = this;

        final Animation animationRotateClockwise = AnimationUtils.loadAnimation(this, R.anim.rotate_clockwise);
        final Animation animationRotateCounterClockwise = AnimationUtils.loadAnimation(this,R.anim.rotate_counter_clockwise);

        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        relativeLayoutId = relativeLayout.getId();
        robots = new Vector<ImageRobot>();

        new updateFromSmart().execute();

        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 0:                     //update
                        relativeLayout.removeAllViewsInLayout();
                        new updateFromSmart().execute();
                        break;
                    case 1:                     //stumbled on the obstacle
                        for (int i = 0; i < robots.size(); i++) {
                            if (robots.elementAt(i).getFront() != null) {
                                robots.elementAt(i).getFront().hide();
                                robots.elementAt(i).getFront().showOriginal();
                            }
                            if (robots.elementAt(i).getBack() != null) {
                                robots.elementAt(i).getBack().hide();
                                robots.elementAt(i).getBack().showOriginal();
                            }
                            if (robots.elementAt(i).getUp() != null) {
                                robots.elementAt(i).getUp().hide();
                                robots.elementAt(i).getUp().showOriginal();
                            }

                        }
                        robots.firstElement().getAllBack().hide();
                        robots.firstElement().getAllBack().showOriginal();
                        robots.lastElement().getAllFront().hide();
                        robots.lastElement().getAllFront().showOriginal();
                        stop.setVisibility(View.INVISIBLE);
                        break;
                    case 2:                     //correctly rised
                        robots.elementAt(robots.size() - msg.arg1 - 1).getUp().hide();
                        robots.elementAt(robots.size() - msg.arg1 - 1).getUp().showOriginal();
                        robots.elementAt(robots.size() - msg.arg1 - 1).getDown().setName("lower");
                        break;
                    case 3:
                        robots.elementAt(robots.size() - msg.arg1 - 1).getDown().hide();
                        robots.elementAt(robots.size() - msg.arg1 - 1).getDown().showOriginal();
                        robots.elementAt(robots.size() - msg.arg1 - 1).getDown().setName("shrink");
                        break;
                    case 4: //exploreObstacle
                        Log.d("Phone","I'll draw u an obstacle with loading");
                        break;
                    case 5: //obstacleInfo
                        Log.d("Phone","I'll draw u an obstacle with check mark");
                        break;
                    case 6: //rotate clockwise (move forward)
                        animationRotateClockwise.setRepeatCount(-1);
                        if (msg.obj!=null){
                            for (ImageRobot img:robots){
                                img.getWheel().getImageView().startAnimation(animationRotateClockwise);
                            }
                        }
                        else {
                            robots.elementAt(msg.arg1).getWheel().getImageView().startAnimation(animationRotateClockwise);
                        }
                        Main.stop.setVisibility(View.VISIBLE);
                        break;
                    case 7://rotate counterclockwise (move back)
                        animationRotateCounterClockwise.setRepeatCount(0);
                        if (msg.obj!=null){
                            for (ImageRobot img:robots){
                                img.getWheel().getImageView().startAnimation(animationRotateCounterClockwise);
                            }
                        }
                        else {
                            robots.elementAt(msg.arg1).getWheel().getImageView().startAnimation(animationRotateCounterClockwise);
                        }
                        Main.stop.setVisibility(View.VISIBLE);
                        break;
                    case 8: //TODO stop
                        for (ImageRobot img:robots){
                            if (img.isMoveEngine()) {
                                img.getBack().hide();
                                img.getBack().showOriginal();
                                img.getFront().hide();
                                img.getFront().showOriginal();
                                img.getWheel().getImageView().clearAnimation();
                                if (img.isHeadBlock()){
                                    img.getAllFront().hide();
                                    img.getAllFront().showOriginal();
                                }
                                if (img.isTailBlock()){
                                    img.getAllBack().hide();
                                    img.getAllBack().showOriginal();
                                }
                            }
                        }
                        /*animationRotateClockwise.setRepeatCount(0);
                        animationRotateCounterClockwise.setRepeatCount(0);*/
                        Main.stop.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        Log.d("Phone","WTF?!");
                        break;
                }

            }
        };

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SmartM3.subscribe();
                do {
                    if (Thread.interrupted()) {
                        //SmartM3.leave();
                    }

                } while (true);

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

        quitDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
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
            return SmartM3.update();//1
        }

        protected void onPostExecute(Integer status) {
            //status=21111;
            Main.record.setVisibility(View.INVISIBLE);
            robots.clear();
            if (status != 0) {
                Log.d("Status= ", "" + status);

                int count = Character.getNumericValue(String.valueOf(status).charAt(0)) - 1;
                for (int i = 0; i <= count; i++) {
                    boolean moveEngine, liftEngine, headBlock, tailBlock;
                    moveEngine = (status % 10 == 1);
                    status /= 10;
                    liftEngine = (status % 10 == 1);
                    status /= 10;
                    headBlock = (status == count + 1);
                    tailBlock = (i == 0);
                    paintBlock(i, moveEngine, liftEngine, headBlock, tailBlock);

                }
                Main.record.setVisibility(View.VISIBLE);

            }
            PD.hideDialog();

        }
    }


}
