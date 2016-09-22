package com.example.ayaelbeltagye.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.example.ayaelbeltagye.popularmoviesapp.DataBase.Helper;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {


    GridView gridView;
    List<Movie> moviesList;
    MovieAdapter adapter;
    DetailsInterface interfac;
    Movie movie;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridView);
        moviesList = new ArrayList<Movie>();
        updateMovie();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            movie = moviesList.get(position);
                interfac.openDetails(movie);
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        interfac = (DetailsInterface) getActivity();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_Popularity) {
            updateMovie();
            return true;
        }
        if (id == R.id.action_rate) {
            updateMovie();
            return true;
        }
        if (id == R.id.action_Favorite) {
            Helper helper = new Helper(getContext());
            moviesList = helper.get_FavoriteMovie();
            adapter = new MovieAdapter(getActivity(), getAllPosters(moviesList));
            gridView.setAdapter(adapter);

            return true;
            }
            return super.onOptionsItemSelected(item);
        }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        Log.i("onnn", cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)==null?"y":"n");
        if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        return false;
    }
    private void updateMovie() {
        if (isOnline()) {
            FetchMovieTask movieTask = new FetchMovieTask();
            movieTask.execute();
        } else {
          Toast.makeText(getActivity(),"No internet ",Toast.LENGTH_LONG).show();

        }
    }
    private class FetchMovieTask extends AsyncTask<Void, Integer, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(Void... params) {
            BufferedReader reader = null;
            String movieJsonStr = null;
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortOrder = prefs.getString(getString(R.string.sort_order_key), getString((R.string.sort_order_highest_rated)));
            StringBuilder sb = new StringBuilder();
            try {
                String type = sortOrder;
                URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=" + type + ".desc&api_key=1962bc00de1584940b4f338dc55d6887");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                httpURLConnection.setRequestMethod("GET");
                Log.i("Connect", httpURLConnection == null ? "Y" : "N");
                InputStream inputStream = httpURLConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                getMovieDataFromJson(sb.toString());
                return moviesList;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            adapter = new MovieAdapter(getActivity(), getAllPosters(movies));
            gridView.setAdapter(adapter);
        }

        private List<Movie> getMovieDataFromJson(String forecastJsonStr) {
            try {
               moviesList.clear();
                JSONObject jsonObject = new JSONObject(forecastJsonStr);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                String baseUri = "http://image.tmdb.org/t/p/w185/";
                String[] posters = new String[jsonArray.length()];
                Movie movie = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String posterPath = baseUri + object.getString("poster_path");
                    String title = object.getString("original_title");
                    String overview = object.getString("overview");
                    String topRating = object.getString("vote_count");
                    String releseDate = object.getString("release_date");
                    int id = object.getInt("id");
                    movie = new Movie();
                    movie.setPosterPath(posterPath);
                    posters[i] = posterPath;
                    movie.setTitle(title);
                    movie.setTopRating(topRating);
                    movie.setOverview(overview);
                    movie.setReleseDate(releseDate);
                    movie.setId(id);
                    moviesList.add(movie);

                }
            } catch (JSONException jsonExc) {
            }
            return moviesList;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateMovie();
    }
        public String[] getAllPosters(List<Movie> list) {
            String[] posterspath = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                posterspath[i] = moviesList.get(i).getPosterPath();
            }
            return posterspath;
        }
}
