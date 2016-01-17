package com.example.valentine.chamaconnect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @InjectView(R.id.account)
    EditText _account;
    @InjectView(R.id.code) EditText _activationcode;
    @InjectView(R.id.idno) EditText _idnumber;
    @InjectView(R.id.pic) EditText _picture;
    @InjectView(R.id.btn_pic)
    Button _picButton;
    @InjectView(R.id.btn_login)
    Button _loginButton;
    @InjectView(R.id.link_signup)
    TextView _signupLink;
    Context mContext=LoginActivity.this;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);


        _picButton.setOnClickListener(new View.OnClickListener() {
            String mChosenDir;

            @Override
            public void onClick(View v) {
                SimpleFileChooserDialog myDialog = new SimpleFileChooserDialog(mContext, SimpleFileChooserDialog.FILE_SELECT, new SimpleFileChooserDialog.SimpleFileDialogListener() {
                    @Override
                    public void onPositiveButton(String chosenDir) {
                        _picture.setText(chosenDir);
                        Toast.makeText(mContext, "Chosen file: " + chosenDir, Toast.LENGTH_SHORT).show();
                    }
                });
                ArrayList<String> myExts = new ArrayList<>();
                myExts.add(".jpg");
                myExts.add(".jpeg");
                myExts.add(".png");
                myDialog.mAllowedFileExtsList = myExts;
                myDialog.chooseFile_or_Dir();
            }
        });


        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
                Intent intent = new Intent(LoginActivity.this, ListingsActivity.class);
                startActivity(intent);

            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the SignupActivity activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

//        if (!validate()) {
//            onLoginFailed();
//            return;
//        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String account = _account.getText().toString();
        String code = _activationcode.getText().toString();
        String idno = _idnumber.getText().toString();
        String pic = _picture.getText().toString();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }
//
//    public boolean validate() {
//        boolean valid = true;
//
//        String account = _account.getText().toString();
//        String code = _activationcode.getText().toString();
//
//        if (account.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(account).matches()) {
//            _account.setError("enter account number");
//            valid = false;
//        } else {
//            _account.setError(null);
//        }
//
//        if (code.isEmpty() || code.length() < 8 || code.length() > 10) {
//            _activationcode.setError("between 4 and 10 alphanumeric characters");
//            valid = false;
//        } else {
//            _activationcode.setError(null);
//        }
//
//        return valid;
//    }

}
