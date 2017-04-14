package com.buxingzhe.pedestrian.bean.walk;

/**
 * 类描述：天气数据Bean
 *
 * @author zhaishaoping
 * @data 08/04/2017 5:19 PM
 */
public class WalkWeatherInfo {
    private String cityCode;
    private String cityName;
    private String tempHigh;//高温
    private String tempLow;//低温
    private String weather;//天气情况：阴
    private String windDirection;//风向
    private String windDegree;//风大小级：2级
    private String airQuality;////空气质量 天气预报接口没有返回值时，为 -
    private String airAqi;//
    private String suggest;//
    private String wearSuggest;//"建议着薄外套、开衫牛仔衫裤等服装。年老体弱者应适当添加衣物，宜着夹克衫、薄毛衣等。"
    private String sportSuggest;//"阴天，较适宜进行各种户内外运动。"
    private String forecastTime;//"5日星期三"

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTempHigh() {
        return tempHigh;
    }

    public void setTempHigh(String tempHigh) {
        this.tempHigh = tempHigh;
    }

    public String getTempLow() {
        return tempLow;
    }

    public void setTempLow(String tempLow) {
        this.tempLow = tempLow;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getWindDegree() {
        return windDegree;
    }

    public void setWindDegree(String windDegree) {
        this.windDegree = windDegree;
    }

    public String getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(String airQuality) {
        this.airQuality = airQuality;
    }

    public String getAirAqi() {
        return airAqi;
    }

    public void setAirAqi(String airAqi) {
        this.airAqi = airAqi;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public String getWearSuggest() {
        return wearSuggest;
    }

    public void setWearSuggest(String wearSuggest) {
        this.wearSuggest = wearSuggest;
    }

    public String getSportSuggest() {
        return sportSuggest;
    }

    public void setSportSuggest(String sportSuggest) {
        this.sportSuggest = sportSuggest;
    }

    public String getForecastTime() {
        return forecastTime;
    }

    public void setForecastTime(String forecastTime) {
        this.forecastTime = forecastTime;
    }

    @Override
    public String toString() {
        return "WalkWeatherInfo{" +
                "cityCode='" + cityCode + '\'' +
                ", cityName='" + cityName + '\'' +
                ", tempHigh='" + tempHigh + '\'' +
                ", tempLow='" + tempLow + '\'' +
                ", weather='" + weather + '\'' +
                ", windDirection='" + windDirection + '\'' +
                ", windDegree='" + windDegree + '\'' +
                ", airQuality='" + airQuality + '\'' +
                ", airAqi='" + airAqi + '\'' +
                ", suggest='" + suggest + '\'' +
                ", wearSuggest='" + wearSuggest + '\'' +
                ", sportSuggest='" + sportSuggest + '\'' +
                ", forecastTime='" + forecastTime + '\'' +
                '}';
    }
}
