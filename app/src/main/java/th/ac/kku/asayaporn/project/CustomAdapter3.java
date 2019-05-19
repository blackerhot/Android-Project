package th.ac.kku.asayaporn.project;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.file.Files.copy;

public class CustomAdapter3 extends BaseAdapter {

    FirebaseDatabase database;
    DatabaseReference myRef;
    private static final String TAG = "D";
    protected LayoutInflater mInflater;
    protected ViewHolder mViewHolder;
    List<ActivityUser> mActivites;
    Activity activity;
    protected ActivityUser mActivite;


    public CustomAdapter3(Activity activity, List<ActivityUser> posts) {
        activity = activity;
        mInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        mActivites = posts;
    }

    @Override
    public int getCount() {
        return mActivites.size();
    }

    private static class ViewHolder {
        TextView uid;
        RadioGroup rdg;
        TextView email;
        TextView classes;
        RadioButton admin;
        RadioButton mod;
        RadioButton user;

    }

    @Override
    public Object getItem(int position) {
        return mActivites.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.simplerow, parent, false);
            mViewHolder = new ViewHolder();
            mViewHolder.uid = (TextView) convertView.findViewById(R.id.uidTv);
            mViewHolder.email = (TextView) convertView.findViewById(R.id.emailTv);
            mViewHolder.classes = (TextView) convertView.findViewById(R.id.classesTV);
            mViewHolder.rdg = (RadioGroup) convertView.findViewById(R.id.rdg);
            mViewHolder.admin = (RadioButton) convertView.findViewById(R.id.adminr);
            mViewHolder.user = (RadioButton) convertView.findViewById(R.id.userr);
            mViewHolder.mod = (RadioButton) convertView.findViewById(R.id.modr);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mActivite = mActivites.get(position);

        mViewHolder.uid.setText("uid : " + mActivite.uid);
        mViewHolder.email.setText("email : " + mActivite.email);
        mViewHolder.classes.setText("classes : " + mActivite.classes);
        if (mActivite.classes != null) {
            if (mActivite.classes.equals("admin")) {
                mViewHolder.rdg.check(R.id.adminr);
            }
            if (mActivite.classes.equals("mod")) {
                mViewHolder.rdg.check(R.id.modr);
            }
            if (mActivite.classes.equals("user")) {
                mViewHolder.rdg.check(R.id.userr);
            }
        } else {
            mActivite.setClasses("user");
            if (mActivite.classes.equals("user")) {
                mViewHolder.rdg.check(R.id.userr);
            }
        }
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("user");

        mViewHolder.rdg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mActivite = mActivites.get(position);
                Map<String, Object> childUpdates = new HashMap<>();

                if (mActivite.email.equals("knwknw1@gmail.com")) {

                    return;
                }

                switch (checkedId) {
                    case R.id.adminr:
                        mActivite.setClasses("admin");
                        childUpdates.put("/" + mActivite.uid + "/classes", "admin");
                        break;
                    case R.id.modr:
                        mActivite.setClasses("mod");

                        childUpdates.put("/" + mActivite.uid + "/classes", "mod");
                        break;
                    case R.id.userr:
                        mActivite.setClasses("user");

                        childUpdates.put("/" + mActivite.uid + "/classes", "user");
                        break;
                    default:
                        break;
                }


                myRef.updateChildren(childUpdates);

            }
        });


        Button deleteBut = (Button) convertView.findViewById(R.id.deleteBut);

        deleteBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mActivite = mActivites.get(position);
                myRef.child(mActivite.uid).removeValue();
                mActivite.setEmail("ลบเรียบร้อย");
                mActivites.remove(position);

            }
        });

        return convertView;
    }

}
