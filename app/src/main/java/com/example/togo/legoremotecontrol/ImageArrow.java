package com.example.togo.legoremotecontrol;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by ToGo on 16.02.2016.
 */
public class ImageArrow extends Image {
    boolean isTouched;
    private Bitmap bitmapOriginal, bitmapBig;

    public ImageArrow(ImageView imageView) {
        super(imageView);
        imageView.setVisibility(View.INVISIBLE); //default
        bitmapOriginal = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        bitmapBig = Bitmap.createScaledBitmap(bitmapOriginal, bitmapOriginal.getWidth() + bitmapOriginal.getWidth() / 4,
                bitmapOriginal.getHeight() + bitmapOriginal.getHeight() / 4, false);
    }

    public void show() {
        getImageView().setVisibility(View.VISIBLE);
    }

    public void hide() {
        getImageView().setVisibility(View.INVISIBLE);
    }


    public boolean isHit(int eX, int eY) {
        return (eX > getLeftX() && eX < getRightX() && eY > getTopY() && eY < getBottomY());
    }

    public void showOriginal() {
        getImageView().setImageBitmap(bitmapOriginal);
    }

    public void showBig() {
        getImageView().setImageBitmap(bitmapBig);
    }


    public void setIsTouched(boolean isTouched) {
        this.isTouched = isTouched;
    }

    public boolean isTouched() {
        return isTouched;
    }


}
