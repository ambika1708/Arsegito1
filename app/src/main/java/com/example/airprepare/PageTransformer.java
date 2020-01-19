package com.example.airprepare;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class PageTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(@NonNull View page, float position) {
        page.setScaleX(1.0F - .15F * Math.abs(position));
        page.setScaleY(1.0F - .15F * Math.abs(position));
        page.setPivotX(page.getWidth() / 2.0F);
        page.setPivotY(page.getWidth() / 2.0F);
    }
}
