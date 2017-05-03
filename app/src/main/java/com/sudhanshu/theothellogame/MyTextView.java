package com.sudhanshu.theothellogame;

import android.content.Context;
import android.widget.Button;

/**
 * Created by HP on 1/16/2017.
 */
public class MyTextView extends Button {

    private int clicked;
    private boolean valid = false;
    private int coor_x;
    private int coor_y;

    public int getCoor_x() {
        return coor_x;
    }

    public void setCoor_x(int coor_x) {
        this.coor_x = coor_x;
    }

    public int getCoor_y() {
        return coor_y;
    }

    public void setCoor_y(int coor_y) {
        this.coor_y = coor_y;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public MyTextView(Context context) {
        super(context);
    }

    public int getClicked() {
        return clicked;
    }

    public void setClicked(int clicked) {
        this.clicked = clicked;
    }
}
