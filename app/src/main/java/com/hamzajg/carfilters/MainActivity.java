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

    Integer[] imageIDs = {
            R.drawable.number1,
            R.drawable.number2,
            R.drawable.number3,
            R.drawable.number1,
            R.drawable.number2,
            R.drawable.number3
    };
    private int selectedPosition = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        androidGridView = (GridView) findViewById(R.id.grid_view_image);
        androidGridView.setAdapter(new ImageAdapterGridView(this));
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

        public ImageAdapterGridView(Context c) {
            mContext = c;
        }

        public int getCount() {
            return imageIDs.length;
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
                mImageView.setLayoutParams(new GridView.LayoutParams(130, 130));
                mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mImageView.setPadding(16, 16, 16, 16);
            } else {
                mImageView = (ImageView) convertView;
            }
            mImageView.setImageResource(imageIDs[position]);
            return mImageView;
        }
    }
}
