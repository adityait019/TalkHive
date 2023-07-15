package com.example.talkhive.fragments;

import static com.example.talkhive.utilities.VerificationUtilities.replaceInMain;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.talkhive.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

public class LoginFragment extends Fragment {
    final static String FILE = "LOGIN_FRAGMENT";
    private EditText emailEt, passwordEt;
    private TextView dontHaveAccount;
    private Button loginButton;
    private FirebaseAuth auth;
    private static final String INVALID_EMAIL_PASS_MSG = "INVALID EMAIL/PASSWORD";
    private ProgressBar progressBar;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //create a view and return the view
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        autoSetup();
        __init__(root);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailEt.getText().toString();
                final String password = passwordEt.getText().toString();
                Log.i(FILE, "Called Button click()");
                loginUser(email, password);
            }
        });
        dontHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceInMain(new SignUpFragment(), getContext());
            }
        });
        return root;
    }

    private void autoSetup() {
        String folderName="TalkHive";
        File folder=new File(Environment.getExternalStorageDirectory(),folderName);
        if(!folder.exists())
        {
            System.out.println("Hi : "+folder.mkdirs());
        }
    }

    private void loginUser(final String email, final String password) {
        progressBar.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(email,password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            replaceInMain(new MainPage(), getContext());
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), INVALID_EMAIL_PASS_MSG, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void __init__(View root) {
        emailEt = root.findViewById(R.id.Login_email);
        passwordEt = root.findViewById(R.id.Login_password);
        loginButton = root.findViewById(R.id.login_btn);
        dontHaveAccount = root.findViewById(R.id.dont_have_account);
        auth = FirebaseAuth.getInstance();
        progressBar = root.findViewById(R.id.progress_bar);
    }
}