package com.example.weawther;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.app.AlertDialog;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.Toast;

public class choseItem extends AppCompatActivity implements View.OnClickListener {
    Button in_time;  //当前天气状况按钮
    Button future;   //未来天气情况
    LinearLayout start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chose_item);
        findButtons();
        in_time.setOnClickListener(this);  //实况天气按钮处理
        future.setOnClickListener(this);  //未来天气按钮处理
    }


    public void findButtons(){
        in_time=(Button)findViewById(R.id.in_time);
        future=(Button)findViewById(R.id.future);
        start=(LinearLayout)findViewById(R.id.start);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.in_time:
                Intent intent1=new Intent(this,MainActivity.class);  //跳转实况天气
                startActivity(intent1);
                break;
            case R.id.future:
                Intent intent2=new Intent(this,FutureWeather.class);  //跳转未来天气
                startActivity(intent2);
                break;

        }
    }





}
