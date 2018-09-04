package com.hamzajg.carfilters;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;
import java.util.ArrayList;

public class EditPictureActivity extends AppCompatActivity {

    private ArrayList<Note> notes = new ArrayList<Note>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_picture);
        ImageView iv = findViewById(R.id.image);
        ImageView ivfilter = findViewById(R.id.filter_image_edit);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ivfilter.setImageResource(extras.getInt("selectedFilter"));
            File imgFile = new File(extras.getString("picFile"));

            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                iv.setImageBitmap(myBitmap);

            }
        }
        // RelativeLayout. though you can use xml RelativeLayout here too by `findViewById()`
        RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);

        iv.setOnTouchListener((v, event) -> imageViewTouched(iv, relativeLayout, event));
        FloatingActionButton cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(view -> finish());
        FloatingActionButton confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), GalleryActivity.class);
            startActivity(i);
        });
    }

    private boolean imageViewTouched(ImageView iv, RelativeLayout relativeLayout, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            int[] viewCoords = new int[2];
            iv.getLocationOnScreen(viewCoords);

            int imageX = (int) (event.getX() + viewCoords[0]); // viewCoods[0] is the X coordinate
            int imageY = (int) (event.getY() + viewCoords[1]); // viewCoods[1] is the y coordinate
            Log.v("Real x >>>", imageX + "");
            Log.v("Real y >>>", imageY + "");
            addNotesPosition(relativeLayout, imageX, imageY);
        }
        return true;
    }

    private void addNotesPosition(RelativeLayout relativeLayout, int x, int y) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Comment");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            Note note = new Note(new Point(x, y), input.getText().toString());
            if (note != null) {
                notes.add(note);
// ImageView
                ImageView imageView = new ImageView(EditPictureActivity.this);

// Setting layout params to our RelativeLayout
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(125, 125);

                imageView.setImageResource(R.drawable.ic_radio_button_unchecked_red_24dp);
// Setting position of our ImageView
                layoutParams.leftMargin = x;
                layoutParams.topMargin = y;

// Finally Adding the imageView to RelativeLayout and its position
                relativeLayout.addView(imageView, layoutParams);

            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
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
