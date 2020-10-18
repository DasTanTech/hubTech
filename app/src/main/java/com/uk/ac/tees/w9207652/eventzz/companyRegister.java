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
import com.google.firebase.database.DatabaseReference;
import com.uk.ac.tees.w9207652.eventzz.helpers.CONST;
import com.uk.ac.tees.w9207652.eventzz.helpers.firebase;

import java.util.HashMap;

public class companyRegister extends AppCompatActivity {

    //init
    private TextInputLayout gEmail,gPassword,gRePassword;
    private Button gRegister;
    private String EMAIL,PASSWORD,REPASSWORD,COID;

    //firebase
    private DatabaseReference gCompanyRef;
    private HashMap<String,String> dataMap = new HashMap<>();

    //progress dialog
    private ProgressDialog gDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_register);

        gCompanyRef = firebase.gDatabase.getReference().child(CONST.DB_COMPANY);

        gEmail = (TextInputLayout) findViewById(R.id.coRegister_email_et);
        gPassword = (TextInputLayout) findViewById(R.id.coRegister_password_et);
        gRePassword = (TextInputLayout)findViewById(R.id.coRegister_rePassword_et);
        gRegister = (Button)findViewById(R.id.coRegister_register_btn);

        //progress dialog
        gDialog = new ProgressDialog(this);
        gDialog.setTitle("Please wait");
        gDialog.setMessage("While we are loading....");


        gRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EMAIL = gEmail.getEditText().getText().toString();
                PASSWORD = gPassword.getEditText().getText().toString();
                REPASSWORD = gRePassword.getEditText().getText().toString();
                if (EMAIL.isEmpty() || PASSWORD.isEmpty())
                    Toast.makeText(companyRegister.this, "enter email and password.", Toast.LENGTH_SHORT).show();
                else if(!PASSWORD.equals(REPASSWORD))
                    Toast.makeText(companyRegister.this, "Passwords does not match.", Toast.LENGTH_SHORT).show();
                else
                {
                    createCompany(EMAIL,PASSWORD);
                }
            }
        });

    }

    private void createCompany(String email, String password) {
        gDialog.show();
        firebase.gAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            firebase.gUser = firebase.gAuth.getCurrentUser();
                            firebase.CUR_USER = 1;
                            COID = task.getResult().getUser().getUid() ;
                            firebase.COID = COID;
                            createClientInstance(COID,EMAIL);
                        }
                        else
                        {
                            gDialog.dismiss();
                            Toast.makeText(companyRegister.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void createClientInstance(String coid, String EMAIL) {

        dataMap.put(CONST.DB_CO_EMAIL,EMAIL);

        gCompanyRef.child(coid).setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    gDialog.dismiss();
                   toCompanyEditProfile();
                }
                else
                {
                    gDialog.dismiss();
                    Toast.makeText(companyRegister.this, "error at db.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void toCompanyEditProfile() {
        Intent toCompanyEditProfile = new Intent(companyRegister.this,companyEditProfile.class);
        toCompanyEditProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toCompanyEditProfile);
        finish();
    }

}