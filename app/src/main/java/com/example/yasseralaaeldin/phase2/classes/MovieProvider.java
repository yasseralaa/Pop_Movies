package com.example.yasseralaaeldin.phase2.classes;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Yasser Alaa Eldin on 1/1/2016.
 */
public class MovieProvider extends ContentProvider {

    // fields for my content provider
    static final String PROVIDER_NAME = "com.movies.provider.movies";
    static final String URL = "content://" + PROVIDER_NAME + "/details";
    static final String URL2 = "content://" + PROVIDER_NAME + "/review";
    static final String URL3 = "content://" + PROVIDER_NAME + "/trailer";

    public static final Uri CONTENT_URI1 = Uri.parse(URL);
    public static final Uri CONTENT_URI2 = Uri.parse(URL2);
    public static final Uri CONTENT_URI3 = Uri.parse(URL3);
    // details Table Columns names
    public static final String KEY_DETAILS_ID = "id";
    public static final String KEY_MOVIEID = "movie_id";
    public  static final String KEY_TITLE = "title";
    public  static final String KEY_IMAGEPATH = "imagepath";
    public  static final String KEY_DATE = "date";
    public  static final String KEY_RATE = "rate";
    public  static final String KEY_OVERVIEW = "overview";

    // review Table Columns names
    public  static final String KEY_REVIEW_ID = "id";
    public  static final String KEY_REVIEW_MOVIEID = "movie_id";
    public  static final String KEY_AUTHOR = "author";
    public  static final String KEY_CONTENT = "content";


    // trailer Table Columns names
    public  static final String KEY_TRAILER_ID = "id";
    public  static final String KEY_TRAILER_MOVIEID = "movie_id";
    public  static final String KEY_KEY = "key";
    public  static final String KEY_NAME = "name";
    public  static final String KEY_SITE = "site";
    public  static final String KEY_SIZE = "size";
    public  static final String KEY_TYPE = "type";


    // integer values used in content URI
    static final int DETAILS = 1;
    static final int DETAILS_ID = 2;
    static final int REVIEW = 3;
    static final int REVIEW_ID = 4;
    static final int TRAILER = 5;
    static final int TRAILER_ID = 6;

    DBHelper dbHelper;

    // projection map for a query
    private static HashMap<String, String> MovieMap;

