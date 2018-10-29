package com.example.hejiale.app;

import android.app.Application;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.hejiale.bean.City;
import com.example.hejiale.db.CityDB;
import com.example.hejiale.miniweathertry.selectCity;

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
    private static List<City> mCityList;

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG,"myapplication onCreate");
        myApplication=this;
        mCityDB=openCityDB();
        initCityList();
    }

    private void initCityList(){//建立mCityList，同时在线程里运行prepareCityList函数，不需要和主线程交互，因此没有Handler
        mCityList=new ArrayList<City>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareCityList();
            }
        }).start();
    }

    private boolean prepareCityList(){//用mCityList保存city.db中的项目，在log里输出城市代码及名称
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

    public static List<City> getmCityList() {
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
        if(!db.exists()){//如果city.db文件不存在
            String pathfoler="/data"
                    + Environment.getDataDirectory().getAbsolutePath()
                    + File.separator+getPackageName()
                    + File.separator+"databases1"
                    + File.separator;
            File dirFirstFolder=new File(pathfoler);
            if (!dirFirstFolder.exists()){
                dirFirstFolder.mkdirs();//创建此抽象路径指定的目录，包括所有必须但不存在的父目录
                Log.d(TAG,"mkdirs");
            }
            Log.d(TAG,"db doesn't exit");
            try {
                InputStream inputStream=getAssets().open("city.db");//干嘛的？
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
