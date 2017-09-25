package com.renasoft.popmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements MovieItemListener {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MainActivityFragment mainFragment = new MainActivityFragment();
        if (null == savedInstanceState) {
            mainFragment = new MainActivityFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.movies_container, mainFragment, "DetailsFragment").commit();
        } else {
            mainFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentByTag("DetailsFragment");
        }

        mainFragment.setMovieItemListener(this);

        FrameLayout details_container = (FrameLayout) findViewById(R.id.details_container);
        mTwoPane = null != details_container;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_refresh:
                finish();
                startActivity(getIntent());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public void setMovieDetails(Bundle details) {
        if (!mTwoPane) {
            Intent detailsIntent = new Intent(this, MovieDetailsActivity.class);
            detailsIntent.putExtras(details);
            startActivity(detailsIntent);
        } else {
            MovieDetailsFragment detailsFragment = new MovieDetailsFragment();
            detailsFragment.setArguments(details);
            getSupportFragmentManager().beginTransaction().replace(R.id.details_container, detailsFragment).commit();
        }
    }
}
