package com.hamzajg.carfilters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;

public class EditPictureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_picture);
        ImageView iv = findViewById(R.id.image);
        ImageView ivfilter = findViewById(R.id.filter_image_edit);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            ivfilter.setImageResource(extras.getInt("selectedFilter"));
            File imgFile = new  File(extras.getString("picFile"));

            if(imgFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                iv.setImageBitmap(myBitmap);

            }
        }
        // RelativeLayout. though you can use xml RelativeLayout here too by `findViewById()`
        RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);

        iv.setOnTouchListener((v, event) -> {
            int[] viewCoords = new int[2];
            iv.getLocationOnScreen(viewCoords);

            int imageX = (int) (event.getX() + viewCoords[0]); // viewCoods[0] is the X coordinate
            int imageY = (int) (event.getY() + viewCoords[1]); // viewCoods[1] is the y coordinate
            Log.v("Real x >>>",imageX+"");
            Log.v("Real y >>>",imageY+"");
// ImageView
            ImageView imageView = new ImageView(EditPictureActivity.this);

// Setting layout params to our RelativeLayout
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(125, 125);

            imageView.setImageResource(R.drawable.ic_radio_button_unchecked_red_24dp);
// Setting position of our ImageView
            layoutParams.leftMargin = imageX;
            layoutParams.topMargin = imageY;

// Finally Adding the imageView to RelativeLayout and its position
            relativeLayout.addView(imageView, layoutParams);
         return true;
        });
        FloatingActionButton cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(view -> {
            finish();
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        if (hasFocus)
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}
