package com.javatechig.gridview;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * @author javatechig {@link http://javatechig.com}
 */
public class MainActivity extends Activity {
    private GridView gridView;
    private GridViewAdapter customGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gridView);
        customGridAdapter = new GridViewAdapter(this, R.layout.row_grid, getData());
        gridView.setAdapter(customGridAdapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(MainActivity.this, position + "#Selected", Toast.LENGTH_SHORT)
                        .show();
            }

        });

    }

    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
        // retrieve String drawable array
        //		TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);

        TypedArray imgs = getResources().obtainTypedArray(R.array.album_res_id);
        for (int i = 0; i < imgs.length(); i++) {

            // Decode original image to scale down version
            // Add scaled down version to image array list

            //			Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),
            //					imgs.getResourceId(i, -1));
            Bitmap bitmap =
                    decodeSampledBitmapFromResource(getResources(), imgs.getResourceId(i, -1), 100,
                            100);
            imageItems.add(new ImageItem(bitmap, "Image#" + i));
        }

        return imageItems;

    }

    private Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth,
                                                   int reqHeight) {

        // First decode with inJustDecodeBounds=true
        BitmapFactory.Options options = new BitmapFactory.Options();
        // Avoid to allocate memory while just resolve bitmap options
        // So set inJustDecodeBounds=true
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate simple size
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode original resource to scale down version
        // BitmapFactory try to decode whole picture instead of just bounds
        // So set inJustDecodeBounds=false
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height/2;
            final int halfWidth = width/2;

            // if scaled height still longer than required height
            // increase scale number by power of 2
            while (reqHeight < halfHeight/inSampleSize && reqWidth < halfWidth/inSampleSize) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
