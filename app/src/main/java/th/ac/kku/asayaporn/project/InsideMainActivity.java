package th.ac.kku.asayaporn.project;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

public class InsideMainActivity extends AppCompatActivity {
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inside_main);

        dl = (DrawerLayout)findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);
        dl.addDrawerListener(t);
        t.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nv = (NavigationView)findViewById(R.id.nav_bar);

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.account:
                        Toast.makeText(InsideMainActivity.this, "My Account",Toast.LENGTH_SHORT).show();
                    case R.id.settings:
                        Toast.makeText(InsideMainActivity.this, "Settings",Toast.LENGTH_SHORT).show();
                    case R.id.mycart:
                        Toast.makeText(InsideMainActivity.this, "My Cart",Toast.LENGTH_SHORT).show();
                    default:
                        return true;
                }




            }
        });



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
                            Bundle extras = getIntent().getExtras();
                            String email = extras.getString("email");
                            String uid = extras.getString("uid");

                            Bundle bundle = new Bundle();
                            bundle.putString("email", email);
                            bundle.putString("uid", uid);
                            selectedFragment.setArguments(bundle);
                            break;
                    }

                    getSupportFragmentManager().
                            beginTransaction().
                            replace(R.id.fragment_container, selectedFragment).
                            commit();
                    return true;


                }
            };
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }


}
