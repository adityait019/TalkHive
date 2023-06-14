package com.example.talkhive;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {
    private EditText email,password,confirmPassword;
    private Button SignUpButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText=email.getText().toString();
                String passwordText=password.getText().toString();
                String confirmPasswordText=confirmPassword.getText().toString();
                if(emailText.isEmpty() || passwordText.isEmpty())
                {
                    return;
                }
                Toast.makeText(SignUp.this,emailText+"\n"+"has successfully connected"+"\n"
                ,Toast.LENGTH_LONG).show();
            }
        });
    }
    private void init()
    {
        email=findViewById(R.id.email_et);
        password=findViewById(R.id.pass_et);
        confirmPassword=findViewById(R.id.confirm_pass_et);
        SignUpButton=(Button) findViewById(R.id.signUp_button);
    }
}