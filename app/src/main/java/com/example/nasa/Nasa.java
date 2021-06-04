package com.example.nasa;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Locale;

@Entity(tableName = "nasa_galery")
class Nasa {
    @PrimaryKey
    @NonNull
    int _id;
    String photo_url, description;

    // ignore annotation here
//    @Ignore
//    public Nasa() {
//        this._id = 11;
//        this.photo_url = 1999;
//
//    }

    public Nasa(int _id, String photo_url, String description) {
        this._id = _id;
        this.photo_url = photo_url;
        this.description = description;
    }

    @Override
    public String toString() { return String.format(Locale.getDefault(), "%s: %s (%d)", _id, photo_url, description); }


}
