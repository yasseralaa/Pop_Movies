package com.example.yasseralaaeldin.phase2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.yasseralaaeldin.phase2.R;
import com.example.yasseralaaeldin.phase2.helpers.CheckNetwork;
import com.example.yasseralaaeldin.phase2.classes.Trailers;


public class ItemDetailActivity extends AppCompatActivity {
    String Titleforhead = ItemDetailFragment.ARG_ITEM_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);


        getSupportActionBar().setTitle("Movie Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ItemDetailFragment.ARG_ITEM_ID, getIntent().getStringExtra(ItemDetailFragment.ARG_ITEM_ID));
            arguments.putString(ItemDetailFragment.ARG_ITEM_title, getIntent().getStringExtra(ItemDetailFragment.ARG_ITEM_title));
            arguments.putString(ItemDetailFragment.ARG_ITEM_img, getIntent().getStringExtra(ItemDetailFragment.ARG_ITEM_img));
            arguments.putString(ItemDetailFragment.ARG_ITEM_date, getIntent().getStringExtra(ItemDetailFragment.ARG_ITEM_date));
            arguments.putString(ItemDetailFragment.ARG_ITEM_rate, getIntent().getStringExtra(ItemDetailFragment.ARG_ITEM_rate));
            arguments.putString(ItemDetailFragment.ARG_ITEM_overview, getIntent().getStringExtra(ItemDetailFragment.ARG_ITEM_overview));
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.item_detail_container, fragment).commit();



        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details_menu, menu);

        return true;
    }
    //action bar area
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //share
        if(CheckNetwork.isInternetAvailable(ItemDetailActivity.this)) //returns true if internet available
        {
            if (id == R.id.menu_item_share) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                share.putExtra(Intent.EXTRA_SUBJECT, Titleforhead + " Movie Trailer !!");
                share.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=" + Trailers.share);

                startActivity(Intent.createChooser(share, "Share trailer!"));
            }
        }
        else
            Toast.makeText(ItemDetailActivity.this, "you can't share without internet connection..", Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }
}
