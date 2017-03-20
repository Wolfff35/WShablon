package com.wolff.wshablon;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wolff.wshablon.fragments.Fragment_camera;
import com.wolff.wshablon.fragments.Fragment_catalog;
import com.wolff.wshablon.fragments.Fragment_item;
import com.wolff.wshablon.fragments.Fragment_logo;
import com.wolff.wshablon.objects.WItem;

import java.io.File;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,Fragment_catalog.Fragment_catalogListener {
    private FloatingActionButton fab;
    private Fragment_logo fragment_logo;
    private Fragment_item fragment_item;
    private Fragment_camera fragment_camera;
    private Fragment_catalog fragment_catalog;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
 ///================================================================================================
        fragment_logo = new Fragment_logo();
        displayFragment(fragment_logo);

        NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(ActivityMain.this, DividerItemDecoration.HORIZONTAL));

        //HEADER
        View headerLayout = navigationView.getHeaderView(0);
        TextView tvHeader_line1 = (TextView) headerLayout.findViewById(R.id.tvHeader_line1);
        TextView tvHeader_line2 = (TextView) headerLayout.findViewById(R.id.tvHeader_line2);
        tvHeader_line1.setText("");
        tvHeader_line2.setText("");
        fragment_catalog = new Fragment_catalog();
        //fragment_item = new Fragment_item();
        fragment_camera = new Fragment_camera();
        displayFragment(fragment_catalog);
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
        getMenuInflater().inflate(R.menu.activity_main, menu);
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
           displayFragment(fragment_camera);


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displayFragment(fragment_catalog);
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//==================================================================================================
public void displayFragment(Fragment fragment){
    FragmentTransaction fragmentTransaction;
    fragmentTransaction = getFragmentManager().beginTransaction();
    fragmentTransaction.replace(R.id.content_activity_main,fragment);
    fragmentTransaction.commit();
    if(fragment.getClass().getSimpleName().equalsIgnoreCase("Fragment_catalog")){
        fab.setVisibility(View.VISIBLE);
    }else {
        fab.setVisibility(View.INVISIBLE);
    }
}


    @Override
    public void onItemInListSelected(WItem item) {
        fragment_item = Fragment_item.newInstance(item);
        displayFragment(fragment_item);
        Log.e("ACTIVITY","onItemInListSelected"+item.getName());
    }

/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        Log.e("MAIN","onActivityResult: requestCode - "+requestCode+"; resultCode - "+resultCode);
        //String name = data.getStringExtra("name");
        //tvName.setText("Your name is " + name);
    }
*/
}
