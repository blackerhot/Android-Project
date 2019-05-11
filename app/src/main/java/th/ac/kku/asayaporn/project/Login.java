package th.ac.kku.asayaporn.project;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login extends Activity {
    private static final int REQUEST_CODE = 10;
    private FirebaseAuth mAuth;
    static String TAG = "LogtestLogin";
    EditText id = null;
    EditText pass = null;
    private SignInButton logingoo;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;


    private static final String TAGF = "FacebookLogin";
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.setApplicationId("302892413797138");
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.popuplogin);
        Button register =(Button)findViewById(R.id.but_signup);
        Button login = (Button) findViewById(R.id.but_login);
        mAuth = FirebaseAuth.getInstance();
        id = (EditText) findViewById(R.id.user_login);
        pass = (EditText) findViewById(R.id.pass_login);
        logingoo = (SignInButton) findViewById(R.id.loginGoo);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        printHashKey(this);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        SharedPreferences settings = getSharedPreferences("LOGIN", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("doskip", true);
        editor.commit();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentFirebaseUser = mAuth.getCurrentUser();
        Intent myIntent = new Intent(Login.this, InsideMainActivity.class);
        if(currentFirebaseUser!=null)
        {
           startActivity(myIntent);
        }



        getWindow().setLayout((int)(width),(int)(height));
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.loginFace);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

                updateUI(null);
            }



            @Override
            public void onError(FacebookException error) {

                updateUI(null);

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Login.this, Register.class);
                startActivity(myIntent);

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(id.getText().length()==0 || pass.getText().length()==0){
                    Toast.makeText(Login.this, "Enter a id or password",
                            Toast.LENGTH_SHORT).show();
                }else{
                    signIn(String.valueOf(id.getText()),String.valueOf(pass.getText()));

                }


            }

        });

        logingoo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }
    //devvvvvvvvvvvvvvv
    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }
    // [START auth_with_facebook]
    private void handleFacebookAccessToken(AccessToken token) {
        showProgressDialog();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Intent myIntent = new Intent(Login.this, InsideMainActivity.class);

                            startActivityForResult(myIntent ,REQUEST_CODE);
                        } else {
                            updateUI(null);
                        }


                    }
                });

        hideProgressDialog();
    }

    private void hideProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading ... ");
        progressDialog.setCancelable(true);
        progressDialog.cancel();
    }

    private void showProgressDialog(){

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading ... ");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    // [END auth_with_facebook]
    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, e.toString(), e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
        //facebook here
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

// [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            Intent myIntent = new Intent(Login.this, InsideMainActivity.class);

                            startActivityForResult(myIntent ,REQUEST_CODE);
                        } else {

                            updateUI(null);
                        }

                    }
                });
        hideProgressDialog();
    }
// [END auth_with_google]

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    private void updateUI(FirebaseUser user) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            String uid = user.getUid();
            Log.d(TAG,uid);
        }
    }



    private void signIn(String email , String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Intent myIntent = new Intent(Login.this, InsideMainActivity.class);
                            startActivityForResult(myIntent ,REQUEST_CODE);

                        } else {

                            updateUI(null);
                        }


                    }
                });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("user",id.getText().toString());
        outState.putString("pass",pass.getText().toString());

    }

    @Override
    public void onRestoreInstanceState(Bundle saveInstanceState){
        super.onRestoreInstanceState(saveInstanceState);
        id.setText(saveInstanceState.getString("user"));
        pass.setText(saveInstanceState.getString("pass"));
    }
}

