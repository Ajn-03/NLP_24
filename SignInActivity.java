package com.example.loginpagetp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {

    private EditText signInEmailInput;
    private EditText signInPasswordInput;
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signInEmailInput = findViewById(R.id.signInEmailInput);
        signInPasswordInput = findViewById(R.id.signInPasswordInput);
        signInButton = findViewById(R.id.signInButton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = signInEmailInput.getText().toString().trim();
                String password = signInPasswordInput.getText().toString().trim();

                if (!email.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!password.isEmpty()) {
                        // Here you would handle the sign-in process
                        // For now, we'll just simulate it with a toast message
                        Toast.makeText(SignInActivity.this, "Sign In successful", Toast.LENGTH_SHORT).show();

                        // Navigate to the main screen or dashboard
                        // Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        // startActivity(intent);
                    } else {
                        Toast.makeText(SignInActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignInActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
