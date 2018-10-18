package com.example.hejiale.miniweathertry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class selectCity extends Activity implements View.OnClickListener{

    private ImageView mBackBtn;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.select_city);

        mBackBtn=(ImageView) findViewById(R.id.titile_back);
        mBackBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.titile_back:{
                Intent intent=new Intent();
                intent.putExtra("cityCode","101160101");//注意value的外面要加双引号
                setResult(RESULT_OK,intent);
                finish();
                break;
            }
            default:
                break;
        }
    }

}
