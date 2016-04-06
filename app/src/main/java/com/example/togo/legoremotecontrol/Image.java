package com.example.togo.legoremotecontrol;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Calendar;

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
                                            case "across":
                                                Log.d("touchTime", " " + Calendar.getInstance().getTime());
                                                new useSmart().execute(new String[]{"robot", imageArrow.getName()});
                                                break;
                                            case "rise":
                                                new useSmart().execute(new String[]{blockName, imageArrow.getName()});
                                                //imageArrow.setName("shrink");
                                                //imageArrow.getImageView().setImageDrawable(Main.context.getResources().getDrawable(R.drawable.move_down));
                                                //imageArrow.setBitmap();
                                                //imageArrow.hide();
                                                break;
                                            case "shrink":
                                                new useSmart().execute(new String[]{blockName, imageArrow.getName()});
                                                imageArrow.setName("rise");
                                                imageArrow.getImageView().setImageDrawable(Main.context.getResources().getDrawable(R.drawable.move_up));
                                                imageArrow.setBitmap();
                                                imageArrow.hide();
                                                break;
                                            case "allBack":
                                                new useSmart().execute(new String[]{"robot", "back"});
                                                Main.imageView.setVisibility(View.VISIBLE);
                                                Main.imageView.setOnTouchListener(new View.OnTouchListener() {
                                                    @Override
                                                    public boolean onTouch(View v, MotionEvent event) {
                                                        new useSmart().execute(new String[]{"robot", "stop"});
                                                        Main.imageView.setVisibility(View.INVISIBLE);
                                                        imageArrow.hide();
                                                        imageArrow.showOriginal();
                                                        return false;
                                                    }
                                                });
                                                break;
                                            case "allFront":
                                                new useSmart().execute(new String[]{"robot", "forward"});
                                                Main.imageView.setVisibility(View.VISIBLE);
                                                Main.imageView.setOnTouchListener(new View.OnTouchListener() {
                                                    @Override
                                                    public boolean onTouch(View v, MotionEvent event) {
                                                        new useSmart().execute(new String[]{"robot", "stop"});
                                                        Main.imageView.setVisibility(View.INVISIBLE);
                                                        imageArrow.hide();
                                                        imageArrow.showOriginal();
                                                        return false;
                                                    }
                                                });
                                                break;
                                            default:
                                                new useSmart().execute(new String[]{blockName, imageArrow.getName()});
                                                Main.imageView.setVisibility(View.VISIBLE);
                                                Main.imageView.setOnTouchListener(new View.OnTouchListener() {
                                                    @Override
                                                    public boolean onTouch(View v, MotionEvent event) {
                                                        new useSmart().execute(new String[]{"robot", "stop"});
                                                        Main.imageView.setVisibility(View.INVISIBLE);
                                                        imageArrow.hide();
                                                        imageArrow.showOriginal();
                                                        return false;
                                                    }
                                                });
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
            SmartM3.insert(new SmartSpaceTriplet(params[0][0], "commandIs", params[0][1]));
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
