package com.example.eventfinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import android.content.SharedPreferences.OnSharedPreferenceChangeListener;


//public class FavoritesFragment extends Fragment {
//
//
//    public FavoritesFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View favoritesFragmentLayout = inflater.inflate(R.layout.fragment_favorites, container, false);
//        return favoritesFragmentLayout;
//    }
//
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        androidx.recyclerview.widget.RecyclerView rvFavorite = view.findViewById(R.id.rvFavorites);
//        rvFavorite.setLayoutManager(new LinearLayoutManager(getContext()));
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        JSONArray myJsonArray = getAllJsonObjectsFromSharedPreferences(requireContext());
//        // This is the place to turn the shared preferences into a JSONArray
//        if (myJsonArray.length() == 0) {
//           requireView().findViewById(R.id.noFavorites).setVisibility(View.VISIBLE);
//           requireView().findViewById(R.id.rvFavorites).setVisibility(View.GONE);
//        } else {
//            requireView().findViewById(R.id.noFavorites).setVisibility(View.GONE);
//            requireView().findViewById(R.id.rvFavorites).setVisibility(View.VISIBLE);
//        }
//        BusinessAdapter adapter = new BusinessAdapter(getContext(), myJsonArray, new BusinessAdapter.OnItemClickListener() {
//            @Override
//            public void onClick(int pos) {
//                try {
//                    JSONObject object = myJsonArray.getJSONObject(pos);
//                    //Set to another attribute here
//                    String id = object.getString("Id");
//                    String venue = object.getString("Venue");
//                    String url = object.getString("Url");
//                    //Toast.makeText(getContext(), venue, Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getContext(), DetailActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("id", id);
//                    bundle.putString("venue", venue);
//                    bundle.putString("object",object.toString());
//                    bundle.putString("url", url);
//                    bundle.putString("event", object.getString("Event"));
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        androidx.recyclerview.widget.RecyclerView rvFavorite = requireView().findViewById(R.id.rvFavorites);
//        rvFavorite.setAdapter(adapter);
//    }
//
//
//
//
//    private JSONArray getAllJsonObjectsFromSharedPreferences(Context context) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE);
//        Map<String, ?> allEntries = sharedPreferences.getAll();
//
//        JSONArray jsonArray = new JSONArray();
//        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
//            try {
//                JSONObject jsonObject = new JSONObject(entry.getValue().toString());
//                jsonArray.put(jsonObject);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return jsonArray;
//    }
//
//
//}


public class FavoritesFragment extends Fragment implements OnSharedPreferenceChangeListener {

    private SharedPreferences sharedPreferences;
    private RecyclerView rvFavorite;
    private BusinessAdapter adapter;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View favoritesFragmentLayout = inflater.inflate(R.layout.fragment_favorites, container, false);
        return favoritesFragmentLayout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvFavorite = view.findViewById(R.id.rvFavorites);
        rvFavorite.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences = requireActivity().getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        updateUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (sharedPreferences != null) {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        }
    }

    private void updateUI() {
        JSONArray myJsonArray = getAllJsonObjectsFromSharedPreferences(requireContext());
        if (myJsonArray.length() == 0) {
            requireView().findViewById(R.id.noFavorites).setVisibility(View.VISIBLE);
            requireView().findViewById(R.id.rvFavorites).setVisibility(View.GONE);
        } else {
            requireView().findViewById(R.id.noFavorites).setVisibility(View.GONE);
            requireView().findViewById(R.id.rvFavorites).setVisibility(View.VISIBLE);
        }
        adapter = new BusinessAdapter(getContext(), myJsonArray, new BusinessAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
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
            }
        });

        rvFavorite.setAdapter(adapter);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateUI();
    }

    private JSONArray getAllJsonObjectsFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();

        JSONArray jsonArray = new JSONArray();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            try {
                JSONObject jsonObject = new JSONObject(entry.getValue().toString());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return jsonArray;
    }

}

