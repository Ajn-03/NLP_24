package com.example.loginpagetp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class VerifyOtpActivity extends AppCompatActivity {

    private EditText otpInput;
    private Button verifyOtpButton;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        otpInput = findViewById(R.id.otpInput);
        verifyOtpButton = findViewById(R.id.verifyOtpButton);

        // Get the email from the intent
        email = getIntent().getStringExtra("email");

        verifyOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = otpInput.getText().toString().trim();
                if (!otp.isEmpty()) {
                    // Simulate OTP verification (replace with actual verification logic)
                    if (otp.equals("123456")) {
                        // OTP verification successful
                        Toast.makeText(VerifyOtpActivity.this, "OTP verified for " + email, Toast.LENGTH_SHORT).show();

                        // Navigate to Create Password screen
                        Intent intent = new Intent(VerifyOtpActivity.this, CreatePasswordActivity.class);
                        startActivity(intent);
                        finish(); // Optional: Finish current activity after navigation
                    } else {
                        // OTP verification failed
                        Toast.makeText(VerifyOtpActivity.this, "Invalid OTP. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VerifyOtpActivity.this, "Please enter the OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
