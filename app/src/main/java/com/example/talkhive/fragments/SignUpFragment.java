package com.example.talkhive.fragments;

import static com.example.talkhive.utilities.VerificationUtilities.checkEqualityPassword;
import static com.example.talkhive.utilities.VerificationUtilities.replaceInMain;
import static com.example.talkhive.utilities.VerificationUtilities.verifyEmail;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.talkhive.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpFragment extends Fragment {
    private EditText emailEt, passwordEt, confirmPasswordEt;
    private Button signUpButton;
    private final String INVALID_STRING_MSG = "INVALID EMAIL";
    private final String PASSWORD_NOT_MATCHED_MSG = "PASSWORDS DO NOT MATCH";
    private final String MAIL_NOT_SEND = "MAIL COULD NOT BE SENT, TRY AFTER SOME TIME";
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sign_up, container, false);
        __init__(root);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailEt.getText().toString();
                if (!verifyEmail(email)) {
                    Toast.makeText(getContext(), INVALID_STRING_MSG, Toast.LENGTH_SHORT).show();
                    return;
                }
                final String password = passwordEt.getText().toString();
                final String confPassword = confirmPasswordEt.getText().toString();
                if (!checkEqualityPassword(password, confPassword)) {
                    Toast.makeText(getContext(), PASSWORD_NOT_MATCHED_MSG, Toast.LENGTH_SHORT).show();
                    return;
                }
                registerUser(email, password);
            }
        });

        return root;
    }

    private void registerUser(final String email, final String password) {
        enableDisableProgressBar(true);
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null)
                                sendVerificationEmail(user);
                            else {
                                enableDisableProgressBar(false);
                                Toast.makeText(getContext(), MAIL_NOT_SEND, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            enableDisableProgressBar(false);
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null)
                                deleteUser(user);
                        }

                    }
                });
    }

    private void deleteUser(final FirebaseUser user) {
        user.delete();
    }

    private void sendVerificationEmail(final FirebaseUser user) {
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    enableDisableProgressBar(false);
                    replaceInMain(new LoginFragment(), getContext());
                } else {
                    enableDisableProgressBar(false);
                    deleteUser(user);
                    Toast.makeText(getContext(), "Problem with verification email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void enableDisableProgressBar(boolean flag) {
        if (flag) progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.GONE);
    }

    private void __init__(View root) {
        emailEt = root.findViewById(R.id.signUp_email);
        passwordEt = root.findViewById(R.id.signUp_password);
        confirmPasswordEt = root.findViewById(R.id.signUp_confirm_password);
        auth = FirebaseAuth.getInstance();
        signUpButton = root.findViewById(R.id.signUp_button);
        progressBar = root.findViewById(R.id.progress_bar);
    }

}