package com.launcher.foodlauncher;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.launcher.foodlauncher.ui.favourites.FavouritesFragment;
import com.launcher.foodlauncher.ui.find_food.FindFoodFragment;
import com.launcher.foodlauncher.ui.home.HomeFragment;
import com.launcher.foodlauncher.ui.user.UserFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    HomeFragment homeFragment;
    FindFoodFragment findFoodFragment;
    FavouritesFragment favouritesFragment;
    UserFragment userFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        homeFragment = new HomeFragment();
        findFoodFragment = new FindFoodFragment();
        favouritesFragment = new FavouritesFragment();
        userFragment = new UserFragment();

        init();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_find_food, R.id.navigation_favourites, R.id.navigation_user)
//                .build();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                switch(itemId) {
                    case R.id.navigation_home: {
                        loadFragment(homeFragment, null);
                        return true;
                    }
                    case R.id.navigation_find_food: {
                        loadFragment(findFoodFragment, null);
                        return true;
                    }
                    case R.id.navigation_favourites: {
                        loadFragment(favouritesFragment, null);
                        return true;
                    }
                    case R.id.navigation_user: {
                        loadFragment(userFragment, null);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void init() {
        loadFragment(homeFragment, "");
    }

    private void loadFragment(Fragment fragment, String comeFrom) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("come_from", comeFrom);
        fragment.setArguments(bundle);
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
