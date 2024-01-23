package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //sideBar
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView iconMenu;

    private EditText nameDevice, valueDevice;
    private Button btnAddDevice;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;

    private ListView listDevices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        drawerLayout = findViewById(R.id.drawer_layout_home);
        navigationView = findViewById(R.id.navigation_view_home);
        iconMenu = findViewById(R.id.icon_home);
        nameDevice = findViewById(R.id.nameDevice);
        valueDevice = findViewById(R.id.valueDevice);
        btnAddDevice = findViewById(R.id.btnAddDevice);
        listDevices = findViewById(R.id.listDevices);

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();

        btnAddDevice.setOnClickListener(v -> {
            String deviceNameS = nameDevice.getText().toString();
            String deviceValueS = valueDevice.getText().toString();

            HashMap<String, String> deviceMap = new HashMap<>();
            deviceMap.put("name",deviceNameS);
            deviceMap.put("value",deviceValueS);
            reference.child("Devices").push().setValue(deviceMap);

            nameDevice.setText("");
            valueDevice.setText("");
            nameDevice.clearFocus();
            valueDevice.clearFocus();
            Toast.makeText(this, "New device added seccussfully ! ", Toast.LENGTH_SHORT).show();
        });

        ArrayList<String> deviceArrayList = new ArrayList<>();

        navigationDrawer();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId()==R.id.devices) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                if (item.getItemId()==R.id.profile) {
                    startActivity(new Intent(HomeScreen.this, ProfilActivity.class));
                }
                return true;
            }
        });

    }

    private void navigationDrawer() {
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.devices);
        navigationView.bringToFront();

        iconMenu.setOnClickListener(v -> {
            if(drawerLayout.isDrawerVisible(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.START);
            }else drawerLayout.openDrawer(GravityCompat.START);
        });

        drawerLayout.setScrimColor(getResources().getColor(R.color.colorApp));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else super.onBackPressed();
    }
}
