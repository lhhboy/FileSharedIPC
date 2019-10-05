package com.lhh.filesharedipc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class SecondActivity extends AppCompatActivity {
private TextView tv;
private Handler textHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        tv = findViewById(R.id.textView);
        initHandler();
        initView();
    }

    private void initView() {
        ActivityCompat.requestPermissions(SecondActivity.this,new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
        },1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = null ;
                File file = new File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath()+File.separator + "file_ipc"+ File.separator
                        +"fileipc.txt");
                if(file.exists()){
                    ObjectInputStream ois = null;
                    try {
                        ois = new ObjectInputStream(new FileInputStream(file));
                        user = (User) ois.readObject();
                        Message msg = Message.obtain();
                        msg.what = 1;
                        msg.obj = user;
                        textHandler.sendMessage(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }finally {
                        if(ois!=null){
                            try {
                                ois.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }).start();

    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        textHandler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what == 1){
                    User user = (User) msg.obj;
                    tv.setText("读取文件成功\r\n");
                    tv.append("用户编号："+ user.userId+"\r\n");
                    tv.append("用户姓名："+ user.userName+"\r\n");
                    tv.append("用户性别："+ (user.isMale? "男":"女") + "\r\n");
                }
            }
        };

    }
}
