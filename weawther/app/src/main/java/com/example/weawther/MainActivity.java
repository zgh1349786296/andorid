package com.example.weawther;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;

public class MainActivity extends AppCompatActivity implements Runnable,View.OnClickListener{
    /* Date curDate=new Date(System.currentTimeMillis());
     String str=format.format(curDate);*/
    HttpURLConnection httpConn=null;
    InputStream din=null;    //
    Vector<String> cityname=new Vector<String>();   //定义矢量字体
    Vector<String> low=new Vector<String>();
    Vector<String> high=new Vector<String>();
    Vector<Bitmap> bitmap=new Vector<Bitmap>();
    Vector<String> icon=new Vector<String>();
    Vector<String> summary=new Vector<String>();
    Vector<String> windDir=new Vector<String>();
    Vector<String> windPower=new Vector<String>();
    Vector<String> windState=new Vector<String>();
    Vector<String> humidity=new Vector<String>();
    Vector<String> time=new Vector<String>();
    Vector<String> temNow=new Vector<String>();

    //int weatherIndex[] =new int[20];
    String city="guangzhou";
    //boolean bPress=false;
    //boolean bHasData=false;
    private ListView myListView;
    private MyBaseAdapter myBaseAdapter;
    LinearLayout body;   //定义布局
    FrameLayout main;
    Button find;
    Button update;
    Button concern;
    EditText value;

    boolean isup = false;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        concern=findViewById(R.id.concern);

        //setTitle("天气实况查询");
        findViews();  //查找当前实况天气
        find.setOnClickListener(this);
        update.setOnClickListener(this);
        concern.setOnClickListener(this);
        init();

    }

