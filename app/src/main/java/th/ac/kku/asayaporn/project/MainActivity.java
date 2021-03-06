package th.ac.kku.asayaporn.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activites_login);
        SharedPreferences settings = getSharedPreferences("LOGIN", 0);

        boolean doskip = settings.getBoolean("doskip", false);
        Intent myIntent = new Intent(MainActivity.this, InsideMainActivity.class);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentFirebaseUser = mAuth.getCurrentUser();
        if(currentFirebaseUser!=null)
        {
            startActivity(myIntent);
        }
        if(doskip)
        {
            startActivity(myIntent);
        }

        Button login = (Button) findViewById(R.id.btnlogin);
        TextView tvskip = (TextView) findViewById(R.id.tvskip) ;
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, Login.class);
                startActivity(myIntent);
            }

        });
        tvskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, InsideMainActivity.class);
                myIntent.putExtra("email","");
                myIntent.putExtra("uid","");
                startActivity(myIntent);
            }
        });
    }
}
