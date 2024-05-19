package com.example.loginpagetp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText emailInput;
    private Button sendOtpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailInput = findViewById(R.id.emailInput);
        sendOtpButton = findViewById(R.id.sendOtpButton);

        sendOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                if (!email.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    // Here you would send the OTP to the user's email address
                    // For now, we'll just simulate it with a toast message
                    Toast.makeText(CreateAccountActivity.this, "OTP sent to " + email, Toast.LENGTH_SHORT).show();

                    // Navigate to OTP verification screen
                    Intent intent = new Intent(CreateAccountActivity.this, VerifyOtpActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                } else {
                    Toast.makeText(CreateAccountActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
