package com.example.yasseralaaeldin.phase2.activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yasseralaaeldin.phase2.R;
import com.example.yasseralaaeldin.phase2.helpers.CheckNetwork;
import com.example.yasseralaaeldin.phase2.classes.Details;
import com.example.yasseralaaeldin.phase2.classes.MovieProvider;
import com.example.yasseralaaeldin.phase2.classes.Reviews;
import com.example.yasseralaaeldin.phase2.classes.Trailers;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class ItemDetailFragment extends Fragment {
    //activity members :-
    //-------------------------------------
    //default url
    private static String Vurl;

    private static String Rurl;

    //array list for holding data
    ArrayList<Trailers> trailersList = new ArrayList<Trailers>();
    ArrayList<Reviews> reviewsList = new ArrayList<Reviews>();
    Details details = new Details();

    TableLayout tableLayout;
    LinearLayout linearLayout;

    //download image members
    String file = "/sdcard/PopMovies/";
    // Progress Dialog
    private ProgressDialog pDialog;
    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;

    // File url to download
    private static String file_url = "";

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_title = "item_title";
    public static final String ARG_ITEM_img = "item_img";
    public static final String ARG_ITEM_date = "item_date";
    public static final String ARG_ITEM_rate = "item_rate";
    public static final String ARG_ITEM_overview = "item_overview";
    /*
     String title;
    String img_path;
    String date;
    String rate;
    String overview;
    String id;

     */
    /**
     * The dummy content this fragment is presenting.
     */
    View rootView;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.details, container, false);
        if(CheckNetwork.isInternetAvailable(getActivity())) //returns true if internet available
        {
            if (getArguments().containsKey(ARG_ITEM_ID)) {

                details.setId(getArguments().getString(ARG_ITEM_ID));
                details.setTitle(getArguments().getString(ARG_ITEM_title));
                details.setImg_path(getArguments().getString(ARG_ITEM_img));
                details.setDate(getArguments().getString(ARG_ITEM_date));
                details.setRate(getArguments().getString(ARG_ITEM_rate));
                details.setOverview(getArguments().getString(ARG_ITEM_overview));


                Vurl = "http://api.themoviedb.org/3/movie/" + details.getId() + "/videos?api_key=APIKEY";
                Rurl = "http://api.themoviedb.org/3/movie/" + details.getId() + "/reviews?api_key=APIKEY";
            }
            //-------------------------------------
            //GET json - getdatafromjson - handle data
            new GetTrailers().execute();
            new GetReviews().execute();
            //-------------------------------------
            // Show the dummy content as text in a TextView.
            ((TextView) rootView.findViewById(R.id.originaltitle)).setText(details.getTitle());
            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + details.getImg_path()).error(R.drawable.android).fit().tag(this).into(((ImageView) rootView.findViewById(R.id.movieposter)));
            ((TextView) rootView.findViewById(R.id.releasedate)).setText(details.getDate().substring(0, 4));
            ((TextView) rootView.findViewById(R.id.userrating)).setText(Double.valueOf(details.getRate()) / 1.0 + "/10");
            ((TextView) rootView.findViewById(R.id.plotsynopsis)).setText(details.getOverview());


            ImageView favouriteImage = (ImageView) rootView.findViewById(R.id.favourite);


            favouriteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int flag = 0;
                    String URL = "content://com.movies.provider.movies/details";
                    Uri movies = Uri.parse(URL);
                    Cursor c = getActivity().getContentResolver().query(movies, null, null, null, "id");
                    String result = "Movies Results:";

                    if (!c.moveToFirst()) {
                        Toast.makeText(getActivity()," no content yet!", Toast.LENGTH_LONG).show();
                    } else {
                        do {
                            // Log.d("HEREEEEe : ",c.getString(c.getColumnIndex(MovieProvider.KEY_TITLE)) + " " + c.getString(c.getColumnIndex(MovieProvider.KEY_IMAGEPATH)) + c.getString(c.getColumnIndex(MovieProvider.KEY_DATE)) + " " + c.getString(c.getColumnIndex(MovieProvider.KEY_RATE)) + " "+ c.getString(c.getColumnIndex(MovieProvider.KEY_OVERVIEW)) + " "+ c.getString(c.getColumnIndex(MovieProvider.KEY_MOVIEID)) + " " );
                            if (details.getTitle().equals(c.getString(c.getColumnIndex(MovieProvider.KEY_TITLE)))) {
                                flag = 1;
                                Toast.makeText(getActivity(), details.getTitle() + "is already in your favourites", Toast.LENGTH_LONG).show();
                                break;
                            }
                        } while (c.moveToNext());
                        Toast.makeText(getActivity(), "Movie Inserted", Toast.LENGTH_LONG).show();
                    }

                    if (flag == 0) {
                        Log.d("Insert: ", "Inserting ..");
                        file = "/sdcard/PopMovies/" + details.getImg_path();
                        new DownloadImageFromURL().execute("http://image.tmdb.org/t/p/w185/" + details.getImg_path());

                        ContentValues detailsvalues = new ContentValues();

                        detailsvalues.put(MovieProvider.KEY_TITLE,
                                details.getTitle());

                        detailsvalues.put(MovieProvider.KEY_IMAGEPATH,
                                details.getImg_path());

                        detailsvalues.put(MovieProvider.KEY_DATE,
                                details.getDate().substring(0, 4));
                        detailsvalues.put(MovieProvider.KEY_RATE,
                                details.getRate());
                        detailsvalues.put(MovieProvider.KEY_OVERVIEW,
                                details.getOverview());
                        detailsvalues.put(MovieProvider.KEY_MOVIEID,
                                details.getId());

                        Uri uri = getActivity().getContentResolver().insert(
                                MovieProvider.CONTENT_URI1, detailsvalues);

                        for (int i = 0; i < reviewsList.size(); i++) {
                            ContentValues reviewsvalues = new ContentValues();

                            reviewsvalues.put(MovieProvider.KEY_REVIEW_MOVIEID,
                                    details.getId());

                            reviewsvalues.put(MovieProvider.KEY_AUTHOR,
                                    reviewsList.get(i).getAuthor());

                            reviewsvalues.put(MovieProvider.KEY_CONTENT,
                                    reviewsList.get(i).getContent());


                            Uri uri2 = getActivity().getContentResolver().insert(
                                    MovieProvider.CONTENT_URI2, reviewsvalues);
                        }

                        for (int i = 0; i < trailersList.size(); i++) {
                            ContentValues trailervalues = new ContentValues();

                            trailervalues.put(MovieProvider.KEY_TRAILER_MOVIEID,
                                    details.getId());

                            trailervalues.put(MovieProvider.KEY_KEY,
                                    trailersList.get(i).getKey());

                            trailervalues.put(MovieProvider.KEY_SITE,
                                    trailersList.get(i).getSite());

                            trailervalues.put(MovieProvider.KEY_SIZE,
                                    trailersList.get(i).getSize());

                            trailervalues.put(MovieProvider.KEY_TYPE,
                                    trailersList.get(i).getType());

                            trailervalues.put(MovieProvider.KEY_NAME,
                                    trailersList.get(i).getName());

                            Uri uri3 = getActivity().getContentResolver().insert(
                                    MovieProvider.CONTENT_URI3, trailervalues);

                        }

                    }

                }
            });

        }
        else
        {
            TextView textView = (TextView) rootView.findViewById(R.id.originaltitle);
            textView.setText(details.getTitle());

            ImageView imageView = (ImageView) rootView.findViewById(R.id.movieposter);

            // setting downloaded into image view
            File imgFile = new  File(file+details.getImg_path());

            if(imgFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                imageView.setImageBitmap(myBitmap);

            }

            TextView date = (TextView) rootView.findViewById(R.id.releasedate);
            date.setText(details.getDate().substring(0, 4));

            TextView rate = (TextView) rootView.findViewById(R.id.userrating);
            rate.setText(Double.valueOf(details.getRate()) / 1.0 + "/10");

            TextView overview = (TextView) rootView.findViewById(R.id.plotsynopsis);
            overview.setText(details.getOverview());

            ImageView favouriteImage = (ImageView) rootView.findViewById(R.id.favourite);

            favouriteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "It's Already favourite movie", Toast.LENGTH_LONG).show();
                }
            });


            //----------------------------------------------------------------------------------
            //Get Trailers from DB
            //clear our list to keep it updated
            trailersList.clear();
            String URL = "content://com.movies.provider.movies/trailer";
            Uri movies = Uri.parse(URL);
            final Cursor c = getActivity().getContentResolver().query(movies, null, null, null, "id");
            String result = "Movies Results:";
            //trailersList.add(new Trailers(key, name, site, size, type,details.getId()));
            if (!c.moveToFirst()) {
                Toast.makeText(getActivity(), " no content yet!", Toast.LENGTH_LONG).show();
            }else{
                do{
                    trailersList.add(new Trailers(c.getString(c.getColumnIndex(MovieProvider.KEY_KEY)), c.getString(c.getColumnIndex(MovieProvider.KEY_NAME)), c.getString(c.getColumnIndex(MovieProvider.KEY_SITE)), c.getString(c.getColumnIndex(MovieProvider.KEY_SIZE)), c.getString(c.getColumnIndex(MovieProvider.KEY_TYPE)), c.getString(c.getColumnIndex(MovieProvider.KEY_TRAILER_MOVIEID))));
                } while (c.moveToNext());
            }

            GetTrailers();
            //----------------------------------------------------------------------------------
            //Get Reviews from DB
            //clear our list to keep it updated
            reviewsList.clear();
            URL = "content://com.movies.provider.movies/review";
            movies = Uri.parse(URL);
            final Cursor c2 = getActivity().getContentResolver().query(movies, null, null, null, "id");
            //reviewsList.add(new Reviews(author,content,details.getId()));
            if (!c2.moveToFirst()) {
                Toast.makeText(getActivity()," no content yet!", Toast.LENGTH_LONG).show();
            }else{
                do{
                    reviewsList.add(new Reviews(c2.getString(c2.getColumnIndex(MovieProvider.KEY_AUTHOR)),c2.getString(c2.getColumnIndex(MovieProvider.KEY_CONTENT)),c2.getString(c2.getColumnIndex(MovieProvider.KEY_REVIEW_MOVIEID))));
                } while (c2.moveToNext());
            }
            GetReviews();

            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

        return rootView;
    }

    //get movies class into main activity
    public class GetTrailers extends AsyncTask<String, Void, String[]> {
        private final String LOG_TAG = GetTrailers.class.getSimpleName();

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
            final String FORECAST_BASE_URL = String.valueOf(Vurl);
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
            trailersList.clear();
            //main strings to get :-
            final String OWM_RESULTS = "results";
            final String OWM_KEY = "key";
            final String OWM_NAME = "name";
            final String OWM_SITE = "site";
            final String OWM_SIZE = "size";
            final String OWM_TYPE = "type";

            JSONObject movieJson = new JSONObject(String.valueOf(movieJsonStr));
            JSONArray moviesArray = movieJson.getJSONArray(String.valueOf(OWM_RESULTS));
            String[] resultStrs = new String[moviesArray.length()];
            for (int i = 0; i < moviesArray.length(); i++) {
                String key;
                String name;
                String site;
                String size;
                String type;
                // Get the JSON object
                JSONObject movie = moviesArray.getJSONObject(i);
                key = movie.getString(String.valueOf(OWM_KEY));
                name = movie.getString(String.valueOf(OWM_NAME));
                site = movie.getString(String.valueOf(OWM_SITE));
                size = movie.getString(String.valueOf(OWM_SIZE));
                type = movie.getString(String.valueOf(OWM_TYPE));


                Log.d("Our Movie detals: ", "> " + key + " " + name + " " + site + " " + size + " " + type);

                trailersList.add(new Trailers(key, name, site, size, type,details.getId()));
            }
            return resultStrs;

        }

        //-------------------------------------------------------------------------------------------------------
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);

            GetTrailers();

        }

    }
    Bitmap bmImg = null;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
     void  GetTrailers()
    {
        for (int i = 0; i < trailersList.size(); i++) {

            if(trailersList.get(i).getId().equals(details.getId())) {
                Log.d("Compare : ",trailersList.get(i).getId()+" --> "+details.getId());
                final String key = trailersList.get(i).getKey();
                String name = trailersList.get(i).getName();
                String site = trailersList.get(i).getSite();
                String size = trailersList.get(i).getSize();
                String type = trailersList.get(i).getType();

                LinearLayout trailerlayout = new LinearLayout(getActivity());
                trailerlayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                trailerlayout.setPadding(0, 10, 0, 0);
                trailerlayout.setWeightSum(6f);
                trailerlayout.setLayoutParams(LLParams);
                ImageView trailerimage = new ImageView(getActivity());
                if (CheckNetwork.isInternetAvailable(getActivity())) //returns true if internet available
                {

                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                    URL newurl = null;
                    try {
                        newurl = new URL("http://image.tmdb.org/t/p/w185/" + details.getImg_path());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    try {
                        bmImg = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                        }
                    });
                    BitmapDrawable background = new BitmapDrawable(bmImg);
                    trailerimage.setMaxHeight(80);
                    trailerimage.setMaxWidth(110);
                    trailerimage.setImageResource(R.drawable.youtube);
                    trailerimage.setBackground(background);

                    trailerimage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key)));
                            Log.i("Video", "Video Playing....");
                        }
                    });
                } else {
                    // setting downloaded into image view
                    File imgFile = new File(file + details.getImg_path());

                    if (imgFile.exists()) {

                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        BitmapDrawable background = new BitmapDrawable(myBitmap);
                        trailerimage.setImageBitmap(myBitmap);
                        trailerimage.setBackground(background);
                        trailerimage.setMaxHeight(80);
                        trailerimage.setMaxWidth(110);
                        trailerimage.setImageResource(R.drawable.youtube);

                    }

                    trailerimage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                LinearLayout trailerlayout2 = new LinearLayout(getActivity());
                trailerlayout2.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams LLParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                trailerlayout2.setWeightSum(6f);
                trailerlayout2.setLayoutParams(LLParams2);

                TextView nameText = new TextView(getActivity());
                nameText.setPadding(30, 30, 0, 0);
                nameText.setTextSize(25);
                nameText.setText(name);

                TextView siteText = new TextView(getActivity());
                siteText.setPadding(30, 30, 0, 0);
                siteText.setTextSize(15);
                siteText.setText("Site : " + site);

                TextView sizeText = new TextView(getActivity());
                sizeText.setPadding(30, 30, 0, 0);
                sizeText.setTextSize(15);
                sizeText.setText("Size : " + size);

                TextView typeText = new TextView(getActivity());
                typeText.setPadding(30, 30, 0, 0);
                typeText.setTextSize(15);
                typeText.setText("Type : " + type);

                View v = new View(getActivity());
                v.setLayoutParams(rootView.findViewById(R.id.line).getLayoutParams());
                v.setBackground(rootView.findViewById(R.id.line).getBackground());

                trailerlayout2.addView(nameText);
                trailerlayout2.addView(siteText);
                trailerlayout2.addView(sizeText);
                trailerlayout2.addView(typeText);

                trailerlayout.addView(trailerimage);
                trailerlayout.addView(trailerlayout2);
                LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.TrailersLayout);
                linearLayout.addView(trailerlayout);
                linearLayout.addView(v);

                Log.d("Result: ", "> " + trailersList.get(i).getKey() + " " + trailersList.get(i).getName() + " " + trailersList.get(i).getSite() + " " + trailersList.get(i).getSize() + " " + trailersList.get(i).getType());
            }
        }
        Trailers.share = trailersList.get(0).getKey();
    }

    //get movies class into main activity
    public class GetReviews extends AsyncTask<String, Void, String[]> {
        private final String LOG_TAG = GetReviews.class.getSimpleName();

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
            final String FORECAST_BASE_URL = String.valueOf(Rurl);
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
            reviewsList.clear();
            //main strings to get :-
            final String OWM_RESULTS = "results";
            final String OWM_AUTHOR = "author";
            final String OWM_CONTENT = "content";

            JSONObject movieJson = new JSONObject(String.valueOf(movieJsonStr));
            JSONArray moviesArray = movieJson.getJSONArray(String.valueOf(OWM_RESULTS));
            String[] resultStrs = new String[moviesArray.length()];
            for (int i = 0; i < moviesArray.length(); i++) {
                String author;
                String content;
                // Get the JSON object
                JSONObject movie = moviesArray.getJSONObject(i);
                author = movie.getString(String.valueOf(OWM_AUTHOR));
                content = movie.getString(String.valueOf(OWM_CONTENT));


                Log.d("Our Movie detals: ", "> " + author + " " + content);

                reviewsList.add(new Reviews(author,content,details.getId()));
            }
            return resultStrs;

        }

        //-------------------------------------------------------------------------------------------------------
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);

            GetReviews();

            Log.d("Result: ", "> " + result.length);
        }

    }

    void GetReviews()
    {
        for (int i = 0; i < reviewsList.size(); i++) {
            if (reviewsList.get(i).getId().equals(details.getId())) {
                Log.d("Compare : ", reviewsList.get(i).getId() + " --> " + details.getId());
                Log.d("Compare2 : ", String.valueOf(reviewsList.size()));
                String author = reviewsList.get(i).getAuthor();
                String content = reviewsList.get(i).getContent();

                LinearLayout trailerlayout = new LinearLayout(getActivity());
                trailerlayout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                trailerlayout.setPadding(0, 10, 0, 0);
                trailerlayout.setWeightSum(6f);
                trailerlayout.setLayoutParams(LLParams);

                TextView authortxt = new TextView(getActivity());
                authortxt.setText("Review By : " + author);
                authortxt.setTextSize(22);
                authortxt.setTypeface(authortxt.getTypeface(), Typeface.BOLD);

                TextView contenttxt = new TextView(getActivity());
                contenttxt.setText(content);
                contenttxt.setTextSize(20);

                View v = new View(getActivity());
                v.setLayoutParams(rootView.findViewById(R.id.line).getLayoutParams());
                v.setBackground(rootView.findViewById(R.id.line).getBackground());

                LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.ReviewsLayout);
                linearLayout.addView(trailerlayout);
                linearLayout.addView(authortxt);
                linearLayout.addView(contenttxt);
                linearLayout.addView(v);

            }
        }

    }



    /**
     * Background Async Task to download file
     * */
    class DownloadImageFromURL extends AsyncTask<String, String, String> {

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                File folder = new File("/sdcard/PopMovies/");
                boolean success = true;
                if (!folder.exists())
                    success = folder.mkdir();


                // Output stream
                OutputStream output = new FileOutputStream(file);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }



    }

}
