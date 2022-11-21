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


    private static final int PICK_IMAGE = 1;

    Button upload, choose;
    TextView alert;
    ArrayList<Uri> ImageList = new ArrayList<>();
    private Uri ImageUri;
    private ProgressDialog progressDialog;
    private int upload_count = 0;

    private Button button;

    private ImageView imageView;
    private ProgressBar progressBar;
    private final DatabaseReference root = FirebaseDatabase.getInstance().getReference("Image");
    private final StorageReference reference = FirebaseStorage.getInstance().getReference();

    private Uri imageUri;



    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference conditionRef = mRootRef.child("text");


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








    } // onCreate


    private void StoreLink(String url){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("UserOne");
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Imagelink",url);

        databaseReference.push().setValue(hashMap);

        progressDialog.dismiss();
        alert.setText("Image Uploaded Successfully");
        upload.setVisibility(View.GONE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE){
            if(resultCode == RESULT_OK){
                if(data.getClipData() != null){

                    int countClipData = data.getClipData().getItemCount();

                    int currentImageSelect = 0;
                    while(currentImageSelect < countClipData){

                        ImageUri = data.getClipData().getItemAt(currentImageSelect).getUri();
                        ImageList.add(ImageUri);
                        currentImageSelect = currentImageSelect +1;
                    }

                    alert.setVisibility(View.VISIBLE);
                    alert.setText("YOU HAVE SELECTED"+ImageList.size()+"Images");
                    choose.setVisibility(View.GONE);
                }
                else{
                    Toast.makeText(this, "Please choose images", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // 사진 가져오기
    ActivityResultLauncher<Intent> activityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();

                        imageView.setImageURI(imageUri);
                    }
                }
            });



    //파일타입 가져오기
    private String getFileExtension(Uri uri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(cr.getType(uri));
    }





}

    

