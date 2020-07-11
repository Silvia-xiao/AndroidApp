package com.example.androidass.fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidass.R;
import com.example.androidass.Utility.GetMovieImage;
import com.example.androidass.model.Cinema;
import com.example.androidass.model.Person;
import com.example.androidass.networkconnection.NetworkConnection;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap gMap;
    private Geocoder geocoder;
    private Address local = new Address(Locale.CANADA);
    private Person person;
    private NetworkConnection networkConnection=null;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        person = getActivity().getIntent().getExtras().getParcelable("personHome");
        geocoder = new Geocoder(getContext(),new Locale("AU"));
        networkConnection = new NetworkConnection();
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);


        return view;
    }

    /**
     * set map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        new GetHome().execute();
        new GetCinema().execute();
    }

    class GetHome extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                //get the log in user address
                local = geocoder.getFromLocationName((person.getAddress() + " " + person.getPostcode()),1).get(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            super.onPostExecute(v);
            //get latitude and longitude of the address
            double latitude = local.getLatitude();
            double longitude = local.getLongitude();
            LatLng lalon = new LatLng(latitude,longitude);
            //add marker to this address
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title("Home");
            markerOptions.position(lalon);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            gMap.addMarker(markerOptions);
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lalon,6f));

        }
    }

    class GetCinema extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String result = networkConnection.getData("cinema");
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String str){
            super.onPostExecute(str);
            Gson gson = new Gson();
            List<Cinema> cinemas = new ArrayList<>();
            List<String> locations = new ArrayList<>();
            //get the locations
            try{
                JSONArray jsonArray = new JSONArray(str);
                for(int i = 0;i < jsonArray.length(); i++) {
                    cinemas.add((gson.fromJson(jsonArray.getJSONObject(i).toString(),Cinema.class)));
                    locations.add(cinemas.get(i).getLocation());
                }
                for(int i = 0; i < locations.size(); i++){
                    //Cinema cinema = gson.fromJson(jsonArray.getJSONObject(i).toString(),Cinema.class);
                    Address locationCinema = geocoder.getFromLocationName(locations.get(i),1).get(0);
                    LatLng latLng = new LatLng(locationCinema.getLatitude(),locationCinema.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.title(cinemas.get(i).getCinemaName());
                    markerOptions.position(latLng);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    gMap.addMarker(markerOptions);
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }
}
