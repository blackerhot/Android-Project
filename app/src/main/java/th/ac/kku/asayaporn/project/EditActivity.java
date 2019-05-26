package th.ac.kku.asayaporn.project;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
        , TimePickerDialog.OnTimeSetListener {
    FirebaseUser currentFirebaseUser;
    private FirebaseAuth mAuth;
    EditText etitle;
    EditText eurl;
    EditText ephone;
    EditText eplace;
    EditText esponsor,eimg;
    EditText econtent;
    Button sendBut;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Button btnstartdate;
    Button btnenddate;
    int year, month, day, hour, minute;
    int year_start, month_start, day_start, hour_start, minute_start;
    int year_end, month_end, day_end, hour_end, minute_end;
    Boolean state;
    String dateSt = "ยังว่าง";
    String dateEd = "ยังว่าง";
    String timeSt = "ยังว่าง";
    String timeEd = "ยังว่าง";
    Bundle para;
    String stausfi="pending";
    private static int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_EX = 1;
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
            actionBar.setTitle("Edit Activities");
        }
        eimg = (EditText) findViewById(R.id.eimg);
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
        mAuth = FirebaseAuth.getInstance();
        currentFirebaseUser = mAuth.getCurrentUser();
        para = getIntent().getExtras();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if ((ds.child("key").getValue() + "").equals(para.getString("key"))) {
                        stausfi=ds.child("status").getValue() + "";
                        etitle.setText(ds.child("title").getValue() + "");
                        eurl.setText(ds.child("url").getValue() + "");
                        ephone.setText(ds.child("phone").getValue() + "");
                        eimg.setText(ds.child("image").getValue() + "");
                        eplace.setText(ds.child("place").getValue() + "");
                        esponsor.setText(ds.child("sponsor").getValue() + "");
                        econtent.setText(ds.child("content").getValue() + "");
                        btnenddate.setText(ds.child("dateEd").getValue() + " : " + ds.child("timeEd").getValue());
                        btnstartdate.setText(ds.child("dateSt").getValue() + " : " + ds.child("timeSt").getValue());
                        timeEd = ds.child("timeEd").getValue() + "";
                        timeSt = ds.child("timeSt").getValue() + "";
                        dateSt = ds.child("dateSt").getValue() + "";
                        dateEd = ds.child("dateEd").getValue() + "";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sendBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (year_start <= year_end && month_start < month_end) {
                    addEvent();
                } else if (year_start <= year_end && month_start == month_end && day_start < day_end) {
                    addEvent();
                } else if (year_start <= year_end && month_start == month_end && day_start == day_end &&
                        hour_start < hour_end) {
                    addEvent();
                } else if (year_start <= year_end && month_start == month_end && day_start == day_end &&
                        hour_start == hour_end && minute_start <= minute_end) {
                    addEvent();
                } else {
                    Toast.makeText(EditActivity.this, "Please Check Start Date And End Date incorrect", Toast.LENGTH_LONG).show();
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
                DatePickerDialog datePickDialog = new DatePickerDialog(EditActivity.this, EditActivity.this, year, month, day);
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
                DatePickerDialog datePickDialog = new DatePickerDialog(EditActivity.this, EditActivity.this, year_start, month, day);
                datePickDialog.show();
                state = true;
            }
        });
        // load img form media
        Button btnImg = (Button) findViewById(R.id.imgAc);
        btnImg.setOnClickListener(new View.OnClickListener() {
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
    protected void onActivityResult(int requrestCode, int resultCode, Intent data) {
        super.onActivityResult(requrestCode, resultCode, data);
        if (requrestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selec = data.getData();
            String[] filePC = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selec, filePC, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePC[0]);
            final String pic = cursor.getString(columnIndex);
            cursor.close();
            verifyStoragePermissions(EditActivity.this);
            ImageView imgv = (ImageView) findViewById(R.id.imgshow);
            imgv.setImageBitmap(BitmapFactory.decodeFile(pic));
        }
    }

    // load img form media
    private static void verifyStoragePermissions(EditActivity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EX
            );
        }
    }

    private void addEvent() {
        String url = eurl.getText() + "";
        String image =eimg.getText()+"";
        String title = etitle.getText() + "";
        String place = eplace.getText() + "";
        String content = econtent.getText() + "";
        String phone = ephone.getText() + "";
        String website = eurl.getText() + "";
        String sponsor = esponsor.getText() + "";

        writeNewPost(null, url, image, title, place, content,
                dateSt, dateEd, phone, website, timeSt, timeEd, sponsor);
        readNewUser();
        Toast.makeText(EditActivity.this,
                "Refresh for updating!", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    private void writeNewPost(JsonObject contact, String url, String image, String title, String place,
                              String content, String dateSt, String dateEd, String phone, String website,
                              String timeSt, String timeEt, String sponsor) {

        if (image == "") {
            image = "https://it.nkc.kku.ac.th/frontend/images/logo-color.png";
        }
        String key = para.getString("key");
        ActivityKKU post = new ActivityKKU(contact, url, image, title, place, content,
                dateSt, dateEd, phone, website, timeSt, timeEt, sponsor);

        post.setStatus(stausfi);
        post.setKey(key);
        if (currentFirebaseUser != null) {
            post.setBy(currentFirebaseUser.getEmail());
        }

        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, postValues);
        myRef.updateChildren(childUpdates);

    }

    private void readNewUser() {
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String s0 = "";
                Gson gson = new Gson();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    //Here you can access the child.getKey()


                    ActivityKKU user1 = new ActivityKKU(null, child.child("url").getValue() + "",
                            child.child("image").getValue() + "",
                            child.child("title").getValue() + "",
                            child.child("title").getValue() + "",
                            child.child("content").getValue() + "",
                            child.child("dateSt").getValue() + "",
                            child.child("dateEd").getValue() + "",
                            child.child("phone").getValue() + "",
                            child.child("website").getValue() + "",
                            child.child("timeSt").getValue() + "",
                            child.child("timeEt").getValue() + "",
                            child.child("sponsor").getValue() + "");

                    user1.setKey(child.getKey());
                    user1.setStatus("pending");
                    s0 += gson.toJson(user1.toMap()) + ",";

                }
                if (s0.length() != 0) {
                    s0 = s0.substring(0, s0.length() - 1);
                }

                SharedPreferences sp;
                SharedPreferences.Editor editor;
                sp = EditActivity.this.getSharedPreferences("USER", Context.MODE_PRIVATE);
                editor = sp.edit();
                editor.putString("jsonByUSER", new String(String.valueOf("{\"activities\":[" + s0 + "]}")));
                editor.commit();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


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
        if (state == false) {
            year_start = i;
            month_start = i1 + 1;
            day_start = i2;
            TimePickerDialog timePickerDialog = new TimePickerDialog(EditActivity.this, EditActivity.this, hour, minute, true);
            timePickerDialog.show();
        } else if (state == true) {
            year_end = i;
            month_end = i1 + 1;
            day_end = i2;
            TimePickerDialog timePickerDialog = new TimePickerDialog(EditActivity.this, EditActivity.this, hour, minute, true);
            timePickerDialog.show();
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        if (state == false) {
            hour_start = i;
            minute_start = i1;
            //  Toast.makeText(AddingActivity.this,"AM"+ year_start+" "+month_start+
            //         " "+day_start+" "+hour_start+" "+minute_start,Toast.LENGTH_LONG).show();
            dateSt = year_start + "-" + month_start + "-" + day_start;
            if (hour_start >= 12) {

                int hour_start_PM = hour_start - 12;
                if (hour_start_PM < 10) {
                    if (minute_start < 10) {
                        timeSt = "PM. 0" + hour_start_PM + ".0" + minute_start;
                    } else {
                        timeSt = "PM. 0" + hour_start_PM + "." + minute_start;
                    }

                } else {
                    if (minute_start < 10) {
                        timeSt = "PM. " + hour_start_PM + ".0" + minute_start;
                    } else {
                        timeSt = "PM. " + hour_start_PM + "." + minute_start;
                    }

                }

            } else {
                if (hour_start < 10) {
                    if (minute_start < 10) {
                        timeSt = "AM. 0" + hour_start + ".0" + minute_start;
                    } else {
                        timeSt = "AM. 0" + hour_start + "." + minute_start;
                    }

                } else {
                    if (minute_end < 10) {
                        timeSt = "AM. " + hour_end + ".0" + minute_start;
                    } else {
                        timeSt = "AM. " + hour_end + "." + minute_start;
                    }

                }

            }
            btnstartdate.setText(dateSt + ", " + timeSt);
        } else if (state == true) {

            hour_end = i;
            minute_end = i1;
            // Toast.makeText(AddingActivity.this,"PM" + year_end+" "+month_end+
            //         " "+day_end+" "+hour_end+" "+minute_end,Toast.LENGTH_LONG).show();
            dateEd = year_end + "-" + month_end + "-" + day_end;
            if (hour_end >= 12) {

                int hour_end_PM = hour_end - 12;
                if (hour_end_PM < 10) {
                    if (minute_start < 10) {
                        timeEd = "PM. 0" + hour_end_PM + ".0" + minute_end;
                    } else {
                        timeEd = "PM. 0" + hour_end_PM + "." + minute_end;
                    }

                } else {
                    if (minute_start < 10) {
                        timeEd = "PM. " + hour_end_PM + ".0" + minute_end;
                    } else {
                        timeEd = "PM. " + hour_end_PM + "." + minute_end;
                    }

                }

            } else {
                if (hour_end < 10) {
                    if (minute_start < 10) {
                        timeEd = "AM. 0" + hour_end + ".0" + minute_end;
                    } else {
                        timeEd = "AM. 0" + hour_end + "." + minute_end;
                    }

                } else {
                    if (minute_end < 10) {
                        timeEd = "AM. " + hour_end + ".0" + minute_end;
                    } else {
                        timeEd = "AM. " + hour_end + "." + minute_end;
                    }

                }

            }
            btnenddate.setText(dateEd + ", " + timeEd);
        }
    }
}
