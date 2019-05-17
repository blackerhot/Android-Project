package th.ac.kku.asayaporn.project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class AddingActivity extends AppCompatActivity {
    EditText etitle;
    EditText eurl;
    EditText ephone;
    EditText eplace;
    EditText esponsor;
    EditText econtent;
    Button sendBut;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Sending Activities");
        }

        etitle = (EditText) findViewById(R.id.etitle);
        eurl = (EditText) findViewById(R.id.eurl);
        ephone = (EditText) findViewById(R.id.ephone);
        eplace = (EditText) findViewById(R.id.eplace);
        esponsor = (EditText) findViewById(R.id.esponsor);
        econtent = (EditText) findViewById(R.id.econtent);
        sendBut = (Button) findViewById(R.id.send_But);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("activities");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            private String TAG = "TAGGGG";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String name = "";
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    name += (String) messageSnapshot.child("title").getValue();
                    String message = (String) messageSnapshot.child("website").getValue();

                }
                //Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                // Toast.makeText(getContext(),name,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        sendBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = eurl.getText() + "เว็ปยังว่างเปล่า";
                String image = eurl.getText() + "";
                String title = etitle.getText() + "";
                String place = eplace.getText() + "ยังว่าง";
                String content = econtent.getText() + "ยังว่าง";
                String dateSt = "ยังว่าง";
                String dateEd = "ยังว่าง";
                String timeSt = "ยังว่าง";
                String timeEd = "ยังว่าง";
                String phone = ephone.getText() + "ยังว่าง";
                String website = eurl.getText() + "ยังว่าง";
                String sponsor = esponsor.getText() + "ยังว่าง";

                writeNewPost(null, url, image, title, place, content,
                        dateSt, dateEd, phone, website, timeSt, timeEd, sponsor);
                readNewUser();
                onBackPressed();
            }
        });


    }

    private void writeNewPost(JsonObject contact, String url, String image, String title, String place,
                              String content, String dateSt, String dateEd, String phone, String website,
                              String timeSt, String timeEt, String sponsor) {

        String key = myRef.child("activities").push().getKey();
        ActivityKKU post = new ActivityKKU(contact, url, image, title, place, content,
                dateSt, dateEd, phone, website, timeSt, timeEt, sponsor);
        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, postValues);
        myRef.updateChildren(childUpdates);

    }

    private void readNewUser() {
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String TAG = "TAGGing";
                String name = "";
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    name += (String) messageSnapshot.toString();
                }

                Toast.makeText(AddingActivity.this,
                        "You add is " + name, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ActivityKKU user = dataSnapshot.getValue(ActivityKKU.class);
                Toast.makeText(AddingActivity.this,
                        "You Changed is " + user.toMap().get("title"), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
