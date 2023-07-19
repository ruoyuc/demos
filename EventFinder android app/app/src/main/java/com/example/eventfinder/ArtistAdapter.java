package com.example.eventfinder;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;




public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {

    private final JSONArray mArtists;

    private final Context mContext;

    public ArtistAdapter(JSONArray mArtists, Context mContext) {
        this.mArtists = mArtists;
        this.mContext = mContext;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView artistName, artistFollowers, artistPopularity,artistLink;
        public ImageView ivImg, ivImg1, ivImg2, ivImg3;

        private PercentageRingView percentageRingView;
        public ViewHolder(View itemView) {
            super(itemView);
            artistName = itemView.findViewById(R.id.artistName);
            artistFollowers = itemView.findViewById(R.id.artistFollowers);
            artistLink = itemView.findViewById(R.id.artistLink);
            artistName.post(() -> {artistName.setSelected(true);});
            ivImg = itemView.findViewById(R.id.artistImage);
            ivImg1 = itemView.findViewById(R.id.artistImage1);
            ivImg2 = itemView.findViewById(R.id.artistImage2);
            ivImg3 = itemView.findViewById(R.id.artistImage3);
            percentageRingView = itemView.findViewById(R.id.percentageRingView);

        }
    }


    @NonNull
    @Override
    public ArtistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View artistView = inflater.inflate(R.layout.artists_item, parent, false);

        return new ViewHolder(artistView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistAdapter.ViewHolder holder, int position) {

        try {
            JSONObject events = mArtists.getJSONObject(position);
            ArrayList<String> albums = jsonArrayToArrayList(events.getJSONArray("albums"));
            holder.artistName.setText(events.getString("name"));
            holder.artistFollowers.setText(events.getString("followers"));
            String link = events.getString("spotifyLink");
            String popularity = events.getString("popularity");
            holder.percentageRingView.setPercentage(Integer.parseInt(popularity));
            //holder.artistPopularity.setText(events.getString("Local_date"));
            Glide.with(mContext).load(events.getString("logo")) .error(R.drawable.twitter).into(holder.ivImg);
            Glide.with(mContext).load(albums.get(0)) .error(R.drawable.twitter).into(holder.ivImg1);
            Glide.with(mContext).load(albums.get(1)) .error(R.drawable.twitter).into(holder.ivImg2);
            Glide.with(mContext).load(albums.get(2)) .error(R.drawable.twitter).into(holder.ivImg3);
            holder.artistLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    browserIntent.setPackage("org.mozilla.firefox");
                    mContext.startActivity(browserIntent);
                }
            });

//            Picasso.get().load(events.getString("logo")).into(holder.ivImg);
//            Picasso.get().load(albums.get(0)).into(holder.ivImg1);
//            Picasso.get().load(albums.get(1)).into(holder.ivImg2);
//            Picasso.get().load(albums.get(2)).into(holder.ivImg3);

//            ImageRequest request = new ImageRequest.Builder(mContext)
//                    .data(events.getString("logo"))
//                    .target(holder.ivImg)
//                    .build();
//
//            Coil.imageLoader(mContext).enqueue(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mArtists.length();
    }


    public static ArrayList<String> jsonArrayToArrayList(JSONArray jsonArray) throws JSONException {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            arrayList.add(jsonArray.getString(i));
        }
        return arrayList;
    }
}
