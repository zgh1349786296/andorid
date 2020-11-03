package com.example.notebook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


@SuppressLint("Registered")
public class LoginActivity extends BaseActivity {
    private EditText author;        //定义作者
    private Button btn_login;       //定义登录按钮
    private SharedPreferences sp;//声明SharedPreferences对象
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        author=findViewById(R.id.login_author);
        btn_login=findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //登录按钮功能
                String set_author = author.getText().toString();  //得到作者
                sp=getSharedPreferences("text.txt", MODE_PRIVATE);  //创建SharedPreferences对象，定义数据类型以及保存位文件
                SharedPreferences.Editor editor=sp.edit();      //创建SharedPreferences的编辑器
                editor.putString("author",set_author);      //保存作者
                editor.commit();        //执行
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);  //跳转新建界面
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });

    }
}
