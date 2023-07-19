package com.example.eventfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import java.util.Objects;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class DetailActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_detail);
//        //Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar
//
//    }

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private TextView toolbar_text;
    private String event_name, event_id,event_url,json_string;

    boolean isHeartRed = false;




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);



        Toolbar toolbar = this.findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);


        tabLayout = this.findViewById(R.id.tab_layout_detail);
        viewPager = this.findViewById(R.id.view_pager_detail);
        toolbar_text = this.findViewById(R.id.toolbar_text);
        toolbar_text.post(() -> {toolbar_text.setSelected(true);});


        Intent intent = this.getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        event_id = bundle.getString("id");
        event_name = bundle.getString("event");
        event_url = bundle.getString("url");
        json_string = bundle.getString("object");
        toolbar_text.setText(event_name);

        isHeartRed = isKeyPresentInSharedPreferences(this, event_id);


//        View heart = (View) this.findViewById(R.id.action_red_heart);
//        if (isKeyPresent) {
//           heart.setSelected(true);
//        } else {
//            heart.setSelected(false);
//        }




        ViewPagerAdapterDetail viewPagerAdapter = new ViewPagerAdapterDetail(this);
        viewPager.setAdapter(viewPagerAdapter);

        // Connect the TabLayout with the ViewPager
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setIcon(R.drawable.info_icon);
                    tab.setText("Details");
                    break;
                case 1:
                    tab.setIcon(R.drawable.artist_icon);
                    tab.setText("Artist(S)");
                    break;
                case 2:
                    tab.setIcon(R.drawable.venue_icon);
                    tab.setText("Venue");
                    break;
            }
        });
        tabLayoutMediator.attach();


        ImageButton backArrowButton = this.findViewById(R.id.back_arrow_button);
        backArrowButton.setOnClickListener(v -> {
            // Handle back arrow button click
            onBackPressed();
        });
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem heartMenuItem = menu.findItem(R.id.action_red_heart);
        setHeartColor(heartMenuItem, isHeartRed);
        return super.onPrepareOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_facebook:
                // Handle Facebook button click
                String fackbook = "https://www.facebook.com/sharer/sharer.php?u="+event_url;
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fackbook));
                facebookIntent.setPackage("org.mozilla.firefox");
                startActivity(facebookIntent);

                return true;

            case R.id.action_twitter:
                // Handle Twitter button click
                String twitter= "https://twitter.com/intent/tweet?url="+event_url+"&text=Check%20"+event_name+"%20on%20Ticketmaster";

                Intent twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitter));
                twitterIntent.setPackage("org.mozilla.firefox");
                startActivity(twitterIntent);

                return true;

            case R.id.action_red_heart:

                toggleHeartColor(item);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpViewPager(ViewPager2 viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
    }



    private void saveJsonObjectToSharedPreferences(String events, String id) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(id, events);
        editor.apply();
    }

    private void removeJsonObjectFromSharedPreferences(String id) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(id);
        editor.apply();
    }


    public boolean isKeyPresentInSharedPreferences(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE);
        return sharedPreferences.contains(key);
    }

    private void setHeartColor(MenuItem heartMenuItem, boolean isRed) {
        if (isRed) {
            heartMenuItem.setIcon(R.drawable.ic_heart_red);
        } else {
            heartMenuItem.setIcon(R.drawable.ic_heart_empty);
        }
    }


    private void toggleHeartColor(MenuItem heartMenuItem) {
        if (isHeartRed) {
            // If the heart is red, change it to white
            heartMenuItem.setIcon(R.drawable.ic_heart_empty);
            removeJsonObjectFromSharedPreferences(event_id);
            Toast.makeText(this, event_name+"is Removed from Favorites", Toast.LENGTH_SHORT).show();
            isHeartRed = false;


        } else {
            // If the heart is white, change it to red
            heartMenuItem.setIcon(R.drawable.ic_heart_red);
            saveJsonObjectToSharedPreferences(json_string, event_url);
            Toast.makeText(this, event_name+"is Added to Favorites", Toast.LENGTH_SHORT).show();
            isHeartRed = true;

        }
    }
}