package com.example.talkhive;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.talkhive.fragments.LoginFragment;

public class MainActivity extends AppCompatActivity {
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        __init__();
        addOrReplace(new LoginFragment());
    }

public void addOrReplace(Fragment fragment) {
    FragmentTransaction transaction = manager.beginTransaction();

    // Remove any existing fragment with the same container id
    Fragment existingFragment = manager.findFragmentById(R.id.container);
    if (existingFragment != null) {
        transaction.remove(existingFragment);
    }

    // Add the new fragment to the container
    transaction.add(R.id.container, fragment);
    transaction.commit();
}


    private void __init__() {
        manager = getSupportFragmentManager();
    }
}