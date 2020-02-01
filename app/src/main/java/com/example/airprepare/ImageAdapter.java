package com.example.airprepare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ImageAdapter extends PagerAdapter {

    private Context mContext;
    private int[] mImageIds = new int[]{R.drawable.bundh, R.drawable.elections, R.drawable.floods};

    ImageAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View page = inflater.inflate(R.layout.activity_homescreen, null);
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(mImageIds[position]);
        page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == 0) {
                    Toast.makeText(mContext, "pressed 1", Toast.LENGTH_SHORT).show();
                } else if (position == 1) {
                    Toast.makeText(mContext, "pressed 2", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(mContext, "pressed 3", Toast.LENGTH_SHORT).show();
            }
        });

        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }

    @Override
    public int getCount() {
        return mImageIds.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
