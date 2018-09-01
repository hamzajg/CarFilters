package com.hamzajg.carfilters;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class EditPictureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_picture);
        ImageView iv = findViewById(R.id.image);
        iv.setOnTouchListener((v, event) -> {
            int[] viewCoords = new int[2];
            iv.getLocationOnScreen(viewCoords);

            int imageX = 0 + viewCoords[0]; // viewCoods[0] is the X coordinate
            int imageY = 0+ viewCoords[1]; // viewCoods[1] is the y coordinate
            Log.v("Real x >>>",imageX+"");
            Log.v("Real y >>>",imageY+"");
         return true;
        });
    }
}
