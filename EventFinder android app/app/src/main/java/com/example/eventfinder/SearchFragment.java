package com.example.eventfinder;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import androidx.lifecycle.ViewModelProvider;




public class SearchFragment extends Fragment {



    private SharedViewModel sharedViewModel;
    private Spinner spCategory;
    private Button mBtnSubmit, mBtnClear;
    private EditText etDistance, etLocation;
    private AutoCompleteTextView etKeyword;
    private Switch swLocation;
    private String lat, lgt;

    private String category ="";


    public SearchFragment() {
        // Required empty public constructor
        //super(R.layout.fragment_first_child);
    }


    private boolean checkInput() {

        if(etKeyword.length() == 0||etDistance.length() == 0||etDistance.length() == 0){
            //Toast.makeText(getContext(), "Please fill all fields.", Toast.LENGTH_SHORT).show();
            Snackbar.make(getView(), "Please fill all fields.", Snackbar.LENGTH_LONG).show();
            return false;
        }

        if(!swLocation.isChecked()){
            if(etLocation.length() == 0){
                //Toast.makeText(getContext(), "Please fill all fields.", Toast.LENGTH_SHORT).show();
                Snackbar.make(getView(), "Please fill all fields.", Snackbar.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    private void getLocByAuto() {
        String url = "https://ipinfo.io/json?token=4992363e5c1e3c";
        StringRequest myRequest = new StringRequest(url,
                response -> {
                    try {
                        JSONObject myJsonObject = new JSONObject(response);
                        String[] loc = myJsonObject.getString("loc").split(",");
                        lat = loc[0];
                        lgt = loc[1];

                        getRecycleView();
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                volleyError -> {
                    Log.d("error", volleyError.getMessage());
                }
        );
        RequestQueue volleyQueue = Volley.newRequestQueue(this.getContext());
        volleyQueue.add(myRequest);

    }

    private void getLocByInput() {
        String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=AIzaSyBnVligqwXbQJORHbdxYjg40sBXew7Q23Q", etLocation.getText().toString());
        StringRequest myRequest = new StringRequest(url,
                response -> {
                    try {
                        JSONObject myJsonObject = new JSONObject(response);
                        double nlat = myJsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                        double nlgt = myJsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                        lat = Double.toString(nlat);
                        lgt = Double.toString(nlgt);

                        getRecycleView();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                volleyError -> {
                    Log.d("error", volleyError.getMessage());
                }
        );
        RequestQueue volleyQueue = Volley.newRequestQueue(this.getContext());
        volleyQueue.add(myRequest);
    }

    private void getRecycleView(){
        //spCategory.getSelectedItem().toString(),
        //String url = String.format("https://hw8-382323.wl.r.appspot.com/searchEvent?Keyword=%s&Location=%s,%s&Distance=%s&Category=", etKeyword.getText().toString(), lat, lgt, etDistance.getText().toString());
        String url = String.format("https://hw9back-385702.wl.r.appspot.com/searchEvent?Keyword=%s&Location=%s,%s&Distance=%s&Category=%s", etKeyword.getText().toString(), lat, lgt, etDistance.getText().toString(),category);
        category = "";
        //Log.d("url", url);
        //SearchFragmentDirections.ActionSearchFragmentToSearchResultFragment action = SearchFragmentDirections.actionSearchFragmentToSearchResultFragment(url);
        //NavHostFragment.findNavController(SearchFragment.this).navigate(SearchFragmentDirections.actionSearchFragmentToSearchResultFragment(url));
        sharedViewModel.setText(url);
        NavHostFragment.findNavController(this).navigate(R.id.action_searchFragment_to_recycle_view_Fragment);
    }







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_search, container, false);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        spCategory = view.findViewById(R.id.category);
        mBtnSubmit = view.findViewById(R.id.btn_search);
        mBtnClear = view.findViewById(R.id.btn_clear);
        etDistance = view.findViewById(R.id.distance);
        etDistance.setText("10");
        etLocation = view.findViewById(R.id.location);
        etKeyword = view.findViewById(R.id.keyword);
        swLocation = view.findViewById(R.id.switch1);





        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);


        //Clear Button
        mBtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etKeyword.setText("");
                etDistance.setText("");
                etLocation.setText("");
                spCategory.setSelection(0);
                category = "";
                swLocation.setChecked(false);
                etLocation.setEnabled(true);
                etLocation.setVisibility(View.VISIBLE);
            }
        });

        //Switch
        swLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!swLocation.isChecked()) {
                    etLocation.setEnabled(true);
                    etLocation.setVisibility(View.VISIBLE);
                    etLocation.setText("");

                } else {
                    etLocation.setEnabled(false);
                    etLocation.setVisibility(View.GONE);
                    etLocation.setText("");
                }
                //Log.d(String.valueOf(swLocation), "onClick() returned: " + swLocation.isChecked());
            }
        });

        //AutoCompleteTextView
        etKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length() == 0) return;
                //String url_autocomplete =  "https://hw8-382323.wl.r.appspot.com/autocomplete?text=" + charSequence.toString();
                String url_autocomplete =  "https://hw9back-385702.wl.r.appspot.com/autocomplete?text=" + charSequence.toString();
                StringRequest myRequest = new StringRequest(url_autocomplete,
                        response -> {
                            try {
                                JSONArray myJsonArray = new JSONArray(response);
                                List<String> myList = new ArrayList<String>();
                                for(int j = 0; j < myJsonArray.length(); j++){
                                    myList.add(myJsonArray.getString(j));
                                }
                                int size = myList.size();
                                String[] myStringArray = myList.toArray(new String[size]);
                                ArrayAdapter<String> autoAdapter = new ArrayAdapter<String>(
                                        requireActivity().getApplicationContext(),
                                        R.layout.spinner_item, myStringArray);

                                etKeyword.setThreshold(1);
                                etKeyword.setAdapter(autoAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        volleyError -> {
                            Log.d("error", volleyError.getMessage());
                        }
                );
                RequestQueue volleyQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());
                volleyQueue.add(myRequest);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //Submit Button
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean allChecked = checkInput();
                if(allChecked) {
                    if(swLocation.isChecked()){
                        Log.d("auto", "auto");
                        getLocByAuto();
                    }else{
                        Log.d("test", "input");
                        getLocByInput();
                    }
                }

            }
        });


//
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//                getActivity(), R.array.spinner_options, R.layout.spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spCategory.setAdapter(adapter);
        String[] spinneroptions = new String[]{
                "All",
                "Music",
                "Sports",
                "Arts & Theatre",
                "Film",
                "Miscellaneous"
        };
        Map<String, String> dictionary = new HashMap<>();
        dictionary.put("All", "");
        dictionary.put("Music", "KZFzniwnSyZfZ7v7nJ");
        dictionary.put("Sports", "KZFzniwnSyZfZ7v7nE");
        dictionary.put("Arts & Theatre", "KZFzniwnSyZfZ7v7na");
        dictionary.put("Film", "KZFzniwnSyZfZ7v7nn");
        dictionary.put("Miscellaneous", "KZFzniwnSyZfZ7v7n1");


        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                getActivity(), R.layout.spinner_item, spinneroptions);
        spCategory.setAdapter(spinnerAdapter);
        spCategory.setSelection(0);
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = spinneroptions[position];
                // Perform your action based on the selected item
                category = dictionary.get(selectedItem);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }
}