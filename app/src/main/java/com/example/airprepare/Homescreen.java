package com.example.airprepare;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class Homescreen extends AppCompatActivity{
    public int i = 0, t = 0;
    public String str, loc;
    TextView tv;
    TextView tvnews;
    public Button ngetlocation;
    public String cur_city;
    FloatingActionButton floatingActionButton;
    int count = 0;
    android.os.Handler customHandler = new android.os.Handler();
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            ngetlocation.performClick();
            customHandler.postDelayed(this, 60000);
        }
    };
    String[] news;
    Runnable updateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        floatingActionButton = findViewById(R.id.fab);
        tvnews = findViewById(R.id.newstext);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        AnimationDrawable animationDrawable = new AnimationDrawable();
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.floods),5000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.elections),5000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.bundh),5000);
        animationDrawable.setOneShot(false);
        ImageView imageView = findViewById(R.id.newsimage);
        imageView.setBackgroundDrawable(animationDrawable);
        animationDrawable.start();

        ngetlocation = findViewById(R.id.getLocation);
        customHandler.postDelayed(updateTimerThread, 0);
        drawerLayout = findViewById(R.id.drawer_layout);
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
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
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
                .setContentText("welcome to " + cur_city);
        notificationManager.notify(notificationId, mBuilder.build());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
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
        } else if (requestCode == 100) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("+919848833207", null, loc, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(),
                    "SMS faild, please try again.", Toast.LENGTH_LONG).show();
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
                        cityname = adr.getLocality() + "count=" + count;
                        count++;
                        loc = "http://maps.google.com/maps?saddr=" + lat + "," + lon + "\n" + adr.getPremises() + "\n" + adr.getFeatureName() + "\n" + adr.getSubLocality() + "\n" + adr.getLocality() + "\n" + adr.getSubAdminArea() + "\n" + adr.getAdminArea() + "\n" + adr.getCountryName();
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

    public void rooms(View view) {
        Intent i = getPackageManager().getLaunchIntentForPackage("com.oyo.consumer");
        if (i != null)
            startActivity(i);
        else {
            String URL = "https://www.oyorooms.com/";
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            builder.addDefaultShareMenuItem();
            customTabsIntent.launchUrl(this, Uri.parse(URL));
        }
    }

    public void hospitals(View view) {
        Intent i = new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel:9290645115"));
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 10);
        } else {
            startActivity(i);
        }

    }

    public void police(View view) {
        Intent i = new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel:9182933505"));
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 11);
        } else {
            startActivity(i);
        }
    }

    public void setFloatingActionButton(FloatingActionButton floatingActionButton) {
        this.floatingActionButton = floatingActionButton;
    }


    public void Emergency(View view) {
        floatingActionButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(Homescreen.this, "Emergency", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        Toast.makeText(this, "Emegency2", Toast.LENGTH_SHORT).show();
        sendSMSMessage();

    }

    protected void sendSMSMessage() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 100);
        } else {

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("+919848833207", null, loc, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.",
                    Toast.LENGTH_LONG).show();
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