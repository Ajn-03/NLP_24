package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MonumentContext extends AppCompatActivity {
    private static final String WIKIPEDIA_API_URL = "https://en.wikipedia.org/w/api.php";
    private OkHttpClient httpClient;
    private TextView textView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monument_context);

        httpClient = new OkHttpClient();
        textView = findViewById(R.id.textView);
        imageView= findViewById(R.id.imageView); // Assuming you have an ImageView with id "imageView" in your layout

        Intent intent = getIntent();
        String monumentName = intent.getStringExtra("Monument_Name");
        String imageUriString = intent.getStringExtra("imageUri");
        Uri imageUri = Uri.parse(imageUriString);
        imageView.setImageURI(imageUri);
        Drawable drawable = imageView.getDrawable();
        int alpha = 230; // Adjust this value as needed
        drawable.setAlpha(alpha);
        imageView.setImageDrawable(drawable);
        if (Objects.equals(monumentName, "Amer Fort"))
        {
            monumentName="Amber Fort";
        }
        // Execute AsyncTask to perform network operations
        new NetworkTask().execute(monumentName);
    }

    // AsyncTask to perform network operations in the background
    private class NetworkTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... monumentNames) {
            String monumentName = monumentNames[0];
            System.out.println(monumentName);
            try {
                // Make a request to Wikipedia API to search for the monument
                String apiUrl = WIKIPEDIA_API_URL +
                        "?action=query" +
                        "&format=json" +
                        "&prop=info|extracts|pageimages" +
                        "&inprop=url" +
                        "&exintro=true" +
                        "&explaintext=true" +
                        "&titles=" + monumentName.replace(" ", "%20");

                Request request = new Request.Builder()
                        .url(apiUrl)
                        .build();

                Response response = httpClient.newCall(request).execute();

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected response code: " + response);
                }

                // Parse the JSON response and extract relevant information
                String responseData = response.body().string();
                JSONObject jsonObject = new JSONObject(responseData);
                JSONObject pages = jsonObject.getJSONObject("query").getJSONObject("pages");
                String pageId = pages.keys().next(); // Get the first page ID
                JSONObject page = pages.getJSONObject(pageId);
                // Check if the "extract" field exists in the JSON response
                if (page.has("extract")) {
                    return page.getString("extract");
                } else {
                    return "No summary available for this monument";
                }

                //return page.getString("extract");

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null; // Handle JSON parsing error
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                // Update UI with the result obtained from the network request
                textView.setText(result);
            } else {
                // Handle error
                textView.setText("Error occurred while fetching data");
            }
        }
    }
}
