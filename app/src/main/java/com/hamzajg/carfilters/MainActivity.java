package com.hamzajg.carfilters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    GridView androidGridView;
    GridView additionalPictures;

    Integer[] imageIDs = {
            R.drawable.face_1,
            R.drawable.face_2,
            R.drawable.face_3,
            R.drawable.face_4,
            R.drawable.face_5,
            R.drawable.face_6
    };
    Integer[] freePicturesIDs = {
            R.drawable.free_picture,
            R.drawable.free_picture,
            R.drawable.free_picture,
            R.drawable.free_picture,
            R.drawable.free_picture,
            R.drawable.free_picture,
            R.drawable.free_picture,
            R.drawable.free_picture,
            R.drawable.free_picture,
            R.drawable.free_picture,
            R.drawable.free_picture
    };
    private int selectedPosition = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        androidGridView = (GridView) findViewById(R.id.grid_view_image);
        androidGridView.setAdapter(new ImageAdapterGridView(this, imageIDs));
        additionalPictures = (GridView) findViewById(R.id.add_grid_view_image);
        additionalPictures.setAdapter(new ImageAdapterGridView(this, freePicturesIDs));
        androidGridView.setOnItemClickListener((adapterView, view, i, l) -> {
            selectedPosition = i;
        });
        FloatingActionButton btn = findViewById(R.id.floatingActionButton);
        btn.setOnClickListener((view) -> {
            if(selectedPosition == -1)
                return;
            Intent i = new Intent(getApplicationContext(), CameraActivity.class);
            i.putExtra("selectedFilter", imageIDs[selectedPosition]);
            startActivity(i);
        });
    }

    public class ImageAdapterGridView extends BaseAdapter {
        private Context mContext;
        private Integer[] mImages;
        public ImageAdapterGridView(Context c, Integer[] images) {
            mContext = c;
            mImages = images;
        }

        public int getCount() {
            return mImages.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView mImageView;

            if (convertView == null) {
                mImageView = new ImageView(mContext);
                mImageView.setLayoutParams(new GridView.LayoutParams(250, 250));
                mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                mImageView.setPadding(3, 3, 3, 3);
            } else {
                mImageView = (ImageView) convertView;
            }
            mImageView.setImageResource(mImages[position]);
            return mImageView;
        }
    }
}
