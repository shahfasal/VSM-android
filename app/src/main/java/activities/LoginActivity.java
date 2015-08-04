package activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.getvsm.ava.LoginAsyncTask;
import com.getvsm.ava.R;

/**
 * Created by fasal on 01-08-2015.
 */
public class LoginActivity extends ActionBarActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        Button signinButton = (Button) findViewById(R.id.signin_button);
        Button signupButton = (Button) findViewById(R.id.register);
        signinButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin_button:
                EditText usernameTextView = ((EditText) findViewById(R.id.email_edit_text));
                EditText passwordTextView = ((EditText) findViewById(R.id.password_edit_text));

             LoginAsyncTask   loginAsyncTask = new LoginAsyncTask(this);
                loginAsyncTask.execute(
                        new String[]{
                                usernameTextView.getText().toString(),
                                passwordTextView.getText().toString()});

                break;

        }

    }
}
