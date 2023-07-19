package com.example.eventfinder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;


public class EventDetails extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private TextView event_name, event_time, event_date, event_venue, event_category, event_price, event_status, event_buy;

    private String event_id;




    public EventDetails() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_details, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        event_name = view.findViewById(R.id.artistNamevalue);
        event_name.post(() -> {event_name.setSelected(true);});
        event_time = view.findViewById(R.id.Timevalue);
        event_date = view.findViewById(R.id.Datevalue);
        event_venue = view.findViewById(R.id.Venuevalue);
        event_venue.post(() -> {event_venue.setSelected(true);});
        event_category = view.findViewById(R.id.Genrevalue);
        event_category.post(() -> {event_category.setSelected(true);});
        event_price = view.findViewById(R.id.Pricevalue);
        event_status = view.findViewById(R.id.Statusvalue);
        event_buy = view.findViewById(R.id.BuyTicketvalue);
        event_buy.post(() -> {event_buy.setSelected(true);});


        Intent intent = getActivity().getIntent();
        if(null != intent){
            Bundle bundle = intent.getBundleExtra("bundle");
            if(null != bundle){
                event_id = bundle.getString("id");
                String url ="https://hw9back-385702.wl.r.appspot.com/event?id="+event_id;
                RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    // Assuming the JSON response is an object with key-value pairs
                                    event_date.setText(response.getString("Date"));
                                    event_time.setText(response.getString("Time"));
                                    event_name.setText(response.getString("Artist"));
                                    event_venue.setText(response.getString("Venue"));
                                    event_category.setText(response.getString("Genre"));
                                    event_status.setText(response.getString("Status"));
                                    String x = response.getString("Link");
                                    event_buy.setText(x);
                                    event_buy.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent detail = new Intent(Intent.ACTION_VIEW, Uri.parse(x));
                                            detail.setPackage("org.mozilla.firefox");
                                            startActivity(detail);
                                        }
                                    });
                                    Glide.with(requireContext()).load(response.getString("SeatMap")).into((android.widget.ImageView) view.findViewById(R.id.imagedetails));
                                    event_price.setText(response.getString("Price"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

                requestQueue.add(jsonObjectRequest);
            }
        }


//        Bundle bundle = this.getArguments();
//        event_date.setText(bundle.getString("id"));
//        if (bundle != null) {
//            event_id = bundle.getString("id");
//        }





//        table = view.findViewById(R.id.table);
//        fetchTableData(requireContext());
    }




}