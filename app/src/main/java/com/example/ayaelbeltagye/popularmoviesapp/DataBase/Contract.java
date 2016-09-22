package com.example.ayaelbeltagye.popularmoviesapp.DataBase;

import android.provider.BaseColumns;

/**
 * Created by Aya Elbeltagye on 8/25/2016.
 */
 public class Contract  {
    public static class movies implements BaseColumns {
        public static final String TABLE_NAME="movies";
        public static final String COLUMN_ID="id";
        public static final String COLUMN_POSTER_PATH="poster";
        public static final String COLUMN_OVERVIEW="overview";
        public static final String COLUMN_MOVIE_ID="movie_id";
        public static final String COLUMN_TITLE="movie_title";
        public static final String COLUMN_VOTE="vote";
        public static final String COLUMN_RELEASE_DATE="release_year";
        public static final String CREATE_TABLE_MOVIE= "CREATE TABLE "+TABLE_NAME+"("
                        +COLUMN_ID+" INTEGER PRIMARYKEY AUTOINCREAMENT, "
                        +COLUMN_POSTER_PATH+" TEXT, "
                        +COLUMN_OVERVIEW+" TEXT, "
                        +COLUMN_MOVIE_ID+" TEXT, "
                        +COLUMN_TITLE+" TEXT, "
                        +COLUMN_VOTE+" INT, "
                        +COLUMN_RELEASE_DATE+" TEXT"
                        +")";
    }
}
