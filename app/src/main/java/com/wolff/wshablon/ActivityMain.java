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
import android.text.TextUtils;
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
import com.wolff.wshablon.yahooWeather.WeatherInfo;
import com.wolff.wshablon.yahooWeather.YahooWeather;
import com.wolff.wshablon.yahooWeather.YahooWeatherInfoListener;


public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,Fragment_catalog.Fragment_catalogListener, YahooWeatherInfoListener {
    private FloatingActionButton fab;
    private Fragment_logo fragment_logo;
    private Fragment_item fragment_item;
    private Fragment_catalog fragment_catalog;
    private Fragment_selection fragment_selection;
    private Fragment_preferences fragment_preferences;
    private SharedPreferences sharedPreferences;
    private YahooWeather mYahooWeather;
    private WeatherInfo mWeatherInfo;
    private int currentFragment; //1 - logo,2 - catalog, 3 - selection, 4 - preferences, 5 - item
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
        //WEATHER
        mYahooWeather = YahooWeather.getInstance(5000, true);
        String _location = "Kiev";
        if (!TextUtils.isEmpty(_location)) {
            //  InputMethodManager imm = (InputMethodManager)getSystemService(
            //          Context.INPUT_METHOD_SERVICE);
            //  imm.hideSoftInputFromWindow(mEtAreaOfCity.getWindowToken(), 0);
            searchByPlaceName(_location);
            //  showProgressDialog();
        } else {
            // Toast.makeText(getApplicationContext(), "location is not inputted", Toast.LENGTH_SHORT).show();
        }


        //WEATHER
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
        Log.e("CURRENTFRAG"," = = = = = = = "+currentFragment);
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
                if(currentFragment!=3) {
                    Bundle args = new Bundle();
                    args.putSerializable("WeatherInfo", mWeatherInfo);
                    fragment_selection.setArguments(args);
                }
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
private void displayFragment(Fragment fragment){
    FragmentTransaction fragmentTransaction;
    fragmentTransaction = getFragmentManager().beginTransaction();
    fragmentTransaction.replace(R.id.content_activity_main,fragment);
    fragmentTransaction.commit();
    if(fragment.getClass().getSimpleName().equalsIgnoreCase("Fragment_catalog")){
        fab.setVisibility(View.VISIBLE);
    }else {
        fab.setVisibility(View.INVISIBLE);
    }
    //currentFragment; //1 - logo,2 - catalog, 3 - selection, 4 - preferences, 5 - item
    switch (fragment.getClass().getSimpleName()){
        case "Fragment_logo":
            currentFragment=1;
            break;
        case "Fagment_catalog":
            currentFragment=2;
            break;
        case "Fragment_selection":
            currentFragment=3;
            break;
        case "Fragment_preferences":
            currentFragment=4;
            break;
        case "Fragment_item":
            currentFragment=5;
            break;
    }
}
    @Override
    public void gotWeatherInfo(WeatherInfo weatherInfo, YahooWeather.ErrorType errorType) {
        if (weatherInfo != null) {
            if (mYahooWeather.getSearchMode() == YahooWeather.SEARCH_MODE.GPS) {
                if (weatherInfo.getAddress() != null) {
                    //mEtAreaOfCity.setText(YahooWeather.addressToPlaceName(weatherInfo.getAddress()));
                    //Log.e("== ADRESS",YahooWeather.addressToPlaceName(weatherInfo.getAddress()));
                }
            }
            mWeatherInfo = weatherInfo;
           /* Log.e("== TITLE",weatherInfo.getTitle());
            Log.e("== CURRENT","====== CURRENT ======" + "\n" +
                    "date: " + weatherInfo.getCurrentConditionDate() + "\n" +
                    "weather: " + weatherInfo.getCurrentText() + "\n" +
                    "temperature in ºC: " + weatherInfo.getCurrentTemp() + "\n" +
                    "wind chill: " + weatherInfo.getWindChill() + "\n" +
                    "wind direction: " + weatherInfo.getWindDirection() + "\n" +
                    "wind speed: " + weatherInfo.getWindSpeed() + "\n" +
                    "Humidity: " + weatherInfo.getAtmosphereHumidity() + "\n" +
                    "Pressure: " + weatherInfo.getAtmospherePressure() + "\n" +
                    "Visibility: " + weatherInfo.getAtmosphereVisibility()
            );*/
        } else {

        }
        displayFragment(fragment_catalog);

    }

    private void searchByPlaceName(String location) {
        mYahooWeather.setNeedDownloadIcons(true);
        mYahooWeather.setUnit(YahooWeather.UNIT.CELSIUS);
        mYahooWeather.setSearchMode(YahooWeather.SEARCH_MODE.PLACE_NAME);
        mYahooWeather.queryYahooWeatherByPlaceName(getApplicationContext(), location, ActivityMain.this);
    }

    @Override
    public void onItemInListSelected(WItem item) {
        fragment_item = Fragment_item.newInstance(item);
        displayFragment(fragment_item);
        //Log.e("ACTIVITY","onItemInListSelected"+item.getName());
    }

}
