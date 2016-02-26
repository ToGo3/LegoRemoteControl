package com.example.togo.legoremotecontrol;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by ToGo on 18.02.2016.
 */
public class ImageRobot {

    private Image block, wheel;
    private ImageArrow back, front, up, across;
    private RelativeLayout relativeLayout;
    private String name;
    private Context context;

    public ImageRobot(Context context, int wheelId, int wheelLeftMargin, String name) {

        setName(name);
        this.context = context;
        relativeLayout = (RelativeLayout) ((Activity) context).getWindow().getDecorView().findViewById(R.id.relativeLayout);
        wheel = new Image(new ImageView(context), wheelId, context.getResources().getDrawable(R.drawable.ic_wheel));
        wheel.getParams().addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        wheel.getParams().addRule(RelativeLayout.ALIGN_PARENT_START);
        wheel.getParams().setMargins(wheelLeftMargin, 0, 0, 0);
        relativeLayout.addView(wheel.getImageView(), wheel.getParams());


        block = new Image(new ImageView(context), wheel.getId() + 1, context.getResources().getDrawable(R.drawable.ic_lego));
        block.getParams().addRule(RelativeLayout.ABOVE, wheel.getImageView().getId());
        block.getParams().addRule(RelativeLayout.ALIGN_LEFT, wheel.getImageView().getId());
        block.getParams().setMargins(dpToPx(-10), 0, 0, 0);
        relativeLayout.addView(block.getImageView(), block.getParams());

    }

    public Image getBlock() {
        return block;
    }

    public Image getWheel() {
        return wheel;
    }

    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public void initWheelArrows(boolean isFrontEnable, boolean isBackEnable) {

        front = new ImageArrow(new ImageView(context), -1, context.getResources().getDrawable(R.drawable.move_front), isFrontEnable, "forward");
        front.getParams().addRule(RelativeLayout.ALIGN_TOP, this.wheel.getId());
        front.getParams().addRule(RelativeLayout.RIGHT_OF, this.wheel.getId());
        front.getParams().setMargins(dpToPx(12), dpToPx(15), 0, 0);
        relativeLayout.addView(front.getImageView(), front.getParams());


        back = new ImageArrow(new ImageView(context), -1, context.getResources().getDrawable(R.drawable.move_back), isBackEnable, "back");
        back.getParams().addRule(RelativeLayout.ALIGN_TOP, this.wheel.getId());
        //back.getParams().addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        back.getParams().addRule(RelativeLayout.LEFT_OF, this.block.getId());
        back.getParams().setMargins(0, dpToPx(15), dpToPx(12), 0);
        relativeLayout.addView(back.getImageView(), back.getParams());

        this.wheel.setOnTouchListener(name, front, back);

    }

    public void initBlockArrows(boolean isUpEnable, boolean isAcrossEnable) {

        up = new ImageArrow(new ImageView(context), -1, context.getResources().getDrawable(R.drawable.move_up), isUpEnable, "rise");
        up.getParams().addRule(RelativeLayout.ABOVE, block.getId());
        up.getParams().addRule(RelativeLayout.ALIGN_START, block.getId());
        up.getParams().setMargins(dpToPx(5), 0, 0, dpToPx(12));
        relativeLayout.addView(up.getImageView(), up.getParams());


        across = new ImageArrow(new ImageView(context), up.getId() + 1, context.getResources().getDrawable(R.drawable.move_across), isAcrossEnable, "across");
        across.getParams().addRule(RelativeLayout.ABOVE, block.getId());
        across.getParams().addRule(RelativeLayout.ALIGN_END, block.getId());
        across.getParams().setMargins(0, 0, dpToPx(-35), dpToPx(12));
        relativeLayout.addView(across.getImageView(), across.getParams());


        block.setOnTouchListener(name, up, across);
    }

    public ImageArrow getFront() {
        return front;
    }

    public ImageArrow getBack() {
        return back;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
