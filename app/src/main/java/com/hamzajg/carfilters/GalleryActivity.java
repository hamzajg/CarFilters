package com.hamzajg.carfilters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;

public class GalleryActivity extends AppCompatActivity {
    GridView gallery;

    private File[] files;
    private String[] filesPaths;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        gallery = (GridView) findViewById(R.id.gallery);
        try {
            File dirDownload = getExternalFilesDir(null);
            if (dirDownload.isDirectory()) {
                files = dirDownload.listFiles();
                filesPaths = new String[files.length];

                for (int i = 0; i < files.length; i++) {
                    filesPaths[i] = files[i].getAbsolutePath();
                }
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        gallery.setAdapter(new ImageAdapterGridView(this, filesPaths));

        gallery.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(getApplicationContext(), EditPictureActivity.class);
            intent.putExtra("picFile", filesPaths[i]);
            startActivity(intent);
        });
    }

    public class ImageAdapterGridView extends BaseAdapter {
        private Context mContext;
        private final String[] filesPaths;

        public ImageAdapterGridView(Context c, String[] filesPaths) {
            this.mContext = c;
            this.filesPaths = filesPaths;
        }

        public int getCount() {
            return filesPaths.length;
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
                mImageView.setLayoutParams(new GridView.LayoutParams(350, 350));
                mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mImageView.setPadding(16, 16, 16, 16);
            } else {
                mImageView = (ImageView) convertView;
            }
            Bitmap bmp = BitmapFactory.decodeFile(filesPaths[position]);
            mImageView.setImageBitmap(bmp);
            return mImageView;
        }
    }
}
