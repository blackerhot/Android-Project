package th.ac.kku.asayaporn.project;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class activitesFragment extends  Fragment {
    public TextView testtv;
    public Button btn_go_calen;
    Intent intentother = null;

    String[] lstTime = new String[]{
            "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00","18:00","19:00"
    };

    String[] lstTitle = new String[]{
            "Android ListView ", "Android ListView ", "Android ListView ", "Android ListView ",
            "Android ListView ", "Android ListView ", "Android ListView ", "Android ListView ",
            "Android ListView ", "Android ListView "
    };
    String[] lstItems = new String[]{
            "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description",
            "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description",
            "Android ListView Short Description", "Android ListView Short Description"
    };
    private static final String TAG = "MainActivity";




    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activites,container,false);
        SwipeMenuListView listView;
        listView = (SwipeMenuListView) view.findViewById(R.id.lst);


        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
        // LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.bgActivity);

        for (int i = 0; i < 10; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title", lstTime[i]);
            hm.put("listview_discription", lstTitle[i]);
            hm.put("listview_item", lstItems[i]);
              /*  if( i % 2 ==0) {
                    linearLayout.setBackgroundResource(R.drawable.bgnotify2);
                } else{
                    linearLayout.setBackgroundResource(R.drawable.bgnotify);
                } */
            aList.add(hm);
        }

        String[] from = { "listview_title", "listview_discription", "listview_item"};
        int[] to = {R.id.lstTime, R.id.lstTitle, R.id.lstItems};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), aList, R.layout.listview_activity, from, to);
        listView.setAdapter(simpleAdapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // deleteItem
                SwipeMenuItem deleteItem = new SwipeMenuItem(getContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xcc, 0x00, 0x00)));
                deleteItem.setWidth(120);
                deleteItem.setTitle("Delete");
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:

                        Log.d(TAG, "onMenuItemClick: clicked item " + index);
                        break;
                  /*  case 1:
                        Log.d(TAG, "onMenuItemClick: clicked item " + index);
                        break; */
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        return view;
    }
}
