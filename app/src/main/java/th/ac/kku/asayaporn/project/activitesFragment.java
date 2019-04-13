package th.ac.kku.asayaporn.project;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class activitesFragment extends  Fragment {
    public TextView testtv;
    public Button btn_go_calen;
    Intent intentother = null;

    int[] listviewImage = new int[]{
            R.drawable.ic_menu_camera1, R.drawable.ic_menu_camera2, R.drawable.ic_menu_camera3, R.drawable.ic_menu_camera4,
            R.drawable.ic_menu_camera5, R.drawable.ic_menu_camera6, R.drawable.ic_menu_camera7, R.drawable.ic_menu_camera8,R.drawable.ic_menu_camera9, R.drawable.ic_menu_camera10,
    };

    String[] listviewShortDescription = new String[]{
            "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description",
            "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description",
    };

    String[] listviewTitle = new String[]{
            "ListView Title 1", "ListView Title 2", "ListView Title 3", "ListView Title 4",
            "ListView Title 5", "ListView Title 6", "ListView Title 7", "ListView Title 8",
    };

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activites,container,false);


            List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

            for (int i = 0; i < 8; i++) {
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("listview_title", listviewTitle[i]);
                hm.put("listview_discription", listviewShortDescription[i]);
                hm.put("listview_image", Integer.toString(listviewImage[i]));
                aList.add(hm);
            }

            String[] from = {"listview_image", "listview_title", "listview_discription"};
            int[] to = {R.id.listview_image, R.id.listview_item_title, R.id.listview_item_short_description};

            SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), aList, R.layout.listview_activity, from, to);
            ListView androidListView = (ListView) view.findViewById(R.id.lst);
            androidListView.setAdapter(simpleAdapter);

        return view;
    }
}
