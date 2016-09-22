package com.example.ayaelbeltagye.popularmoviesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ayaelbeltagye.popularmoviesapp.DataBase.Helper;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    public DetailsActivityFragment() {
    }

    ReviewsAdapter reviewsAdapter;
    LinearLayout reviewsListView;
    TrailersAdapter trailerAdapter;
    LinearLayout trailersListView;
    Movie movie;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_details, container, false);
        Configuration configuration = getResources().getConfiguration();
        if (configuration.smallestScreenWidthDp >= 600) {
            Bundle b = getArguments();
            movie = (Movie) b.getSerializable("movie");
        }
        else {
            Intent intent = getActivity().getIntent();
            if (intent.hasExtra("movie")) {
                if (intent.getExtras().get("movie") != null) {
                    movie = (Movie) intent.getExtras().getSerializable("movie");
                }
            }
        }
        if (movie != null) {
            ImageView imageView = (ImageView) root.findViewById(R.id.imageView);
            Picasso.with(getContext()).load(movie.getPosterPath()).into(imageView);
            TextView title = (TextView) root.findViewById(R.id.textView8);
            title.setText(movie.getTitle());
            TextView overview = (TextView) root.findViewById(R.id.textView5);
            overview.append("\n" + movie.getOverview());
            TextView vote_count = (TextView) root.findViewById(R.id.textView2);
            vote_count.append(movie.getTopRating());
            TextView release_date = (TextView) root.findViewById(R.id.textView1);
            release_date.append(movie.getReleseDate());
            reviewsListView = (LinearLayout) root.findViewById(R.id.review_list);
            trailersListView = (LinearLayout) root.findViewById(R.id.trailer_list);
            update();
        }

        final CheckBox favorite = (CheckBox) root.findViewById(R.id.check);
        final SharedPreferences prefs = getContext().getSharedPreferences("favorite", 0);
        final SharedPreferences.Editor e = prefs.edit();
        favorite.setChecked(prefs.getBoolean("checked" +movie.getId(),false));
        favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked) {
                    Helper helper = new Helper(getContext());
                    helper.add_To_FAVORITE(movie);
                    e.putBoolean("checked" + movie.getId(), favorite.isClickable());
                    e.commit();
                    Toast.makeText(getContext(), "Add to your favourites", Toast.LENGTH_LONG).show();
                   PosterImage posterImage = new PosterImage(movie.getPosters());
                   Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/"+movie.getPosters()).into(posterImage.traget);



                } else {
                    Helper helper = new Helper(getContext());
                    helper.deleteFromFavorite(movie.getId() + "");
                    e.putBoolean("checked" + movie.getId(), favorite.isClickable());
                    e.commit();
                    Toast.makeText(getContext(), "....", Toast.LENGTH_LONG).show();

                }
            }
        });

        return root;
    }

        private void update() {
           connectReview connectReview = new connectReview();
           connectReview.execute();
            connectTrailer connectTrailer = new connectTrailer();
            connectTrailer.execute();

        }


    class connectTrailer extends AsyncTask<Void, Integer, ArrayList<Trailer>> {

        @Override
        protected ArrayList<Trailer> doInBackground(Void... params) {
            String action = "videos";
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String data = null;
            int id = movie.getId();

            try {
                String movie_param = "movie";
                String baseUrl = "http://api.themoviedb.org/3/";
                String apiKey = "api_key";
                String movieId = "" + id + "";
                Uri builtUri = Uri.parse(baseUrl).buildUpon()
                        .appendPath(movie_param).appendPath(movieId).appendEncodedPath(action)
                        .appendQueryParameter(apiKey, "1962bc00de1584940b4f338dc55d6887").build();
                URL url = new URL(builtUri.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                InputStream input = connection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (input == null) {
                    data = null;
                }
                reader = new BufferedReader(new InputStreamReader(input));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    data = null;
                }
                data = buffer.toString();

            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                data = null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }

                try {
                    return getMoviesTrailers(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Trailer> list) {
            trailerAdapter = new TrailersAdapter(getContext(), list);
            for (int i = 0; i < list.size(); i++) {
                View view = trailerAdapter.getView(i, null, null);
                trailersListView.addView(view);
            }
            trailerAdapter.notifyDataSetChanged();
        }

    }

    private ArrayList<Trailer> getMoviesTrailers(String jsonResult) throws JSONException {
        ArrayList<Trailer> trailersList = new ArrayList<Trailer>();
        if(jsonResult!=null) {
            JSONObject jsonObject = new JSONObject(jsonResult);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String name = object.getString("name");
                String key = object.getString("key");
                Trailer trailer = new Trailer();
                trailer.setName(name);
                trailer.setKey(key);
                trailersList.add(trailer);
            }
        }
        return trailersList;
    }


    class connectReview extends AsyncTask<Void, Integer, ArrayList<Review>> {

        @Override
        protected ArrayList<Review> doInBackground(Void... params) {
            String action = "reviews";
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String data = null;
            int id = movie.getId() ;

            try {
                String movie_param = "movie";
                String baseUrl = "http://api.themoviedb.org/3/";
                String apiKey = "api_key";
                String movieId = "" + id + "";
                Uri builtUri = Uri.parse(baseUrl).buildUpon()
                        .appendPath(movie_param).appendPath(movieId).appendEncodedPath(action)
                        .appendQueryParameter(apiKey,"1962bc00de1584940b4f338dc55d6887").build();
                URL url = new URL(builtUri.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                InputStream input = connection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (input == null) {
                    data = null;
                }
                reader = new BufferedReader(new InputStreamReader(input));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    data = null;
                }
                data = buffer.toString();

            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                data = null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }

                try {
                    return getReviewsOfMovies(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Review> list) {
            reviewsAdapter = new ReviewsAdapter(getContext(), list);
            for (int i = 0; i < list.size(); i++) {
                View view = reviewsAdapter.getView(i, null, null);
                reviewsListView.addView(view);
            }
            reviewsAdapter.notifyDataSetChanged();

        }

        private ArrayList<Review> getReviewsOfMovies(String jsonResult) throws JSONException {
            ArrayList<Review> reviewsList = new ArrayList<Review>();

            if(jsonResult!=null) {
                JSONObject jsonObject = new JSONObject(jsonResult);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String content = object.getString("content");
                    Review review = new Review();
                    review.setContent(content);
                    reviewsList.add(review);
                }
            }
            return reviewsList;
        }

    }

}




