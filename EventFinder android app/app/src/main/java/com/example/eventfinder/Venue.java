package com.example.eventfinder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class Venue extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;

    private String event_venue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_venue, container, false);

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Intent intent = requireActivity().getIntent();
        if(null != intent) {
            Bundle bundle = intent.getBundleExtra("bundle");
            if (null != bundle) {
                event_venue = bundle.getString("venue");
            }
        }

        boolean[] isTextExpanded = {false, false, false};
        TextView openHour = view.findViewById(R.id.expandable_textview1);
        TextView gRules = view.findViewById(R.id.expandable_textview2);
        TextView cRules = view.findViewById(R.id.expandable_textview3);
        TextView name = view.findViewById(R.id.nameValue);
        name.post(() -> {name.setSelected(true);});
        TextView address = view.findViewById(R.id.addressValue);
        address.post(() -> {address.setSelected(true);});
        TextView city = view.findViewById(R.id.cityStateValue);
        city.post(() -> {city.setSelected(true);});
        TextView contactinfo = view.findViewById(R.id.contactInfoValue);
        contactinfo.post(() -> {contactinfo.setSelected(true);});
        toggleTextView(openHour, isTextExpanded, 0);
        toggleTextView(gRules, isTextExpanded, 1);
        toggleTextView(cRules, isTextExpanded, 2);


        if(event_venue != null){
            String url = "https://hw9back-385702.wl.r.appspot.com/venue?name="+event_venue;
            RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                double latitude = response.getDouble("Latitude");
                                double longitude = response.getDouble("Longitude");
                                openHour.setText(response.getString("Open"));
                                cRules.setText(response.getString("Crule"));
                                gRules.setText(response.getString("Grule"));
                                name.setText(response.getString("Name"));
                                address.setText(response.getString("Address"));
                                city.setText(response.getString("City"));
                                contactinfo.setText(response.getString("Phone"));
                                // Update the map with the fetched latitude and longitude
                                if (googleMap != null) {
                                    updateMap(latitude, longitude);
                                }
                            } catch (JSONException e) {
                                Log.e("VenueFragment", "Error parsing JSON response", e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VenueFragment", "Error fetching latitude and longitude", error);
                        }
                    });

            requestQueue.add(jsonObjectRequest);
        }

    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        MapsInitializer.initialize(requireActivity());

        // Set the latitude and longitude for the red pointer
        double latitude = 0;
        double longitude = 0;
        LatLng location = new LatLng(latitude, longitude);

        // Add a red pointer to the map
        googleMap.addMarker(new MarkerOptions().position(location).title("Marker Title"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));

        // Customize the map as needed, e.g., enable zoom controls, set the map type, etc.
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void toggleTextView(TextView textView, boolean[] isExpanded, int index) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded[index]) {
                    textView.setMaxLines(3);
                } else {
                    textView.setMaxLines(Integer.MAX_VALUE);
                }
                isExpanded[index] = !isExpanded[index];
            }
        });
    }




    private void updateMap(double latitude, double longitude) {
        LatLng location = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(location).title("Marker Title"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }

}