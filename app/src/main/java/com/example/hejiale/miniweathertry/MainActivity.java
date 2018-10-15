package com.example.hejiale.miniweathertry;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hejiale.bean.TodayWeather;
import com.example.hejiale.util.NetUtil;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity implements View.OnClickListener{

    private static final int UPDATE_TODAY_WEATHER=1;

    private ImageView updateBtn;

    private TextView timeTv,moistTv,weekTv,pmDataTv,pmQualityTv,tempTv,climateTv,cityNameTv,templowtohighTv,suggestTv;
    private ImageView weatherImg,pmImg;

    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case UPDATE_TODAY_WEATHER:{
                    updateTodayWeather((TodayWeather) msg.obj);
                    break;
                }
                default:break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);

        updateBtn=(ImageView) findViewById(R.id.title_update_btn);
        updateBtn.setOnClickListener(this);

        if(NetUtil.getNetworkState(this)!=NetUtil.NETWORN_NONE){
            Log.d("myWeather","Net OK");
            Toast.makeText(MainActivity.this,"Net OK",Toast.LENGTH_SHORT).show();
        }
        else{
            Log.d("myWeather","Net down");
            Toast.makeText(MainActivity.this,"Net down",Toast.LENGTH_SHORT).show();
        }

        initView();
    }

    @Override
    public void onClick(View view){

        if(view.getId()==R.id.title_update_btn){//如果点击的是update_btn

            SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
            String cityCode=sharedPreferences.getString("main_city_code","101010100");
            Log.d("myWeather",cityCode);

            if(NetUtil.getNetworkState(this)!=NetUtil.NETWORN_NONE){
                Log.d("myWeather","Net OK");
                queryWeatherInfo(cityCode);
            }
            else {
                Log.d("myWeather","Net down");
                Toast.makeText(MainActivity.this,"Net Down",Toast.LENGTH_LONG).show();
            }

        }
    }

    private void queryWeatherInfo(String cityCode){
        final String address="http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("myWeather",address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conct=null;
                TodayWeather todayWeather=null;
                try {
                    URL url = new URL(address);
                    conct = (HttpURLConnection) url.openConnection();
                    conct.setRequestMethod("GET");
                    conct.setDoInput(true);
                    conct.setConnectTimeout(8*1000);//单位：毫秒
                    conct.setReadTimeout(8*1000);
                    InputStream input=conct.getInputStream();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(input));//存入缓存区
                    StringBuilder response=new StringBuilder();
                    String str=reader.readLine();
                    while((str)!=null){//当这行没结束
                        response.append(str);//把str加到response后面
                        //Log.d("myWeather",str);
                        str=reader.readLine();
                    }
                    String responseString=response.toString();
                    Log.d("myWeather",responseString);

                    todayWeather=parseXML(responseString);
                    if(todayWeather!=null){
                        Log.d("my",todayWeather.toString());

                        Message msg=new Message();
                        msg.what=UPDATE_TODAY_WEATHER;
                        msg.obj=todayWeather;
                        mHandler.sendMessage(msg);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(conct!=null){
                        conct.disconnect();
                    }
                }

            }
        }).start();

    }

    private TodayWeather parseXML(String xmldata){//对xmldata进行处理，实例化todayweather
        TodayWeather todayWeather=null;
        int fengxiangCnt=0;
        int fengliCnt=0;
        int dateCnt=0;
        int highCnt=0;
        int lowCnt=0;
        int typeCnt=0;
        try{
            XmlPullParserFactory fac=XmlPullParserFactory.newInstance();//获得pull解析器工厂
            XmlPullParser xmlPullParser=fac.newPullParser();//实例化对象xmlPullParser
            xmlPullParser.setInput(new StringReader(xmldata));//xmlPullParser是对象！
            int eventType=xmlPullParser.getEventType();//先用第一个eventType赋值
            Log.d("myWeather","parseXML");
            while(eventType!=XmlPullParser.END_DOCUMENT){//直到结尾
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:break;//文件开头，break进入下一个tag
                    case XmlPullParser.START_TAG:{//tag开头，处理tag内容
                        if(xmlPullParser.getName().equals("resp")){
                            todayWeather=new TodayWeather();//新建对象
                        }
                        if(todayWeather!=null){
                            if(xmlPullParser.getName().equals("city")){
                                eventType=xmlPullParser.next();
                                todayWeather.setCity(xmlPullParser.getText());
                                Log.d("myWeather","cityname:"+xmlPullParser.getText());
                            }else if(xmlPullParser.getName().equals("updatetime")){
                                eventType=xmlPullParser.next();
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                                Log.d("myWeather","updatetime:"+xmlPullParser.getText());
                            }else if(xmlPullParser.getName().equals("wendu")){
                                eventType=xmlPullParser.next();
                                todayWeather.setTemperature(xmlPullParser.getText());
                                Log.d("myWeather","temp:"+xmlPullParser.getText());
                            }else if((xmlPullParser.getName().equals("fengli"))&&(fengliCnt==0)){
                                eventType=xmlPullParser.next();
                                todayWeather.setWindpower(xmlPullParser.getText());
                                Log.d("myWeather","feng:"+xmlPullParser.getText());
                                fengliCnt++;
                            }else if(xmlPullParser.getName().equals("shidu")){
                                eventType=xmlPullParser.next();
                                todayWeather.setMoist(xmlPullParser.getText());
                                Log.d("myWeather","moist:"+xmlPullParser.getText());
                            }else if((xmlPullParser.getName().equals("fengxiang"))&&(fengxiangCnt==0)){
                                eventType=xmlPullParser.next();
                                todayWeather.setWinddir(xmlPullParser.getText());
                                Log.d("myWeather","wind dirction:"+xmlPullParser.getText());
                                fengxiangCnt++;
                            }else if (xmlPullParser.getName().equals("sunrise_1")){
                                eventType=xmlPullParser.next();
                                Log.d("myWeather","sunrise:"+xmlPullParser.getText());
                            }else if (xmlPullParser.getName().equals("sunset_1")){
                                eventType=xmlPullParser.next();
                                Log.d("myWeather","sunset:"+xmlPullParser.getText());
                            }else if (xmlPullParser.getName().equals("pm25")){
                                eventType=xmlPullParser.next();
                                todayWeather.setPm25(xmlPullParser.getText());
                                Log.d("myWeather","pm2.5:"+xmlPullParser.getText());
                            }else if (xmlPullParser.getName().equals("suggest")){
                                eventType=xmlPullParser.next();
                                todayWeather.setSuggest(xmlPullParser.getText());
                                Log.d("my","suggestion:"+xmlPullParser.getText());
                            }else if (xmlPullParser.getName().equals("quality")){
                                eventType=xmlPullParser.next();
                                todayWeather.setQuality(xmlPullParser.getText());
                                Log.d("my","air quality:"+xmlPullParser.getText());
                            }else if ((xmlPullParser.getName().equals("date"))&&(dateCnt==0)){
                                eventType=xmlPullParser.next();
                                todayWeather.setDate(xmlPullParser.getText());
                                Log.d("my","date:"+xmlPullParser.getText());
                                dateCnt++;
                            }else if ((xmlPullParser.getName().equals("high"))&&highCnt==0){
                                eventType=xmlPullParser.next();
                                todayWeather.setHigh(xmlPullParser.getText().substring(2).trim());
                                Log.d("my","high temp:"+xmlPullParser.getText());
                                highCnt++;
                            }else if ((xmlPullParser.getName().equals("low"))&&lowCnt==0){
                                eventType=xmlPullParser.next();
                                todayWeather.setLow(xmlPullParser.getText().substring(2).trim());
                                Log.d("my","low temp:"+xmlPullParser.getText());
                                lowCnt++;
                            }else if ((xmlPullParser.getName().equals("type"))&&typeCnt==0){
                                eventType=xmlPullParser.next();
                                todayWeather.setWeather(xmlPullParser.getText());
                                Log.d("my","type:"+xmlPullParser.getText());
                                typeCnt++;
                            }
                        }

                        break;
                    }
                    case XmlPullParser.END_TAG:break;
                }
                eventType=xmlPullParser.next();
            }
        }catch (XmlPullParserException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return todayWeather;
    }

    void initView(){//和weather_info.xml绑定，用于初始化N/A
        cityNameTv=(TextView) findViewById(R.id.title_city_name);
        timeTv=(TextView) findViewById(R.id.publish_time);
        moistTv=(TextView) findViewById(R.id.moist);
        weekTv=(TextView) findViewById(R.id.date);
        pmDataTv=(TextView) findViewById(R.id.pm2_5);
        pmQualityTv=(TextView) findViewById(R.id.pollution);
        pmImg=(ImageView) findViewById(R.id.pm2_5_img);
        tempTv=(TextView) findViewById(R.id.temperature_now);
        climateTv=(TextView) findViewById(R.id.climate);
        weatherImg=(ImageView) findViewById(R.id.weather_icon);
        templowtohighTv=(TextView) findViewById(R.id.temp_low_high);
        suggestTv=(TextView) findViewById(R.id.suggestion);

        cityNameTv.setText("N/A");
        timeTv.setText("N/A");
        moistTv.setText("N/A");
        weekTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        tempTv.setText("N/A");
        climateTv.setText("N/A");
        templowtohighTv.setText("N/A");
        pmImg.setImageDrawable(null);
        weatherImg.setImageDrawable(null);
        suggestTv.setText("N/A");

    }

    void updateTodayWeather(TodayWeather todayWeather){
        cityNameTv.setText(todayWeather.getCity()+"天气");
        timeTv.setText("发布时间："+todayWeather.getUpdatetime());
        moistTv.setText("湿度："+todayWeather.getMoist());
        weekTv.setText(todayWeather.getDate());
        pmQualityTv.setText("空气质量："+todayWeather.getQuality());
        pmDataTv.setText("pm2.5:"+todayWeather.getPm25());
        tempTv.setText("当前温度:"+todayWeather.getTemperature()+"℃");
        climateTv.setText("天气："+todayWeather.getWeather());
        templowtohighTv.setText("今日温度："+todayWeather.getLow()+"~"+todayWeather.getHigh());
        suggestTv.setText("出行建议："+todayWeather.getSuggest());

        switch (todayWeather.getWeather()){//更新天气图标
            case "暴雪":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoxue);
                break;
            }
            case "暴雨":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoyu);
                break;
            }
            case "大暴雨":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
                break;
            }
            case "大雪":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_daxue);
                break;
            }
            case "大雨":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_dayu);
                break;
            }
            case "多云":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_duoyun);
                break;
            }
            case "雷阵雨":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
                break;
            }
            case "雷阵雨冰雹":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
                break;
            }
            case "沙尘暴":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
                break;
            }
            case "小雨":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
                break;
            }
            case "特大暴雨":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
                break;
            }
            case "雾":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_wu);
                break;
            }
            case "小雪":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
                break;
            }
            case "阴":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_yin);
                break;
            }
            case "雨夹雪":{//需要注意看一下是不是这3个字！
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
                break;
            }
            case "阵雪":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
                break;
            }
            case "阵雨":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
                break;
            }
            case "中雪":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
                break;
            }
            case "中雨":{
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
                break;
            }
        }

        int pm25Int=Integer.parseInt(todayWeather.getPm25());//更新pm图标
        if((pm25Int>0)&&(pm25Int<=50)){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_0_50);
        }else if(pm25Int<=100){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_51_100);
        }else if(pm25Int<=150){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_101_150);
        }else if(pm25Int<=200){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_151_200);
        }else if(pm25Int<=300){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_201_300);
        }

        Toast.makeText(MainActivity.this,"更新成功！",Toast.LENGTH_LONG).show();
    }

}
