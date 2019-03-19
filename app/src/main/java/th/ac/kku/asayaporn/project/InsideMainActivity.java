package th.ac.kku.asayaporn.project;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class InsideMainActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inside_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new activitesFragment()).commit();
        }

    }

    private  BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.activiteBar:
                            selectedFragment = new activitesFragment();
                            break;
                        case R.id.feedBar:
                            selectedFragment = new feedFragment();
                            break;
                        case R.id.calendarBar:
                            selectedFragment = new calendarFragment();
                            break;
                        case R.id.Profile:
                            selectedFragment = new profileFragment();
                            break;
                    }

                    getSupportFragmentManager().
                            beginTransaction().
                            replace(R.id.fragment_container, selectedFragment).
                            commit();
                    return true;


                }
            };


}
