package com.example.togo.legoremotecontrol;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import wrapper.SmartSpaceTriplet;

/**
 * Created by ToGo on 16.02.2016.
 */
public class Image {
    private ImageView imageView;

    private RelativeLayout.LayoutParams params;

    private int topY, bottomY, rightX, leftX;

    public Image(ImageView imageView, int id, Drawable drawable) {
        this.imageView = imageView;
        this.params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.imageView.setId(id);
        this.imageView.setImageDrawable(drawable);
    }

    public int getTopY() {
        return topY;
    }

    public int getRightX() {
        return rightX;
    }

    public int getLeftX() {
        return leftX;
    }

    public int getBottomY() {
        return bottomY;
    }

    public int getId() {
        return imageView.getId();
    }

    public ImageView getImageView() {
        return imageView;
    }

    public RelativeLayout.LayoutParams getParams() {
        return params;
    }



    public void coordinates() {
        topY = getImageView().getTop();
        bottomY = getImageView().getBottom();
        rightX = getImageView().getRight();
        leftX = getImageView().getLeft();
    }

    public void setOnTouchListener(final String blockName, final ImageArrow... args) {
        Main.record.setOnTouchListener(new View.OnTouchListener() {//todo check counts of incoming
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (Main.recordFlag){
                    new useSmart().execute(new String[]{"stopRecord", "rec"});
                    Main.record.setImageDrawable(Main.context.getResources().getDrawable(R.drawable.ic_start_record));
                    Main.recordFlag=false;
                }
                else
                {
                    new useSmart().execute(new String[]{"startRecord", "rec"});
                    Main.record.setImageDrawable(Main.context.getResources().getDrawable(R.drawable.ic_stop_record));
                    Main.recordFlag=true;
                }
                return false;
            }
        });
        Main.stop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                new useSmart().execute(new String[]{"robot1", "stop"});
                Main.handler.sendMessage(Main.handler.obtainMessage(8,-1));
                return false;
            }
        });
        if (args.length != 0) {
            this.imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            for (ImageArrow imageArrow : args) {
                                if (imageArrow != null) {
                                    imageArrow.show();
                                    imageArrow.setIsTouched(false);
                                    imageArrow.coordinates();
                                }

                            }
                            coordinates();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            int eX = (int) event.getX() + getLeftX();
                            int eY = (int) event.getY() + getTopY();

                            for (ImageArrow imageArrow : args) {
                                if (imageArrow != null) {
                                    if (imageArrow.isHit(eX, eY)) {
                                        imageArrow.showBig();
                                        imageArrow.setIsTouched(true);
                                    } else {
                                        imageArrow.showOriginal();
                                        imageArrow.setIsTouched(false);
                                    }
                                }
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            for (final ImageArrow imageArrow : args) {
                                if (imageArrow != null) {
                                    if (imageArrow.isTouched()) {
                                        switch (imageArrow.getName()) {
                                            case "acrossObstacle":
                                                Log.d("touchTime", " ");
                                                new useSmart().execute(new String[]{"robot1", imageArrow.getName()});
                                                break;
                                            case "rise":
                                                new useSmart().execute(new String[]{"robot1|"+blockName, imageArrow.getName()});
                                                break;
                                            case "shrink":
                                                new useSmart().execute(new String[]{"robot1|"+blockName, imageArrow.getName()});
                                                break;
                                            case "lower":
                                                new useSmart().execute(new String[]{"robot1|"+blockName, imageArrow.getName()});
                                                break;
                                            case "moveBack":
                                                new useSmart().execute(new String[]{"robot1", "moveBack"});
                                                Main.handler.sendMessage(Main.handler.obtainMessage(7,-1));
                                                /*Main.stop.setOnTouchListener(new View.OnTouchListener() {
                                                    @Override
                                                    public boolean onTouch(View v, MotionEvent event) {
                                                        new useSmart().execute(new String[]{"robot1", "stop"});
                                                        Main.handler.sendMessage(Main.handler.obtainMessage(8,-1));
                                                        imageArrow.hide();
                                                        imageArrow.showOriginal();
                                                        return false;
                                                    }
                                                });*/
                                                break;
                                            case "moveForward":
                                                new useSmart().execute(new String[]{"robot1", "moveForward"});
                                                Main.handler.sendMessage(Main.handler.obtainMessage(6,-1));
                                                /*Main.stop.setOnTouchListener(new View.OnTouchListener() {
                                                    @Override
                                                    public boolean onTouch(View v, MotionEvent event) {
                                                        new useSmart().execute(new String[]{"robot1", "stop"});
                                                        Main.handler.sendMessage(Main.handler.obtainMessage(8,-1));
                                                        imageArrow.hide();
                                                        imageArrow.showOriginal();
                                                        return false;
                                                    }
                                                });*/
                                                break;
                                            case "forward":
                                                new useSmart().execute(new String[]{"robot1|"+blockName, "moveForward"});
                                                Main.handler.sendMessage(Main.handler.obtainMessage(6, Integer.parseInt(blockName.substring(5)),0)); //magic with arg1
                                                break;
                                            case "back":
                                                new useSmart().execute(new String[]{"robot1|"+blockName, "moveBack"});
                                                Main.handler.sendMessage(Main.handler.obtainMessage(7, Integer.parseInt(blockName.substring(5)),0));
                                                break;
                                            default:
                                                new useSmart().execute(new String[]{"robot1|"+blockName, "move"+imageArrow.getName()});
                                                /*Main.stop.setVisibility(View.VISIBLE);
                                                Main.stop.setOnTouchListener(new View.OnTouchListener() {
                                                    @Override
                                                    public boolean onTouch(View v, MotionEvent event) {
                                                        new useSmart().execute(new String[]{"robot1", "stop"});
                                                        Main.handler.sendMessage(Main.handler.obtainMessage(8, Integer.parseInt(blockName.substring(5)),0));
                                                        imageArrow.hide();
                                                        imageArrow.showOriginal();
                                                        return false;
                                                    }
                                                });*/
                                                break;

                                        }
                                    }
                                    else
                                        imageArrow.hide();
                                }
                            }
                            break;
                        default:
                            break;

                    }
                    return true;
                }
            });

        }

    }

    class useSmart extends AsyncTask<String[], Void, Boolean> {

        @Override
        protected Boolean doInBackground(String[]... params) {
            SmartM3.insert(new SmartSpaceTriplet(params[0][0], "task", params[0][1]));
            Log.d("Command",params[0][0]+ " task "+ params[0][1]);
            return true;
        }

        protected void onPreExecute() {
            //PD.showDialog(MainActivity.this, "Connecting...");
        }

        @Override
        protected void onPostExecute(Boolean result) {

        }

    }


}
