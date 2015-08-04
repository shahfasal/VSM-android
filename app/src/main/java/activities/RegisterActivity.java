package activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.getvsm.ava.R;
import com.getvsm.ava.RegisterAsyncTask;

/**
 * Created by fasal on 02-08-2015.
 */
public class RegisterActivity extends ActionBarActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account_activity);
        Button signinButton = (Button) findViewById(R.id.btRegister);
        ImageView back = (ImageView) findViewById(R.id.icon_back);
        signinButton.setOnClickListener(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        EditText name = ((EditText) findViewById(R.id.etName));
        EditText email = ((EditText) findViewById(R.id.etEmail));
        EditText password = ((EditText) findViewById(R.id.etPassword));

        RegisterAsyncTask registerAsyncTask = new RegisterAsyncTask(this);
        registerAsyncTask.execute(
                new String[]{
                        name.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString()});
    }
}

