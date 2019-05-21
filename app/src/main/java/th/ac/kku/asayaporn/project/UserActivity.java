package th.ac.kku.asayaporn.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    ListView listView;
    FirebaseDatabase database;
    DatabaseReference myRef;
    protected CustomAdapter3 mAdapter;
    EditText editSearch;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_manage_activities);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Manage User");
        }
        ((AppCompatActivity) UserActivity.this).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ((AppCompatActivity) UserActivity.this).getSupportActionBar().setCustomView(R.layout.actionbar_feed);
        listView = (ListView) findViewById(R.id.listview1);
        editSearch = (EditText) findViewById(R.id.edit_search);
        editSearch.setHint("Search by Email");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("user");
        myRef.addValueEventListener(new ValueEventListener() {
            private String TAG = "TAGGGG";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String s0 = "";
                Gson gson = new Gson();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    //Here you can access the child.getKey()

                    ActivityUser user = child.getValue(ActivityUser.class);

                    s0 += gson.toJson(user.toMap()) + ",";

                }
                if (s0.length() != 0) {
                    s0 = s0.substring(0, s0.length() - 1);
                }

                String json = new String(String.valueOf("{\"user\":[" + s0 + "]}"));

                Blog blog = gson.fromJson(json, Blog.class);
                List<ActivityUser> activity = blog.getUser();
                mAdapter = new CustomAdapter3(UserActivity.this, activity);
                listView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                ActivityUser activityKKU;
                ArrayList<String> str = new ArrayList<String>();
                str.add("ไม่พบคำที่ค้นหา");
                final ArrayList<ActivityUser> arra = new ArrayList<>();
                if (editSearch.length() != 0) {
                    for (int i = 0; i < mAdapter.getCount(); i++) {
                        activityKKU = (ActivityUser) mAdapter.getItem(i);

                        if (activityKKU.getEmail().toString().contains(s)) {

                            arra.add(activityKKU);
                        }

                            listView.setAdapter(new CustomAdapter3(UserActivity.this, arra));
                    }
                } else {
                    listView.setAdapter(mAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
