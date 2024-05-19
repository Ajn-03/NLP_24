package com.example.loginpagetp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreatePasswordActivity extends AppCompatActivity {

    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private Button createPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        createPasswordButton = findViewById(R.id.createPasswordButton);

        createPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordInput.getText().toString().trim();
                String confirmPassword = confirmPasswordInput.getText().toString().trim();

                if (!password.isEmpty() && password.equals(confirmPassword)) {
                    // Here you would handle the password creation
                    // For now, we'll just simulate it with a toast message
                    Toast.makeText(CreatePasswordActivity.this, "Password created successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreatePasswordActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
