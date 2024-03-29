package com.sitepoint.contentdemoaccess;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentProviderClient;
import android.content.CursorLoader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ContentProviderClient imageProviderContentResolver = null;
    Cursor imageProviderCursor = null;
    String PROVIDER_NAME = "sitepoint.contentdemo.images";
    Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/images");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("CONTENT_DEMO_ACCESS", "Hello world");

        imageProviderContentResolver = getContentResolver().acquireContentProviderClient(CONTENT_URI);
        try {
            imageProviderCursor = imageProviderContentResolver.query(CONTENT_URI, null, null, null, null);
        } catch (RemoteException e) {
            Log.i("CONTENT_DEMO_ACCESS", "Cursor exception");
            e.printStackTrace();
        }

        CursorLoader cursorLoader = new CursorLoader(getBaseContext(), CONTENT_URI,
                null, null, null, null);
        imageProviderCursor = cursorLoader.loadInBackground();
        imageProviderCursor.moveToFirst();

        Log.i("CONTENT_DEMO_ACCESS", "here");

        if (imageProviderCursor == null) {
            Log.i("CONTENT_DEMO_ACCESS", "cursor not obtained");
        } else {
            Log.i("CONTENT_DEMO_ACCESS", "cursor successfully obtained !");
            Log.i("CONTENT_DEMO_ACCESS", "Cursor content: "+ DatabaseUtils.dumpCursorToString(imageProviderCursor));
        }
    }
}