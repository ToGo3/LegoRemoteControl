package com.example.togo.legoremotecontrol;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by ToGo on 16.02.2016.
 */
public class ImageArrow extends Image {
    boolean isTouched, isEnable;
    private Bitmap bitmapOriginal, bitmapBig;
    private String name;

    public ImageArrow(ImageView imageView, int id, Drawable drawable, boolean isEnable, String name) {
        super(imageView, id, drawable);
        imageView.setVisibility(View.INVISIBLE); //default
        setBitmap();
        setIsEnable(isEnable);
        setName(name);
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


    public boolean isEnable() {
        return isEnable;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBitmap() {
        bitmapOriginal = ((BitmapDrawable) getImageView().getDrawable()).getBitmap();
        bitmapBig = Bitmap.createScaledBitmap(bitmapOriginal, bitmapOriginal.getWidth() + bitmapOriginal.getWidth() / 4,
                bitmapOriginal.getHeight() + bitmapOriginal.getHeight() / 4, false);

    }


}
