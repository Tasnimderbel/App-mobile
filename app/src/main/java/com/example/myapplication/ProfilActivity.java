package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilActivity extends AppCompatActivity {

    private Button btneditProfil, btnLogout;
    private EditText FullNameProfil,emailProfil,CinProfil,PhoneProfil;
    private FirebaseUser loggedUser;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        btneditProfil=findViewById(R.id.btneditProfil);
        btnLogout=findViewById(R.id.btnLogout);
        FullNameProfil=findViewById(R.id.FullNameProfil);
        emailProfil=findViewById(R.id.emailProfil);
        CinProfil=findViewById(R.id.CinProfil);
        PhoneProfil=findViewById(R.id.PhoneProfil);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        loggedUser=firebaseAuth.getCurrentUser();
        reference=firebaseDatabase.getReference().child("Users").child(loggedUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fullNameS = snapshot.child("fullName").getValue().toString();
                String emailS = snapshot.child("email").getValue().toString();
                String ncinS = snapshot.child("ncin").getValue().toString();
                String phoneNumberS = snapshot.child("phoneNumber").getValue().toString();
                FullNameProfil.setText(fullNameS);
                emailProfil.setText(emailS);
                CinProfil.setText(ncinS);
                PhoneProfil.setText(phoneNumberS);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfilActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
            btnLogout.setOnClickListener(v -> {
                SharedPreferences preferences =getSharedPreferences("checkBox",MODE_PRIVATE);
                SharedPreferences.Editor editor= preferences.edit();
                editor.putBoolean("remember",false);
                editor.apply();
                startActivity(new Intent(ProfilActivity.this,SignInActivity.class));
            });
            btneditProfil.setOnClickListener(v -> {
                FullNameProfil.setFocusableInTouchMode(true);
                CinProfil.setFocusableInTouchMode(true);
                PhoneProfil.setFocusableInTouchMode(true);
                btneditProfil.setText("save");
                btneditProfil.setOnClickListener(v1 -> {
                    String editFullName=FullNameProfil.getText().toString();
                    String editPhone=PhoneProfil.getText().toString();
                    String editCin=CinProfil.getText().toString();
                    reference.child("fullName").setValue(editFullName);
                    reference.child("ncin").setValue(editCin);
                    reference.child("phoneNumber").setValue(editPhone);
                    Toast.makeText(this, "your data has been modified", Toast.LENGTH_SHORT).show();
                    FullNameProfil.setFocusableInTouchMode(false);
                    CinProfil.setFocusableInTouchMode(false);
                    PhoneProfil.setFocusableInTouchMode(false);
                    btneditProfil.setText("edit");
                    FullNameProfil.clearFocus();
                    CinProfil.clearFocus();
                    PhoneProfil.clearFocus();
                });
            });
    }
}
