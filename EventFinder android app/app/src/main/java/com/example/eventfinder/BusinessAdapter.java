package com.example.eventfinder;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.SharedPreferences;
import android.widget.Toast;

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.ViewHolder> {

    private final JSONArray mBusinesses;
    private final Context mContext;
    private final OnItemClickListener mListener;


    public BusinessAdapter(Context context, JSONArray businesses, OnItemClickListener listener){
        mBusinesses = businesses;
        mContext = context;
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvEvent, tvVenue, tvGenre, tvLocal_Date, tvLocal_time;
        public ImageView ivImg;

        public ImageButton heartButton;
        public ViewHolder(View itemView) {
            super(itemView);
            tvEvent = itemView.findViewById(R.id.Event);
            tvEvent.post(() -> {tvEvent.setSelected(true);});
            tvVenue = itemView.findViewById(R.id.Venue);
            tvVenue.post(() -> {tvVenue.setSelected(true);});
            tvGenre = itemView.findViewById(R.id.Genre);
            tvLocal_Date = itemView.findViewById(R.id.Local_date);
            tvLocal_time = itemView.findViewById(R.id.Local_time);
            ivImg = itemView.findViewById(R.id.imageView);
            heartButton = itemView.findViewById(R.id.heartButton);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View businessView = inflater.inflate(R.layout.event_item, parent, false);
        return new ViewHolder(businessView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            JSONObject events = mBusinesses.getJSONObject(position);
            holder.tvEvent.setText(events.getString("Event"));
            holder.tvVenue.setText(events.getString("Venue"));
            holder.tvGenre.setText(events.getString("Genre"));
            holder.tvLocal_Date.setText(convertDateFormat(events.getString("Local_date")));
            holder.tvLocal_time.setText(convertTimeFormat(events.getString("Local_time")));
            Glide.with(mContext).load(events.getString("Icon")).into(holder.ivImg);
            boolean isKeyPresent = isKeyPresentInSharedPreferences(mContext, events.getString("Id"));

            holder.heartButton.setSelected(isKeyPresent);

            holder.heartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isSelected = holder.heartButton.isSelected();
                    holder.heartButton.setSelected(!isSelected);
                    //Set the favorite list here
                    if (!isSelected) {
                        try {
                            String id = events.getString("Id");
                            String name = events.getString("Event");

                            saveJsonObjectToSharedPreferences(events, id);
                            Toast.makeText(mContext, name+"is Added to Favorites", Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            String id = events.getString("Id");
                            String name = events.getString("Event");
                            removeJsonObjectFromSharedPreferences(id);
                            Toast.makeText(mContext, name+"is Removed from Favorites", Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onClick(holder.getBindingAdapterPosition());
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mBusinesses.length();
    }

    public interface OnItemClickListener {
        void onClick(int pos);
    }

    public static String convertTimeFormat(String inputTime) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm a");

        try {
            Date date = inputFormat.parse(inputTime);
            assert date != null;
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Invalid time format";
        }
    }

    public static String convertDateFormat(String inputDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inputFormat.parse(inputDate);

            SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveJsonObjectToSharedPreferences(JSONObject events, String id) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(id, events.toString());
        editor.apply();
    }

    private void removeJsonObjectFromSharedPreferences(String id) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(id);
        editor.apply();
    }


    public boolean isKeyPresentInSharedPreferences(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE);
        return sharedPreferences.contains(key);
    }

}
