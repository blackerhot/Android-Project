package th.ac.kku.asayaporn.project;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static th.ac.kku.asayaporn.project.Login.TAG;

public class feedFragment extends Fragment {
    protected ListView mListView;
    protected CustomAdapter mAdapter;
    protected EditText editSearch;
    FirebaseDatabase database;
    DatabaseReference myRef;

    public static feedFragment newInstance() {

        Bundle args = new Bundle();
        feedFragment fragment = new feedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feed,
                container, false);
        getActivity().setTitle("Feed Activities");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.actionbar_feed);
        setHasOptionsMenu(true);
        mListView = (ListView) view.findViewById(R.id.feedlistview);
        TextView more = (TextView) view.findViewById(R.id.clickmore);
        editSearch = (EditText) getActivity().findViewById(R.id.edit_search);
        editSearch.setText("");


        SharedPreferences sp;
        SharedPreferences.Editor editor;

        sp = getContext().getSharedPreferences("USER", Context.MODE_PRIVATE);
        editor = sp.edit();
        showData(sp.getString("json", ""));
        editor.commit();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), ItemActivity.class);
                ActivityKKU mActivite = (ActivityKKU) parent.getItemAtPosition(position);



                intent.putExtra("img", String.valueOf(mActivite.image));
                intent.putExtra("title", String.valueOf(mActivite.title));
                intent.putExtra("address", String.valueOf(mActivite.place));
                intent.putExtra("detail", String.valueOf(mActivite.content));
                intent.putExtra("datest", String.valueOf(mActivite.dateSt));
                intent.putExtra("dateend", String.valueOf(mActivite.dateEd));
                intent.putExtra("website", String.valueOf(mActivite.url));
                intent.putExtra("sponsor", String.valueOf(mActivite.sponsor));

                if (mActivite.phone == null) {
                    intent.putExtra("phone", String.valueOf(mActivite.getContact().get("phone")));
                } else {
                    intent.putExtra("phone", String.valueOf(mActivite.phone));
                }
                view.getContext().startActivity(intent);


            }
        });


        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Boolean isnull = false;
                ActivityKKU activityKKU;
                ArrayList<String> str = new ArrayList<String>();
                str.add("ไม่พบคำที่ค้นหา");
                final ArrayList<ActivityKKU> arra = new ArrayList<>();
                if (editSearch.length() != 0) {
                    for (int i = 0; i < mAdapter.getCount(); i++) {
                        activityKKU = (ActivityKKU) mAdapter.getItem(i);

                        if (activityKKU.getTitle().toString().contains(s)) {
                            isnull=false;
                            arra.add(activityKKU);

                        }else{
                            isnull=true;

                        }

                        if(isnull){
                            mListView.setAdapter(new CustomAdapter(getActivity(), arra));
                        }else {
                            mListView.setAdapter(new CustomAdapter(getActivity(), arra));
                        }

                    }
                } else {

                    mListView.setAdapter(mAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("activities");


        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            private String TAG = "TAGGGG";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String name="";
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
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
        return view;
    }
    private void writeNewPost(JsonObject contact, String url, String image, String title,
                              String place, String content, String dateSt, String dateEd,
                              String phone, String website, String sponsor) {

        String key = myRef.child("activities").push().getKey();
        ActivityKKU post = new ActivityKKU(contact, url, image, title, place, content,
                dateSt, dateEd, phone, website, sponsor);
        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, postValues);
        myRef.updateChildren(childUpdates);
    }
    private void readNewUser() {
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String TAG="TAGGing";
                String name="";
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    name += (String) messageSnapshot.toString();
                }

                Toast.makeText(getContext(),
                        "You add is "+name,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ActivityKKU user = dataSnapshot.getValue(ActivityKKU.class);
                Toast.makeText(getContext(),
                        "You Changed is "+user.toMap().get("title"),Toast.LENGTH_LONG).show();

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_feed, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_adding) {
            final AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = LayoutInflater.from(getContext());

            final View view = inflater.inflate(R.layout.adding, null);
            builder.setView(view);


            final EditText etitle = (EditText) view.findViewById(R.id.etitle);
            final EditText eurl = (EditText) view.findViewById(R.id.eurl);
            final EditText ephone = (EditText) view.findViewById(R.id.ephone);
            final EditText eplace = (EditText) view.findViewById(R.id.eplace);
            final EditText edateend = (EditText) view.findViewById(R.id.edateend);
            final EditText edatest = (EditText) view.findViewById(R.id.edatest);
            final EditText esponsor = (EditText) view.findViewById(R.id.esponsor);
            final EditText ewebstie = (EditText) view.findViewById(R.id.ewebstie);
            final EditText econtent = (EditText) view.findViewById(R.id.econtent);


            builder.setPositiveButton("เพิ่ม", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String url = eurl.getText() + "เว็ปยังว่างเปล่า";
                    String image = eurl.getText() + "";
                    String title = etitle.getText() + "";
                    String place = eplace.getText() + "ยังว่าง";
                    String content = econtent.getText() + "ยังว่าง";
                    String dateSt = edatest.getText() + "ยังว่าง";
                    String dateEd = edateend.getText() + "ยังว่าง";
                    String phone = ephone.getText() + "ยังว่าง";
                    String website = ewebstie.getText() + "ยังว่าง";
                    String sponsor = esponsor.getText() + "ยังว่าง";

                    writeNewPost(null, url, image, title, place, content,
                            dateSt, dateEd, phone, website, sponsor);
                    readNewUser();
                }
            });
            builder.setNegativeButton("กลับ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                }
            });

            builder.show();

            return true;
        }
        if (id == R.id.action_sortbydatelates) {
            ActivityKKU activityKKU;
            final ArrayList<ActivityKKU> arra = new ArrayList<>();


            for (int i = 0; i < mAdapter.getCount(); i++) {
                activityKKU = (ActivityKKU) mAdapter.getItem(i);
                arra.add(activityKKU);

            }


            Collections.sort(arra, new Comparator<ActivityKKU>() {
                @Override
                public int compare(ActivityKKU p0, ActivityKKU p1) {
                    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = null;
                    Date date2 = null;
                    try {
                        date1 = formatter2.parse(p0.getDateSt());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        date2 = formatter2.parse(p1.getDateSt());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (date1.compareTo(date2) < 0) {
                        return 1;
                    } else {
                        return -1;
                    }

                }
            });
            mListView.setAdapter(new CustomAdapter(getActivity(), arra));
            return true;
        }
        if (id == R.id.action_sortbydateleast) {
            ActivityKKU activityKKU;
            final ArrayList<ActivityKKU> arra = new ArrayList<>();
            for (int i = 0; i < mAdapter.getCount(); i++) {
                activityKKU = (ActivityKKU) mAdapter.getItem(i);
                arra.add(activityKKU);
            }
            Collections.sort(arra, new Comparator<ActivityKKU>() {
                @Override
                public int compare(ActivityKKU p0, ActivityKKU p1) {
                    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = null;
                    Date date2 = null;
                    try {
                        date1 = formatter2.parse(p0.getDateSt());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        date2 = formatter2.parse(p1.getDateSt());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (date1.compareTo(date2) < 0) {
                        return -1;
                    } else {
                        return 1;
                    }

                }
            });
            mListView.setAdapter(new CustomAdapter(getActivity(), arra));
            return true;
        }
        if (id == R.id.action_sortbytitle1) {
            ActivityKKU activityKKU;
            final ArrayList<ActivityKKU> arra = new ArrayList<>();

            for (int i = 0; i < mAdapter.getCount(); i++) {
                activityKKU = (ActivityKKU) mAdapter.getItem(i);
                arra.add(activityKKU);
            }
            Collections.sort(arra, new Comparator<ActivityKKU>() {
                @Override
                public int compare(ActivityKKU p0, ActivityKKU p1) {
                    return p1.getTitle().compareTo(p0.getTitle());

                }
            });
            mListView.setAdapter(new CustomAdapter(getActivity(), arra));
            return true;
        }
        if (id == R.id.action_sortbytitle2) {
            ActivityKKU activityKKU;
            final ArrayList<ActivityKKU> arra = new ArrayList<>();
            for (int i = 0; i < mAdapter.getCount(); i++) {
                activityKKU = (ActivityKKU) mAdapter.getItem(i);
                arra.add(activityKKU);
            }
            Collections.sort(arra, new Comparator<ActivityKKU>() {
                @Override
                public int compare(ActivityKKU p0, ActivityKKU p1) {
                    return p0.getTitle().compareTo(p1.getTitle());

                }
            });
            mListView.setAdapter(new CustomAdapter(getActivity(), arra));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void showData(String json) {
        Gson gson = new Gson();
        Blog blog = gson.fromJson(json, Blog.class);
        List<ActivityKKU> activity = blog.getActivities();
        mAdapter = new CustomAdapter(this.getActivity(), activity);
        mListView.setAdapter(mAdapter);

    }
}


