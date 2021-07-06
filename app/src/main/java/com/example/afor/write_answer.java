package com.example.afor;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class write_answer extends AppCompatActivity {

    Uri uriA;
    ImageView imageQ2;
    Button textqB2;
    Button imageqB2;
    StorageReference mStRef;
    private DatabaseReference mydbRef1;



    TextView quesnumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_answer);

        imageQ2=(ImageView)findViewById(R.id.imageq2);
        textqB2=(Button)findViewById(R.id.uploadtext2);
        imageqB2=(Button)findViewById(R.id.uploadimage2);
        mStRef = FirebaseStorage.getInstance().getReference();
        mydbRef1 = FirebaseDatabase.getInstance().getReference("Answers");

        quesnumber = (TextView)findViewById(R.id.quesno);

        String quesno = "GETTING IT";

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            quesno = extras.getString("name");
        }

        quesnumber.setText(quesno);
        //now upload the answer
        //selecting file
        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            //file operation

                            assert data != null;
                            //path = data.getData().getPath();  //gets the directory path
                            // file_path.setText(path);
                            uriA = data.getData();
                            imageQ2.setImageURI(uriA);

                        }
                    }

                });

        textqB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fileupload1();

            }
        });

        imageqB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent fileIntent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                someActivityResultLauncher.launch(fileIntent1);

                Toast.makeText(write_answer.this,"FILE OPENING", Toast.LENGTH_LONG).show();



            }
        });
    }


    public String getExtension(Uri uri){

        ContentResolver contentResolver = getContentResolver();
        //for file extension
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private  void  fileupload1(){
        //naming files
        StorageReference Ref = mStRef.child("A"+System.currentTimeMillis()+"."+getExtension(uriA));

        Ref.putFile(uriA)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //url for to the realtime databse
                        Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                model message1 = new model(uri.toString());
                                String modelId = mydbRef1.push().getKey();
                                mydbRef1.child(modelId).setValue(message1);



                                Toast.makeText(write_answer.this, "ANSWER Uploaded Successfully", Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(write_answer.this, "ANSWER NOT UPLOADED", Toast.LENGTH_SHORT).show();

                    }
                });






    }

}