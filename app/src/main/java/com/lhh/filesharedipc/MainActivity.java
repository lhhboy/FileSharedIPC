package com.lhh.filesharedipc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.annotation.Inherited;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
     //   Intent intent = new Intent(MainActivity.this,SecondActivity.class);
       // startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = new User(1001,"Alice",false);
                String sdCard = Environment.getExternalStorageState();
                // 判断sd卡在手机上正常使用状态
                if(Environment.MEDIA_MOUNTED.equals(sdCard)){
                    File dir = new File(Environment.getExternalStorageDirectory()
                            .getAbsolutePath()+"/file_ipc");
                    if(!dir.exists()){
                        dir.mkdirs();
                    }
                    File temFile = new File(dir + File.separator + "fileipc.txt");
                    if(temFile.exists()){
                        temFile.delete();
                    }
                    File file = new File(dir + File.separator + "fileipc.txt");
                    Log.d("TAG", file.getPath());
                    if(!file.exists()){
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    ObjectOutputStream oos = null;
                    try {
                        oos = new ObjectOutputStream(new FileOutputStream(file));
                        oos.writeObject(user);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        if(oos!=null) {
                            try {
                                oos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }).start();
    }
}
