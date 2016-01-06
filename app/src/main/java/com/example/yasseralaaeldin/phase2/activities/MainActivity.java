package com.example.yasseralaaeldin.phase2.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yasseralaaeldin.phase2.R;
import com.example.yasseralaaeldin.phase2.classes.Trailers;
import com.example.yasseralaaeldin.phase2.helpers.CheckNetwork;
import com.example.yasseralaaeldin.phase2.classes.Details;
import com.example.yasseralaaeldin.phase2.classes.MovieProvider;
import com.example.yasseralaaeldin.phase2.helpers.ImageAdapter;
import com.example.yasseralaaeldin.phase2.helpers.LocalImageAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //activity members :-
    //-------------------------------------
    //default url
    private static String url = "http://api.themoviedb.org/3/discover/movie?api_key=APIKEY";
    //main page gridview
    GridView gridview;
    //array list for holding data
    ArrayList<Details> details = new ArrayList<Details>();
    //-------------------------------------
    int id;
    private boolean mTwoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        //define the action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.item_detail_container) != null)
            mTwoPane = true;


        if(CheckNetwork.isInternetAvailable(MainActivity.this)) //returns true if internet available
        {

            //-------------------------------------
            //GET json - getdatafromjson - handle data
            new GetMovies().execute();
            //-------------------------------------
            //Grid View Area
            gridview = (GridView) findViewById(R.id.gridview);
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ItemDetailFragment.ARG_ITEM_ID, details.get(position).getId());
                        arguments.putString(ItemDetailFragment.ARG_ITEM_title, details.get(position).getTitle());
                        arguments.putString(ItemDetailFragment.ARG_ITEM_img, details.get(position).getImg_path());
                        arguments.putString(ItemDetailFragment.ARG_ITEM_date, details.get(position).getDate());
                        arguments.putString(ItemDetailFragment.ARG_ITEM_rate, details.get(position).getRate());
                        arguments.putString(ItemDetailFragment.ARG_ITEM_overview, details.get(position).getOverview());
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();

                    } else {
                        Intent detailIntent = new Intent(MainActivity.this, ItemDetailActivity.class);
                        detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_ID, details.get(position).getId());
                        detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_title, details.get(position).getTitle());
                        detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_img, details.get(position).getImg_path());
                        detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_date, details.get(position).getDate());
                        detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_rate, details.get(position).getRate());
                        detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_overview, details.get(position).getOverview());
                        startActivity(detailIntent);
                    }
                }
            });
            //-------------------------------------
        }
        else {

            String URL = "content://com.movies.provider.movies/details";
            Uri movies = Uri.parse(URL);
            final Cursor c = getContentResolver().query(movies, null, null, null, "id");
            String result = "Movies Results:";
            final ArrayList<Details> favourites = new ArrayList<Details>();
            if (!c.moveToFirst()) {
                Toast.makeText(MainActivity.this," no content yet!", Toast.LENGTH_LONG).show();
            } else {
                do {
                    favourites.add(new Details(c.getString(c.getColumnIndex(MovieProvider.KEY_TITLE)), c.getString(c.getColumnIndex(MovieProvider.KEY_IMAGEPATH)), c.getString(c.getColumnIndex(MovieProvider.KEY_DATE)), c.getString(c.getColumnIndex(MovieProvider.KEY_RATE)), c.getString(c.getColumnIndex(MovieProvider.KEY_OVERVIEW)), c.getString(c.getColumnIndex(MovieProvider.KEY_MOVIEID))));
                    Log.d("Image : ", c.getString(0));
                } while (c.moveToNext());
            }
            final String[] favoutitePaths = new String[favourites.size()];
            for (int i = 0; i < favourites.size(); i++) {
                favoutitePaths[i] = favourites.get(i).getImg_path();
                Log.d("Image : ", favourites.get(i).getImg_path());
            }
            if (favourites.size() > 0) {
                gridview = (GridView) findViewById(R.id.gridview);
                gridview.setAdapter(new LocalImageAdapter(MainActivity.this, favoutitePaths));
                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {
                        if (mTwoPane) {
                            Bundle arguments = new Bundle();
                            arguments.putString(ItemDetailFragment.ARG_ITEM_ID, favourites.get(position).getId());
                            arguments.putString(ItemDetailFragment.ARG_ITEM_title, favourites.get(position).getTitle());
                            arguments.putString(ItemDetailFragment.ARG_ITEM_img, favourites.get(position).getImg_path());
                            arguments.putString(ItemDetailFragment.ARG_ITEM_date, favourites.get(position).getDate());
                            arguments.putString(ItemDetailFragment.ARG_ITEM_rate, favourites.get(position).getRate());
                            arguments.putString(ItemDetailFragment.ARG_ITEM_overview, favourites.get(position).getOverview());
                            ItemDetailFragment fragment = new ItemDetailFragment();
                            fragment.setArguments(arguments);
                            getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();

                        } else {
                            Intent detailIntent = new Intent(MainActivity.this, ItemDetailActivity.class);
                            detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_ID, (Serializable) favourites.get(position).getId());
                            detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_title, (Serializable) favourites.get(position).getTitle());
                            detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_img, (Serializable) favourites.get(position).getImg_path());
                            detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_date, (Serializable) favourites.get(position).getDate());
                            detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_rate, (Serializable) favourites.get(position).getRate());
                            detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_overview, (Serializable) favourites.get(position).getOverview());
                            startActivity(detailIntent);
                        }
                    }
                });
                Toast.makeText(MainActivity.this, "No Internet Connection, Enjoy Your Favourites :) ", Toast.LENGTH_LONG).show();
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    //action bar area
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        id = item.getItemId();

        if(CheckNetwork.isInternetAvailable(MainActivity.this)) //returns true if internet available
        {
            //sort by highst-rated :-
            if (id == R.id.highest_rated)
            {
                url = "http://api.themoviedb.org/3/discover/movie?sort_by=highest-rated.desc&api_key=APIKEY";
                new GetMovies().execute();
                return true;
            }
            //sort by most popular :-
            if (id == R.id.most_popular) {
                //i didn't manage to upload it as rar so i removed API key to upload it to Github
                url = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=APIKEY";
                new GetMovies().execute();
                return true;
            }

            //favourites:-
            if (id == R.id.myfavourite) {
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                String URL = "content://com.movies.provider.movies/details";
                Uri movies = Uri.parse(URL);
                final Cursor c = getContentResolver().query(movies, null, null, null, "id");
                String result = "Movies Results:";
                final ArrayList<Details> favourites = new ArrayList<Details>();
                if (!c.moveToFirst()) {
                    Toast.makeText(MainActivity.this, " no content yet!", Toast.LENGTH_LONG).show();
                } else {
                    do {
                        favourites.add(new Details(c.getString(c.getColumnIndex(MovieProvider.KEY_TITLE)), c.getString(c.getColumnIndex(MovieProvider.KEY_IMAGEPATH)), c.getString(c.getColumnIndex(MovieProvider.KEY_DATE)), c.getString(c.getColumnIndex(MovieProvider.KEY_RATE)), c.getString(c.getColumnIndex(MovieProvider.KEY_OVERVIEW)), c.getString(c.getColumnIndex(MovieProvider.KEY_MOVIEID))));
                        Log.d("Image : ", c.getString(0));
                    } while (c.moveToNext());
                }
                final String[] favoutitePaths = new String[favourites.size()];
                for (int i = 0; i < favourites.size(); i++) {
                    favoutitePaths[i] = favourites.get(i).getImg_path();
                    Log.d("Image : ", favourites.get(i).getImg_path());
                }
                if (favourites.size() > 0) {
                    gridview = (GridView) findViewById(R.id.gridview);
                    gridview.setAdapter(new LocalImageAdapter(MainActivity.this, favoutitePaths));
                    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v,
                                                int position, long id) {
                            if (mTwoPane) {
                                Bundle arguments = new Bundle();
                                arguments.putString(ItemDetailFragment.ARG_ITEM_ID, favourites.get(position).getId());
                                arguments.putString(ItemDetailFragment.ARG_ITEM_title, favourites.get(position).getTitle());
                                arguments.putString(ItemDetailFragment.ARG_ITEM_img, favourites.get(position).getImg_path());
                                arguments.putString(ItemDetailFragment.ARG_ITEM_date, favourites.get(position).getDate());
                                arguments.putString(ItemDetailFragment.ARG_ITEM_rate, favourites.get(position).getRate());
                                arguments.putString(ItemDetailFragment.ARG_ITEM_overview, favourites.get(position).getOverview());
                                ItemDetailFragment fragment = new ItemDetailFragment();
                                fragment.setArguments(arguments);
                                getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();

                            } else {
                                Intent detailIntent = new Intent(MainActivity.this, ItemDetailActivity.class);
                                detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_ID, (Serializable) favourites.get(position).getId());
                                detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_title, (Serializable) favourites.get(position).getTitle());
                                detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_img, (Serializable) favourites.get(position).getImg_path());
                                detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_date, (Serializable) favourites.get(position).getDate());
                                detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_rate, (Serializable) favourites.get(position).getRate());
                                detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_overview, (Serializable) favourites.get(position).getOverview());
                                startActivity(detailIntent);
                            }
                        }
                    });
                }
            }

            //share
            if(CheckNetwork.isInternetAvailable(MainActivity.this)) //returns true if internet available
            {
                String Titleforhead = ItemDetailFragment.ARG_ITEM_title;
                if (id == R.id.menu_item_share && Trailers.share!= null) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                    share.putExtra(Intent.EXTRA_SUBJECT, Titleforhead + " Movie Trailer !!");
                    share.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=" + Trailers.share);

                    startActivity(Intent.createChooser(share, "Share trailer!"));
                }
            }
            else
                Toast.makeText(MainActivity.this, "you can't share without internet connection..", Toast.LENGTH_LONG).show();

        }
        else
        {
            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }


        return super.onOptionsItemSelected(item);
    }
    //-------------------------------------------------------------------------------------------------------------------------------

    //get movies class into main activity
    public class GetMovies extends AsyncTask<String, Void, String[]> {
        private final String LOG_TAG = GetMovies.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("onPreExecute: ", "> " + "onPreExecute");
        }
        //-------------------------------------------------------------------------------------------------------
        //background method :-
        @Override
        protected String[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;

            //using our url
            final String FORECAST_BASE_URL = String.valueOf(url);
            Uri builtUri = Uri.parse(String.valueOf(FORECAST_BASE_URL)).buildUpon().build();
            try {
                URL url = new URL(String.valueOf(builtUri));
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = String.valueOf(buffer);
                Log.d("Response: ", "> " + movieJsonStr);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                return getMoviesDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        //method for handling data after get json :-
        private String[] getMoviesDataFromJson(String movieJsonStr) throws JSONException {
            //clear our list to keep it updated
            details.clear();
            //main strings to get :-
            final String OWM_RESULTS = "results";
            final String OWM_TITLE = "title";
            final String OWM_POSTER = "poster_path";
            final String OWM_VOTE = "vote_average";
            final String OWM_DATE = "release_date";
            final String OWM_OVERVIEW = "overview";
            final String OWM_ID = "id";

            JSONObject movieJson = new JSONObject(String.valueOf(movieJsonStr));
            JSONArray moviesArray = movieJson.getJSONArray(String.valueOf(OWM_RESULTS));
            String[] resultStrs = new String[moviesArray.length()];
            for(int i = 0; i < moviesArray.length(); i++) {
                String title;
                String poster;
                String vote;
                String date;
                String overview;
                String id;
                // Get the JSON object
                JSONObject movie = moviesArray.getJSONObject(i);
                title = movie.getString(String.valueOf(OWM_TITLE));
                poster = movie.getString(String.valueOf(OWM_POSTER));
                vote = movie.getString(String.valueOf(OWM_VOTE));
                date = movie.getString(String.valueOf(OWM_DATE));
                overview = movie.getString(String.valueOf(OWM_OVERVIEW));
                id = movie.getString(String.valueOf(OWM_ID));


                Log.d("Our Movie detals: ", "> " + title + " " + poster + " " + vote + " " + date + " " + overview + " " + id);

                resultStrs[i] = String.valueOf(poster);

                details.add(new Details(title,poster,date,vote,overview,id));
            }
            return resultStrs;

        }
        //-------------------------------------------------------------------------------------------------------
        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            gridview.setAdapter(new ImageAdapter(MainActivity.this, result));
        }

    }


    private static final String STATE_ACTIVATED_POSITION = "activated_position";


    private Callbacks mCallbacks = sDetailsCallbacks;

    private int mActivatedPosition = gridview.INVALID_POSITION;


    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(String id);
    }

    private static Callbacks sDetailsCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        mCallbacks = sDetailsCallbacks;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != gridview.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically give items the 'activated' state when touched.
        gridview.setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            gridview.setItemChecked(mActivatedPosition, false);
        } else {
            gridview.setItemChecked(position, true);
        }
        mActivatedPosition = position;
    }
}
