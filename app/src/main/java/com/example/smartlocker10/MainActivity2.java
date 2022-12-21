package com.example.smartlocker10;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);   //메인 2레이아웃 실행

        EditText editText = (EditText) findViewById(R.id.Date);

        //파이어베이스에서 이미지 불러오기
        Button Button_load = (Button) findViewById(R.id.image_load);

        Button_load.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) { //이미지 불러오기 이미지 전환
                String value = editText.getText().toString();
                if(value.length()==8) {
                    Intent intent = new Intent(MainActivity2.this, Activity.class);
                    intent.putExtra("Value", value);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(MainActivity2.this,"날짜 형식에 맞게 입력",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




}

    

