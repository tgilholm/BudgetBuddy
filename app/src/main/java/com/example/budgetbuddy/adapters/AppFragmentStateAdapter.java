package com.example.budgetbuddy.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.budgetbuddy.fragments.AddFragment;
import com.example.budgetbuddy.fragments.OverviewFragment;
import com.example.budgetbuddy.fragments.SettingsFragment;
import com.example.budgetbuddy.fragments.TransactionsFragment;

/**
 * Extends <code>FragmentStateAdapter</code> to create all the fragments
  */
public class AppFragmentStateAdapter extends FragmentStateAdapter {

    public AppFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity)
    {
        super(fragmentActivity);
    }

    /**
     * Get the number of fragment entities
     * @return int value of fragment count
     */
    @Override
    public int getItemCount() {
        return 4;
    }

    /**
     * Returns the <code>Fragment</code> corresponding to the <code>position</code> passed to it
     * @param position the index of the fragment
     * @return the corresponding fragment
     */
    @NonNull
    @Override
    public Fragment createFragment(int position)
    {
        // Switch to select which of the fragments to return
        switch (position) {
            case 3:
                return new SettingsFragment();
            case 2:
                return new TransactionsFragment();
            case 1:
                return new AddFragment();
            default: // By default, the "overview" window will open
                return new OverviewFragment();
        }
    }

}
