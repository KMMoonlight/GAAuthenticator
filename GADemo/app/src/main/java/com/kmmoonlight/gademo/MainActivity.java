package com.kmmoonlight.gademo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            CalcCode();
            handler.sendEmptyMessageDelayed(0, 1000);
        }
    };


    private ImageView iv_add;

    private AlertDialog alertDialog;

    private CodeAdapter codeAdapter;

    private List<Code> codeList;
    private List<Code> secretList;

    private ListView lv_code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        handler.sendEmptyMessageDelayed(0, 1000);
    }

    private void initView() {
        iv_add = findViewById(R.id.iv_add);
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_secret_dialog, null);
                final EditText et_name = view.findViewById(R.id.et_name);
                final EditText et_code = view.findViewById(R.id.et_code);
                builder.setView(view).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = et_name.getText().toString().trim();
                        String secret = et_code.getText().toString().trim();
                        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(secret)) {
                            SaveSecret(name, secret);
                        }

                        //刷新列表
                        secretList.clear();
                        secretList.addAll(getSecret());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog = builder.create();

                alertDialog.show();
            }
        });


        codeList = new ArrayList<>();
        codeAdapter = new CodeAdapter(codeList, this);
        lv_code = findViewById(R.id.lv_authenticator);
        lv_code.setAdapter(codeAdapter);

        lv_code.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                deleteSecret(codeList.get(position));

                secretList.clear();
                secretList.addAll(getSecret());
            }
        });

        secretList = new ArrayList<>();
        secretList.clear();
        secretList.addAll(getSecret());

        handler.sendEmptyMessageDelayed(0, 1000);
    }


    private void CalcCode() {
        codeList.clear();
        for (Code code :  secretList) {
            codeList.add(new Code(code.getCodeName(), Ga.getTOTP(code.getCodeContent(), System.currentTimeMillis() / 1000 / 30)));
        }
        codeAdapter.notifyDataSetChanged();
    }


    private void SaveSecret(String name, String secret) {
        SharedPreferences preferences = getSharedPreferences("GA", MODE_PRIVATE);
        Set<String> ga = preferences.getStringSet("ga", new HashSet<String>());
        ga.add(name + "--" + secret);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putStringSet("ga", ga);
        edit.commit();
    }

    private List<Code> getSecret() {
        List<Code> secretList = new ArrayList<>();
        SharedPreferences preferences = getSharedPreferences("GA", MODE_PRIVATE);
        Set<String> ga = preferences.getStringSet("ga", new HashSet<String>());
        for (String str: ga) {
            secretList.add(new Code(str.split("--")[0],str.split("--")[1]));
        }

        return secretList;
    }


    private void deleteSecret(Code code) {
        String saveStr = code.getCodeName() + "--" + code.getCodeContent();
        SharedPreferences preferences = getSharedPreferences("GA", MODE_PRIVATE);
        Set<String> ga = preferences.getStringSet("ga", new HashSet<String>());
        ga.remove(saveStr);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putStringSet("ga", ga);
        edit.commit();
    }


}