package th.ac.kku.asayaporn.project;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends Activity {
    private FirebaseAuth mAuth;
    String TAG = "LogtestLogin";
    private static final int REQUEST_CODE = 10;
    EditText user, pass1, pass2 =null;
    Button btn=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupregister);

        user = (EditText) findViewById(R.id.user);
        pass1 = (EditText) findViewById(R.id.password1);
        pass2 = (EditText) findViewById(R.id.password2);
        btn = (Button) findViewById(R.id.but_register);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width),(int)(height));
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(user.getText().length()==0 || pass1.getText().length()==0 || pass2.getText().length()==0){

                }else {

                    if(pass1.getText().toString().equals(pass2.getText().toString())){
                        mAuth = FirebaseAuth.getInstance();
                        createAccount(user.getText().toString(),pass1.getText().toString());

                    }else {
                        Toast.makeText(Register.this, "Password not matching",
                                Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }




    private void updateUI(FirebaseUser user) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            String uid = user.getUid();
            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String str = "name: " + name +
                    "\nemail: " + email +
                    "\nuid: " + uid +
                    "\nphotoUrl: " + photoUrl;
            Toast.makeText(Register.this, str,
                    Toast.LENGTH_SHORT).show();
        }
    }
    private void createAccount(final String email , final String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Register.this, "Register Success!!",
                                    Toast.LENGTH_SHORT).show();
                            signIn(email,password);


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });
    }

    private void signIn(final String email , final String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Toast.makeText(Register.this, "Success!",
                                    Toast.LENGTH_SHORT).show();
                            Intent myIntent = new Intent(Register.this, InsideMainActivity.class);
                            myIntent.putExtra("email",user.getEmail());
                            myIntent.putExtra("uid",user.getUid());
                            startActivityForResult(myIntent ,REQUEST_CODE);

                        } else {

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Please check your ID , Password",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }


                    }
                });
    }
}
