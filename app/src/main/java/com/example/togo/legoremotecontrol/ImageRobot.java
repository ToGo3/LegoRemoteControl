package com.example.togo.legoremotecontrol;

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

    public ImageRobot(Context context, int wheelId, int wheelLeftMargin) {

        wheel = new Image(new ImageView(context), wheelId, context.getResources().getDrawable(R.drawable.ic_wheel));
        wheel.getParams().addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        wheel.getParams().addRule(RelativeLayout.ALIGN_PARENT_START);
        wheel.getParams().setMargins(wheelLeftMargin, 0, 0, 0);


        block = new Image(new ImageView(context), wheel.getId() + 1, context.getResources().getDrawable(R.drawable.ic_lego));
        block.getParams().addRule(RelativeLayout.ABOVE, wheel.getImageView().getId());
        block.getParams().addRule(RelativeLayout.ALIGN_LEFT, wheel.getImageView().getId());
        block.getParams().setMargins(dpToPx(-10, context), 0, 0, 0);

    }

    public Image getBlock() {
        return block;
    }

    public Image getWheel() {
        return wheel;
    }

    private int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public void initWheelArrows(Context context, boolean isFrontEnable, boolean isBackEnable) {

        front = new ImageArrow(new ImageView(context), -1, context.getResources().getDrawable(R.drawable.move_front), isFrontEnable);
        front.getParams().addRule(RelativeLayout.ALIGN_TOP, this.wheel.getId());
        front.getParams().addRule(RelativeLayout.RIGHT_OF, this.wheel.getId());
        front.getParams().setMargins(dpToPx(12, context), dpToPx(15, context), 0, 0);

        back = new ImageArrow(new ImageView(context), -1, context.getResources().getDrawable(R.drawable.move_back), isBackEnable);
        back.getParams().addRule(RelativeLayout.ALIGN_TOP, wheel.getId());
        back.getParams().addRule(RelativeLayout.LEFT_OF, wheel.getId());
        back.getParams().setMargins(0, dpToPx(15, context), dpToPx(12, context), 0);

        wheel.setOnTouchListener(front, back);

    }

    public ImageArrow getFront() {
        return front;
    }

    public ImageArrow getBack() {
        return back;
    }
}
