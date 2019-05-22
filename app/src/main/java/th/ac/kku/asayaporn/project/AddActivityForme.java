package th.ac.kku.asayaporn.project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddActivityForme  extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
        , TimePickerDialog.OnTimeSetListener {
    EditText etitle;
    EditText eplace;
    EditText econtent;
    Button sendBut;
    Button btnstartdate;
    Button btnenddate;
    ArrayList<ExampleItem> mExampleList = new ArrayList<ExampleItem>();
    int year , month,day,hour,minute;
    int year_start , month_start,day_start,hour_start,minute_start;
    int year_end, month_end, day_end, hour_end, minute_end;
    Boolean state;
    String dateSt = "";
    String dateEd = "";
    String timeSt = "";
    String timeEd = "";
    String title;
    String place;
    String content;

    FirebaseDatabase database;
    DatabaseReference myRef;
    ConnectivityManager manager ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activites_addevent);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Sending Activities");
        }

        etitle = (EditText) findViewById(R.id.edt_title_for_me);
        eplace = (EditText) findViewById(R.id.edt_place_for_me);
        econtent = (EditText) findViewById(R.id.edt_command_for_me);
        sendBut = (Button) findViewById(R.id.btn_send_for_me);
        btnstartdate = (Button) findViewById(R.id.btn_start_date_for_me);
        btnenddate = (Button) findViewById(R.id.btn_end_date_for_me);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("user");
        sendBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = etitle.getText() + "";
                if (title.equals("")){
                    Toast.makeText(AddActivityForme.this,"Please Check Title incorrect",Toast.LENGTH_LONG).show();
                }else {
                    if (timeSt.equals("") && timeEd.equals("")){
                        Toast.makeText(AddActivityForme.this,"Please Check Start Date And End Date incorrect",Toast.LENGTH_LONG).show();
                    }else {
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
                            Toast.makeText(AddActivityForme.this,"Please Check Start Date And End Date incorrect",Toast.LENGTH_LONG).show();
                        }
                    }

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
                DatePickerDialog datePickDialog = new DatePickerDialog(AddActivityForme.this,AddActivityForme.this,year,month,day);
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
                DatePickerDialog datePickDialog = new DatePickerDialog(AddActivityForme.this,AddActivityForme.this,year_start,month,day);
                datePickDialog.show();
                state = true;
            }
        });
    }

    private void addEvent() {
       place = eplace.getText() + "";
       content = econtent.getText() + "";
            loaddata();
            mExampleList.add(new ExampleItem(timeSt,timeEd,dateSt, dateEd,title,content, place));
            SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(mExampleList);
            editor.putString("task list", json);
            editor.apply();
            manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null){
                    Map<String,Object> childUpdates = new HashMap<>();
                    childUpdates.put("/"+user.getUid()+"/Activities_me/" + title + "/title",title);
                    childUpdates.put("/"+user.getUid()+"/Activities_me/" + title + "/TimeStart",timeSt);
                    childUpdates.put("/"+user.getUid()+"/Activities_me/"+ title +"/TimeEnd",timeEd);
                    childUpdates.put("/"+user.getUid()+"/Activities_me/"+ title +"/DateStart",dateSt);
                    childUpdates.put("/"+user.getUid()+"/Activities_me/" + title +"/DateEnd",dateEd);
                    childUpdates.put("/"+user.getUid()+"/Activities_me/"+ title +"/content",content);
                    childUpdates.put("/"+user.getUid()+"/Activities_me/"+ title +"/place",place);
                    myRef.updateChildren(childUpdates);
                }
            }

            onBackPressed();

    }

    private void loaddata() {
        SharedPreferences sharedPreferences =  getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<ExampleItem>>() {}.getType();
        mExampleList = gson.fromJson(json, type);

        if (mExampleList == null) {
            mExampleList = new ArrayList<>();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddActivityForme.this, InsideMainActivity.class);
       // startActivity(intent);
       // finish();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        if (state == false){
            year_start = i;
            month_start = i1+1;
            day_start = i2;
            TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivityForme.this,AddActivityForme.this,hour,minute,true);
            timePickerDialog.show();
        }else if (state == true){
            year_end = i;
            month_end = i1+1;
            day_end = i2;
            TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivityForme.this,AddActivityForme.this,hour,minute,true);
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
            if (month_start < 10){
                dateSt = year_start + "-0" +  month_start + "-" + day_start;
            }else {
                dateSt = year_start + "-" + month_start + "-" + day_start;
            }
            if (hour_start >= 12){

                int hour_start_PM = hour_start -12;
                if (hour_start_PM < 10){
                    if (minute_start < 10){
                        //timeSt = "PM. 0" + hour_start_PM + ".0" + minute_start;
                        timeSt = "" + hour_start + ".0" + minute_start;
                    } else {
                        //timeSt = "PM. 0" + hour_start_PM + "." + minute_start;
                        timeSt = "" + hour_start + "." + minute_start;
                    }

                }else {
                    if (minute_start < 10){
                       // timeSt = "PM. " + hour_start_PM + ".0" + minute_start;
                        timeSt = "" + hour_start + ".0" + minute_start;
                    } else {
                        //timeSt = "PM. " + hour_start_PM + "." + minute_start;
                        timeSt = "" + hour_start + "." + minute_start;
                    }

                }

            }else {
                if (hour_start < 10){
                    if (minute_start < 10){
                       // timeSt = "AM. 0" + hour_start + ".0" + minute_start;
                        timeSt = "0" + hour_start + ".0" + minute_start;
                    } else {
                       // timeSt = "AM. 0" + hour_start + "." + minute_start;
                       timeSt = "0" + hour_start + "." + minute_start;
                    }

                }else {
                    if (minute_end < 10){
                       // timeSt = "AM. " + hour_end + ".0" + minute_start;
                        timeSt = "" + hour_start + ".0" + minute_start;
                    } else {
                        //timeSt = "AM. " + hour_end + "." + minute_start;
                        timeSt = "" + hour_start + "." + minute_start;
                    }


                }
            }
            btnstartdate.setText(dateSt + ", " + timeSt);
        }else if (state == true){

            hour_end = i;
            minute_end =i1;
            // Toast.makeText(AddingActivity.this,"PM" + year_end+" "+month_end+
            //         " "+day_end+" "+hour_end+" "+minute_end,Toast.LENGTH_LONG).show();
            if (month_end < 10){
                dateEd = year_end + "-0" +  month_end + "-" + day_end;
            }else {
                dateEd = year_end + "-" + month_end + "-" + day_end;
            }
            if (hour_end >= 12){

                int hour_end_PM = hour_end -12;
                if (hour_end_PM < 10){
                    if (minute_start < 10){
                       // timeEd = "PM. 0" + hour_end_PM + ".0" + minute_end;
                        timeEd = "" + hour_end + ".0" + minute_end;
                    } else {
                       // timeEd = "PM. 0" + hour_end_PM + "." + minute_end;
                        timeEd = "" + hour_end + "." + minute_end;
                    }

                }else {
                    if (minute_start < 10){
                       // timeEd = "PM. " + hour_end_PM + ".0" + minute_end;
                        timeEd = "" + hour_end + ".0" + minute_end;
                    } else {
                        // timeEd = "PM. " + hour_end_PM + "." + minute_end;
                        timeEd = "" + hour_end + "." + minute_end;
                    }

                }

            }else {
                if (hour_end < 10){
                    if (minute_start < 10){
                       // timeEd = "AM. 0" + hour_end + ".0" + minute_end;
                        timeEd = "0" + hour_end + ".0" + minute_end;
                    } else {
                       // timeEd = "AM. 0" + hour_end + "." + minute_end;
                       timeEd = "0" + hour_end + "." + minute_end;
                    }

                }else {
                    if (minute_end < 10){
                        //timeEd = "AM. " + hour_end + ".0" + minute_end;
                        timeEd = "" + hour_end + ".0" + minute_end;
                    } else {
                       // timeEd = "AM. " + hour_end + "." + minute_end;
                        timeEd = "" + hour_end + "." + minute_end;
                    }


                }
            } btnenddate.setText(dateEd + ", " + timeEd);
        }
    }
}

