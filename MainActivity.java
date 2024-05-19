package com.example.loginpagetp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button createAccountButton;
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createAccountButton = findViewById(R.id.createAccountButton);
        signInButton = findViewById(R.id.signInButton);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Create Account activity
                Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Sign In activity
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.skipText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Skip text click
                Toast.makeText(MainActivity.this, "Skip clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
