package com.wolff.wshablon.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wolff.wshablon.yahooWeather.WeatherInfo;
import com.wolff.wshablon.yahooWeather.YahooWeather;
import com.wolff.wshablon.R;
import com.wolff.wshablon.yahooWeather.YahooWeatherInfoListener;


/**
 * Created by wolff on 24.03.2017.
 */

public class Fragment_selection extends Fragment implements YahooWeatherInfoListener {
    private YahooWeather mYahooWeather = YahooWeather.getInstance(5000, true);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selection,container, false);
         return view;
    }

    @Override
    public void gotWeatherInfo(WeatherInfo weatherInfo, YahooWeather.ErrorType errorType) {
        if (weatherInfo != null) {
            if (mYahooWeather.getSearchMode() == YahooWeather.SEARCH_MODE.GPS) {
                if (weatherInfo.getAddress() != null) {
                    //mEtAreaOfCity.setText(YahooWeather.addressToPlaceName(weatherInfo.getAddress()));
                    Log.e("== ADRESS",YahooWeather.addressToPlaceName(weatherInfo.getAddress()));
                }
            }
  //          mWeatherInfosLayout.removeAllViews();
            //mTvTitle.setText(weatherInfo.getTitle());
            Log.e("== TITLE",weatherInfo.getTitle());
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
            );
            //if (weatherInfo.getCurrentConditionIcon() != null) {
            //    mIvWeather0.setImageBitmap(weatherInfo.getCurrentConditionIcon());
            //}
            for (int i = 0; i < YahooWeather.FORECAST_INFO_MAX_SIZE; i++) {
             //   final LinearLayout forecastInfoLayout = (LinearLayout)
             //           getLayoutInflater().inflate(R.layout.forecastinfo, null);
             //   final TextView tvWeather = (TextView) forecastInfoLayout.findViewById(R.id.textview_forecast_info);
             //   final WeatherInfo.ForecastInfo forecastInfo = weatherInfo.getForecastInfoList().get(i);
             //   tvWeather.setText("====== FORECAST " + (i+1) + " ======" + "\n" +
             //           "date: " + forecastInfo.getForecastDate() + "\n" +
             //           "weather: " + forecastInfo.getForecastText() + "\n" +
             //           "low  temperature in ºC: " + forecastInfo.getForecastTempLow() + "\n" +
             //           "high temperature in ºC: " + forecastInfo.getForecastTempHigh() + "\n"
             //   );
             //   final ImageView ivForecast = (ImageView) forecastInfoLayout.findViewById(R.id.imageview_forecast_info);
             //   if (forecastInfo.getForecastConditionIcon() != null) {
             //       ivForecast.setImageBitmap(forecastInfo.getForecastConditionIcon());
             //   }
             //   mWeatherInfosLayout.addView(forecastInfoLayout);
            }
        } else {
            //setNoResultLayout(errorType.name());
        }

    }

    private void searchByPlaceName(String location) {
        mYahooWeather.setNeedDownloadIcons(true);
        mYahooWeather.setUnit(YahooWeather.UNIT.CELSIUS);
        mYahooWeather.setSearchMode(YahooWeather.SEARCH_MODE.PLACE_NAME);
        mYahooWeather.queryYahooWeatherByPlaceName(getContext(), location, Fragment_selection.this);
    }

}
