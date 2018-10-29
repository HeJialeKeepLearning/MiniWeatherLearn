package com.example.hejiale.miniweathertry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hejiale.app.MyApplication;
import com.example.hejiale.bean.City;
import com.example.hejiale.listview.CityAdapter;
import com.example.hejiale.listview.sCity;

import java.util.ArrayList;
import java.util.List;


public class selectCity extends Activity implements View.OnClickListener{

    private ImageView mBackBtn;
    private TextView currentCityNameTv;

    private List<sCity> cityList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.select_city);

        initCity();
        CityAdapter adapter=new CityAdapter(selectCity.this,R.layout.city_item,cityList);
        ListView listView=(ListView) findViewById(R.id.select_city_listview);
        listView.setAdapter(adapter);

        mBackBtn=(ImageView) findViewById(R.id.titile_back);
        mBackBtn.setOnClickListener(this);

        listView.setOnItemClickListener(cityItemListener);

        updateView();
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.titile_back:{
                Intent intent=new Intent();//返回数据的时候intent是new的，注意和接收数据区别
                intent.putExtra("cityCode","101160101");//注意value的外面要加双引号
                setResult(RESULT_OK,intent);
                finish();
                break;
            }
            default:
                break;
        }
    }

    private void updateView(){//更新x"当前城市：x"
        currentCityNameTv=(TextView) findViewById(R.id.title_name);

        Intent intent=getIntent();//接收数据，不需要new intent了
        currentCityNameTv.setText("当前城市："+intent.getStringExtra("currentCityName"));
    }

    private void initCity(){//初始化选择城市界面
        List<City> preCityListShow= MyApplication.getmCityList();
        for (City city:preCityListShow){
            sCity cityListShow=new sCity(city.getCity(),city.getNumber());
            cityList.add(cityListShow);
        }
    }

    AdapterView.OnItemClickListener cityItemListener=new AdapterView.OnItemClickListener() {
        @Override//list条目被选中的处理
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            sCity cityselected=cityList.get(position);
            Toast.makeText(selectCity.this,cityselected.getName(),Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(selectCity.this,MainActivity.class);
            intent.putExtra("cityCode",cityselected.getCode());
            setResult(RESULT_OK,intent);
            finish();
        }
    };

}
