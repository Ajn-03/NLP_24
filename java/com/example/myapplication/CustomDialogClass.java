package com.example.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class CustomDialogClass extends Dialog implements View.OnClickListener {
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_CAMERA_CAPTURE = 101;
    TextView predictedClass;
    private Activity a;
    private Dialog d;
    private Button cam, gallery;
    AssetManager assetManager;
    String predictedClassLabel;
    public interface OnActivityResultListener {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

    public CustomDialogClass(Activity a, AssetManager assetManager) {
        super(a);
        this.a = a;
        this.assetManager = assetManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_box);
        predictedClass=findViewById(R.id.predictedClass);
        cam = findViewById(R.id.cam);
        gallery = findViewById(R.id.gallery);
        cam.setOnClickListener(this);
        gallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int ch = v.getId() ;
        if (ch==R.id.cam) {
            if (ContextCompat.checkSelfPermission(a, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                ActivityCompat.requestPermissions(a,
                        new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }
        }
        else if (ch==R.id.gallery) {
            openGallery();
        }

    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(a.getPackageManager()) != null) {
            a.startActivityForResult(takePictureIntent, REQUEST_CAMERA_CAPTURE);
        } else {
            Toast.makeText(a, "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        a.startActivityForResult(pickPhotoIntent, Home.GALLERY_REQUEST_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(a, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected int argmax(ByteBuffer buffer, int numClasses) {
        buffer.rewind(); // Rewind the buffer to read from the beginning
        int argmax = 0;
        float maxVal = Float.MIN_VALUE;
        for (int i = 0; i < numClasses; i++) {
            float val = buffer.getFloat();
            if (val > maxVal) {
                maxVal = val;
                argmax = i;
            }
        }
        return argmax;
    }

    protected ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap, ByteBuffer inputBuffer) {

        inputBuffer.rewind(); // Rewind the buffer to clear any existing data
        // Get the bitmap pixels by iterating through each pixel
        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        // Iterate through each pixel to extract RGB values and normalize them
        for (int pixelValue : pixels) {
            // Extract RGB values
            float r = Color.red(pixelValue) / 255.0f;
            float g = Color.green(pixelValue) / 255.0f;
            float b = Color.blue(pixelValue) / 255.0f;
            // Add the normalized RGB values to the input buffer
            inputBuffer.putFloat(b);
            inputBuffer.putFloat(g);
            inputBuffer.putFloat(r);
        }
        return inputBuffer;
    }
    public void updatePredictedClassTextView(String text) {

        predictedClass.setText(text);
    }
    public void labels(int predictedClassIndex) {
        List<String> classLabels = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open("Labels.txt"))))
            {
                String line;
                while((line=reader.readLine())!=null)
                {    classLabels.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        predictedClassLabel= classLabels.get(predictedClassIndex);
        updatePredictedClassTextView(predictedClassLabel);
    }

}