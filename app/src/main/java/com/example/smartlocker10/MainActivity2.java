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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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






        //파이어베이스에서 이미지 불러오기
        Button Button_load = (Button) findViewById(R.id.image_load);

        Button_load.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) { //이미지 불러오기 이미지 전환
                Intent intent = new Intent(getApplicationContext(), Activity.class);
                startActivity(intent);
            }
        });


        alert = findViewById(R.id.alert);
        upload = findViewById(R.id.upload_image);
        choose = findViewById(R.id.chooser);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Image Uploading... please wait..");
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(intent,PICK_IMAGE);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();
                alert.setText("IF loading takes too long please Press the button again");

                StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("ImageFolder");

                for(upload_count = 0; upload_count < ImageList.size(); upload_count++){

                    Uri IndividualImage = ImageList.get(upload_count);
                    StorageReference ImageName = ImageFolder.child("Image"+IndividualImage.getLastPathSegment());

                    ImageName.putFile(IndividualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = String.valueOf(uri);

                                    StoreLink(url);
                                }
                            });
                        }
                    });




                }

            }
        });

        button = (Button) findViewById(R.id.rdbutton);

    } // onCreate


    @Override //  realtimeDB 시작점
    protected void onStart() {
        super.onStart();

        conditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conditionRef.setValue("True");
            }
        });
    }
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

    private void uploadToFirebase(Uri uri){

        StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));

        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener((new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        // 이미지 모델에 담기
                        Model model = new Model(uri.toString());

                        //키로 아이디 생성
                        String modelId = root.push().getKey();

                        // 데이터 넣기
                        root.child(modelId).setValue(model);

                        // 프로그래스바 숨김
                        progressBar.setVisibility(View.INVISIBLE);

                        Toast.makeText(MainActivity2.this, "업로드 성공", Toast.LENGTH_SHORT).show();

                        imageView.setImageResource(R.drawable.ic_add_photo);

                    }
                }));
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                // 프로그래스바 보여주기
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                // 프로그래스바 숨김
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity2.this,"업로드 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //파일타입 가져오기
    private String getFileExtension(Uri uri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(cr.getType(uri));
    }





    }

    

