package com.hamzajg.carfilters;

import androidx.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.provider.MediaStore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditPictureActivity extends AppCompatActivity {

    private List<Note> notes = new ArrayList<Note>();
    private final Map<String, ImageView> markerViews = new HashMap<>();
    private static final String DATABASE_NAME = "note_db";
    private NoteDatabase noteDatabase;
    private String fileName;
    private boolean isChanged;
    // notesLoaded guards user taps until the DB read has been applied on
    // the main thread; saving prevents persistData from double-firing.
    private boolean notesLoaded;
    private boolean saving;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_picture);
        noteDatabase = Room.databaseBuilder(getApplicationContext(),
                NoteDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
        ImageView iv = findViewById(R.id.image);
        ImageView ivfilter = findViewById(R.id.filter_image_edit);
        // RelativeLayout. though you can use xml RelativeLayout here too by `findViewById()`
        RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);
        FloatingActionButton cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(view -> finish());
        FloatingActionButton confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(view -> {
            persistData();
            Intent i = new Intent(getApplicationContext(), GalleryActivity.class);
            startActivityForResult(i, GalleryActivity.GALLERY_REQUEST);
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int rotation = extras.getInt("rotation");

//            ivfilter.setImageResource(extras.getInt("selectedFilter"));
            Bitmap bMap = BitmapFactory.decodeResource(getResources(),extras.getInt("selectedFilter"));
            if (bMap != null)
                ivfilter.setImageBitmap(rotateImage(bMap, 90));

            File imgFile = new File(extras.getString("picFile"));
            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                iv.setImageBitmap(rotateImage(myBitmap, 90));

                fileName = imgFile.getAbsolutePath().substring(imgFile.getAbsolutePath().lastIndexOf("/") + 1, imgFile.getAbsolutePath().lastIndexOf("."));
                if (!fileName.isEmpty())
                    new Thread(() ->
                    {
                        List<Note> loadedNotes = noteDatabase.daoAccess().fetchAllNotesByFileName(fileName);
                        Log.i("notes", loadedNotes.size() + "");
                        runOnUiThread(() -> {
                            notes = loadedNotes;
                            for(Note n : notes)  {
                                addNoteMarker(relativeLayout, n);
                            }
                            notesLoaded = true;
                        });
                    }).start();
            }
        }

        iv.setOnTouchListener((v, event) -> imageViewTouched(iv, relativeLayout, event));
    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix,
                true);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    // Idempotent save: delete all rows for this photo, then re-insert the
    // current list — all inside one transaction, off the main thread.
    // Re-entry is blocked by `saving` so confirm can't double-fire.
    private void persistData() {
        if (!isChanged || saving)
            return;
        saving = true;
        new Thread(() -> noteDatabase.runInTransaction(() -> {
            noteDatabase.daoAccess().deleteNotesByFileName(fileName);
            noteDatabase.daoAccess().insertMultipleMovies(notes);
        })).start();
        isChanged = false;
    }

    private boolean imageViewTouched(ImageView iv, RelativeLayout relativeLayout, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            // Taps during the async note load are ignored so they can't
            // interleave with the loaded markers.
            if (!notesLoaded) {
                Toast.makeText(this, "Loading notes…", Toast.LENGTH_SHORT).show();
                return true;
            }
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
            Note note = new Note(fileName, x, y, input.getText().toString());
            if (note != null) {
                isChanged = true;
                notes.add(note);
                addNoteMarker(relativeLayout, note);

            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    // Creates the red-circle marker for a note and tracks it by UUID so
    // later waves can dispatch click/edit/delete on the marker view.
    private ImageView addNoteMarker(RelativeLayout relativeLayout, Note note) {
        ImageView imageView = new ImageView(EditPictureActivity.this);

// Setting layout params to our RelativeLayout
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(125, 125);

        imageView.setImageResource(R.drawable.ic_radio_button_unchecked_red_24dp);
// Setting position of our ImageView
        layoutParams.leftMargin = note.getX();
        layoutParams.topMargin = note.getY();
        imageView.setTag(note.getUUID());
// Markers are children of relativeLayout (a sibling overlay of the photo
// ImageView), so their clicks never reach the photo's touch listener —
// tapping a marker opens its note dialog instead of creating a new note.
        imageView.setOnClickListener(v -> showNoteDialog(note, relativeLayout));

// Finally Adding the imageView to RelativeLayout and its position
        relativeLayout.addView(imageView, layoutParams);
        markerViews.put(note.getUUID(), imageView);
        return imageView;
    }

    // Tapping a marker shows the note text with Edit / Delete / Cancel.
    private void showNoteDialog(Note note, RelativeLayout relativeLayout) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Note");
        builder.setMessage(note.getNoteString());
        builder.setPositiveButton("Edit", (dialog, which) -> showEditNoteDialog(note, relativeLayout));
        builder.setNegativeButton("Delete", (dialog, which) -> confirmDeleteNote(note, relativeLayout));
        builder.setNeutralButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void showEditNoteDialog(Note note, RelativeLayout relativeLayout) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit note");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(note.getNoteString());
        builder.setView(input);
        builder.setPositiveButton("OK", (dialog, which) -> {
            note.setNoteString(input.getText().toString());
            // Update is immediate (not part of the save-all persistData),
            // so it runs off the main thread; the in-memory note already
            // carries the new text for the next save.
            new Thread(() -> noteDatabase.daoAccess().updateNote(note)).start();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void confirmDeleteNote(Note note, RelativeLayout relativeLayout) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete note?");
        builder.setMessage(note.getNoteString());
        builder.setPositiveButton("Delete", (dialog, which) -> {
            new Thread(() -> noteDatabase.daoAccess().deleteNote(note)).start();
            ImageView marker = markerViews.remove(note.getUUID());
            if (marker != null) {
                relativeLayout.removeView(marker);
            }
            notes.remove(note);
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
