package com.example.hejiale.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hejiale.miniweathertry.R;

import java.util.List;

public class CityAdapter extends ArrayAdapter<sCity>{

    private int resourceId;

    public CityAdapter(Context context, int textViewResourceId, List<sCity> objects){

        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        sCity city=getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView==null){//convertView是之前布局的缓存
            view=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.cityName=(TextView) view.findViewById(R.id.select_city_name);
            viewHolder.cityCode=(TextView) view.findViewById(R.id.select_city_code);
            view.setTag(viewHolder);
        }else {
            view=convertView;//之前有缓存，则不新加载
            viewHolder=(ViewHolder) view.getTag();//重新获取ViewHolder
        }

        viewHolder.cityName.setText(city.getName());
        viewHolder.cityCode.setText(city.getCode());

        return view;
    }

    class ViewHolder{
        TextView cityName;
        TextView cityCode;
    }

}
