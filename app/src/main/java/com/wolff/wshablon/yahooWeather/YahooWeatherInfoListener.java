package com.wolff.wshablon.yahooWeather;

import com.wolff.wshablon.yahooWeather.WeatherInfo;
import com.wolff.wshablon.yahooWeather.YahooWeather;

/**
 * Created by wolff on 24.03.2017.
 */

public interface YahooWeatherInfoListener {
    public void gotWeatherInfo(WeatherInfo weatherInfo, YahooWeather.ErrorType errorType);
}

