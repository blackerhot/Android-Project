package th.ac.kku.asayaporn.project;

import android.app.Activity;
import android.app.MediaRouteButton;
import android.app.assist.AssistContent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class profileFragment extends Fragment {
    private FirebaseAuth mAuth;
    String email="";
    String uid="";
    String disname ="";
    String url_photo = "";
    FirebaseUser currentFirebaseUser ;
    ArrayList<ExampleItem> mExampleList;
    TextView tv_num_favorite;
    TextView tv_num_event;

    FirebaseDatabase database;
    DatabaseReference myRef;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle saveInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        getActivity().setTitle("Management");
        ((AppCompatActivity) getActivity()).getSupportActionBar().
                setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
        TextView emailtv = (TextView) view.findViewById(R.id.emailTv);
        final TextView statusTv = (TextView) view.findViewById(R.id.statusTv);
        Button btn = (Button) view.findViewById(R.id.logoutBut);
        Button adminBut = (Button) view.findViewById(R.id.adminBut);
        TextView tv_name_user = (TextView) view.findViewById(R.id.tv_name_user);
        CircleImageView user_photo = (CircleImageView) view.findViewById(R.id.user_photo_id);

        final LinearLayout layoutmanage = (LinearLayout) view.findViewById(R.id.layoutmanage);
        final LinearLayout layoutadmon = (LinearLayout) view.findViewById(R.id.layoutadmin);
        mAuth = FirebaseAuth.getInstance();
        currentFirebaseUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("user");
        layoutadmon.setVisibility(View.INVISIBLE);
        layoutmanage.setVisibility(View.INVISIBLE);
        myRef.addValueEventListener(new ValueEventListener() {
            private String TAG = "TAGGGG";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (currentFirebaseUser != null) {


                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        if (data.child("email").getValue().toString()
                                .equals(currentFirebaseUser.getEmail())) {

                                if(data.child("status").getValue()!=null){
                                    if (data.child("status").getValue().toString().equals("admin")) {
                                        statusTv.setText("Administrator");

                                        layoutmanage.setVisibility(View.VISIBLE);
                                        layoutadmon.setVisibility(View.VISIBLE);
                                    } else if (data.child("status").getValue().toString().equals("mod")) {
                                        statusTv.setText("Moderator");
                                        layoutadmon.setVisibility(View.VISIBLE);
                                    }
                                }else {
                                    statusTv.setText("User");
                                    layoutadmon.setVisibility(View.INVISIBLE);
                                }

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        if (currentFirebaseUser == null) {
            view = inflater.inflate(R.layout.activity_request_login, container, false);
            Button btn_login_re = (Button) view.findViewById(R.id.btn_requ_login_id);
            btn_login_re.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), Login.class));
                }
            });
            return view;
        }
        try {
            if (currentFirebaseUser.getDisplayName() == null) {
                email = currentFirebaseUser.getEmail();
                uid = currentFirebaseUser.getUid();
                disname = "unknown";
            } else {
                for (UserInfo userInfo : currentFirebaseUser.getProviderData()) {
                    if (userInfo.getProviderId().equals("facebook.com")) {
                        Toast.makeText(getContext(), userInfo.getProviderId().toString(), Toast.LENGTH_SHORT).show();
                    } else if (userInfo.getProviderId().equals("google.com")) {
                        Toast.makeText(getContext(), userInfo.getProviderId().toString(), Toast.LENGTH_SHORT).show();
                    } else if (userInfo.getProviderId().equals("firebase")) {
                        Toast.makeText(getContext(), userInfo.getProviderId().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                url_photo = currentFirebaseUser.getPhotoUrl().toString();
                disname = currentFirebaseUser.getDisplayName();
                email = currentFirebaseUser.getEmail();
                uid = currentFirebaseUser.getUid();
                if (url_photo.equals("")) {

                } else {
                    Picasso.get().load(url_photo).into(user_photo);
                }
            }
        } catch (Exception e) {
            email = currentFirebaseUser.getEmail();
            uid = currentFirebaseUser.getUid();
            disname = "unknown";
        }



        tv_name_user.setText(disname);
        emailtv.setText(email);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FacebookSdk.setApplicationId("302892413797138");
                FacebookSdk.sdkInitialize(getContext());
                mAuth.signOut();
                LoginManager.getInstance().logOut();
                AccessToken.setCurrentAccessToken(null);//logout facebook
                // settings.edit().remove("LOGIN").commit();

                startActivity(new Intent(getContext(), MainActivity.class));

            }
        });

        adminBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AdminActivity.class));
            }
        });
        loadData();
        tv_num_event = (TextView) view.findViewById(R.id.num_event_tv);
        tv_num_favorite = (TextView) view.findViewById(R.id.num_favorite_tv);
        String event_num_str = String.valueOf(mExampleList.size());
        tv_num_event.setText(event_num_str);
        return view;


    }

    private void loadData() {
        SharedPreferences sharedPreferences =  this.getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<ExampleItem>>() {}.getType();
        mExampleList = gson.fromJson(json, type);

        if (mExampleList == null) {
            mExampleList = new ArrayList<>();
        }
    }

}