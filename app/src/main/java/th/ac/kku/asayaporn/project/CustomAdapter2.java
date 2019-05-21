package th.ac.kku.asayaporn.project;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.file.Files.copy;

public class CustomAdapter2 extends BaseAdapter {

    FirebaseDatabase database;
    DatabaseReference myRef;
    private static final String TAG = "D";
    protected LayoutInflater mInflater;
    protected ViewHolder mViewHolder;
    List<ActivityKKU> mActivites;
    Activity activity;
    protected ActivityKKU mActivite;


    public CustomAdapter2(Activity activity, List<ActivityKKU> posts) {
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
        TextView address;
        TextView datest;
        TextView detail;
        ImageView pic;
        TextView title;
        TextView statusTv;
        TextView phone;
        TextView website;
        TextView sponser;
        Button buton;
        Button butoff;
        TextView key;

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
            convertView = mInflater.inflate(R.layout.activites_kku_approve, parent, false);
            mViewHolder = new ViewHolder();
            mViewHolder.pic = (ImageView) convertView.findViewById(R.id.post_pic);
            mViewHolder.title = (TextView) convertView.findViewById(R.id.post_title);
            mViewHolder.detail = (TextView) convertView.findViewById(R.id.post_detail);
            mViewHolder.datest = (TextView) convertView.findViewById(R.id.post_date);
            mViewHolder.key = (TextView) convertView.findViewById(R.id.keyTv);
            mViewHolder.statusTv = (TextView) convertView.findViewById(R.id.stateTv);
            //  mViewHolder.dateend = (TextView) convertView.findViewById(R.id.post_dateend);
            // mViewHolder.phone = (TextView) convertView.findViewById(R.id.post_phone);
            //  mViewHolder.website = (TextView) convertView.findViewById(R.id.post_website);
            //  mViewHolder.sponser = (TextView) convertView.findViewById(R.id.post_sponser);
            mViewHolder.address = (TextView) convertView.findViewById(R.id.post_address);
            mViewHolder.buton = (Button) convertView.findViewById(R.id.buton);
            mViewHolder.butoff = (Button) convertView.findViewById(R.id.butoff);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("activities");
        mActivite = mActivites.get(position);

            Picasso.get().load(mActivite.image).resize(2048, 1600).
                    onlyScaleDown().into(mViewHolder.pic);//wait for img
        mViewHolder.title.setText(mActivite.title);
        mViewHolder.address.setText("สถานที่จัดงาน : " + mActivite.place);
        mViewHolder.datest.setText("วันที่เริ่มงาน : " + mActivite.dateSt + " (" + mActivite.timeSt + ")");


        if(!mActivite.status.equals("pending")){
            if(new Boolean(mActivite.status)){
                mViewHolder.buton.setVisibility(View.GONE);
                mViewHolder.butoff.setVisibility(View.VISIBLE);
                mViewHolder.statusTv.setTextColor(Color.GREEN);
                mViewHolder.statusTv.setText("Status : Showing ");


            }else{

                mViewHolder.butoff.setVisibility(View.GONE);
                mViewHolder.buton.setVisibility(View.VISIBLE);
                mViewHolder.statusTv.setTextColor(Color.RED);
                mViewHolder.statusTv.setText("Status : Hiding ");
            }
        }else {
            mViewHolder.statusTv.setTextColor(Color.YELLOW);
            mViewHolder.butoff.setVisibility(View.VISIBLE);
            mViewHolder.buton.setVisibility(View.VISIBLE);
            Map<String,Object> childUpdates = new HashMap<>();
            childUpdates.put("/"+mActivite.key+"/status",String.valueOf(false));
            myRef.updateChildren(childUpdates);
        }


        mViewHolder.key.setText("Request By : "+mActivite.getBy());
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
                if(s0.length()!=0){
                    s0=s0.substring(0,s0.length()-1);
                }

                SharedPreferences sp;
                SharedPreferences.Editor editor;
                sp = mInflater.getContext().getSharedPreferences("USER", Context.MODE_PRIVATE);
                editor = sp.edit();
                editor.putString("jsonByUSER", new String(String.valueOf("{\"activities\":[" + s0 + "]}")));
                editor.commit();

            }
            @Override
            public void onCancelled(DatabaseError error) {

                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        mViewHolder.buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mInflater.getContext(),"Showing Already! wait a sec to refresh",Toast.LENGTH_SHORT).show();
                mActivite = mActivites.get(position);
                mActivite.setStatus("true");

                Map<String,Object> childUpdates = new HashMap<>();
                childUpdates.put("/"+mActivite.key+"/status",String.valueOf(true));
                myRef.updateChildren(childUpdates);
                mViewHolder.butoff.setVisibility(View.VISIBLE);
                mViewHolder.buton.setVisibility(View.GONE);

            }
        });

        mViewHolder.butoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mInflater.getContext(),"Hiding Already! wait a sec to refresh",Toast.LENGTH_SHORT).show();
                mActivite = mActivites.get(position);
                mActivite.setStatus("false");

                Map<String,Object> childUpdates = new HashMap<>();
                childUpdates.put("/"+mActivite.key+"/status",String.valueOf(false));
                myRef.updateChildren(childUpdates);
                mViewHolder.butoff.setVisibility(View.GONE);
                mViewHolder.buton.setVisibility(View.VISIBLE);

            }
        });



        Button editBut = (Button) convertView.findViewById(R.id.editBut);
        Button deleteBut = (Button) convertView.findViewById(R.id.deleteBut);
        TextView clickmore = (TextView) convertView.findViewById(R.id.clickmore);

        editBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditActivity.class);

                mActivite = mActivites.get(position);
                intent.putExtra("key",mActivite.key);
                v.getContext().startActivity(intent);


            }
        });
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
                        myRef.child(mActivite.key).removeValue();
                        mActivite.setTitle("ลบเรียบร้อย");
                        mActivites.remove(position);
                        Toast.makeText(mInflater.getContext(),"Deleted Already!",Toast.LENGTH_SHORT).show();
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
        clickmore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), ItemActivity2.class);

                mActivite = mActivites.get(position);

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
                v.getContext().startActivity(intent);

            }
        });
        return convertView;
    }

}
