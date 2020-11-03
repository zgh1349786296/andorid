package com.example.notebook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.notebook.database.MyDB;
import com.example.notebook.enity.Record;

import java.util.ArrayList;
import java.util.List;
public class MainActivity extends BaseActivity  implements View.OnClickListener,
        AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{
    private final static String TAG = "MainActivity";

    MyDB myDB;  //定义数据集
    private ListView myListView;  //定义布局
    private Button createButton;  //定义新建按钮
    private Button backButton;
    private MyBaseAdapter myBaseAdapter;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  //设置主界面
        init();
    }
    private void init(){  //初始化
        createButton = findViewById(R.id.createButton);  //设置按钮
        createButton.setOnClickListener(this);   //设置点击事件
        backButton=findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

        myListView = findViewById(R.id.list_view);   //设置布局

        List<Record> recordList = new ArrayList<>();  //事件集合
        myDB = new MyDB(this);  //设置数据库
        SQLiteDatabase db = myDB.getReadableDatabase();  //打开数据库
        Cursor cursor = db.query(MyDB.TABLE_NAME_RECORD,null,
                null,null,null,
                null,MyDB.NOTICE_TIME+","+MyDB.RECORD_TIME+" DESC");//将数据库信息按时间排序
        if(cursor.moveToFirst()){  //遍历数据库中record
            Record record;
            while (!cursor.isAfterLast()){  //给每个record赋值到对象
                record = new Record();
                record.setId(
                        Integer.valueOf(cursor.getString(cursor.getColumnIndex(MyDB.RECORD_ID))));
                record.setTitleName(
                        cursor.getString(cursor.getColumnIndex(MyDB.RECORD_TITLE))
                );
                record.setTextBody(
                        cursor.getString(cursor.getColumnIndex(MyDB.RECORD_BODY))
                );
                record.setAuthor(
                        cursor.getString(cursor.getColumnIndex(MyDB.RECORD_AUTHOR))
                );
                record.setCreateTime(
                        cursor.getString(cursor.getColumnIndex(MyDB.RECORD_TIME)));
                record.setNoticeTime(
                        cursor.getString(cursor.getColumnIndex(MyDB.NOTICE_TIME)));
                recordList.add(record);  //加入数组
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        // 创建一个Adapter的实例
        myBaseAdapter = new MyBaseAdapter(this,recordList,R.layout.list_items);
        myListView.setAdapter(myBaseAdapter);
        // 设置点击监听
        myListView.setOnItemClickListener(this);
        myListView.setOnItemLongClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){ //点击新建按钮
            case R.id.createButton:
                Intent creat_intent = new Intent(MainActivity.this, EditActivity.class);  //跳转新建界面
                startActivity(creat_intent);
                MainActivity.this.finish();
                break;
            case R.id.backButton:
                Intent back_intent = new Intent(MainActivity.this, LoginActivity.class);  //跳转登录界面
                startActivity(back_intent);
                MainActivity.this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //点击事件
        Intent intent = new Intent(MainActivity.this,AmendActivity.class);  //跳修改界面
        Record record = (Record) myListView.getItemAtPosition(position);  //传值到修改界面
        intent.putExtra(MyDB.RECORD_TITLE,record.getTitleName().trim());
        intent.putExtra(MyDB.RECORD_BODY,record.getTextBody().trim());
        intent.putExtra(MyDB.RECORD_AUTHOR,record.getAuthor().trim());
        intent.putExtra(MyDB.RECORD_TIME,record.getCreateTime().trim());
        intent.putExtra(MyDB.RECORD_ID,record.getId().toString().trim());
        if (record.getNoticeTime()!=null) {
            intent.putExtra(MyDB.NOTICE_TIME, record.getNoticeTime().trim());
        }
        this.startActivity(intent);
        MainActivity.this.finish();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Record record = (Record) myListView.getItemAtPosition(position);
        showDialog(record,position);  //长按删除
        return true;
    }

    void showDialog(final Record record,final int position){

        final AlertDialog.Builder dialog =
                new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("是否删除？");  //提示符
        String textBody = record.getTitleName();  //界面显示标题
        dialog.setMessage(
                textBody.length()>150?textBody.substring(0,150)+"...":textBody);  //标题过长删减
        dialog.setPositiveButton("删除",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = myDB.getWritableDatabase();
                        db.delete(MyDB.TABLE_NAME_RECORD,
                                MyDB.RECORD_ID +"=?",
                                new String[]{String.valueOf(record.getId())});  //查找并删除
                        db.close();
                        myBaseAdapter.removeItem(position); //移除已经删除的界面
                        myListView.post(new Runnable() {
                            @Override
                            public void run() {
                                myBaseAdapter.notifyDataSetChanged();//更新列表
                            }
                        });
                    }
                });
        dialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        dialog.show();
    }



    /**
     * ListView展示的适配器类
     */
    class MyBaseAdapter extends BaseAdapter {  //自定义Adapter
        private List<Record> recordList;//数据集合
        private Context context;
        private int layoutId;

        public MyBaseAdapter(Context context,List<Record> recordList,int layoutId){
            this.context = context;
            this.recordList = recordList;  //事物列表
            this.layoutId = layoutId;  //界面id
        }

        @Override
        public int getCount() {
            if (recordList!=null&&recordList.size()>0)
                return recordList.size();
            else
                return 0;
        }

        @Override
        public Object getItem(int position) {  //得到某事件
            if (recordList!=null&&recordList.size()>0)
                return recordList.get(position);
            else
                return null;
        }

        public void removeItem(int position){
            this.recordList.remove(position);
        } //移除事件

        @Override
        public long getItemId(int position) {
            return position;
        }  //得到事件头标

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(
                        getApplicationContext()).inflate(R.layout.list_items, parent,
                        false);
                viewHolder  = new ViewHolder();//定义viewHolder用来查找特定目录
                viewHolder.titleView = convertView.findViewById(R.id.list_item_title);
                viewHolder.bodyView = convertView.findViewById(R.id.list_item_body);
                viewHolder.authorView = convertView.findViewById(R.id.list_item_author);
                viewHolder.timeView = convertView.findViewById(R.id.list_item_time);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Record record = recordList.get(position);
            String tile = record.getTitleName(); //设置标题
            viewHolder.titleView.setText((position+1)+"."+(tile.length()>7?tile.substring(0,7)+"...":tile));
//            viewHolder.titleView.setText(tile);
            String body = record.getTextBody();  //设置主体
            viewHolder.bodyView.setText(body.length()>13?body.substring(0,12)+"...":body);
//            viewHolder.bodyView.setText(body);

            String author =record.getAuthor();
            viewHolder.authorView.setText(author);

            String createTime = record.getCreateTime();  //设置创建时间
            viewHolder.timeView.setText(createTime);


            return convertView;
        }
    }

    /**
     * ListView里的组件包装类
     */
    class ViewHolder{ //组件包
        TextView titleView;
        TextView bodyView;
        TextView authorView;
        TextView timeView;

    }
}