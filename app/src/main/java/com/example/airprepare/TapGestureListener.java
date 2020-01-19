package com.example.airprepare;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class TapGestureListener extends GestureDetector.SimpleOnGestureListener {
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }
}