public void init(){

        myListView=findViewById(R.id.list_view);

    SharedPreferences appSharedPrefs = PreferenceManager
            .getDefaultSharedPreferences(this.getApplicationContext());
    Gson gson = new Gson();
    String json = appSharedPrefs.getString("MyObject", "");
    weatherss weatherlist = gson.fromJson(json, weatherss.class);

    if(weatherlist!=null){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight=80;
        params.height=80;

        for (weathers ws : weatherlist.getWss()) {
            if(ws.isConcern()==true){

                final LinearLayout City = new LinearLayout(this);
                City.setOrientation(LinearLayout.VERTICAL);
                City.setGravity(Gravity.CENTER);

                final TextView cityView = new TextView(this);
                cityView.setLayoutParams(params);
                cityView.setText(ws.getWeathername());
                cityView.setTextColor(getResources().getColor(R.color.colorAccent));
                cityView.setTextSize(20);
                cityView.setGravity(Gravity.CENTER);
                /*TextView space = new TextView(this);
                space.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                space.setText("----------------------------------------------------------------------");*/
                //space.setGravity(Gravity.CENTER);
                City.addView(cityView);
                City.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        city=cityView.getText().toString();
                        isup = true;
                        run();
                    }
                });
                //City.addView(space);

                body.addView(City);
            }
        }

    }
    else{
        body.removeAllViews();//清除存储原有的查询结果的组件
    }


}
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        body.removeAllViews();
        city=value.getText().toString();


        switch (v.getId()){
            case R.id.find:
                Toast.makeText(this,"正在查询天气......",Toast.LENGTH_SHORT).show();
                Thread th=new Thread(this);
                th.start();

                break;
            case R.id.update:
                Toast.makeText(this,"正在更新天气......",Toast.LENGTH_SHORT).show();
                isup = true;
                Thread ths=new Thread(this);
                ths.start();
                break;
            case R.id.concern:
                addconcern();
                break;
            default:
                break;
        }

    }
    public void findViews(){
        body=(LinearLayout)findViewById(R.id.my_body);  //设置布局
        value=(EditText)findViewById(R.id.value);
        find=(Button)findViewById(R.id.find);
        update=(Button)findViewById(R.id.update);
        main=(FrameLayout)findViewById(R.id.main);
    }

    public void addconcern(){
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("MyObject", "");
        weatherss weatherlist = gson.fromJson(json, weatherss.class);
        if(weatherlist!=null){
            for (weathers ws : weatherlist.getWss()) {
                if(ws.getWeathername().equals(city)){
                    ws.setConcern(true);
                    Toast.makeText(this,"关注成功",Toast.LENGTH_LONG).show();
                }

            }
            init();
        }
        else{
            Toast.makeText(this,"关注失败",Toast.LENGTH_LONG).show();
        }

        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        String jsonweathers = gson.toJson(weatherlist);
        prefsEditor.putString("MyObject", jsonweathers);
        prefsEditor.commit();

    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {   //Thread线程运行是启动
        cityname.removeAllElements();
        low.removeAllElements();
        high.removeAllElements();
        //icon.removeAllElements();
        bitmap.removeAllElements();
        summary.removeAllElements();
        boolean exit=false;
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("MyObject", "");
        weatherss weatherlist = gson.fromJson(json, weatherss.class);
        if(weatherlist!=null) {
            for (weathers ws : weatherlist.getWss()) {
                if (ws.getWeathername().equals(city)) {
                    for (weather w : ws.getWs()) {
                        cityname.addElement(w.getCityname());
                        //天气情况概述
                        summary.addElement(w.getSummary());
                        //最低温度
                        low.addElement("最高：" + w.getLow());
                        //最高温度
                        high.addElement("最低：" + w.getHigh());
                        windDir.addElement("风向： " + w.getWindDir());
                        windPower.addElement("风力： " + w.getWindPower());
                        windState.addElement("当前风的状况： " + w.getWindState());
                        humidity.addElement("湿度： " + w.getHumidity());
                        time.addElement("更新时间： " + w.getTime());
                        temNow.addElement("当前温度： " + w.getTemNow());
                    }

                        exit = true;
                }
            }
        }
        if(exit!=true||isup==true){
            parseData();
        }


        //downImage();
        Message message=new Message();
        message.what=1;

        handler.sendMessage(message);
    }



    //获取数据
    public void parseData(){
        int i=0;

        List<weathers> wss=new ArrayList<weathers>();
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("MyObject", "");
        weatherss wess = gson.fromJson(json, weatherss.class);
        if(wess!=null){
            wss=wess.getWss();
        }
        wess=new weatherss();
        String svalue;
        //city为城市名字的拼音
        String weatherUrl="http://flash.weather.com.cn/wmaps/xml/"+city+".xml";/*"http://api.k780.com:88/?app=weather.future&weaid="+"city"+"&&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=xml";*//*"http://wthrcdn.etouch.cn/weather_mini?city=北京"*/;//"http://3g.163.com/touch/jsonp/sy/recommend/0-9.html?miss=48&refresh=B02callback=syrec4";//
        //String weatherIcon="http://m.weather.com.cn/img/c";
        try{
            URL url=new URL(weatherUrl);
            //建立天气预报查询连接
            httpConn=(HttpURLConnection)url.openConnection();
            //采用get请求方法
            httpConn.setRequestMethod("GET");
            //打开数据输入流
            din=httpConn.getInputStream();
            InputStreamReader in = new InputStreamReader(httpConn.getInputStream());
            //为输出创建BufferReader
            BufferedReader buffer = new BufferedReader(in);

            String inputLine="";
            String resultData="";
            //使用循环来读取获得的天气数据
            while((inputLine=buffer.readLine())!=null){
                //在每一行后面添加一个"/n"进行换行
                resultData+=inputLine;
            }
            //获得XmlPullParser解析器
            XmlPullParser xmlParser= Xml.newPullParser();
            ByteArrayInputStream tInputStringStream=null;
            tInputStringStream=new ByteArrayInputStream(resultData.getBytes());

            xmlParser.setInput(tInputStringStream,"UTF-8");
            //获得解析到的事件类别,这里有开始文档、结束文档、开始标签、结束标签、文本等事件
            int evtType=xmlParser.getEventType(); //String evtType;
            weathers weathers = new weathers();
            weathers.setWeathername(city);
            List<weather> ws=new ArrayList<>();

            while(evtType!= XmlPullParser.END_DOCUMENT){//一直循环，直到文档结束
                switch (evtType){
                    /*case "addata":*/
                    case XmlPullParser.START_TAG:
                        String tag=xmlParser.getName();

                        //如果从city标签开始，则说明有新的一条城市天气信息
                        if(tag.equalsIgnoreCase(/*"addata"*//*"city"*/"city")){
                            //城市天气预报

                            cityname.addElement(xmlParser.getAttributeValue(null,"cityname"));
                            //天气情况概述
                            summary.addElement(xmlParser.getAttributeValue(null,"stateDetailed"));
                            //最低温度
                            low.addElement("最高："+xmlParser.getAttributeValue(null,"tem2"));
                            //最高温度
                            high.addElement("最低："+xmlParser.getAttributeValue(null,"tem1"));
                            windDir.addElement("风向： "+xmlParser.getAttributeValue(null,"windDir"));
                            windPower.addElement("风力： "+xmlParser.getAttributeValue(null,"windPower"));
                            windState.addElement("当前风的状况： "+xmlParser.getAttributeValue(null,"windState"));
                            humidity.addElement("湿度： "+xmlParser.getAttributeValue(null,"humidity"));
                            time.addElement("更新时间： "+xmlParser.getAttributeValue(null,"time"));
                            temNow.addElement("当前温度： "+xmlParser.getAttributeValue(null,"temNow"));
                            // icon.addElement(weatherIcon+xmlParser.getAttributeValue(null,"state1")+".gif");
                            weather weather= new weather();
                            weather.setCityname(xmlParser.getAttributeValue(null,"cityname"));
                            weather.setSummary(xmlParser.getAttributeValue(null,"stateDetailed"));
                            weather.setLow(xmlParser.getAttributeValue(null,"tem2"));
                            weather.setHigh(xmlParser.getAttributeValue(null,"tem1"));
                            weather.setWindDir(xmlParser.getAttributeValue(null,"windDir"));
                            weather.setWindPower(xmlParser.getAttributeValue(null,"windPower"));
                            weather.setWindState(xmlParser.getAttributeValue(null,"windState"));
                            weather.setHumidity(xmlParser.getAttributeValue(null,"humidity"));
                            weather.setTime(xmlParser.getAttributeValue(null,"time"));
                            weather.setTemNow(xmlParser.getAttributeValue(null,"temNow"));
                            ws.add(weather);
                        }

                        break;

                    case XmlPullParser.END_TAG:
                        //标签结束
                    default:break;
                }
                //如果xml没有结束,则移到下一个节点
                evtType=xmlParser.next();
            }
            boolean nameex=false;
            for(weathers w :wss){
                if(w.getWeathername().equals(city)){
                     w.setWs(ws);
                     wess.setWss(wss);
                     nameex=true;
                }
            }
            if(nameex==false){
                weathers.setWs(ws);
                wss.add(weathers);
                wess.setWss(wss);
            }
            //SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            //Gson gson = new Gson();
            String jsonweathers = gson.toJson(wess);
            prefsEditor.putString("MyObject", jsonweathers);
            prefsEditor.commit();


        }


        catch(Exception e){
            e.printStackTrace();
        }finally {
            //释放连接
            try{
                din.close();
                httpConn.disconnect();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private final Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {
                case 1:
                    showData();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //显示结果
    public void showData(){
        body.removeAllViews();//清除存储原有的查询结果的组件
        body.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight=80;
        params.height=80;
        if(cityname.size()==0){
            new AlertDialog.Builder(this)
                    .setTitle("错误页面")//标题
                    .setMessage("请核对您输入的城市名称拼音是否正确！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .create()
                    .show();
        }
        else{
            for(int i=0;i<cityname.size();i++) {
                LinearLayout City = new LinearLayout(this);
                /*LinearLayout Weather = new LinearLayout(this);*/
                LinearLayout Tem=new LinearLayout(this);
                LinearLayout Sum=new LinearLayout(this);
                LinearLayout Wind=new LinearLayout(this);
                Tem.setOrientation(LinearLayout.HORIZONTAL);
                Wind.setOrientation(LinearLayout.HORIZONTAL);
                City.setOrientation(LinearLayout.HORIZONTAL);
                Sum.setOrientation(LinearLayout.HORIZONTAL);
                //linearlayout.setBackgroundResource(R.drawable.back2);
                //linearlayout.setOrientation(LinearLayout.HORIZONTAL);
                //城市
                TextView cityView = new TextView(this);
                cityView.setLayoutParams(params);
                cityView.setText(cityname.elementAt(i));
                cityView.setTextColor(getResources().getColor(R.color.colorAccent));
                cityView.setTextSize(20);
                cityView.setGravity(Gravity.CENTER);
                City.addView(cityView);
                body.addView(City);

                //更新时间
                TextView timeView = new TextView(this);
                timeView.setLayoutParams(params);
                timeView.setText(time.elementAt(i));
                body.addView(timeView);

                //天气描述
                TextView summaryView = new TextView(this);
                TextView summaryPre = new TextView(this);
                summaryPre.setText("天气状况： ");
                summaryView.setLayoutParams(params);
                summaryView.setText(summary.elementAt(i));
                Sum.addView(summaryPre);
                Sum.addView(summaryView);
                body.addView(Sum);
                //Weather.addView(summaryView);
                TextView temPre = new TextView(this);
                temPre.setText("温度状况：  ");
                Tem.addView(temPre);
                //最低温度
                TextView lowView = new TextView(this);
                lowView.setLayoutParams(params);
                lowView.setText(low.elementAt(i));
                Tem.addView(lowView);
                //最高温度
                TextView highView = new TextView(this);
                highView.setLayoutParams(params);
                highView.setText(high.elementAt(i));
                Tem.addView(highView);
                //当前温度
                TextView temNowView = new TextView(this);
                temNowView.setLayoutParams(params);
                temNowView.setText(temNow.elementAt(i));
                Tem.addView(temNowView);
                body.addView(Tem);

                //风向
                TextView windDirView = new TextView(this);
                windDirView.setLayoutParams(params);
                windDirView.setText(windDir.elementAt(i));
                Wind.addView(windDirView);

                //风力
                TextView windPowerView = new TextView(this);
                windPowerView.setLayoutParams(params);
                windPowerView.setText(windPower.elementAt(i));
                Wind.addView(windPowerView);
                body.addView(Wind);
                //当前风的状况
                TextView windStateView = new TextView(this);
                windStateView.setLayoutParams(params);
                windStateView.setText(windState.elementAt(i));
                body.addView(windStateView);

                //湿度
                TextView humidityView = new TextView(this);
                humidityView.setLayoutParams(params);
                humidityView.setText(humidity.elementAt(i));
                body.addView(humidityView);

                TextView space = new TextView(this);
                space.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                space.setText("----------------------------------------------------------------------");
                body.addView(space);





                //body.addView(City);


                // body.setBackgroundResource(R.drawable.back3);
            }
        }
    }
    class MyBaseAdapter extends BaseAdapter {  //自定义Adapter
        private List<weathers> weathers_l;
        private int layoutId;
        private Context context;

        public MyBaseAdapter(List<com.example.weawther.weathers> weathers, int layoutId, Context context) {
            this.weathers_l = weathers;
            this.layoutId = layoutId;
            this.context = context;
        }



        @Override
        public int getCount() {
            if (weathers_l!=null&&weathers_l.size()>0)
                return weathers_l.size();
            else
                return 0;
        }

        @Override
        public Object getItem(int position) {
            if (weathers_l!=null&&weathers_l.size()>0)
                return weathers_l.get(position);
            else
                return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(
                        getApplicationContext()).inflate(R.layout.list_item, parent,
                        false);
                viewHolder  = new ViewHolder();//定义viewHolder用来查找特定目录
                viewHolder.city = convertView.findViewById(R.id.list_item_body);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            weathers ws = weathers_l.get(position);
            String city=ws.weathername;
            viewHolder.city.setText(city);
            return convertView;
        }
    }


    class ViewHolder{ //组件包
        TextView city;
    }
}
