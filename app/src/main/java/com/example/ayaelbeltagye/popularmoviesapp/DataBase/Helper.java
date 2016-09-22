package com.example.ayaelbeltagye.popularmoviesapp.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.ayaelbeltagye.popularmoviesapp.Movie;
import java.util.ArrayList;

/**
 * Created by Aya Elbeltagye on 8/25/2016.
 */
 public class Helper extends SQLiteOpenHelper {
    private static final int VERSION=1;
    private static final String DATABASE="movies";

    public Helper(Context context) {
        super(context, DATABASE, null, VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Contract.movies.CREATE_TABLE_MOVIE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.movies.TABLE_NAME);
        onCreate(db);
    }
    public void add_To_FAVORITE(Movie movie){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues value=new ContentValues();
        value.put(Contract.movies.COLUMN_POSTER_PATH, movie.getPosterPath());
        value.put(Contract.movies.COLUMN_OVERVIEW,movie.getOverview());
        value.put(Contract.movies.COLUMN_MOVIE_ID,movie.getId());
        value.put(Contract.movies.COLUMN_TITLE,movie.getTitle());
        value.put(Contract.movies.COLUMN_VOTE,movie.getTopRating());
        value.put(Contract.movies.COLUMN_RELEASE_DATE, movie.getReleseDate());
        db.insert(Contract.movies.TABLE_NAME,null,value);
    }
    public void deleteFromFavorite(String id){
        SQLiteDatabase db=getWritableDatabase();
         db.delete(Contract.movies.TABLE_NAME, Contract.movies.COLUMN_MOVIE_ID + " =?", new String[]{id});
        db.close();
    }
   public ArrayList<Movie> get_FavoriteMovie(){
        SQLiteDatabase db=getReadableDatabase();
        ArrayList<Movie>movieList=new ArrayList<>();
        Cursor cursor=db.query(Contract.movies.TABLE_NAME, null, null, null, null, null, null);
        ArrayList<String> posters =new ArrayList<>();
        if(cursor!=null){
            while (cursor.moveToNext()){
                Movie movie=new Movie();
                movie.setPosterPath(cursor.getString(1));
                movie.setOverview(cursor.getString(2));
                movie.setId(cursor.getInt(3));
                movie.setTitle(cursor.getString(4));
                movie.setTopRating(cursor.getString(5));
                movie.setReleseDate(cursor.getString(6));
                posters.add(cursor.getString(1));
                movieList.add(movie);
            }
        }
        return movieList;
    }
}
