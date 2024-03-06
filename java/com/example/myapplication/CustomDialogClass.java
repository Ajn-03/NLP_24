package com.example.myapplication;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class CustomDialogClass extends Dialog implements View.OnClickListener {
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_CAMERA_CAPTURE = 101;
    private static final int REQUEST_GALLERY = 102;
    private Activity c;
    private Dialog d;
    private Button cam, gallery;
    public interface OnActivityResultListener {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

    public CustomDialogClass(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_box);
        cam = findViewById(R.id.cam);
        gallery = findViewById(R.id.gallery);
        cam.setOnClickListener(this);
        gallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
         int ch = v.getId() ;
            if (ch==R.id.cam) {
                if (ContextCompat.checkSelfPermission(c, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    ActivityCompat.requestPermissions(c,
                            new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                }
            }
            else if (ch==R.id.gallery) {
                openGallery();
            }

        }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(c.getPackageManager()) != null) {
            c.startActivityForResult(takePictureIntent, REQUEST_CAMERA_CAPTURE);
        } else {
            Toast.makeText(c, "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        c.startActivityForResult(pickPhotoIntent, REQUEST_GALLERY);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(c, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // This method is called by the activity to pass the result back to the dialog
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA_CAPTURE && data != null) {
                // Handle captured image
                // You can get the image data from 'data' Intent
            } else if (requestCode == REQUEST_GALLERY && data != null) {
                // Handle selected image from gallery
                // You can get the image URI from 'data' Intent
                Uri selectedImageUri = data.getData();
                // Do something with the selected image URI
            }
        }
    }
}
