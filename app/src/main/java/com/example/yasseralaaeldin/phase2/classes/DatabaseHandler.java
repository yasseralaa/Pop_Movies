package com.example.yasseralaaeldin.phase2.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yasser Alaa Eldin on 12/28/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "movies";

    // Movies table name
    private static final String TABLE_DETAILS = "details";
    private static final String TABLE_REVIEW = "review";
    private static final String TABLE_TRAILER = "trailer";

    // details Table Columns names
    private static final String KEY_DETAILS_ID = "id";
    private static final String KEY_MOVIEID = "movie_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_IMAGEPATH = "imagepath";
    private static final String KEY_DATE = "date";
    private static final String KEY_RATE = "rate";
    private static final String KEY_OVERVIEW = "overview";

    // review Table Columns names
    private static final String KEY_REVIEW_ID = "id";
    private static final String KEY_REVIEW_MOVIEID = "movie_id";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_CONTENT = "content";


    // trailer Table Columns names
    private static final String KEY_TRAILER_ID = "id";
    private static final String KEY_TRAILER_MOVIEID = "movie_id";
    private static final String KEY_KEY = "key";
    private static final String KEY_NAME = "name";
    private static final String KEY_SITE = "site";
    private static final String KEY_SIZE = "size";
    private static final String KEY_TYPE = "type";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        //create details table
        String CREATE_DETAILS_TABLE = "CREATE TABLE " + TABLE_DETAILS + "("
                + KEY_DETAILS_ID + " INTEGER PRIMARY KEY, "+KEY_TITLE  + " TEXT, " + KEY_IMAGEPATH + " TEXT, "
                + KEY_DATE + " TEXT, " + KEY_RATE + " TEXT, "  + KEY_OVERVIEW + " TEXT, "  +  KEY_MOVIEID + " TEXT"  + ")";
        db.execSQL(CREATE_DETAILS_TABLE);

        //create review table
        String CREATE_REVIEW_TABLE = "CREATE TABLE " + TABLE_REVIEW + "("
                + KEY_REVIEW_ID + " INTEGER PRIMARY KEY, "+KEY_REVIEW_MOVIEID + " TEXT, " +KEY_AUTHOR + " TEXT, " + KEY_CONTENT + " TEXT"  + ")";
        db.execSQL(CREATE_REVIEW_TABLE);

        //create trailer table
        String CREATE_TRAILER_TABLE = "CREATE TABLE " + TABLE_TRAILER + "("
                + KEY_TRAILER_ID + " INTEGER PRIMARY KEY, "+KEY_TRAILER_MOVIEID + " TEXT, " +KEY_KEY + " TEXT, " +KEY_SITE + " TEXT, " +KEY_SIZE + " TEXT, " +KEY_TYPE + " TEXT, " + KEY_NAME + " TEXT"  + ")";
        db.execSQL(CREATE_TRAILER_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEW);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAILER);

        // Create tables again
        onCreate(db);
    }

    /**
     * Operations
    */

    // Adding new details
    public void addDetails(Details details) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MOVIEID, details.getId()); // Movie ID
        values.put(KEY_TITLE, details.getTitle()); // Movie TITLE
        values.put(KEY_IMAGEPATH, details.getImg_path()); // Movie IMAGEPATH
        values.put(KEY_DATE, details.getDate()); // Movie DATE
        values.put(KEY_RATE, details.getRate()); // Movie RATE
        values.put(KEY_OVERVIEW, details.getOverview()); // Movie OVERVIEW
        // Inserting Row
        db.insert(TABLE_DETAILS, null, values);
        db.close(); // Closing database connection
    }

    // Adding new review
    public void addReview(Reviews reviews) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AUTHOR, reviews.getAuthor()); // Review Author
        values.put(KEY_REVIEW_MOVIEID, reviews.getId()); // Movie ID
        values.put(KEY_CONTENT, reviews.content); // Review Content

        // Inserting Row
        db.insert(TABLE_REVIEW, null, values);
        db.close(); // Closing database connection
    }


    // Adding new trailer
    public void addTrailer(Trailers trailers) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_KEY, trailers.getKey());   // Trailers KEY
        values.put(KEY_TRAILER_MOVIEID, trailers.getId()); // Movie ID
        values.put(KEY_NAME, trailers.getName()); // Trailers NAME
        values.put(KEY_SITE, trailers.getSite()); // Trailers SITE
        values.put(KEY_SIZE, trailers.getSize()); // Trailers SIZE
        values.put(KEY_TYPE, trailers.getType()); // Trailers TYPE

        // Inserting Row
        db.insert(TABLE_TRAILER, null, values);
        db.close(); // Closing database connection
    }


    // Getting All Details
    public List<Details> getAllDetails() {
        List<Details> detailsList = new ArrayList<Details>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DETAILS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Details details = new Details();
                details.setDBid(Integer.parseInt(cursor.getString(0)));
                details.setId(cursor.getString(6));
                details.setTitle(cursor.getString(1));
                details.setImg_path(cursor.getString(2));
                details.setDate(cursor.getString(3));
                details.setRate(cursor.getString(4));
                details.setOverview(cursor.getString(5));

                // Adding details to list
                detailsList.add(details);
            } while (cursor.moveToNext());
        }

        // return details list
        return detailsList;
    }


    // Getting All Reviews
    public List<Reviews> getAllReviews() {
        List<Reviews> reviewsList = new ArrayList<Reviews>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REVIEW;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Reviews reviews = new Reviews();
                reviews.setDBid(Integer.parseInt(cursor.getString(0)));
                reviews.setId(cursor.getString(1));
                reviews.setAuthor(cursor.getString(2));
                reviews.setContent(cursor.getString(3));

                // Adding reviews to list
                reviewsList.add(reviews);
            } while (cursor.moveToNext());
        }

        // return reviews list
        return reviewsList;
    }

    // Getting All Trailer
    public List<Trailers> getAllTrailers() {
        List<Trailers> trailersList = new ArrayList<Trailers>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TRAILER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Trailers trailers = new Trailers();
                trailers.setDBid(Integer.parseInt(cursor.getString(0)));
                trailers.setId(cursor.getString(1));
                trailers.setKey(cursor.getString(2));
                trailers.setSite(cursor.getString(3));
                trailers.setSize(cursor.getString(4));
                trailers.setType(cursor.getString(5));
                trailers.setName(cursor.getString(6));

                // Adding trailers to list
                trailersList.add(trailers);
            } while (cursor.moveToNext());
        }

        // return trailers list
        return trailersList;
    }

}