package com.example.eventfinder;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class ViewPagerAdapterDetail extends FragmentStateAdapter{
    public ViewPagerAdapterDetail(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new EventDetails();
            case 1:
                return new Artists();
            case 2:
                return new Venue();
            default:
                return new EventDetails();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
