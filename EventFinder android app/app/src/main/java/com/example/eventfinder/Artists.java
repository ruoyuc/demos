package com.example.eventfinder;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;


//public class Artists extends Fragment {
//
//
//
//
//    private RecyclerView rvArtists;
//
//
//
//    public Artists() {
//        super(R.layout.fragment_artists);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        rvArtists = view.findViewById(R.id.rvArtists);
//        Intent intent = requireActivity().getIntent();
//        String event_id = "";
//        if(null != intent) {
//            Bundle bundle = intent.getBundleExtra("bundle");
//            if (null != bundle) {
//                 event_id = bundle.getString("id");
//            }
//        }
//
//        loadData(event_id);
//    }
//
////    @Override
////    public void onResume() {
////        super.onResume();
////        Intent intent = requireActivity().getIntent();
////        String event_id = "";
////        if(null != intent) {
////            Bundle bundle = intent.getBundleExtra("bundle");
////            if (null != bundle) {
////                event_id = bundle.getString("id");
////            }
////        }
////        loadData(event_id);
////    }
//
//    private void loadData(String id) {
//        String url = "https://hw9back-385702.wl.r.appspot.com/hw9?id="+id;
//        StringRequest myRequest = new StringRequest(url,
//                response -> {
//                    try {
//                        JSONArray myJsonArray = new JSONArray(response);
//                        ArtistAdapter adapter = new ArtistAdapter(myJsonArray,getContext());
//                        rvArtists.setAdapter(adapter);
//                        rvArtists.setLayoutManager(new LinearLayoutManager(getContext()));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                },
//                volleyError -> {
//                    Log.d("error", volleyError.getMessage());
//                }
//        );
//        RequestQueue volleyQueue = Volley.newRequestQueue(requireContext());
//        volleyQueue.add(myRequest);
//    }
//
//
//
//}

public class Artists extends Fragment {

    private RecyclerView rvArtists;
    private TextView noArtists;

    public Artists() {
        super(R.layout.fragment_artists);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvArtists = view.findViewById(R.id.rvArtists);
        noArtists = view.findViewById(R.id.noArtists);

        Intent intent = requireActivity().getIntent();
        String event_id = "";
        if (null != intent) {
            Bundle bundle = intent.getBundleExtra("bundle");
            if (null != bundle) {
                event_id = bundle.getString("id");
            }
        }

        loadData(event_id);
    }

    private void loadData(String id) {
        String url = "https://hw9back-385702.wl.r.appspot.com/hw9?id=" + id;
        StringRequest myRequest = new StringRequest(url,
                response -> {
                    try {
                        JSONArray myJsonArray = new JSONArray(response);

                        if (myJsonArray.length() == 0) {
                            noArtists.setVisibility(View.VISIBLE);
                            rvArtists.setVisibility(View.GONE);
                        } else {
                            noArtists.setVisibility(View.GONE);
                            rvArtists.setVisibility(View.VISIBLE);
                        }

                        ArtistAdapter adapter = new ArtistAdapter(myJsonArray, getContext());
                        rvArtists.setAdapter(adapter);
                        rvArtists.setLayoutManager(new LinearLayoutManager(getContext()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                volleyError -> {
                    Log.d("error", volleyError.getMessage());
                }
        );
        RequestQueue volleyQueue = Volley.newRequestQueue(requireContext());
        volleyQueue.add(myRequest);
    }
}