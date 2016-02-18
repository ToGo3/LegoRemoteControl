package com.example.togo.legoremotecontrol;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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

    public void setOnTouchListener(final ImageArrow... args) {
        if (args.length != 0) {
            this.imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            for (ImageArrow imageArrow : args) {
                                if (imageArrow.isEnable) {
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
                                if (imageArrow.isEnable) {
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
                            for (ImageArrow imageArrow : args) {
                                if (imageArrow.isEnable) {
                                    if (imageArrow.isTouched())
                                        Log.d("A", imageArrow.toString());
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


}
