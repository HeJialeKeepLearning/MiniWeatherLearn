package com.example.hejiale.app;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.example.hejiale.bean.City;
import com.example.hejiale.db.CityDB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application{
    private static final String TAG="myapp";
    private static MyApplication myApplication;
    private CityDB mCityDB;
    private List<City> mCityList;

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG,"myapplication onCreate");
        myApplication=this;
        mCityDB=openCityDB();
        initCityList();
    }

    private void initCityList(){
        mCityList=new ArrayList<City>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareCityList();
            }
        }).start();
    }

    private boolean prepareCityList(){
        mCityList=mCityDB.getAllCity();
        int i=0;
        for (City city :mCityList){
            i++;
            String cityName=city.getCity();
            String cityCode=city.getNumber();
            Log.d(TAG,cityCode+":"+cityName);
        }
        Log.d(TAG,"i="+i);
        return true;
    }

    public List<City> getmCityList() {
        return mCityList;
    }

    public static MyApplication getInstance(){
        return myApplication;
    }

    private CityDB openCityDB(){
        String path="/data"
                + Environment.getDataDirectory().getAbsolutePath()
                + File.separator+getPackageName()
                + File.separator+"databases1"
                + File.separator
                + CityDB.CITY_DB_NAME;
        File db=new File(path);
        Log.d(TAG,path);
        if(!db.exists()){
            String pathfoler="/data"
                    + Environment.getDataDirectory().getAbsolutePath()
                    + File.separator+getPackageName()
                    + File.separator+"databases1"
                    + File.separator;
            File dirFirstFolder=new File(pathfoler);
            if (!dirFirstFolder.exists()){
                dirFirstFolder.mkdirs();
                Log.d(TAG,"mkdirs");
            }
            Log.d(TAG,"db doesn't exit");
            try {
                InputStream inputStream=getAssets().open("city.db");
                FileOutputStream fileOutputStream=new FileOutputStream(db);
                int len=-1;
                byte[] buffer=new byte[1024];
                while ((len=inputStream.read(buffer))!=-1){
                    fileOutputStream.write(buffer,0,len);
                    fileOutputStream.flush();
                }
                fileOutputStream.close();
                inputStream.close();
            }catch (IOException e){
                e.printStackTrace();
                System.exit(0);
            }
        }
        return new CityDB(this,path);
    }
}
