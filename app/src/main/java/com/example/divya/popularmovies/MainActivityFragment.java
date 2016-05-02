package com.example.divya.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import com.example.divya.popularmovies.content.ContentProviderHelper;
import com.example.divya.popularmovies.content.MovieContract;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    MovieGridAdapter mAdapter;
    GridView gridView;
    ArrayList<MovieDetail> movieDetailList;
    int savedInstance = 0;
    String sortBy1 = "";
    private Spinner mSpinner;
    private boolean onlyFavorites = false;
    OnFragmentListener mOnFragmentListener;

    public MainActivityFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movieDetailList", movieDetailList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListener) {
            mOnFragmentListener = (OnFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnFragmentListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null || !savedInstanceState.containsKey("movieDetailList")) {
            movieDetailList = new ArrayList<MovieDetail>();
        } else {
            movieDetailList = savedInstanceState.getParcelableArrayList("movieDetailList");
            savedInstance = 1;
        }

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.poster_gridView);

       /* MovieDetail[] movieDetails={
          new MovieDetail("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"),
          new MovieDetail("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"),
          new MovieDetail("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"),
          new MovieDetail("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg")
        };*/

        mAdapter = new MovieGridAdapter(getActivity(), movieDetailList);

        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                MovieDetail movieDetail = mAdapter.getItem(position);
               /* Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT).show();
                Log.v("forecastWeather", "forecast: " + forecast);*/

                Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.detail_fragment);

                if (f == null) {
                    Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                    intent.putExtra("movieDetail", movieDetail);
                    startActivity(intent);
                } else {
                    mOnFragmentListener.onMovieSelected(movieDetail);
                }

            }
        });

        mSpinner = (Spinner) rootView.findViewById(R.id.movies_spinner);

        // Spinner Drop down elements
        List<String> filter = new ArrayList<String>();
        filter.add("General");
        filter.add("Favorites");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, filter);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(dataAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    movieDetailList.clear();
                    onlyFavorites = true;
                    movieDetailList = ContentProviderHelper.getMoviesFromContentProvider(getActivity());
                    mAdapter.clear();
                    addMoviesToAdapter(movieDetailList);
                    mAdapter.notifyDataSetChanged();
                } else {
                    onlyFavorites = false;
                    fetchMoviesFromNetwork();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    private void fetchMoviesFromNetwork() {
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortBy = sharedPrefs.getString(
                getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_popular));
        if (savedInstance == 0 || !sortBy.equalsIgnoreCase(sortBy1)) {
            sortBy1 = sortBy;
            if (!onlyFavorites) {
                FetchMoviesTask movies = new FetchMoviesTask();
                movies.execute(sortBy);
            }
        }
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<MovieDetail>> {
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected ArrayList<MovieDetail> doInBackground(String... sort) {
            if (sort.length == 0) {
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;
            String PARAM_APIKEY = "api_key";
            String PARAM_SORT = "sort_by";

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String apiKey = "83e702f55739e6bb647e862d983d1505";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(PARAM_SORT, sort[0])
                        .appendQueryParameter(PARAM_APIKEY, apiKey)
                        .build();
                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null)
                    return null;

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }

                movieJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error closing stream", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            //Log.v("Movie json str",movieJsonStr);

            movieDetailList = new ArrayList<MovieDetail>();

            try {
                JSONObject jsonObject = new JSONObject(movieJsonStr);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                //String length=(String)(jsonArray.length);


                for (int i = 0; i < jsonArray.length(); i++) {

                    //Log.v("in for","in for");

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String poster_url = jsonObject1.getString("poster_path");
                    String originalTitle = jsonObject1.getString("original_title");
                    String thumbnail = jsonObject1.getString("backdrop_path");
                    String overview = jsonObject1.getString("overview");
                    String rating = jsonObject1.getString("vote_average");
                    String releaseDate = jsonObject1.getString("release_date");
                    String movieId = jsonObject1.getString("id");


                    movieDetailList.add(new MovieDetail("http://image.tmdb.org/t/p/w185" + poster_url, originalTitle, thumbnail, overview, rating, releaseDate, movieId));


                }

            } catch (Exception e) {

            }
            return movieDetailList;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieDetail> result) {
            mAdapter.clear();
            addMoviesToAdapter(result);
        }
    }

    private void addMoviesToAdapter(ArrayList<MovieDetail> result) {
        for (int i = 0; i < result.size(); i++) {
            MovieDetail movie = result.get(i);
            // Log.v("Movie url",movie.posterUrl);
            mAdapter.add(movie);
        }
    }

    public interface OnFragmentListener {

        void onMovieSelected(MovieDetail movieDetail);
    }
}
