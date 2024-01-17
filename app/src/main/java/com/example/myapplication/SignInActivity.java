package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private TextView goToForgetPass;
    private TextView goToSignUp;
    private EditText email, password;
    private Button btnSignIn;
    private CheckBox remember;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email = findViewById(R.id.emailSignIn);
        password = findViewById(R.id.passwordSignIn);
        goToForgetPass = findViewById(R.id.goToForgetPass);
        goToSignUp = findViewById(R.id.goToSignUp);
        btnSignIn = findViewById(R.id.btnSignIn);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        remember = findViewById(R.id.remember);

        SharedPreferences preferences =getSharedPreferences("chekBox",MODE_PRIVATE);
        boolean checkBoxRemember=preferences.getBoolean("remember",false);
        if (checkBoxRemember){
            startActivity(new Intent(SignInActivity.this, HomeScreen.class));
        }else{
            Toast.makeText(this, "please login", Toast.LENGTH_SHORT).show();
        }
        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.isChecked()){
                    SharedPreferences preferences =getSharedPreferences("chekBox",MODE_PRIVATE);
                    SharedPreferences.Editor editor= preferences.edit();
                    editor.putBoolean("remember",true);
                    editor.apply();
                }else{
                    SharedPreferences preferences =getSharedPreferences("chekBox",MODE_PRIVATE);
                    SharedPreferences.Editor editor= preferences.edit();
                    editor.putBoolean("remember",false);
                    editor.apply();
                }
            }
        });
        goToSignUp.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
        });

        goToForgetPass.setOnClickListener(v -> {
            startActivity((new Intent(SignInActivity.this,ForgetPasswordActivity.class)));
        });

        btnSignIn.setOnClickListener(v -> {

            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        verifyEmail();
                        progressDialog.dismiss();
                    }else{
                        Toast.makeText(SignInActivity.this, "Please verify that your data is correct", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        });
    }

    private void verifyEmail() {
        FirebaseUser connectedUser = firebaseAuth.getCurrentUser();
        boolean isEmailFlag = connectedUser.isEmailVerified();
        if (isEmailFlag){
            startActivity(new Intent(SignInActivity.this,HomeScreen.class));
        }else{
            Toast.makeText(this, "Please Verify your email's account", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}