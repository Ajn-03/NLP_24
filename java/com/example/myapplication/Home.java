package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.ml.MobilenetV110224Quant;
import com.example.myapplication.ml.Model;
import com.example.myapplication.ml.MonumentNew2;
import com.google.android.material.navigation.NavigationView;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.TransformToGrayscaleOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Home extends AppCompatActivity  {
    private DrawerLayout drawerLayout;
    public int size=128;
    GridView gridView; TextView textView;
    ImageView imgView; SearchView searchView;
    private ViewPager viewPager;
    private SliderPageAdapter sliderPagerAdapter; private SimpleAdapter adapter;
    private String[] cities = {"Delhi", "Jodhpur", "Aurangabad", "Mumbai", "Jaipur", "Udaipur"};
    private int[] img={R.drawable.delhi,R.drawable.jodhpur,R.drawable.aurangabad,R.drawable.mumbai,R.drawable.jaipur,R.drawable.udaipur};
    private Handler handler; private Runnable runnable; private int delay = 3000; // Delay in milliseconds between page changes
    public static final int GALLERY_REQUEST_CODE = 101;
    private String[] monu = {"Abhaneri Step Well", "Agrasen ki Baoli", "Ahar Cenotaphs", "Ajanta Caves", "Akshardham Temple", "Albert Hall Museum", "Amer Fort", "Aurangabad Caves", "Bagore Ki Haveli", "Bani Begum Garden", "Bibi-ka-Maqbara", "Birla Mandir", "Chamunda Mata Temple", "Chandpole Gate", "Chhatrapati Shivaji Maharaj Terminus", "Chhatrapati Shivaji Maharaj Vastu Sangrahalaya", "Chhota Qutub Minar", "City Palace", "City Palace jaipur", "Dargah of Pir Ismail", "Daulatabad Fort", "Eklingji Temple", "Elephanta Caves", "Ellora Caves", "Galta Ji", "Gateway of India", "Ghanta Ghar", "Ghantaghar", "Ghrishneshwar Temple", "Global Pagoda", "Haji Ali Dargah", "Haldighati", "Hauz Khas Fort", "Hawa Mahal", "Humayun\u2019s Tomb", "India Gate", "Jag Mandir Palace", "Jagdish Temple", "Jaigarh Fort", "Jain Caves", "Jal  Mahal", "Jama Masjid", "Jama Masjid Aurangabad", "Jantar Mantar", "Jantar Mantar Jaipur", "Jaswant Thada", "Jehangir Art Gallery", "Kailasanatha", "Kanheri Caves", "Killa Arak", "Lake Palace", "Lodhi Gardens", "Lotus Temple", "Mahakali Caves", "Mahalakshmi Temple", "Mahamandir Temple", "Mahim Fort", "Mandore Gardens", "Marine Drive", "Mehrangarh Fort", "Moti Dungri Temple", "Moti Magri", "Mount Mary Church", "Nahargarh Fort", "Panchakki", "Pratap Gaurav Kendra", "Purana Quila", "Qutub Minar", "Rai ka Bagh Palace", "Rajabai Clock Tower", "Rambagh Palace", "Rashtrapati Bhawan", "Red Fort", "Royal Tombs in Gaitor", "Saas Bahu Temple", "Saheliyon Ki Bari", "Sajjangarh Palace", "Sardar Government Museum", "Siddhivinayak Temple", "Sunheri Mahal", "Taj Mahal Palace", "The Buddist Caves", "Toorji ka Jhalra", "Tower of Silence", "Tughlaqabad Fort", "Umaid Bhawan Palace", "Vintage and Classic Car Museum", "Worli Fort"};
    private Interpreter tflite;
     CustomDialogClass customDialog;
    ImageProcessor imageProcessor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Navigation Drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.img_3);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_fav) {
                    // Handle "My Account" item click
                    Toast.makeText(Home.this, "My Account clicked", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.nav_settings) {
                    // Handle "Settings" item click
                    Toast.makeText(Home.this, "Settings clicked", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.nav_currency) {
                    Intent currency = new Intent(Home.this, CurrencyConverter.class);
                    startActivity(currency);
                } else if (id == R.id.nav_scan) {
                    // Handle "Logout" item click
                    // Initialize TensorFlow Lite interpreter
                    AssetManager assetManager = getResources().getAssets();
                    customDialog = new CustomDialogClass(Home.this,assetManager);
                    customDialog.show();
                }
                return false;
            }
        });

        //Slider
        viewPager = findViewById(R.id.viewPager);
        sliderPagerAdapter = new SliderPageAdapter(this, img, cities);
        viewPager.setAdapter(sliderPagerAdapter);
        setViewPagerHeight(viewPager);
        // Initialize handler and runnable
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                int currentItem = viewPager.getCurrentItem();
                int nextItem = currentItem + 1;
                if (nextItem >= img.length) {
                    nextItem = 0;
                }
                viewPager.setCurrentItem(nextItem, true);
                handler.postDelayed(this, delay);
            } };

        //Search View
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        //Grid View
        gridView = findViewById(R.id.gridView);
        textView = findViewById(R.id.textView);
        imgView = findViewById(R.id.imageView);
        // Set up HashMap
        List<HashMap<String,String>> li =new ArrayList<>();
        for(int i=0;i<cities.length;i++)
        {
            HashMap<String,String> hm=new HashMap<>();
            hm.put("name",cities[i]);
            hm.put("pict",img[i]+"");
            li.add(hm);
        }
        String[] from ={"name","pict"};
        int[] to ={R.id.textView,R.id.imageView};
        adapter = new SimpleAdapter(this,li,R.layout.cardvieww,from,to);
        // Set adapter to GridView
        gridView.setAdapter(adapter);

        //ImageProcessor
        //String[] labels = application.assets.open("labels.txt").bufferedReader().readLines();
        //ImageProcessor imageProcessor = ImageProcessor.Builder();
                //.add(NormalizeOp(0.0f, 255.0f))
               // .add(TransformToGrayscaleOp())
                //.add(ResizeOp(224,224, ResizeOp.ResizeMethod. BILINEAR));
                //.build();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            Bitmap bitmap=null;
            if (selectedImage != null) {
                System.out.println("Object not detected");
            }
                try {
                    // Image processor
                    imageProcessor = new ImageProcessor.Builder()
                            //this should be in comment
                            //.add(new NormalizeOp(0.0f, 255.0f))
                            //.add(new TransformToGrayscaleOp())
                            .add(new ResizeOp(size, size, ResizeOp.ResizeMethod.BILINEAR))
                            .build();
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                bitmap = Bitmap.createScaledBitmap(bitmap, size , size, false);
                // Convert bitmap to RGB if it's not already
                //if (bitmap.getConfig() != Bitmap.Config.ARGB_8888) {
                  //  bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                //}
                //TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                //tensorImage.load(bitmap);
                //tensorImage = imageProcessor.process(tensorImage);
                    try {
                        Model model = Model.newInstance(getApplicationContext());
                        //MonumentNew2 model = MonumentNew2.newInstance(getApplicationContext());

                        // Creates inputs for reference.
                        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 128, 128, 3}, DataType.FLOAT32);
                        ByteBuffer byteBuffer= ByteBuffer.allocateDirect(4*size*size*3);
                                //tensorImage.getBuffer();
                        byteBuffer.order(ByteOrder.nativeOrder());

                        int[] intValues=new int[size*size];
                        bitmap.getPixels(intValues,0,bitmap.getWidth(),0,0,bitmap.getWidth(),bitmap.getHeight());
                        int pixel=0;
                        for (int i=0;i<size;i++)
                        {
                            for(int j=0;j<size;j++)
                            {
                                int val=intValues[pixel++];
                                byteBuffer.putFloat(((val>>16)&0xFF)*(1.f/1));
                                byteBuffer.putFloat(((val>>8)&0xFF)*(1.f/1));
                                byteBuffer.putFloat((val&0xFF)*(1.f/1));
                            }
                        }
                        inputFeature0.loadBuffer(byteBuffer);
                        // Runs model inference and gets result.
                        //MonumentNew2.Outputs outputs = model.process(inputFeature0);
                        Model.Outputs outputs = model.process(inputFeature0);
                        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                        float[] confidences=outputFeature0.getFloatArray();
                        // Find the index with the maximum value
                        int maxIdx = 0;
                        float maxValue = 0;
                        for (int i = 0; i < confidences.length; i++) {
                            //float currentValue = outputFeature0.getFloatValue(i);
                            if (confidences[i] > maxValue) {
                                maxIdx = i;
                                maxValue = confidences[i];
                            }
                        }
                        List<String> labels = new ArrayList<>();
                        try {
                            InputStream inputStream = getAssets().open("Lables.txt");
                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                labels.add(line);
                            }
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Intent i = new Intent(Home.this, MonumentContext.class);
                        i.putExtra("Monument_Name", labels.get(maxIdx));
                        i.putExtra("imageUri", selectedImage.toString());
                        startActivity(i);

                        //customDialog.updatePredictedClassTextView("Predicted class: " + labels.get(maxIdx));
                        // Releases model resources if no longer used.
                        model.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            //String predicted_class= monu[maxIdx];
                    //customDialog.labels(maxIdx);
                    //customDialog.updatePredictedClassTextView("Predicted class: " + predicted_class);
                    // Releases model resources if no longer used.
                    //model.close();
                }
             }


                //ByteBuffer inputBuffer = ByteBuffer.allocateDirect(224 * 224 * 12);
                //inputBuffer=customDialog.convertBitmapToByteBuffer(bitmap, inputBuffer);
                //ByteBuffer outputBuffer = ByteBuffer.allocateDirect(88*4);
                // Run inference
                //tflite.run(inputBuffer, outputBuffer);
                // Post-process the output
                //int predictedClassIndex = customDialog.argmax(outputBuffer,88);

        //}
    //}
    //private MappedByteBuffer loadModelFile()throws IOException {
      //  AssetFileDescriptor fileDescriptor = this.getAssets().openFd("monument.tflite");
        //FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        //FileChannel fileChannel = inputStream.getChannel();
        //long startOffset = fileDescriptor.getStartOffset();
        //long declaredLength = fileDescriptor.getDeclaredLength();
        //return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);

    @Override
    protected void onResume() {
        super.onResume();
        // Start the auto-scrolling when the activity is resumed
        handler.postDelayed(runnable, delay);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop the auto-scrolling when the activity is paused
        handler.removeCallbacks(runnable);
    }

    private void setViewPagerHeight(ViewPager viewPager) {
        // Get screen height
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;

        // Calculate 1/4th of the screen height
        int viewPagerHeight = screenHeight * 3 / 8;

        // Set layout parameters
        ViewGroup.LayoutParams params = viewPager.getLayoutParams();
        params.height = viewPagerHeight;
        viewPager.setLayoutParams(params);
    }
}