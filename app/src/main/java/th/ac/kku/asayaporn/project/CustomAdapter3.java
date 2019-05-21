package th.ac.kku.asayaporn.project;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

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
        TextView email;
        TextView classes;
        Button admin;
        Button mod;
        Button user;

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

            mViewHolder.admin = (Button) convertView.findViewById(R.id.adminr);
            mViewHolder.user = (Button) convertView.findViewById(R.id.userr);
            mViewHolder.mod = (Button) convertView.findViewById(R.id.modr);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mActivite = mActivites.get(position);

        mViewHolder.uid.setText("uid : " + mActivite.uid);
        mViewHolder.email.setText("email : " + mActivite.email);
        mViewHolder.classes.setText("classes : " + mActivite.classes);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("user");

        mViewHolder.admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivite = mActivites.get(position);
                Map<String, Object> childUpdates = new HashMap<>();

                if (mActivite.email.equals("knwknw1@gmail.com")) {
                    return;
                }

                mActivite.setClasses("admin");
                mViewHolder.classes.setText("classes : " + mActivite.classes);
                childUpdates.put("/" + mActivite.uid + "/classes", "admin");
                myRef.updateChildren(childUpdates);

            }
        });
        mViewHolder.mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivite = mActivites.get(position);
                Map<String, Object> childUpdates = new HashMap<>();

                if (mActivite.email.equals("knwknw1@gmail.com")) {
                    return;
                }

                mActivite.setClasses("mod");
                mViewHolder.classes.setText("classes : " + mActivite.classes);
                childUpdates.put("/" + mActivite.uid + "/classes", "mod");
                myRef.updateChildren(childUpdates);
            }
        });
        mViewHolder.user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivite = mActivites.get(position);
                Map<String, Object> childUpdates = new HashMap<>();

                if (mActivite.email.equals("knwknw1@gmail.com")) {
                    return;
                }
                mActivite.setClasses("user");
                mViewHolder.classes.setText("classes : " + mActivite.classes);
                childUpdates.put("/" + mActivite.uid + "/classes", "user");
                myRef.updateChildren(childUpdates);
            }
        });


        Button deleteBut = (Button) convertView.findViewById(R.id.deleteBut);

        deleteBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(mInflater.getContext());
                alert.setTitle("Delete");
                alert.setMessage("Are you sure you want to delete?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        mActivite = mActivites.get(position);
                        if(!mActivite.email.equals("knwknw1@gmail.com")){

                        myRef.child(mActivite.uid).removeValue();
                        mActivite.setEmail("ลบเรียบร้อย");
                        mActivites.remove(position);
                        Toast.makeText(mInflater.getContext(),"Deleted Already!",Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(mInflater.getContext(),"Failed! to Delete",Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();


            }
        });

        return convertView;
    }

}
