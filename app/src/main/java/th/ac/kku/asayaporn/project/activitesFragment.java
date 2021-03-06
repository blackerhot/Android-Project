package th.ac.kku.asayaporn.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.facebook.FacebookSdk.getApplicationContext;

public class activitesFragment extends Fragment {
    public TextView testtv;
    public Button btn_go_calen;
    Intent intentother = null;
    ArrayList<ExampleItem> mExampleList;
    ArrayList<ExampleItem> readfirebaselist = null;
    int year, month, day, aftermonth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference myRef2;
    FirebaseUser currentFirebaseUser;
    private FirebaseAuth mAuth;
    ConnectivityManager manager;
    private static final String TAG = "MainActivity";
    String today_str;
    String after_two_month_str;
    Date strDate = null;
    Date endDate = null;
    Date todate = null;
    Date after_two_monthdate = null;
    TextView todayTv;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activites, container, false);
        final SwipeMenuListView today_event_listView;
        final SwipeMenuListView all_ev_listview;
        today_event_listView = (SwipeMenuListView) view.findViewById(R.id.lst);
        all_ev_listview = (SwipeMenuListView) view.findViewById(R.id.all_swip);
        todayTv = (TextView) view.findViewById(R.id.tv_to_day);
        getActivity().setTitle("ํActivities");
        ((AppCompatActivity) getActivity()).getSupportActionBar().
                setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
        setHasOptionsMenu(true);
        SharedPreferences sharedPreferences = this.getActivity().
                getSharedPreferences("shared preferences", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        mAuth = FirebaseAuth.getInstance();
        currentFirebaseUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        if (currentFirebaseUser != null) {
            myRef2 = database.getReference().child("user").child(currentFirebaseUser.getUid()).child("Activities_me");


            myRef2.addValueEventListener(new ValueEventListener() {
                private String TAG = "TAGGGG";

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.

                    if (currentFirebaseUser != null) {
                        String s0 = "";
                        Gson gson = new Gson();
                        String s1 = "";
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            //Here you can access the child.getKey()
                            ExampleItem user = child.getValue(ExampleItem.class);
                            s0 += gson.toJson(user.toMap()) + ",";
                            s1 += gson.toJson(user.toMap()) + ",";
                        }
                        if (s0.length() != 0) {
                            s0 = s0.substring(0, s0.length() - 1);
                            s1 = s1.substring(0, s1.length() - 1);


                            Type type = new TypeToken<ArrayList<ExampleItem>>() {}.getType();
                            mExampleList = gson.fromJson("[" + s1 + "]", type);
                            Log.e("activi", "[" + s1 + "]");
                            if(mExampleList==null){
                                mExampleList = new ArrayList<>();
                            }

                        }
                        final List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
                        final List<HashMap<String, String>> bList = new ArrayList<HashMap<String, String>>();
                        // LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.bgActivity);
                        ExampleItem all_event_item;
                        ExampleItem today_event_item;
                        Calendar c = Calendar.getInstance();
                        year = c.get(Calendar.YEAR);
                        month = c.get(Calendar.MONTH);
                        month = month + 1;
                        aftermonth = month - 2;
                        day = c.get(Calendar.DAY_OF_MONTH);
                        today_str = year + "-" + month + "-" + day;
                        todayTv.setText("Today is " + today_str);
                        after_two_month_str = year + "-" + aftermonth + "-" + day;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                        final ArrayList<ExampleItem> all_exampe = new ArrayList<>();
                        final ArrayList<ExampleItem> to_day_exampe = new ArrayList<>();


                        if (mExampleList != null) {

                            for (int i = 0; i < mExampleList.size(); i++) {
                                try {
                                    Log.e("actiti", mExampleList.get(i).getTitle() + "");
                                    strDate = sdf.parse(mExampleList.get(i).getDatest());
                                    endDate = sdf.parse(mExampleList.get(i).getDateend());
                                    after_two_monthdate = sdf.parse(after_two_month_str);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                try {

                                    if (after_two_monthdate.after(strDate)) {
                                        if (after_two_monthdate.after(endDate)) {
                                            mExampleList.remove(i);
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        if (mExampleList != null) {
                            for (int i = 0; i < mExampleList.size(); i++) {
                                try {
                                    strDate = sdf.parse(mExampleList.get(i).getDatest());
                                    endDate = sdf.parse(mExampleList.get(i).getDateend());
                                    todate = sdf.parse(today_str);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    if (strDate.equals(todate)) {
                                        today_event_item = (ExampleItem) mExampleList.get(i);
                                        to_day_exampe.add(today_event_item);

                                    } else if (strDate.before(todate)) {
                                        if (endDate.after(todate)) {
                                            today_event_item = (ExampleItem) mExampleList.get(i);
                                            to_day_exampe.add(today_event_item);
                                        } else if (endDate.equals(todate)) {
                                            today_event_item = (ExampleItem) mExampleList.get(i);
                                            to_day_exampe.add(today_event_item);
                                        }

                                    } else if (strDate.after(todate)) {
                                        if (endDate.after(todate)) {
                                            all_event_item = (ExampleItem) mExampleList.get(i);
                                            all_exampe.add(all_event_item);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                        Collections.sort(to_day_exampe, new Comparator<ExampleItem>() {
                            @Override
                            public int compare(ExampleItem t0, ExampleItem t1) {
                                SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH.SS");
                                Date time1 = null;
                                Date time2 = null;
                                try {
                                    time1 = formatter2.parse(t0.getDatest() + " " + t0.getDatest());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    time2 = formatter2.parse(t1.getDatest() + " " + t1.getDatest());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    if (time1.compareTo(time2) <= 0) {
                                        return -1;
                                    } else {
                                        return 1;
                                    } }catch (Exception e) {

                                    e.printStackTrace();
                                }
                                return 1;

                            }
                        });
                        Collections.sort(all_exampe, new Comparator<ExampleItem>() {
                            @Override
                            public int compare(ExampleItem p0, ExampleItem p1) {
                                SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH.SS");
                                Date date1 = null;
                                Date date2 = null;
                                try {
                                    date1 = formatter2.parse(p0.getDatest() + " " + p0.getDatest());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    date2 = formatter2.parse(p1.getDatest() + " " + p1.getDatest());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    if (date1.compareTo(date2) < 0) {
                                        return -1;
                                    } else {
                                        return 1;
                                    }
                                } catch (Exception e) {

                                    e.printStackTrace();
                                }


                                return 1;
                            }
                        });
                        if (to_day_exampe.size() != 0) {


                            for (int i = 0; i < to_day_exampe.size(); i++) {
                                HashMap<String, String> hm_td = new HashMap<String, String>();
                                hm_td.put("listview_datestr", to_day_exampe.get(i).getTimestart());
                                hm_td.put("listview_title", to_day_exampe.get(i).getTitle());
                                hm_td.put("listview_comment", to_day_exampe.get(i).getDetail());
                                bList.add(hm_td);
                            }
                        }
                        if (all_exampe.size() != 0) {
                            for (int i = 0; i < all_exampe.size(); i++) {
                                HashMap<String, String> hm = new HashMap<String, String>();
                                hm.put("listview_timestr", all_exampe.get(i).getTimestart());
                                hm.put("listview_datestr", all_exampe.get(i).getDatest());
                                hm.put("listview_title", all_exampe.get(i).getTitle());
                                hm.put("listview_comment", all_exampe.get(i).getDetail());
                                aList.add(hm);
                            }
                        }

                        String[] from = {"listview_datestr", "listview_title", "listview_comment"};
                        final int[] to = {R.id.lstTime, R.id.lstTitle, R.id.lstItems};
                        String[] from_other_event = {"listview_timestr", "listview_datestr", "listview_title", "listview_comment"};
                        final int[] to_other_event = {R.id.lsttime_other_event, R.id.lstdate_other_event, R.id.lstTitle_other_event, R.id.lstItems_other_event};

                        final SimpleAdapter aAdapter = new SimpleAdapter(inflater.getContext(),
                                aList, R.layout.listview_other_event, from_other_event, to_other_event);
                        final SimpleAdapter bAdapter = new SimpleAdapter(inflater.getContext(),
                                bList, R.layout.listview_activity, from, to);

                        all_ev_listview.setAdapter(aAdapter);
                        today_event_listView.setAdapter(bAdapter);
                        // class
                        ListUtils.setDynamicHeight(all_ev_listview);
                        ListUtils.setDynamicHeight(today_event_listView);

                        SwipeMenuCreator creator = new SwipeMenuCreator() {
                            SwipeMenuItem deleteItem;

                            @Override
                            public void create(SwipeMenu menu) {
                                // deleteItem
                                deleteItem = new SwipeMenuItem(getContext());
                                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xcc, 0x00, 0x00)));
                                deleteItem.setWidth(180);

                                deleteItem.setTitle("Delete");
                                deleteItem.setTitleSize(12);
                                deleteItem.setTitleColor(Color.WHITE);
                                menu.addMenuItem(deleteItem);
                            }
                        };

                        all_ev_listview.setMenuCreator(creator);
                        all_ev_listview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                                switch (index) {
                                    case 0:
                                        all_ev_listview.smoothOpenMenu(position);
                                        for (int i = 0; i < mExampleList.size(); i++) {
                                            if (aList.get(position).get("listview_timestr").equals(mExampleList.get(i).getTimestart())) {
                                                if (aList.get(position).get("listview_title").equals(mExampleList.get(i).getTitle())) {


                                                    String key = mExampleList.get(i).getTitle();

                                                    myRef2.child(key).removeValue();

                                                    mExampleList.remove(i);

                                                }
                                            }
                                        }
                                        aList.remove(position);
                                        all_ev_listview.setAdapter(aAdapter);
                                        ListUtils.setDynamicHeight(all_ev_listview);

                                        break;
                  /*  case 1:
                        Log.d(TAG, "onMenuItemClick: clicked item " + index);
                        break; */
                                }
                                // false : close the menu; true : not close the menu
                                return false;
                            }


                        });
                        today_event_listView.setMenuCreator(creator);
                        today_event_listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                                switch (index) {
                                    case 0:
                                        today_event_listView.smoothOpenMenu(position);
                                        for (int i = 0; i < mExampleList.size(); i++) {
                                            if (bList.get(position).get("listview_datestr").equals(mExampleList.get(i).getTimestart())) {
                                                if (bList.get(position).get("listview_title").equals(mExampleList.get(i).getTitle())) {
                                                    String key = mExampleList.get(i).getTitle();

                                                    myRef2.child(key).removeValue();

                                                    mExampleList.remove(i);

                                                }
                                            }
                                        }
                                        bList.remove(position);
                                        today_event_listView.setAdapter(bAdapter);
                                        ListUtils.setDynamicHeight(today_event_listView);
                                        break;
                  /*  case 1:
                        Log.d(TAG, "onMenuItemClick: clicked item " + index);
                        break; */
                                }

                                return false;
                            }
                        });
                        today_event_listView.setCloseInterpolator(new BounceInterpolator());
                        today_event_listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
                            @Override
                            public void onSwipeStart(int position) {

                            }

                            @Override
                            public void onSwipeEnd(int position) {

                            }
                        });


                    }


                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });


        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_activity, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_add_event_me) {
            startActivity(new Intent(getActivity(), AddActivityForme.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }
}