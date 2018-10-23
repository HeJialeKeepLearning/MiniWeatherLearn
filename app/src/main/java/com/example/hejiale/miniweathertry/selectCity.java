package com.example.hejiale.miniweathertry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hejiale.bean.TodayWeather;

public class selectCity extends Activity implements View.OnClickListener{

    private ImageView mBackBtn;
    private TextView currentCityNameTv;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.select_city);

        mBackBtn=(ImageView) findViewById(R.id.titile_back);
        mBackBtn.setOnClickListener(this);

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

    void updateView(){
        currentCityNameTv=(TextView) findViewById(R.id.title_name);

        Intent intent=getIntent();//接收数据，不需要new intent了
        currentCityNameTv.setText("当前城市："+intent.getStringExtra("currentCityName"));
    }

}
