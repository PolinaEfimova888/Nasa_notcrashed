package com.example.nasa;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddPhotoAsyncTask extends AsyncTask<String, Void, Bitmap> {

    String url;
    NasaDB db;

    public AddPhotoAsyncTask(NasaDB nasa_db, String ph_url) {
        this.db = nasa_db;
        this.url = ph_url;
    }

    @Override
    protected Bitmap doInBackground(String... ph_url) {
        return getImageTask(url);
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        db.nasa_galery().insert(new Nasa(1, url,"testrow"));
    }

    private Bitmap getImageTask(String url) {
        Bitmap bmp =null;

        try{
            URL url_url = new URL(url);
            HttpURLConnection con = (HttpURLConnection)url_url.openConnection();
            InputStream is = con.getInputStream();
            bmp = BitmapFactory.decodeStream(is);

        } catch(Exception e){
            e.getLocalizedMessage();
        }
        return bmp;
    }
}

