package th.ac.kku.asayaporn.project;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Register extends AppCompatActivity {

    EditText user, pass1, pass2;
    Button btn;

    private void parameter(){
        user = (EditText) findViewById(R.id.user);
        pass1 = (EditText) findViewById(R.id.password1);
        pass2 = (EditText) findViewById(R.id.password2);
        btn = (Button) findViewById(R.id.but_register);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupregister);
        parameter();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("user",user.getText().toString());
        outState.putString("pass1",pass1.getText().toString());
        outState.putString("pass2",pass2.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle saveInstanceState){
        super.onRestoreInstanceState(saveInstanceState);
        user.setText(saveInstanceState.getString("user"));
        pass1.setText(saveInstanceState.getString("pass1"));
        pass2.setText(saveInstanceState.getString("pass2"));
    }
}
