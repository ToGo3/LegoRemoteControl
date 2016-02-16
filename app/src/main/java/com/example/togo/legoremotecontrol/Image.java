package com.example.togo.legoremotecontrol;

import android.widget.ImageView;

/**
 * Created by ToGo on 16.02.2016.
 */
public class Image {
    private ImageView imageView;

    private int topY, bottomY, rightX, leftX;

    public Image(ImageView imageView) {
        this.imageView = imageView;
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

    public ImageView getImageView() {
        return imageView;
    }

    public void coordinates() {
        topY = getImageView().getTop();
        bottomY = getImageView().getBottom();
        rightX = getImageView().getRight();
        leftX = getImageView().getLeft();
    }


}
