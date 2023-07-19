package com.example.eventfinder;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Recycle_view_Fragment extends Fragment {

    private SharedViewModel sharedViewModel;
    private RecyclerView rvBusiness;
    private TextView noEvents;

    public Recycle_view_Fragment() {
        super(R.layout.fragment_recycle_view_);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        rvBusiness = view.findViewById(R.id.rvBusiness);
        noEvents = view.findViewById(R.id.noEvents);
        Button btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Recycle_view_Fragment.this).navigate(R.id.action_recycle_view_Fragment_to_searchFragment);
            }
        });

        loadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

//    private void loadData() {
//        String url = sharedViewModel.getText().toString();
//        StringRequest myRequest = new StringRequest(url,
//                response -> {
//                    try {
//                        if(response.length() == 0){
//                            noEvents.setVisibility(View.VISIBLE);
////                            rvBusiness.setVisibility(View.GONE);
//                            return;
//                        }else {
//                            noEvents.setVisibility(View.GONE);
//                            //rvBusiness.setVisibility(View.VISIBLE);
//                        }
//
////                        if(response.length()!=0){
////                            noEvents.setVisibility(View.GONE);
////                            rvBusiness.setVisibility(View.VISIBLE);
////                        }
//
//                        JSONArray myJsonArray = new JSONArray(response);
//                        BusinessAdapter adapter = new BusinessAdapter(getContext(), myJsonArray, new BusinessAdapter.OnItemClickListener() {
//                            @Override
//                            public void onClick(int pos) {
//                                try {
//
//                                    JSONObject object = myJsonArray.getJSONObject(pos);
//                                    //Set to another attribute here
//                                    String id = object.getString("Id");
//                                    String venue = object.getString("Venue");
//                                    String url = object.getString("Url");
//                                    //Toast.makeText(getContext(), venue, Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(getContext(), DetailActivity.class);
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("id", id);
//                                    bundle.putString("venue", venue);
//                                    bundle.putString("url", url);
//                                    bundle.putString("event", object.getString("Event"));
//                                    bundle.putString("object",object.toString());
//                                    intent.putExtra("bundle",bundle);
//                                    startActivity(intent);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//                        rvBusiness.setAdapter(adapter);
//                        rvBusiness.setLayoutManager(new LinearLayoutManager(getContext()));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                },
//                volleyError -> {
//                    Log.d("error", volleyError.getMessage());
//                }
//        );
//        RequestQueue volleyQueue = Volley.newRequestQueue(getContext());
//        volleyQueue.add(myRequest);
//    }


    private void loadData() {
        String url = sharedViewModel.getText().toString();
        StringRequest myRequest = new StringRequest(url,
                response -> {
                    try {
                        JSONArray myJsonArray = new JSONArray(response);
                        if (myJsonArray.length() == 0) {
                            noEvents.setVisibility(View.VISIBLE);
                            rvBusiness.setVisibility(View.GONE);
                        } else {
                            noEvents.setVisibility(View.GONE);
                            rvBusiness.setVisibility(View.VISIBLE);

                            BusinessAdapter adapter = new BusinessAdapter(getContext(), myJsonArray, pos -> {
                                try {
                                    JSONObject object = myJsonArray.getJSONObject(pos);
                                    String id = object.getString("Id");
                                    String venue = object.getString("Venue");
                                    String link = object.getString("Url");

                                    Intent intent = new Intent(getContext(), DetailActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("id", id);
                                    bundle.putString("venue", venue);
                                    bundle.putString("url", link);
                                    bundle.putString("event", object.getString("Event"));
                                    bundle.putString("object", object.toString());
                                    intent.putExtra("bundle", bundle);
                                    startActivity(intent);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            });
                            rvBusiness.setAdapter(adapter);
                            rvBusiness.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
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
