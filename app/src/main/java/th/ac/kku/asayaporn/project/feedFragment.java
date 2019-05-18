package th.ac.kku.asayaporn.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("activities");

        // Read from the database
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
                    ActivityKKU user = child.getValue(ActivityKKU.class);
                    s0 += gson.toJson(user.toMap()) + ",";
                }
                s0 = s0.substring(0, s0.length() - 1);
                Log.e("result1fromfirebase", s0);
                SharedPreferences sp;
                SharedPreferences.Editor editor;
                sp = getContext().getSharedPreferences("USER", Context.MODE_PRIVATE);
                editor = sp.edit();
                editor.putString("jsonByUSER", new String(String.valueOf("{\"activities\":[" + s0 + "]}")));
                editor.commit();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        SharedPreferences sp;
        SharedPreferences.Editor editor;
        sp = getContext().getSharedPreferences("USER", Context.MODE_PRIVATE);
        editor = sp.edit();
        String result1 = sp.getString("jsonByUSER", "");
        String result = sp.getString("json", "");
        result = result.replace("&quot;", "'");

        if(handlerData(result1)==""){
            String fromeall = "{\"activities\":[" ;
            fromeall += handlerData1(result) +"]}";
            Log.e("result1all", fromeall);
            showData(fromeall);
        }else {
            String fromeall = "{\"activities\":[" + handlerData(result1);
            fromeall +=","+  handlerData1(result) +"]}";
            Log.e("result1all", fromeall);
            showData(fromeall);
        }

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
                intent.putExtra("timest", String.valueOf(mActivite.timeSt));
                intent.putExtra("timeed", String.valueOf(mActivite.timeEd));
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
                            isnull = false;
                            arra.add(activityKKU);
                        } else {
                            isnull = true;

                        }
                        if (isnull) {
                            mListView.setAdapter(new CustomAdapter(getActivity(), arra));
                        } else {
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


        return view;
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

            startActivity(new Intent(getActivity(), AddingActivity.class));
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    private void showData(String json) {

        Gson gson = new Gson();
        Blog blog = gson.fromJson(json, Blog.class);
        List<ActivityKKU> activity = blog.getActivities();
        mAdapter = new CustomAdapter(this.getActivity(), activity);
        mListView.setAdapter(mAdapter);

    }

    private String handlerData(String json) {

        String string = "";
        Gson gson = new Gson();

        Blog blog = gson.fromJson(json, Blog.class);
        List<ActivityKKU> activity = blog.getActivities();

        for (int i = 0; i < activity.size(); i++) {
            if(activity.get(i).getStatus()==true){
                string += gson.toJson(activity.get(i).toMap())+",";
            }

        }
        if(string.length()==0){
            return "";
        }
        string = string.substring(0, string.length() - 1);
        return string;

    }

    private String handlerData1(String json) {

        String string = "";
        Gson gson = new Gson();

        Blog blog = gson.fromJson(json, Blog.class);
        List<ActivityKKU> activity = blog.getActivities();

        for (int i = 0; i < activity.size(); i++) {

                string += gson.toJson(activity.get(i).toMap())+",";


        }

        string = string.substring(0, string.length() - 1);
        return string;

    }
}


