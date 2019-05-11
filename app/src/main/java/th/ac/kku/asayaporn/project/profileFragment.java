package th.ac.kku.asayaporn.project;

import android.app.Activity;
import android.app.assist.AssistContent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;

import java.time.Instant;

import de.hdodenhof.circleimageview.CircleImageView;

public class profileFragment extends Fragment {
    private FirebaseAuth mAuth;
    String email="";
    String uid="";
    String disname ="";
    String url_photo = "";
    FirebaseUser currentFirebaseUser ;


    @Nullable
    @Override
    public View onCreateView (final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle saveInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        getActivity().setTitle("Profile");
        ((AppCompatActivity)getActivity()).getSupportActionBar().
               setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        TextView emailtv = (TextView) view.findViewById(R.id.emailTv);
        CircleImageView user_photo = (CircleImageView) view.findViewById(R.id.user_photo_id);
        final SharedPreferences settings = this.getActivity().getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        currentFirebaseUser = mAuth.getCurrentUser();




        if(currentFirebaseUser==null){
            view = inflater.inflate(R.layout.activity_request_login, container, false);
            Button btn_login_re = (Button) view.findViewById(R.id.btn_requ_login_id);
            btn_login_re.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(),Login.class));
                }
            });
            return view;
        }
        try {
            if (currentFirebaseUser.getDisplayName() == null){
                email = currentFirebaseUser.getEmail();
                uid = currentFirebaseUser.getUid();
                disname = "unknown";
            }else {
                Toast.makeText(getContext(),currentFirebaseUser.getProviderId().toString(),Toast.LENGTH_SHORT).show();
                url_photo = currentFirebaseUser.getPhotoUrl().toString();
                disname =currentFirebaseUser.getDisplayName();
                email = currentFirebaseUser.getEmail();
                uid = currentFirebaseUser.getUid();
                if (url_photo.equals("")){

                }else {
                    Picasso.get().load(url_photo).into(user_photo);
                }
            }
        }catch (Exception e){
            email = currentFirebaseUser.getEmail();
            uid = currentFirebaseUser.getUid();
            disname = "unknown";
        }



        TextView tv_name_user = (TextView) view.findViewById(R.id.tv_name_user);
        tv_name_user.setText(disname);
        Button btn = (Button) view.findViewById(R.id.buttonlogout);

        emailtv.setText(email);


            btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();
                LoginManager.getInstance().logOut(); //logout facebook
                SharedPreferences.Editor editor = settings.edit();
                editor.clear();
                editor.commit();
                getActivity().finish();
                startActivity(new Intent(getActivity(),MainActivity.class));

            }
        });



        return view;


    }


}
