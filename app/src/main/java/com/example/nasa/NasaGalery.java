package com.example.nasa;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
interface NasaGalery {
    @Query("SELECT * FROM nasa_galery ORDER BY _id")
    List<Nasa> selectAll();

//    @Query("SELECT * FROM nasa_galery WHERE year = 2021 ORDER BY title")
//    List<Nasa> selectThisYearTunes();

    @Query("SELECT * FROM nasa_galery WHERE _id=:id")
    Nasa findById(int id);

    @Query("SELECT COUNT(_id) FROM nasa_galery")
    int getNumberOfRows();

    @Query("SELECT _id FROM nasa_galery")
    int[] getIds();

    @Query("DELETE FROM nasa_galery")
    void deleteTable();

    @Insert
    void insert(Nasa nasa_galery);


    @Delete
    void delete(Nasa... nasa_galery);

    @Update
    void update(Nasa... nasa_galery);
}