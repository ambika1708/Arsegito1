package com.example.airprepare;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;


public class Homescreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public int i = 0;
    public String loc;
    TextView tv;
    public Button ngetlocation;
    public String cur_city;
    int count = 0;
    android.os.Handler customHandler = new android.os.Handler();
    DrawerLayout drawerLayout;
    NavigationView nav_draw;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            ngetlocation.performClick();
            customHandler.postDelayed(this, 60000);
        }
    };
    public ViewPager viewPager;
    CircleIndicator circleIndicator;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);/*
        AnimationDrawable animationDrawable = new AnimationDrawable();
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.floods), 5000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.elections), 5000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.bundh), 5000);
        animationDrawable.setOneShot(false);
        animationDrawable.start();*/

        // Set a Toolbar to replace the ActionBar.





        viewPager = findViewById(R.id.viewpager);
        ImageAdapter adapter = new ImageAdapter(this);
        viewPager.setAdapter(adapter);
        circleIndicator = findViewById(R.id.indicator);
        circleIndicator.setViewPager(viewPager);
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (i == 3) {
                    i = 0;
                }
                viewPager.setCurrentItem(i++, true);
            }
        };
        final GestureDetector fab = new GestureDetector(this, new fab1());
        final GestureDetector tapGestureDetector = new GestureDetector(this, new TapGestureListener());
        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(Homescreen.this, "Hello", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (tapGestureDetector.onTouchEvent(event))
                    Toast.makeText(Homescreen.this, "Welcome", Toast.LENGTH_SHORT).show();
                else {
                    if (viewPager.getCurrentItem() == 0) {
                        Toast.makeText(Homescreen.this, "pressed 1", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(Homescreen.this, "pressed 2", Toast.LENGTH_SHORT).show();
                }
                if (tapGestureDetector.onGenericMotionEvent(event)) {
                    Toast.makeText(Homescreen.this, "Clicked", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

        });
        viewPager.setFadingEdgeLength(200);
        viewPager.setPageTransformer(false, new PageTransformer());
        viewPager.setPageMargin(20);
        viewPager.setClipToPadding(false);
        viewPager.setPadding(80, 20, 80, 20);

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 10000, 10000);


        ngetlocation = findViewById(R.id.getLocation);
        customHandler.postDelayed(updateTimerThread, 0);


        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);


        nav_draw = findViewById(R.id.nvView);
        nav_draw.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new HomeFragment()).commit();
            nav_draw.setCheckedItem(R.id.first);
        }
    }


    public void logout(View view) {
        Intent intent1 = new Intent(Homescreen.this, MainActivity.class);
        startActivity(intent1);
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
                        List<Address> list = geocoder.getFromLocation(lat, lon, 1);
                        Address address = list.get(0);
                        loc = address.getAddressLine(0);
                        loc = loc + "http://maps.google.com/maps?saddr=" + lat + "," + lon + "\n" + adr.getPremises() + "\n" + adr.getFeatureName() + "\n" + adr.getSubLocality() + "\n" + adr.getLocality() + "\n" + adr.getSubAdminArea() + "\n" + adr.getAdminArea() + "\n" + adr.getCountryName();
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



    public void Sms(View view) {
        Toast.makeText(this, "SMS", Toast.LENGTH_SHORT).show();
    }

    public void sendlocationSms() {
        if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

        } else {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("+919848833207", null, loc, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void imageslider1(View view) {
        if (viewPager.getCurrentItem() == 0)
            Toast.makeText(this, "pressed 1", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "pressed 2", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_acc1:
                getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new AboutFragment()).addToBackStack(null).commit();
                break;
            case R.id.nav_acc:
                getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new HelpFragment()).addToBackStack(null).commit();
                break;
            case R.id.nav_acc3:
                getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new SetFragment()).addToBackStack(null).commit();
                break;
            case R.id.first:
                getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new HomeFragment()).commit();
                nav_draw.setCheckedItem(R.id.first);
                break;
            case R.id.nav_acc4:
                new AlertDialog.Builder(this)
                        .setMessage("Are you sure you want to log out Ar Segito?")
                        .setPositiveButton("Yes,Log out", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Intent i = new Intent(Homescreen.this, Userlogin.class);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.nav_acc2:
                getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new RateFragment()).addToBackStack(null).commit();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    /*  @Override
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