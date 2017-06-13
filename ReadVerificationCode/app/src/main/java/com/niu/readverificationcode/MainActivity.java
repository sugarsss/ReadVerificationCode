package com.niu.readverificationcode;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnNext;

    public static final int MSG_RECEIVE_CODE = 1; //收到短信的验证码
    private EditText codeEdt; //短信验证码的输入框
    private MessageContentObserver messageContentObserver;    //回调接口

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_RECEIVE_CODE) {
                //设置读取到的内容
                String code = (String) msg.obj;
                if (!TextUtils.isEmpty(code)) {
                    codeEdt.setText(code);
                    codeEdt.setSelection(code.length());
                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnNext = (Button) findViewById(R.id.btn_next);
        codeEdt = (EditText) findViewById(R.id.et_code);


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readSMSCode();
            }
        });

    }

    /**
     * 发送短信
     */
    public void sendMessage() {
        Toast.makeText(this, "开始发送短信", Toast.LENGTH_SHORT).show();
    }

    /**
     * 读取短信
     */
    private void readSMSCode() {

        int api = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
        if (api >= 23) {
            checkPermission();
        } else {
            messageObserver();

        }
    }

    public void messageObserver() {
        messageContentObserver = new MessageContentObserver(MainActivity.this, handler);
        getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, messageContentObserver);
        sendMessage();
    }

    /**
     * 权限
     */
    private void checkPermission() {
        if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)) {//没有权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, 1);

        } else {
            messageObserver();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//同意
                messageObserver();
            }else{
                sendMessage();
            }

        }
    }

    /**
     * 取消注册
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(messageContentObserver);
    }

}
