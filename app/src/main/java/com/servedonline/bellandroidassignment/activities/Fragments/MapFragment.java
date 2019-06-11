package com.servedonline.bellandroidassignment.activities.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.servedonline.bellandroidassignment.R;
import com.servedonline.bellandroidassignment.activities.Activity.MainActivity;
import com.servedonline.bellandroidassignment.activities.Entity.Status;
import com.servedonline.bellandroidassignment.activities.Network.GsonRequest;
import com.servedonline.bellandroidassignment.activities.Network.JSON.StatusResponse;

import java.util.ArrayList;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    public static final String BACKSTACK_TAG = "_mapFragment";
    private MapView mapView;
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private Location location;
    private LatLng position;
    private Criteria criteria;
    private String provider;

    private ArrayList<Status> items = new ArrayList<>();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    //todo manage what to do without location permission...
                }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = getLayoutInflater().inflate(R.layout.fragment_map, container, false);
        mapView = v.findViewById(R.id.mapView);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Log.d("MAP", "map before getMapAsync");

        ((MainActivity) getActivity()).showBlocker();
        ((MainActivity) getActivity()).showFab();

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);


    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        Log.d("MAP", "onMapReady");

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((MainActivity) getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {

            googleMap.setMyLocationEnabled(true);

            criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, true);
            location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                ((MainActivity) getActivity()).hideBlocker();

                double latitude = location.getLatitude();

                double longitude = location.getLongitude();

                Log.d("MAP", "User location = " + String.valueOf(latitude) + ", " + String.valueOf(longitude));

                position = new LatLng(latitude, longitude);

//                googleMap.addMarker(new MarkerOptions().position(position).title("Your Location"));
//                googleMap.setMinZoomPreference(12);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));

                getTweets();
            }
        }


    }

    private void getTweets() {
        String url = ((MainActivity)getActivity()).getBaseUrl() +"?geocode=" + String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()) + ",5km" + "&count=100";
//        String url = ((MainActivity) getActivity()).getBaseUrl() + "?geocode=45.516136,-73.656830,5km&count=100"; //montreal test...

        CommonsHttpOAuthConsumer commonsHttpOAuthConsumer = new CommonsHttpOAuthConsumer(((MainActivity)getActivity()).getConsumerKey(), ((MainActivity) getActivity()).getConsumerSecret());

        try {
            url = commonsHttpOAuthConsumer.sign(url);
            Log.d("VOLLEY", "URL = " + url);
        } catch (OAuthExpectationFailedException e) {
            Log.d("VOLLEY", "OAuthExpectationFailedException");
            e.printStackTrace();
        } catch (OAuthMessageSignerException e) {
            Log.d("VOLLEY", "OAuthMessageSignerException");
            e.printStackTrace();
        } catch (OAuthCommunicationException e) {
            Log.d("VOLLEY", "OAuthCommunicationException");
            e.printStackTrace();
        }

        GsonRequest gsonRequest = new GsonRequest(url, StatusResponse.class, null, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                StatusResponse statusResponse = (StatusResponse) response;
                if (statusResponse.getStatuses().length > 0) {
                    Log.d("VOLLEY", String.valueOf(statusResponse.getStatuses().length));
                } else {
                    Log.d("VOLLEY", "no length");
                }

                items.clear();

                for (Status status: statusResponse.getStatuses()) {
                    if (status.getCoordinates() != null && status.getCoordinates().getCoordinates().length > 0) {
                        items.add(status);
                        double latitude = (status.getCoordinates().getCoordinates()[1]);
                        double longitude = (status.getCoordinates().getCoordinates()[0]);
                        Log.d("MAP", "add marker at: " + String.valueOf(latitude) + ", " + String.valueOf(longitude));

                        LatLng pos = new LatLng(latitude, longitude);

                        googleMap.addMarker(new MarkerOptions().position(pos));
                    }

                }

                MarkerInfoWindowAdapter markerInfoWindowAdapter = new MarkerInfoWindowAdapter(getContext());
                googleMap.setInfoWindowAdapter(markerInfoWindowAdapter);
                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {

                        LatLng latLng = marker.getPosition();

                        if (items.size() > 0) {
                            for (final Status status : items) {
                                double latitude = (status.getCoordinates().getCoordinates()[1]);
                                double longitude = (status.getCoordinates().getCoordinates()[0]);
                                if (latLng.latitude == latitude && latLng.longitude == longitude) {
                                    Bundle args = new Bundle();
                                    args.putParcelable(ViewTweetFragment.STATUS_KEY, status);
                                    ViewTweetFragment fragment = new ViewTweetFragment();
                                    fragment.setArguments(args);
                                    ((MainActivity) getActivity()).navigate(fragment, BACKSTACK_TAG);
                                }
                            }
                        }


                    }
                });


                if (items.size() == 0) {
                    Log.d("MAP", "No tweets with coordinates data found...");
                    Toast.makeText(getContext(), "No tweets had coordinates in 5km of your location", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VOLLEY", "ERROR: " + error);

                Toast.makeText(getContext(), "Something Went Wrong...", Toast.LENGTH_SHORT).show();
            }
        });

        ((MainActivity) getActivity()).getRequestQueue().add(gsonRequest);
    }

    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private Context context;
        public MarkerInfoWindowAdapter(Context context) {
//            this.context = getActivity().getApplicationContext();
        }

        @Override
        public View getInfoWindow(Marker arg0) {
            return null;
        }

        @Override
        public View getInfoContents(Marker arg0) {
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v =  getLayoutInflater().inflate(R.layout.map_marker, null);
            TextView tvUserName = (TextView) v.findViewById(R.id.tvUserName);
            TextView tvContent = (TextView) v.findViewById(R.id.tvTweetContent);
            LatLng latLng = arg0.getPosition();

            if (items.size() > 0) {
                for (final Status status : items) {
                    double latitude = (status.getCoordinates().getCoordinates()[1]);
                    double longitude = (status.getCoordinates().getCoordinates()[0]);
                    if (latLng.latitude == latitude && latLng.longitude == longitude) {
                        tvUserName.setText(status.getUser().getName());
                        tvContent.setText(status.getText());

                    }
                }
            }

            return v;
        }


    }
}
