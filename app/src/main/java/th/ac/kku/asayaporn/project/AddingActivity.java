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

public class AddingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
        , TimePickerDialog.OnTimeSetListener {
    EditText etitle;
    EditText eurl;
    EditText ephone;
    EditText eplace;
    EditText esponsor;
    EditText econtent;
    Button sendBut;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Button btnstartdate;
    Button btnenddate;
    int year , month,day,hour,minute;
    int year_start , month_start,day_start,hour_start,minute_start;
    int year_end, month_end, day_end, hour_end, minute_end;
    Boolean state;
    String dateSt = "ยังว่าง";
    String dateEd = "ยังว่าง";
    String timeSt = "ยังว่าง";
    String timeEd = "ยังว่าง";
    private static int RESULT_LOAD_IMAGE = 1;
    private static  final int REQUEST_EX = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
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
        btnstartdate = (Button) findViewById(R.id.edatest);
        btnenddate = (Button) findViewById(R.id.edateend);
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            private String TAG = "TAGGGG";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String s0="";
                Gson gson = new Gson();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    //Here you can access the child.getKey()
                    ActivityKKU user = child.getValue(ActivityKKU.class);
                    s0 += gson.toJson(user.toMap()) +",";
                }

                s0=s0.substring(0,s0.length()-1);
                Log.e("result11", s0);
                SharedPreferences sp ;
                SharedPreferences.Editor editor ;
                sp = AddingActivity.this.getSharedPreferences("USER", Context.MODE_PRIVATE);
                editor = sp.edit();
                editor.putString("jsonByUSER", new String(String.valueOf("{\"activities\":["+s0+"]}")));
                editor.commit();
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
                if (year_start <= year_end && month_start < month_end){
                    addEvent();
                }else if(year_start <= year_end && month_start == month_end && day_start < day_end ){
                    addEvent();
                }else if(year_start <= year_end && month_start == month_end && day_start == day_end &&
                hour_start < hour_end){
                    addEvent();
                }else if (year_start <= year_end && month_start == month_end && day_start == day_end &&
                        hour_start == hour_end && minute_start <= minute_end){
                    addEvent();
                }else {
                    Toast.makeText(AddingActivity.this,"Please Check Start Date And End Date incorrect",Toast.LENGTH_LONG).show();
                }

            }
        });
        btnstartdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickDialog = new DatePickerDialog(AddingActivity.this,AddingActivity.this,year,month,day);
                datePickDialog.show();
                state = false;
            }
        });

        btnenddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickDialog = new DatePickerDialog(AddingActivity.this,AddingActivity.this,year_start,month,day);
                datePickDialog.show();
                state = true;
            }
        });
        // load img form media
        Button btn_load = (Button) findViewById(R.id.but_uppic);
        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

    }

    // load img form media
    @Override
    protected void onActivityResult(int requrestCode, int resultCode, Intent data){
        super.onActivityResult(requrestCode, resultCode, data);
        if(requrestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selec = data.getData();
            String[] filePC = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selec, filePC, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePC[0]);
            final String pic = cursor.getString(columnIndex);
            cursor.close();
            verifyStoragePermissions(AddingActivity.this);
            ImageView img = (ImageView) findViewById(R.id.imgAc);
            img.setImageBitmap(BitmapFactory.decodeFile(pic));
        }
    }

    // load img form media
    private static void verifyStoragePermissions(AddingActivity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EX
            );
        }
    }

    private void addEvent() {
        String url = eurl.getText() + "เว็ปยังว่างเปล่า";
        String image = eurl.getText() + "";
        String title = etitle.getText() + "";
        String place = eplace.getText() + "ยังว่าง";
        String content = econtent.getText() + "ยังว่าง";
        String phone = ephone.getText() + "ยังว่าง";
        String website = eurl.getText() + "ยังว่าง";
        String sponsor = esponsor.getText() + "ยังว่าง";

        writeNewPost(null, url, image, title, place, content,
                dateSt, dateEd, phone, website, timeSt, timeEd, sponsor);
        readNewUser();
        onBackPressed();
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
                String s0="";
                Gson gson = new Gson();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    //Here you can access the child.getKey()
                    ActivityKKU user = child.getValue(ActivityKKU.class);
                    s0 += gson.toJson(user.toMap()) +",";

                }
                s0=s0.substring(0,s0.length()-1);

                SharedPreferences sp ;
                SharedPreferences.Editor editor ;
                sp = AddingActivity.this.getSharedPreferences("USER", Context.MODE_PRIVATE);
                editor = sp.edit();
                editor.putString("jsonByUSER", new String(String.valueOf("{\"activities\":["+s0+"]}")));
                editor.commit();
                Toast.makeText(AddingActivity.this,
                        "You added Already", Toast.LENGTH_LONG).show();
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

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        if (state == false){
            year_start = i;
            month_start = i1+1;
            day_start = i2;
            TimePickerDialog timePickerDialog = new TimePickerDialog(AddingActivity.this,AddingActivity.this,hour,minute,true);
            timePickerDialog.show();
        }else if (state == true){
            year_end = i;
            month_end = i1+1;
            day_end = i2;
            TimePickerDialog timePickerDialog = new TimePickerDialog(AddingActivity.this,AddingActivity.this,hour,minute,true);
            timePickerDialog.show();
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        if (state == false){
            hour_start = i;
            minute_start = i1;
          //  Toast.makeText(AddingActivity.this,"AM"+ year_start+" "+month_start+
           //         " "+day_start+" "+hour_start+" "+minute_start,Toast.LENGTH_LONG).show();
            dateSt = year_start + "-" + month_start + "-" + day_start;
            if (hour_start >= 12){

                int hour_start_PM = hour_start -12;
                if (hour_start_PM < 10){
                    if (minute_start < 10){
                        timeSt = "PM. 0" + hour_start_PM + ".0" + minute_start;
                    } else {
                        timeSt = "PM. 0" + hour_start_PM + "." + minute_start;
                    }

                }else {
                    if (minute_start < 10){
                        timeSt = "PM. " + hour_start_PM + ".0" + minute_start;
                    } else {
                        timeSt = "PM. " + hour_start_PM + "." + minute_start;
                    }

                }

            }else {
                if (hour_start < 10){
                    if (minute_start < 10){
                        timeSt = "AM. 0" + hour_start + ".0" + minute_start;
                    } else {
                        timeSt = "AM. 0" + hour_start + "." + minute_start;
                    }

                }else {
                    if (minute_end < 10){
                        timeSt = "AM. " + hour_end + ".0" + minute_start;
                    } else {
                        timeSt = "AM. " + hour_end + "." + minute_start;
                    }

                }
                btnstartdate.setText(dateSt + ", " + timeSt);
            }
        }else if (state == true){

            hour_end = i;
            minute_end =i1;
           // Toast.makeText(AddingActivity.this,"PM" + year_end+" "+month_end+
           //         " "+day_end+" "+hour_end+" "+minute_end,Toast.LENGTH_LONG).show();
            dateEd = year_end + "-" + month_end + "-" + day_end;
            if (hour_end >= 12){

                int hour_end_PM = hour_end -12;
                if (hour_end_PM < 10){
                    if (minute_start < 10){
                        timeEd = "PM. 0" + hour_end_PM + ":0" + minute_end;
                    } else {
                        timeEd = "PM. 0" + hour_end_PM + ":" + minute_end;
                    }

                }else {
                    if (minute_start < 10){
                        timeEd = "PM. " + hour_end_PM + ":0" + minute_end;
                    } else {
                        timeEd = "PM. " + hour_end_PM + ":" + minute_end;
                    }

                }

            }else {
                if (hour_end < 10){
                    if (minute_start < 10){
                        timeEd = "AM. 0" + hour_end + ":0" + minute_end;
                    } else {
                        timeEd = "AM. 0" + hour_end + ":" + minute_end;
                    }

                }else {
                    if (minute_end < 10){
                        timeEd = "AM. " + hour_end + ":0" + minute_end;
                    } else {
                        timeEd = "AM. " + hour_end + ":" + minute_end;
                    }

                }
                btnenddate.setText(dateEd + ", " + timeEd);
            }
        }
    }
}