    // maps content URI "patterns" to the integer values that were set above
    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "details", DETAILS);
        uriMatcher.addURI(PROVIDER_NAME, "details/#", DETAILS_ID);
        uriMatcher.addURI(PROVIDER_NAME, "review", REVIEW);
        uriMatcher.addURI(PROVIDER_NAME, "review/#", REVIEW_ID);
        uriMatcher.addURI(PROVIDER_NAME, "trailer", TRAILER);
        uriMatcher.addURI(PROVIDER_NAME, "trailer/#", TRAILER_ID);
    }

    // database declarations
    private SQLiteDatabase database;

    // Database Name
    private static final String DATABASE_NAME = "movies3";
    // Movies table name
    private static final String TABLE_DETAILS = "details";
    private static final String TABLE_REVIEW = "review";
    private static final String TABLE_TRAILER = "trailer";
    static final int DATABASE_VERSION = 5;

    //create details table
    static String CREATE_DETAILS_TABLE = "CREATE TABLE " + TABLE_DETAILS + "("
            + KEY_DETAILS_ID + " INTEGER PRIMARY KEY, "+KEY_TITLE  + " TEXT, " + KEY_IMAGEPATH + " TEXT, "
            + KEY_DATE + " TEXT, " + KEY_RATE + " TEXT, "  + KEY_OVERVIEW + " TEXT, "  +  KEY_MOVIEID + " TEXT"  + ")";

    //create review table
    static String CREATE_REVIEW_TABLE = "CREATE TABLE " + TABLE_REVIEW + "("
            + KEY_REVIEW_ID + " INTEGER PRIMARY KEY, "+KEY_REVIEW_MOVIEID + " TEXT, " +KEY_AUTHOR + " TEXT, " + KEY_CONTENT + " TEXT"  + ")";

    //create trailer table
    static String CREATE_TRAILER_TABLE = "CREATE TABLE " + TABLE_TRAILER + "("
            + KEY_TRAILER_ID + " INTEGER PRIMARY KEY, "+KEY_TRAILER_MOVIEID + " TEXT, " +KEY_KEY + " TEXT, " +KEY_SITE + " TEXT, " +KEY_SIZE + " TEXT, " +KEY_TYPE + " TEXT, " + KEY_NAME + " TEXT"  + ")";



    // class that creates and manages the provider's database
    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL(CREATE_DETAILS_TABLE);
            db.execSQL(CREATE_REVIEW_TABLE);
            db.execSQL(CREATE_TRAILER_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            Log.w(DBHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ". Old data will be destroyed");
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_DETAILS_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " +  CREATE_REVIEW_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " +  CREATE_TRAILER_TABLE);
            onCreate(db);
        }

    }

    @Override
    public boolean onCreate() {
        // TODO Auto-generated method stub
        Context context = getContext();
        dbHelper = new DBHelper(context);
        // permissions to be writable
        database = dbHelper.getWritableDatabase();

        if(database == null)
            return false;
        else
            return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri _uri = null;
        switch (uriMatcher.match(uri)){
            case 1:
                long _ID1 = database.insert(TABLE_DETAILS, "", values);
                //---if added successfully---
                if (_ID1 > 0) {
                    _uri = ContentUris.withAppendedId(CONTENT_URI1, _ID1);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                break;
            case 3:
                long _ID2 = database.insert(TABLE_REVIEW, "", values);
                //---if added successfully---
                if (_ID2 > 0) {
                    _uri = ContentUris.withAppendedId(CONTENT_URI2, _ID2);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                break;
            case 5:
                long _ID3 = database.insert(TABLE_TRAILER, "", values);
                //---if added successfully---
                if (_ID3 > 0) {
                    _uri = ContentUris.withAppendedId(CONTENT_URI3, _ID3);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                break;
            default: throw new SQLException("Failed to insert row into " + uri);
        }
        return _uri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO Auto-generated method stub
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        // the TABLE_NAME to query on
        switch (uriMatcher.match(uri)){
            case 1:
                queryBuilder.setTables(TABLE_DETAILS);
                break;
            case 3:
                queryBuilder.setTables(TABLE_REVIEW);
                break;
            case 5:
                queryBuilder.setTables(TABLE_TRAILER);
                break;
           // default: throw new SQLException("Failed to insert row into " + uri);
        }


        switch (uriMatcher.match(uri)) {
            // maps all database column names
            case DETAILS:
                queryBuilder.setProjectionMap(MovieMap);
                break;
            case REVIEW:
                queryBuilder.setProjectionMap(MovieMap);
                break;
            case TRAILER:
                queryBuilder.setProjectionMap(MovieMap);
                break;
            case DETAILS_ID:
                queryBuilder.appendWhere(KEY_DETAILS_ID + "=" + uri.getLastPathSegment());
                break;
            case REVIEW_ID:
                queryBuilder.appendWhere( KEY_REVIEW_ID + "=" + uri.getLastPathSegment());
                break;
            case TRAILER_ID:
                queryBuilder.appendWhere( KEY_TRAILER_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        Cursor cursor = queryBuilder.query(database, projection, selection,
                selectionArgs, null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        int count = 0;

        switch (uriMatcher.match(uri)){
            case DETAILS:
                count = database.update(TABLE_DETAILS, values, selection, selectionArgs);
                break;
            case DETAILS_ID:
                count = database.update(TABLE_DETAILS, values, KEY_DETAILS_ID +
                        " = " + uri.getLastPathSegment() +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            case REVIEW:
                count = database.update(TABLE_REVIEW, values, selection, selectionArgs);
                break;
            case REVIEW_ID:
                count = database.update(TABLE_REVIEW, values, KEY_REVIEW_ID +
                        " = " + uri.getLastPathSegment() +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            case TRAILER:
                count = database.update(TABLE_TRAILER, values, selection, selectionArgs);
                break;
            case TRAILER_ID:
                count = database.update(TABLE_TRAILER, values, KEY_TRAILER_ID +
                        " = " + uri.getLastPathSegment() +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        int count = 0;
        String id = "";
        switch (uriMatcher.match(uri)){
            case DETAILS:
                // delete all the records of the table
                count = database.delete(TABLE_DETAILS, selection, selectionArgs);
                break;
            case DETAILS_ID:
                 id = uri.getLastPathSegment();	//gets the id
                count = database.delete(TABLE_DETAILS, KEY_DETAILS_ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            case REVIEW:
                // delete all the records of the table
                count = database.delete(TABLE_REVIEW, selection, selectionArgs);
                break;
            case REVIEW_ID:
                 id = uri.getLastPathSegment();	//gets the id
                count = database.delete(TABLE_REVIEW, KEY_REVIEW_ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            case TRAILER:
                // delete all the records of the table
                count = database.delete(TABLE_REVIEW, selection, selectionArgs);
                break;
            case TRAILER_ID:
                id = uri.getLastPathSegment();	//gets the id
                count = database.delete(TABLE_TRAILER, KEY_TRAILER_ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;


    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        switch (uriMatcher.match(uri)){
            // Get all friend-birthday records
            case DETAILS:
                return "vnd.android.cursor.dir/vnd.example.details";
            // Get a particular friend
            case DETAILS_ID:
                return "vnd.android.cursor.item/vnd.example.details";
            case REVIEW:
                return "vnd.android.cursor.dir/vnd.example.review";
            // Get a particular friend
            case REVIEW_ID:
                return "vnd.android.cursor.item/vnd.example.review";
            case TRAILER:
                return "vnd.android.cursor.dir/vnd.example.trailer";
            // Get a particular friend
            case TRAILER_ID:
                return "vnd.android.cursor.item/vnd.example.trailer";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

}
