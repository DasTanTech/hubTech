package com.uk.ac.tees.w9207652.eventzz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.uk.ac.tees.w9207652.eventzz.helpers.firebase;

public class companyLogin extends AppCompatActivity {

    //init
    private TextInputLayout gEmail,gPassword;
    private Button gRegister,gLogin;
    private String EMAIL,PASSWORD,COID;

    //progress dialog
    private ProgressDialog gDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_login);

        gEmail = (TextInputLayout) findViewById(R.id.coLogin_email_et);
        gPassword = (TextInputLayout) findViewById(R.id.coLogin_password_et);
        gRegister = (Button)findViewById(R.id.coLogin_register_btn);
        gLogin = (Button)findViewById(R.id.coLogin_login_btn);

        //progress dialog
        gDialog = new ProgressDialog(this);
        gDialog.setTitle("Please wait");
        gDialog.setMessage("While we are loading....");

        gLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EMAIL = gEmail.getEditText().getText().toString();
                PASSWORD = gPassword.getEditText().getText().toString();
                if (EMAIL.isEmpty() || PASSWORD.isEmpty())
                    Toast.makeText(companyLogin.this, "enter email n password", Toast.LENGTH_SHORT).show();
                else
                {
                    loginCompany(EMAIL,PASSWORD);
                }
            }
        });

        gRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toCompanyRegister();
            }
        });

    }

    private void toCompanyRegister() {
        Intent toCompanyRegister = new Intent(companyLogin.this,companyRegister.class);
        startActivity(toCompanyRegister);
    }

    private void loginCompany(String email, String password) {
        gDialog.show();
        firebase.gAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            firebase.gUser = firebase.gAuth.getCurrentUser();
                            firebase.CUR_USER = 1;
                            COID = task.getResult().getUser().getUid() ;
                            firebase.COID = COID;
                            gDialog.dismiss();
                            toCompanyHome();
                        }
                        else
                        {
                            gDialog.dismiss();
                            Toast.makeText(companyLogin.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void toCompanyHome() {
        Intent toCompanyHome = new Intent(companyLogin.this,companyHome.class);
        startActivity(toCompanyHome);
        finish();
    }
}