package com.andhikaaw.encrypttext;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

//        private ActivityMainBinding binding;
    private BottomNavigationView bottomNavigationView;
    private Encrypted EncryptedFragment = new Encrypted();
    private Decrypted DecryptedFragment = new Decrypted();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,EncryptedFragment).commit();
    }

//    private void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.frame_layout, fragment);
//        transaction.commit();
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.encrypted:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,EncryptedFragment).commit();
                return true;
            case R.id.decrypted:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,DecryptedFragment).commit();
                return true;
        }
        return true;
    }
}