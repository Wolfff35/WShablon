package com.wolff.wshablon;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wolff.wshablon.fragments.Fragment_catalog;
import com.wolff.wshablon.fragments.Fragment_item;
import com.wolff.wshablon.fragments.Fragment_logo;
import com.wolff.wshablon.fragments.Fragment_preferences;
import com.wolff.wshablon.fragments.Fragment_selection;
import com.wolff.wshablon.objects.WItem;


public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,Fragment_catalog.Fragment_catalogListener {
    private FloatingActionButton fab;
    private Fragment_logo fragment_logo;
    private Fragment_item fragment_item;
    private Fragment_catalog fragment_catalog;
    private Fragment_selection fragment_selection;
    private Fragment_preferences fragment_preferences;
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
                fragment_item = Fragment_item.newInstance(null);
                displayFragment(fragment_item);
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
        fragment_selection = new Fragment_selection();
        fragment_preferences = new Fragment_preferences();

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
    public boolean onOptionsItemSelected(MenuItem item) {
          displayFragment(fragment_catalog);
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //Log.e("NAVIGATION","id = "+id);
        switch (id){
            case R.id.nav_catalog:
                displayFragment(fragment_catalog);
               // Log.e("NAVIGATION","1 Catalog");
                break;
            case R.id.nav_select:
                displayFragment(fragment_selection);
               // Log.e("NAVIGATION","2 Selection");
                break;
            case R.id.nav_preferences:
                displayFragment(fragment_preferences);
               // Log.e("NAVIGATION","3 Preferences");
                break;
            default:
               // Log.e("NAVIGATION","ERROR");
                break;
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

}
