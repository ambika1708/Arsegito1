package com.example.airprepare;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class Homescreen extends AppCompatActivity{

    TextView tv;
    public Toolbar toolbar;
    public Button ngetlocation;
    public String cur_city;
    private Runnable mStatusChecker;
    private Handler mHandler;
    int count = 0;
    android.os.Handler customHandler = new android.os.Handler();
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            ngetlocation.performClick();
            //write here whaterver you want to repeat
            customHandler.postDelayed(this, 60000);
        }
    };

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int welcome_time = 5000;
        setContentView(R.layout.activity_homescreen);
        AnimationDrawable animationDrawable = new AnimationDrawable();
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.floods),5000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.elections),5000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.bundh),5000);
        animationDrawable.setOneShot(false);
        ImageView imageView=(ImageView)findViewById(R.id.newsimage);
        imageView.setBackgroundDrawable(animationDrawable);
        animationDrawable.start();

        ngetlocation = findViewById(R.id.getLocation);
        customHandler.postDelayed(updateTimerThread, 0);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open,R.string.drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }


    public void logout(View view) {
        Intent intent1 = new Intent(Homescreen.this, MainActivity.class);
        startActivity(intent1);
    }

   public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getLocation(View view) {

        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            try {
                String city = location1(location.getLatitude(), location.getLongitude());
                cur_city = city;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Not found", Toast.LENGTH_SHORT).show();
            }
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.announcement)
                .setContentTitle("hello")
                .setContentText("welcome to" + cur_city);
        notificationManager.notify(notificationId, mBuilder.build());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    try {
                        String city = location1(location.getLatitude(), location.getLongitude());
                        tv.setText(city);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Not found", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }

    }


    private String location1(double lat, double lon) {
        String cityname = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 10);
            if (addresses.size() > 0) {
                for (Address adr : addresses) {
                    if (adr.getLocality() != null && adr.getLocality().length() > 0) {
                        cityname = adr.getLocality() + "count=" + String.valueOf(count);
                        count++;

                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityname;
    }

    public void cabs(View view) {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.olacabs.customer");
        if (launchIntent != null)
            startActivity(launchIntent);
        else {

            String URL = "https://www.olacabs.com/";
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            builder.addDefaultShareMenuItem();
            customTabsIntent.launchUrl(this, Uri.parse(URL));


        }
    }

   /* @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setCheckable(true);
        int id = menuItem.getItemId();
        if (id == R.id.nav_acc) {
            Intent i1 = new Intent(this, Help.class);
            startActivity(i1);
        }
        drawerLayout.closeDrawers();
        return true;
    }*/
}