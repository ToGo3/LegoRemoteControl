package com.example.togo.legoremotecontrol;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * Created by ToGo on 18.02.2016.
 */
public class ImageRobot {

    private Image block;
    private Image wheel;

    private Image obstacle;
    private ImageArrow back, front, up, down, across, allBack, allFront;
    private RelativeLayout relativeLayout;
    private String name;
    private Context context;
    private boolean isMoveEngine, isLiftEngine, isHeadBlock, isTailBlock;
    private ProgressBar loader;

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
        return Math.round(dp * displayMetrics.density);
    }

    public void initConnectionLine() {
        ImageView connectionLine = new ImageView(context);connectionLine.setImageDrawable(context.getResources().getDrawable(R.drawable.blackline));

        RelativeLayout.LayoutParams connectionLineRules=new RelativeLayout.LayoutParams(Math.round(70*context.getResources().getDisplayMetrics().density),android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        connectionLineRules.addRule(RelativeLayout.RIGHT_OF, this.block.getId());
        connectionLineRules.addRule(RelativeLayout.ALIGN_TOP, this.block.getId());
        connectionLineRules.setMargins(0, dpToPx(25), 0, 0);
        relativeLayout.addView(connectionLine,connectionLineRules);
    }

    public void initWheelArrows(boolean isMoveEngine) {
        this.isMoveEngine=isMoveEngine;
        if (isMoveEngine) {
            front = new ImageArrow(new ImageView(context), -1, context.getResources().getDrawable(R.drawable.move_front), isMoveEngine, "forward");
            front.getParams().addRule(RelativeLayout.ALIGN_TOP, this.wheel.getId());
            front.getParams().addRule(RelativeLayout.RIGHT_OF, this.wheel.getId());
            front.getParams().setMargins(dpToPx(12), dpToPx(15), 0, 0);
            relativeLayout.addView(front.getImageView(), front.getParams());
        } else front = null;


        if (isMoveEngine) {
            back = new ImageArrow(new ImageView(context), -1, context.getResources().getDrawable(R.drawable.move_back), isMoveEngine, "back");
            back.getParams().addRule(RelativeLayout.ALIGN_TOP, this.wheel.getId());
            back.getParams().addRule(RelativeLayout.LEFT_OF, this.block.getId());
            back.getParams().setMargins(0, dpToPx(15), dpToPx(12), 0);
            relativeLayout.addView(back.getImageView(), back.getParams());
        } else back = null;

        this.wheel.setOnTouchListener(name, front, back);

    }

    public void initBlockArrows(boolean isLiftEngine, boolean headBlock, boolean tailBlock) {
        this.isLiftEngine=isLiftEngine;
        if (isLiftEngine) {
            up = new ImageArrow(new ImageView(context), -1, context.getResources().getDrawable(R.drawable.move_up), isLiftEngine, "rise");
            up.getParams().addRule(RelativeLayout.ABOVE, block.getId());
            up.getParams().addRule(RelativeLayout.ALIGN_START, block.getId());
            up.getParams().setMargins(dpToPx(5), 0, 0, dpToPx(12));
            relativeLayout.addView(up.getImageView(), up.getParams());
        } else up = null;


        if (isLiftEngine) {
            down = new ImageArrow(new ImageView(context), up.getId() + 1, context.getResources().getDrawable(R.drawable.move_down), isLiftEngine, "shrink");
            down.getParams().addRule(RelativeLayout.ABOVE, block.getId());
            down.getParams().addRule(RelativeLayout.ALIGN_END, block.getId());
            down.getParams().setMargins(0, 0, dpToPx(5), dpToPx(12));
            relativeLayout.addView(down.getImageView(), down.getParams());
        } else down = null;

        this.isHeadBlock=headBlock;
        if (headBlock) {
            across = new ImageArrow(new ImageView(context), up.getId() + 1, context.getResources().getDrawable(R.drawable.move_across), headBlock, "acrossObstacle");
            across.getParams().addRule(RelativeLayout.ABOVE, block.getId());
            across.getParams().addRule(RelativeLayout.ALIGN_END, block.getId());
            across.getParams().setMargins(0, 0, dpToPx(-65), dpToPx(12));
            relativeLayout.addView(across.getImageView(), across.getParams());

        } else across = null;

        this.isTailBlock=tailBlock;
        if (tailBlock) {
            allBack = new ImageArrow(new ImageView(context), -1, context.getResources().getDrawable(R.drawable.move_back), tailBlock, "moveBack");
            allBack.getParams().addRule(RelativeLayout.LEFT_OF, this.block.getId());
            allBack.getParams().addRule(RelativeLayout.ABOVE, block.getId());
            allBack.getParams().setMargins(0, 0, dpToPx(15), dpToPx(-40));
            relativeLayout.addView(allBack.getImageView(), allBack.getParams());
        } else allBack = null;

        if (headBlock) {
            allFront = new ImageArrow(new ImageView(context), -1, context.getResources().getDrawable(R.drawable.move_front), headBlock, "moveForward");
            allFront.getParams().addRule(RelativeLayout.RIGHT_OF, this.block.getId());
            allFront.getParams().addRule(RelativeLayout.ABOVE, block.getId());
            allFront.getParams().setMargins(dpToPx(12), 0, 0, dpToPx(-40));
            relativeLayout.addView(allFront.getImageView(), allFront.getParams());

            obstacle = new Image(new ImageView(context), -2, context.getResources().getDrawable(R.drawable.ic_obstacle));
            obstacle.getImageView().setVisibility(View.INVISIBLE);
            obstacle.getParams().addRule(RelativeLayout.RIGHT_OF, this.block.getId());
            obstacle.getParams().addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            obstacle.getParams().setMargins(dpToPx(20),0,0,0);
            relativeLayout.addView(obstacle.getImageView(),obstacle.getParams());

            loader = new ProgressBar(context);
            loader.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.RIGHT_OF,obstacle.getId());
            params.setMargins(dpToPx(-74),0,0,dpToPx(15));
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            relativeLayout.addView(loader,params);

        } else allFront = null;

        block.setOnTouchListener(name, up, down, across, allBack, allFront);
    }

    public ImageArrow getFront() {
        return front;
    }

    public ImageArrow getBack() {
        return back;
    }

    public ImageArrow getUp() {
        return up;
    }

    public ImageArrow getDown(){return down;}

    public ImageArrow getAllBack() {
        return allBack;
    }

    public ImageArrow getAllFront() {
        return allFront;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isMoveEngine() {
        return isMoveEngine;
    }

    public Boolean isLiftEngine() {
        return isLiftEngine;
    }

    public Boolean isHeadBlock() {
        return isHeadBlock;
    }

    public Boolean isTailBlock() {
        return isTailBlock;
    }

    public Image getObstacle() {
        return obstacle;
    }

    public void setObstacle(Image obstacle) {
        this.obstacle = obstacle;
    }

    public ProgressBar getLoader(){
        return loader;
    }

}
