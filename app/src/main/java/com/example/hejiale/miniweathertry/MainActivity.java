package com.example.hejiale.miniweathertry;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hejiale.util.NetUtil;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity implements View.OnClickListener{

    private ImageView updateBtn;

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
        final String address="http://www.weather.com.cn/data/cityinfo/"+cityCode+".html";
        Log.d("myWeather",address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conct=null;
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


}
