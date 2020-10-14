package com.example.shiyan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btn_clean;
    Button btn_del;
    Button btn_jc;
    Button btn_divide;
    Button btn_0;
    Button btn_1;
    Button btn_2;
    Button btn_3;
    Button btn_4;
    Button btn_5;
    Button btn_6;
    Button btn_7;
    Button btn_8;
    Button btn_9;
    Button btn_multiply;
    Button btn_add;
    Button btn_sub;
    Button btn_point;
    Button btn_equal;
    Button btn_change;
    TextView textView;
    boolean clear_flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_0 = findViewById(R.id.btn_0);        //初始化
        btn_1 = findViewById(R.id.btn_1);
        btn_2 = findViewById(R.id.btn_2);
        btn_3 = findViewById(R.id.btn_3);
        btn_4 = findViewById(R.id.btn_4);
        btn_5 = findViewById(R.id.btn_5);
        btn_6 = findViewById(R.id.btn_6);
        btn_7 = findViewById(R.id.btn_7);
        btn_8 = findViewById(R.id.btn_8);
        btn_9 = findViewById(R.id.btn_9);
        btn_jc=findViewById(R.id.btn_jc);

        btn_multiply = findViewById(R.id.btn_multiply);
        btn_divide = findViewById(R.id.btn_divide);
        btn_add = findViewById(R.id.btn_add);
        btn_sub = findViewById(R.id.btn_sub);
        btn_point = findViewById(R.id.btn_point);
        btn_del =findViewById(R.id.btn_del);
        btn_equal = findViewById(R.id.btn_equal);
        btn_clean = findViewById(R.id.btn_clean);
        btn_change=findViewById(R.id.btn_change);

        textView=findViewById(R.id.textView);

        btn_0.setOnClickListener(listener);             //设置按钮的点击事件
        btn_1.setOnClickListener(listener);
        btn_2.setOnClickListener(listener);
        btn_3.setOnClickListener(listener);
        btn_4.setOnClickListener(listener);
        btn_5.setOnClickListener(listener);
        btn_6.setOnClickListener(listener);
        btn_7.setOnClickListener(listener);
        btn_8.setOnClickListener(listener);
        btn_9.setOnClickListener(listener);
        btn_sub.setOnClickListener(listener);
        btn_jc.setOnClickListener(listener);
        btn_multiply.setOnClickListener(listener);
        btn_del.setOnClickListener(listener);
        btn_divide.setOnClickListener(listener);
        btn_point.setOnClickListener(listener);
        btn_add.setOnClickListener(listener);
        btn_equal.setOnClickListener(listener);
        btn_clean.setOnClickListener(listener);

    }
    View.OnClickListener listener= new View.OnClickListener(){
            public void onClick(View v){
            String str=textView.getText().toString();
            switch(v.getId()){
                case R.id.btn_0:
                case R.id.btn_1:
                case R.id.btn_2:
                case R.id.btn_3:
                case R.id.btn_4:
                case R.id.btn_5:
                case R.id.btn_6:
                case R.id.btn_7:
                case R.id.btn_8:
                case R.id.btn_9:
                case R.id.btn_point:
                    if(clear_flag){
                        clear_flag=false;
                        str="";
                        textView.setText("");
                    }//3 + 2
                    textView.setText(str+((Button)v).getText());
                    break;
                case R.id.btn_add:
                case R.id.btn_sub:
                case R.id.btn_multiply:
                case R.id.btn_divide:
                    if(clear_flag){
                        clear_flag=false;
                        textView.setText("");
                    }
                    textView.setText(str+" "+((Button)v).getText()+" ");
                    break;
                case R.id.btn_del:  //删除单个
                    if(clear_flag){
                        clear_flag=false;
                        str="";
                        textView.setText("");
                    }
                    else if(str!=null&&!str.equals("")){
                        textView.setText(str.substring(0,str.length()-1));
                    }
                    break;
                case R.id.btn_clean:
                    clear_flag=false;
                    str="";
                    textView.setText("");
                    break;
                case R.id.btn_change:
                    getchange();
                    break;
                case R.id.btn_equal:
                    getResult();
                    break;
                case R.id.btn_jc:
                    getjc();
                    break;




            }
        }
        private  void getchange(){
            String s=textView.getText().toString();
            if(s==null||s.equals("")){
                return;
            }
            if(!s.contains("")){
                return;
            }
            if(clear_flag){
                clear_flag=false;
                return;
            }
            clear_flag=true;
            if(!s.contains(" ")){
                double x=Double.parseDouble(s);
                x=-x;
                textView.setText(String.valueOf(x));
            }
        }
        private void getjc(){     //计算阶乘方法
            String s=textView.getText().toString();
            if(s==null||s.equals("")){
                return;
            }
            if(!s.contains("")){
                return;
            }
            if(clear_flag){
                clear_flag=false;
                return;
            }
            clear_flag=true;
            if(!s.contains(" ")&&!s.contains(".")){
                int x=Integer.valueOf(s);
                int sum=1;
                for(int j=1;j<=x;j++){
                    sum=sum*j;
                }
                textView.setText(String.valueOf(sum));
            }
            else{
                Toast.makeText(MainActivity.this,"输入规范请重新输入",Toast.LENGTH_SHORT).show();
                s="";
                textView.setText("");
            }


        }
        private void getResult(){
                String s=textView.getText().toString();
                if(s==null||s.equals("")){
                    return;
                }
                if(!s.contains("")){
                    return;
                }
                if(clear_flag){
                    clear_flag=false;
                    return;
                }
                clear_flag=true;
                if(s.charAt(4)==' '){
                    Toast.makeText(MainActivity.this,"输入不规范",Toast.LENGTH_SHORT).show();
                    s="";
                    textView.setText("");
                    return;
                }
            String str1 = s.substring(0,s.indexOf(" "));                      // 获取到运算符前面的字符
            String str_y = s.substring(s.indexOf(" ")+1,s.indexOf(" ")+2);    //获取到运算符
            String str2 = s.substring(s.indexOf(" ")+ 3);                     //获取到运算符后面的字符
            //3 + 2  + 2
            double result=0;
            if(!str1.equals("")&&!str2.equals("")){
                double num1=Double.parseDouble(str1);
                double num2=Double.parseDouble(str2);

                if(str_y.equals("+")){
                    result=num1+num2;
                }
                else if(str_y.equals("-")){
                    result=num1-num2;
                }
                else if(str_y.equals("*")){
                    result=num1*num2;
                }
                else if(str_y.equals("/")){
                    if(num2==0){
                        Toast.makeText(MainActivity.this,"分子不能为0",Toast.LENGTH_SHORT).show();
                        s="";
                        textView.setText("");
                    }
                    else{
                        result=num1/num2;
                    }
                }
                if (!str1.contains (".") && !str2.contains (".") && !str_y.equals ("/")){
                    int k = (int) result;               //强制转换
                    textView.setText (String.valueOf(k));
                }else{
                    textView.setText (result+"");
                }



            }
            else if(!str1.equals("")&&str2.equals("")){
                textView.setText(s);
            }
            else if(str1.equals("")&&!str2.equals("")){
                double num2=Double.parseDouble(str2);
                if(str_y.equals("+")){
                    result=0+num2;
                }
                else if(str_y.equals("-")){
                    result=0-num2;
                }
                else if(str_y.equals("*")){
                    result=0;
                }
                else if(str_y.equals("/")){
                    result=0;
                }
                if (!str2.contains (".")) {
                    int r = (int) result;
                    textView.setText (r + "");
                } else {
                    textView.setText (result + "");
                }
            }
            else{
                textView.setText("");
            }

        }

    };

}