package com.example.nasa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public class MainActivity extends AppCompatActivity {

    NasaDB db;
    LinearLayout ln;
    String date;
    LayoutInflater ltInflater;

    String API_URL = "https://api.nasa.gov/";
    String API_key = "VAJPD46HgUZO89dkBwP6i7QhWAdWVCgnrFPtmd5R";

    private Gson gson = new GsonBuilder().create();

    interface NasaAPI {
        @GET("/planetary/apod") // метод запроса (POST/GET) и путь к API
            // пример содержимого веб-формы q=dogs+and+people&key=MYKEY&image_type=photo
        Call<Response> search( @Query("api_key") String key, @Query("date") String date);
        // Тип ответа, действие, содержание запроса
    }

    public void startSearch(View v) {

        date = ((EditText) findViewById(R.id.date)).getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL) // адрес API сервера
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // создаём обработчик, определённый интерфейсом PixabayAPI выше
        NasaAPI api = retrofit.create(NasaAPI.class);

        Call<Response> getImages = api.search(API_key, date);

        Callback<Response> imagescallback = new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Response r = response.body();
                addNasaPhoto(r);
                Log.d("mytag", r.title);
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.d("mytag", "fail:" + t.getLocalizedMessage());
            }
        };
        getImages.enqueue(imagescallback);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ln = (LinearLayout) findViewById(R.id.main_lin);
        db = NasaDB.create(this, false); // открывает БД, если её нет, создаёт
        date = ((EditText) findViewById(R.id.date)).getText().toString();
        ltInflater = getLayoutInflater();

        int length = db.query("SELECT * FROM nasa_galery", null).getCount();
        Log.d("mytag", String.valueOf(length));

        for (int i=1; i< length+1; i++) {

            Cursor c = db.query("SELECT * FROM nasa_galery WHERE _id="+String.valueOf(i), null);

            if (c != null && c.moveToFirst()) {
                View nasa_item = ltInflater.inflate(R.layout.nasa_item, ln, false);

                ImageView im_photo = nasa_item.findViewById(R.id.photo);
                TextView tx_desc = nasa_item.findViewById(R.id.description);

                Uri photo = Uri.parse(c.getString(c.getColumnIndex("photo_url")));
                Log.d("mytag", "get photo" + photo.toString());
                String desc = c.getString(c.getColumnIndex("description"));
                Picasso p = new Picasso.Builder(getApplicationContext()).build();

                p.load(photo).into(im_photo);
                tx_desc.setText(desc);
                ln.addView(nasa_item);
            }
        }

    }

    public void addNasaPhoto(Response r) {
        new Thread() {
            @Override
            public void run() {
                NasaGalery nasa_galery = db.nasa_galery();
                int all = nasa_galery.getNumberOfRows();
                Nasa nasa = new Nasa(all+1, r.url,r.explanation);
                nasa_galery.insert(nasa);
            }
        }.start();
    }

    public void deleteTable(View v) {
        new Thread() {
            @Override
            public void run() {
                NasaGalery nasa_galery = db.nasa_galery();
                nasa_galery.deleteTable();
            }
        }.start();
    }

    public void updateTable(View v) {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }

}