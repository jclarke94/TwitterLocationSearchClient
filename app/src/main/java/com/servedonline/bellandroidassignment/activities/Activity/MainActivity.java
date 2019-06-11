package com.servedonline.bellandroidassignment.activities.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.servedonline.bellandroidassignment.R;
import com.servedonline.bellandroidassignment.activities.Fragments.MapFragment;
import com.servedonline.bellandroidassignment.activities.Fragments.SearchFragment;
import com.servedonline.bellandroidassignment.activities.Network.GsonRequest;
import com.servedonline.bellandroidassignment.activities.Network.JSON.StatusResponse;

import org.json.JSONObject;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

public class MainActivity extends FragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String CONSUMER_KEY = "8bA6poqMpiYVL5iFWU9HIyrVf";
    public static final String CONSUMER_SECRET = "GP9SsA3XY7DKMOltV8V3UEGBLtpb7BqGisnAjFRPhREwwS5gbh";
    public static final String ACCESS_TOKEN = "2822032122-D5tDNKUsWeHlMUUoCjypPN454owklC0ptYMurt6";
    public static final String ACCESS_SECRET = "bld43rmj6JOm9KZiSa46jpxBT9HpRWwr5FAoOIpRXmaRR";
    public static final String NONCE = "chgc40xwGb7";

    public static final String BASE_URL = "https://api.twitter.com/1.1/search/tweets.json";

    private FrameLayout flBlocker;
    private FloatingActionButton fab;

    public RequestQueue requestQueue;
    public Network network;
    public Cache cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        flBlocker = (FrameLayout) findViewById(R.id.flBlocker);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
                SearchFragment searchFragment = new SearchFragment();
                navigate(searchFragment, MapFragment.BACKSTACK_TAG);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }
        }

        // Instantiate the cache
        cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();


        MapFragment mapFragment = new MapFragment();
        mapFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mapFragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.mapFragmentBtn) {
            MapFragment mapFragment = new MapFragment();
            voidBackstackAndNavigate(null, mapFragment, null);

        } else if (id == R.id.searchFragmentBtn) {
            SearchFragment searchFragment = new SearchFragment();
            voidBackstackAndNavigate(MapFragment.BACKSTACK_TAG, searchFragment, MapFragment.BACKSTACK_TAG);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public String getBaseUrl() {
        return BASE_URL;
    }

    public String getConsumerKey() {
        return CONSUMER_KEY;
    }

    public String getConsumerSecret() {
        return CONSUMER_SECRET;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public Network getNetwork() {
        return network;
    }

    public Cache getCache() {
        return cache;
    }

    public void showBlocker() {
        flBlocker.setVisibility(View.VISIBLE);
    }

    public void hideBlocker() {
        flBlocker.setVisibility(View.GONE);
    }

    @SuppressLint("RestrictedApi")
    public void showFab() { fab.setVisibility(View.VISIBLE); }

    @SuppressLint("RestrictedApi")
    public void hideFab() { fab.setVisibility(View.GONE); }

    /** Fragment control
     * Navigate between fragments within the fragment container
     * @param fragment
     * @param backstackTag
     */
    public void navigate(Fragment fragment, String backstackTag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        if(backstackTag != null) {
            fragmentTransaction.addToBackStack(backstackTag);
        }

        fragmentTransaction.commit();
    }

    /**
     * Removes Fragments in the Stack until we reach the desired Backstack tag
     * @param backUntilTag
     */
    public void voidBackstack(@Nullable String backUntilTag) {
        boolean hasBackstackEntry = false;
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            if (getSupportFragmentManager().getBackStackEntryAt(i).getName().equals(backUntilTag)) {
                hasBackstackEntry = true;
                break;
            }
        }

        if (!hasBackstackEntry) {
            backUntilTag = null;
        }
        getSupportFragmentManager().popBackStackImmediate(backUntilTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    /**
     * Convenience method for moving back through the app backstack hierarchy and immediately presenting a new screen afterwards
     *
     * Much safer than trying to do this in a Fragment instance itself as this all occurs within something that's not as volatile
     *
     * @param backUntilTag      Backstack tag to reverse transactions until
     * @param fragment          Fragment to navigate to after backstack void
     * @param backstackTag      Backstack tag to give to the new Fragment transaction
     */
    public void voidBackstackAndNavigate(@Nullable String backUntilTag, @NonNull Fragment fragment, @Nullable String backstackTag) {
        voidBackstack(backUntilTag);

        navigate(fragment, backstackTag);
    }

}
