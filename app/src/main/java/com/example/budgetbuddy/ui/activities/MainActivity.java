package com.example.budgetbuddy.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.budgetbuddy.R;
import com.example.budgetbuddy.adapters.AppFragmentStateAdapter;
import com.example.budgetbuddy.ui.viewmodel.StartupViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * Contains the fragments within a <code>ViewPager2</code> attached to a <code>TabLayout</code> for top-bar navigation and automatically updates title bar text.
 * Also handles first time startup logic via the <code>StartupViewModel</code>
 */
public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);

        // Initialise viewmodel
        StartupViewModel startupViewModel = new ViewModelProvider(this).get(StartupViewModel.class);


        // Get views from layout
        MaterialToolbar toolbar = findViewById(R.id.title_bar);
        ViewPager2 viewPager = findViewById(R.id.swipePager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);


        // If first run, go to onboard activity
        if (startupViewModel.getFirstRun())
        {
            firstTimeStartup();
            Log.v("MainActivity", "First startup, moving to onboarding activity");
        }


        // Set window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Set adapter on viewPager
        viewPager.setAdapter(new AppFragmentStateAdapter(this));
        new TabLayoutMediator(tabLayout, viewPager,

                // Set the tab to the selected fragment title
                (tab, position) ->
                {
                    switch (position)
                    {
                        case 0:
                            tab.setText(R.string.title_overview);
                            break;
                        case 1:
                            tab.setText(R.string.title_add);
                            break;
                        case 2:
                            tab.setText(R.string.title_transactions);
                            break;
                        case 3:
                            tab.setText(R.string.title_settings);
                    }
                }).attach();


        // Set the title bar text to the currently selected fragment
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback()
        {
            @Override
            public void onPageSelected(int position)
            {
                switch (position)
                {
                    case 0:
                        toolbar.setTitle(R.string.title_overview);
                        break;
                    case 1:
                        toolbar.setTitle(R.string.title_add);
                        break;
                    case 2:
                        toolbar.setTitle(R.string.title_transactions);
                        break;
                    case 3:
                        toolbar.setTitle(R.string.title_settings);
                }
                super.onPageSelected(position);
            }
        });


        // Change currently active fragment to AddFragment if add button pressed
        getSupportFragmentManager().setFragmentResultListener("addPage", this, (requestKey, result) ->
        {
            if (result.getInt(requestKey) == 1)
            {
                toolbar.setTitle(R.string.title_add);
            }
            viewPager.setCurrentItem(1, true); // Set the ViewPager to the addFragment using a smooth scroll
        });

    }

    /**
     * Starts an <code>Intent</code> to go to the <code>FirstTimeStartupActivity</code>
     */
    private void firstTimeStartup()
    {
        startActivity(new Intent(this, FirstTimeStartupActivity.class));
    }
}