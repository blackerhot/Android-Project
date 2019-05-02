package th.ac.kku.asayaporn.project;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.List;

import static java.nio.file.Files.copy;

public class CustomAdapter extends BaseAdapter {


    private static final String TAG = "D";
    protected LayoutInflater mInflater;
    protected ViewHolder mViewHolder;
    List<ActivityKKU> mActivites;
    Activity activity;
    protected ActivityKKU mActivite;


    public CustomAdapter(Activity activity, List<ActivityKKU> posts) {
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
        TextView dateend;
        TextView phone;
        TextView website;
        TextView sponser;


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
            convertView = mInflater.inflate(R.layout.activites_kku, parent, false);
            mViewHolder = new ViewHolder();
            mViewHolder.pic = (ImageView) convertView.findViewById(R.id.post_pic);
            mViewHolder.title = (TextView) convertView.findViewById(R.id.post_title);
            mViewHolder.detail = (TextView) convertView.findViewById(R.id.post_detail);
            mViewHolder.datest = (TextView) convertView.findViewById(R.id.post_date);
          //  mViewHolder.dateend = (TextView) convertView.findViewById(R.id.post_dateend);
           // mViewHolder.phone = (TextView) convertView.findViewById(R.id.post_phone);
          //  mViewHolder.website = (TextView) convertView.findViewById(R.id.post_website);
          //  mViewHolder.sponser = (TextView) convertView.findViewById(R.id.post_sponser);
            mViewHolder.address = (TextView) convertView.findViewById(R.id.post_address);

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mActivite = mActivites.get(position);


        Picasso.get().load(mActivite.image).into(mViewHolder.pic);//wait for img
        mViewHolder.title.setText(mActivite.title);
        mViewHolder.address.setText("สถานที่จัดงาน : " + mActivite.place);
        mViewHolder.datest.setText("วันที่เริ่มงาน : " + mActivite.dateSt);


        TextView clickmore = (TextView) convertView.findViewById(R.id.clickmore);
        clickmore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), ItemActivity.class);

                mActivite = mActivites.get(position);

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
                v.getContext().startActivity(intent);

            }
        });
        return convertView;
    }

}
