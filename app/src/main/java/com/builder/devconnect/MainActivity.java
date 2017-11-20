package com.builder.devconnect;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.builder.devconnect.manager.APIManager;
import com.builder.devconnect.model.ProfileVo;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

import static com.builder.devconnect.model.ProfileVo.castToProfileVo;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BeaconConsumer {

    final static String Tag = MainActivity.class.getSimpleName();
    FrameLayout discoverLayout;
    int X,Y;
    float density;
    Transformation transformation;

    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);

    @Override
    protected void onPause() {
        super.onPause();

        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        discoverLayout = findViewById(R.id.discoverLayout);


        beaconManager.bind(this);
        density = getResources().getDisplayMetrics().density;
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                X = discoverLayout.getWidth();
//                Y = discoverLayout.getHeight();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                Log.d(Tag,"  X = "+X +" Y = "+Y);
//                Log.d(Tag," Density = " +density);
//                for(int i=0;i<10;i++){
//                    setUser(null,ThreadLocalRandom.current().nextFloat()*100);
//                }
//                //startActivity(new Intent(MainActivity.this,FacebookActivity.class));
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        PulsatorLayout pulsator = (PulsatorLayout) findViewById(R.id.pulsator);
        pulsator.start();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(50)
                .oval(false)
                .build();

        Picasso.with(this)
                .load(R.drawable.user)
                .fit()
                .transform(transformation)
                .into((ImageView) findViewById(R.id.myImage));
        ProfileVo profileVo = new ProfileVo();
        profileVo.setName("Venkatesh Yugendar Devale");
        profileVo.setEmail("mr.venkatesh.d@gmail.com");
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("UserData");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null) {
                    for (ParseObject p : objects) {
                        ProfileVo profile = castToProfileVo(p);
                        Log.d(Tag,profile.toString());
                        setUser(profile,ThreadLocalRandom.current().nextFloat()*100);
                    }
                }
                else e.printStackTrace();
            }


        });
//        APIManager.newInstance().saveProfileData(profileVo, new APIManager.Callback() {
//            @Override
//            public void success(Object savedObj) {
//                ParseObject savedProfile = (ParseObject) savedObj;
//                Log.d(Tag, "Saved Id = "+ savedProfile.getObjectId());
//            }
//
//            @Override
//            public void getProfileData(Object p) {
//                // No use
//            }
//
//            @Override
//            public void failure(String error) {
//
//            }
//        });
//        APIManager.newInstance().getProfileData("mr.venkatesh.d@gmail.com", new APIManager.Callback() {
//            @Override
//            public void success(Object savedObj) {
//                ParseObject savedProfile = (ParseObject) savedObj;
//                Log.d(Tag, "Saved Id = "+ savedProfile.getObjectId());
//            }
//
//            @Override
//            public void getProfileData(Object p) {
//
//            }
//
//            @Override
//            public void failure(String error) {
//
//            }
//        });


    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    //EditText editText = (EditText)RangingActivity.this.findViewById(R.id.rangingText);
                    Beacon firstBeacon = beacons.iterator().next();
                    for(final Beacon b: beacons) {
                        APIManager.newInstance().getProfileData(b.getId1().toString(),
                        new APIManager.Callback() {
                            @Override
                            public void success(Object obj) {
                            }

                            @Override
                            public void getProfileData(Object p) {

                                setUser((ProfileVo)p, (float) b.getDistance());
                            }

                            @Override
                            public void failure(String error) {

                            }
                        });

                    }
             }
            }

        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {   }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void setUser(final ProfileVo profile, float distance){
        X = discoverLayout.getWidth();
        Y = discoverLayout.getHeight();
        float angle = ThreadLocalRandom.current().nextFloat()*360f;
        Point p = getPoint(distance,angle);

        ImageView img;

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int)(40*density), (int)(40*density));

        if(profile!=null) {
            img = new ImageView(this);
            Log.d(Tag,"Seetting :" + profile.getProfilePicURL());
            Log.d(Tag,"Size :" + (int)(30*density));
            Picasso.with(this).load(profile.getProfilePicURL())
                    .centerCrop()
                    .resize((int)(40*density), (int)(40*density))
                    .transform(transformation).into(img);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,ProfileViewActivity.class).putExtra("PROFILE",profile));
                }
            });
        }
        else{
           img  = new ImageView(this);
            if (angle < 90) {
                img.setBackgroundColor(Color.RED);
            } else if (angle < 180) {
                img.setBackgroundColor(Color.BLUE);
            } else if (angle < 270) {
                img.setBackgroundColor(Color.GREEN);
            } else if (angle < 360) {
                img.setBackgroundColor(Color.YELLOW);
            }
        }
        params.leftMargin = p.x;
        params.topMargin  = p.y;
        discoverLayout.addView(img, params);
//..load something inside the ImageView, we just set the background color

    }

    private Point getPoint(float r, float angle) {
        Log.d(Tag,"r = "+r);
        Log.d(Tag," Angle = "+angle);
        if(angle>180.0) {
            angle = (float) -(angle - 180.0);
        }
        Log.d(Tag,"adjusted Angle = "+angle);
        double y = r * Math.sin(Math.toRadians(angle)) ;
        double x = r * Math.cos(Math.toRadians(angle)) ;
        Log.d(Tag,"  x = "+x +" y = "+y);
        x = x * (X/200);
        y = y * (X/200);
        Log.d(Tag,"Adjusted   x = "+x +" y = "+y);
        int xpx = (int) ((X/2 + x) );
        int ypx = (int) (((X/2 - y) + (Y-X)/2));
        Log.d(Tag," Pont x = "+xpx +" y = "+ypx);
        return new Point(xpx,ypx);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            startActivity(new Intent(this,ProfileUpdateActivity.class));
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
