package com.example.afor;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class asker extends AppCompatActivity {

    EditText textq;
    ImageView imageQ;
    Button textqB;
    Button imageqB;
   public Uri uriS;
    StorageReference mStRef;
    private DatabaseReference mydbRef;
    private static  asker instance;
    Uri uri1 = Uri.parse("android.resource://your.package.here/drawable/a1.png");
    String uriChecker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asker);

        textq=(EditText)findViewById(R.id.texter);
        imageQ=(ImageView)findViewById(R.id.imageq);
        textqB=(Button)findViewById(R.id.uploadtext);
        imageqB=(Button)findViewById(R.id.uploadimage);
        mStRef= FirebaseStorage.getInstance().getReference();
        mydbRef = FirebaseDatabase.getInstance().getReference("Questions");

        instance = this;




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
                            uriS = data.getData();
                            imageQ.setImageURI(uriS);

                        }
                    }

                });


        textqB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fileupload();


            }
        });


        imageqB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent fileIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                someActivityResultLauncher.launch(fileIntent);

                Toast.makeText(asker.this,"FILE OPENING", Toast.LENGTH_LONG).show();


                //fileupload();

            }
        });

    }

    static asker getInstance(){
        return  instance;
    }



    public String getExtension(Uri uri){

        ContentResolver contentResolver = getContentResolver();
        //for file extension
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    public void  fileupload(){
        //naming files
        StorageReference Ref = mStRef.child(System.currentTimeMillis()+"."+getExtension(uriS));

        Ref.putFile(uriS)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //url for to the realtime databse
                Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String name = textq.getText().toString();
                        //uriChecker = uri.toString();
                        answer_recieve message = new answer_recieve(name, uri.toString());
                        String modelId = mydbRef.push().getKey();
                        if(name != null && !name.isEmpty()) {
                            mydbRef.child(modelId).setValue(message);
                            Toast.makeText(asker.this, "Question Uploaded Successfully", Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(asker.this,"PLEASE ADD THE TEXT QUESTION AS WELL", Toast.LENGTH_LONG).show();

                    }
                });
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(asker.this, "QUESTION NOT UPLOADED", Toast.LENGTH_SHORT).show();

                    }
                });






    }
}