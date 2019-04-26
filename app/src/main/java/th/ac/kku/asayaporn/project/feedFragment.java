package th.ac.kku.asayaporn.project;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class feedFragment extends Fragment {
    protected ListView mListView;
    protected CustomAdapter mAdapter;
    protected Button butadding;
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
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP|ActionBar.DISPLAY_SHOW_CUSTOM);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.actionbar_feed);
        setHasOptionsMenu(true);
        mListView = (ListView) view.findViewById(R.id.feedlistview);
        butadding = (Button) view.findViewById(R.id.AddActiv);
        editSearch = (EditText) getActivity().findViewById(R.id.edit_search);
        editSearch.setText("");
        butadding.setVisibility(View.GONE);
        new JsonTask().execute("https://www.kku.ac.th/ikku/api/activities/services/topActivity.php");


        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                ActivityKKU activityKKU;
                final ArrayList<ActivityKKU> arra=new ArrayList<>();
                if(editSearch.length()!=0) {
                    for(int i=0;i<mAdapter.getCount();i++){
                        activityKKU= (ActivityKKU) mAdapter.getItem(i);

                        if( activityKKU.getTitle().toString().contains(s)){

                            arra.add(activityKKU);

                        }
                        mListView.setAdapter(new CustomAdapter(getActivity(), arra));
                    }
                }else{

                    mListView.setAdapter(mAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        database = FirebaseDatabase.getInstance();
         myRef = database.getReference("activities");

        butadding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url="thisis url";
                String image="this is image";
                String title="titleeeee";
                String place="placeeee";
                String content="contenttttt";
                String dateSt="datestttt";
                String dateEd="dateddd";
                String phone="honeee";
                String website="siteeee";
                String sponsor="sponsorrrrr";
                writeNewPost(null, url, image,title,place,content,
                        dateSt,dateEd,phone,website,sponsor);

            }
        });


        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            private String TAG = "TAGGGG";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                Log.d(TAG, "Value is: " + map);
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
        ActivityKKU post = new ActivityKKU(contact, url, image,title,place,content,
                                            dateSt,dateEd,phone,website,sponsor);
        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, postValues);

        myRef.updateChildren(childUpdates);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_feed, menu);
        super.onCreateOptionsMenu(menu,inflater);
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
            final SharedPreferences sp ;
            final SharedPreferences.Editor editor ;
            sp = getContext().getSharedPreferences("USER", Context.MODE_PRIVATE);
            editor = sp.edit();

         //   final EditText title = (EditText)view.findViewById(R.id.eName1);
         //   final EditText type = (EditText)view.findViewById(R.id.eType);
         //   final EditText quantity = (EditText)view.findViewById(R.id.eQuan);
          //  final EditText unit = (EditText)view.findViewById(R.id.eUnit);
          //  final EditText calories = (EditText)view.findViewById(R.id.eCAl1);



            builder.setPositiveButton("เพิ่ม", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {



                    SharedPreferences sp ;
                    SharedPreferences.Editor editor ;



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
        if(id==R.id.action_sortbydatelates){
            ActivityKKU activityKKU;
            final ArrayList<ActivityKKU> arra=new ArrayList<>();



            for(int i = 0; i<mAdapter.getCount(); i++){
                activityKKU=(ActivityKKU) mAdapter.getItem(i);
                arra.add(activityKKU);

            }


            Collections.sort(arra,new Comparator<ActivityKKU>() {
                @Override
                public int compare(ActivityKKU p0, ActivityKKU p1) {
                    SimpleDateFormat formatter2=new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = null;
                    Date date2=null;
                    try {
                        date1=formatter2.parse(p0.getDateSt());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        date2=formatter2.parse(p1.getDateSt());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (date1.compareTo(date2)<0) {
                        return 1;
                    } else {
                        return -1;
                    }

                }
            });
            mListView.setAdapter(new CustomAdapter(getActivity(), arra));
            return true;
        }
        if(id==R.id.action_sortbydateleast){
            ActivityKKU activityKKU;
            final ArrayList<ActivityKKU> arra=new ArrayList<>();



            for(int i = 0; i<mAdapter.getCount(); i++){
                activityKKU=(ActivityKKU) mAdapter.getItem(i);
                arra.add(activityKKU);

            }


            Collections.sort(arra,new Comparator<ActivityKKU>() {
                @Override
                public int compare(ActivityKKU p0, ActivityKKU p1) {
                    SimpleDateFormat formatter2=new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = null;
                    Date date2=null;
                    try {
                        date1=formatter2.parse(p0.getDateSt());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        date2=formatter2.parse(p1.getDateSt());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (date1.compareTo(date2)<0) {
                        return -1;
                    } else {
                        return 1;
                    }

                }
            });
            mListView.setAdapter(new CustomAdapter(getActivity(), arra));
            return true;
        }
        if(id==R.id.action_sortbytitle1)
        {
            ActivityKKU activityKKU;
            final ArrayList<ActivityKKU> arra=new ArrayList<>();



            for(int i = 0; i<mAdapter.getCount(); i++){
                activityKKU=(ActivityKKU) mAdapter.getItem(i);
                arra.add(activityKKU);

            }


            Collections.sort(arra,new Comparator<ActivityKKU>() {
                @Override
                public int compare(ActivityKKU p0, ActivityKKU p1) {
                    return  p1.getTitle().compareTo(p0.getTitle());

                }
            });
            mListView.setAdapter(new CustomAdapter(getActivity(), arra));
            return true;
        }
        if(id==R.id.action_sortbytitle2)
        {
            ActivityKKU activityKKU;
            final ArrayList<ActivityKKU> arra=new ArrayList<>();



            for(int i = 0; i<mAdapter.getCount(); i++){
                activityKKU=(ActivityKKU) mAdapter.getItem(i);
                arra.add(activityKKU);

            }


            Collections.sort(arra,new Comparator<ActivityKKU>() {
                @Override
                public int compare(ActivityKKU p0, ActivityKKU p1) {
                    return  p0.getTitle().compareTo(p1.getTitle());

                }
            });
            mListView.setAdapter(new CustomAdapter(getActivity(), arra));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    ProgressDialog pd;

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(getContext());
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()) {
                pd.dismiss();
            }
            JSONObject obj = null;
            try {
                obj = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            showData(new String(String.valueOf(obj)));
        }
    }
    private void showData(String json) {
        Gson gson = new Gson();
        Blog blog = gson.fromJson(json, Blog.class);
        List<ActivityKKU> activity = blog.getActivities();


        mAdapter = new CustomAdapter(this.getActivity(), activity);

        mListView.setAdapter(mAdapter);


    }
}


