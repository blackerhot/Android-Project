package th.ac.kku.asayaporn.project;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ItemActivity extends AppCompatActivity {
    TextView address;
    TextView datest;
    TextView detail;
    ImageView pic;
    TextView title;
    TextView phone;
    TextView website;
    TextView sponser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activites_kkudialog);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pic = (ImageView) findViewById(R.id.post_pic);
        title = (TextView) findViewById(R.id.post_title);
        detail = (TextView) findViewById(R.id.post_detail);
        datest = (TextView) findViewById(R.id.post_date);
        phone = (TextView) findViewById(R.id.post_phone);
        website = (TextView) findViewById(R.id.post_website);
       sponser = (TextView) findViewById(R.id.post_sponser);
        address = (TextView) findViewById(R.id.post_address);

        Bundle para = getIntent().getExtras();

        Picasso.get().load(para.getString("img")).into(pic);//wait for img
        title.setText(para.getString("title"));
        getSupportActionBar().setTitle("");

        website.setText(para.getString("website"));
        sponser.setText(para.getString("sponsor"));
        detail.setText(para.getString("detail"));
        datest.setText(para.getString("datest") +" ,  "+ para.getString("dateend"));
        phone.setText(para.getString("phone"));
        address.setText(para.getString("address"));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

