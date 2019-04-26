package th.ac.kku.asayaporn.project;

import android.content.Intent;
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

public class profileFragment extends Fragment {
    private FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,
                    container, false);
        getActivity().setTitle("Profile");
        ((AppCompatActivity)getActivity()).getSupportActionBar().
                setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        TextView emailtv = (TextView) view.findViewById(R.id.emailTv);

        Button btn = (Button) view.findViewById(R.id.buttonlogout);

        String email = getArguments().getString("email");
        String uid = getArguments().getString("uid");

        emailtv.setText(email);


            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();
                    LoginManager.getInstance().logOut(); //logout facebook

                    startActivity(new Intent(getActivity(),MainActivity.class));
                }
            });



        return view;


    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);






    }
}
