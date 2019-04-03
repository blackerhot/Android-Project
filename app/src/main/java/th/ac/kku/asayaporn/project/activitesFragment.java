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
import android.widget.TextView;

import java.util.ArrayList;

public class activitesFragment extends  Fragment {
    public TextView testtv;
    public Button btn_go_calen;
    Intent intentother = null;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activites,container,false);
        /*
        btn_go_calen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentother = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://gsuite.google.com/intl/en_id/products/calendar/?utm_source=google&utm_medium=cpc&utm_campaign=japac-TH-all-en-dr-bkws-all-golden-trial-e-dr-1003989&utm_content=text-ad-none-none-DEV_c-CRE_336301664879-ADGP_Hybrid%20%7C%20AW%20SEM%20%7C%20BKWS%20~%20EXA%20%7C%20Calendar%20%7C%20%5B1:1%5D%20%7C%20TH%20%7C%20EN%20%7C%20google.calendar-KWID_43700041781917229-kwd-380869074165-userloc_9073383&utm_term=KW_google.calendar&gclid=Cj0KCQjwpsLkBRDpARIsAKoYI8yZQggj7xEHcQhzdQUTlFKgHpciR4-JktbRa7YEEFwfxs7BK5-bPygaAk-SEALw_wcB&gclsrc=aw.ds"));
                startActivity(intentother);
                testtv.setText("5555");
            }
        }); */
        String[] lst = {"1","2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
        ListView listView = (ListView) view.findViewById(R.id.lst);
        listView.requestFocus();
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,lst
        );
        listView.setAdapter(listViewAdapter);
        return view;
    }
}
