package com.example.internapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class shopadd extends AppCompatActivity {

    private TextView addshop;
    private TextView imagebox;
    private ImageView image;
    private TextView addshopname;
    private TextView Address;
    private TextView city;
    private TextView category;
    private TextView Mobile;
    private EditText name;
    private EditText Addressenter;
    private EditText cityenter;
    private EditText categoryenter;
    private EditText Mobileenter;
    private TextView shoptimeview;
    private EditText shoptime;
    private Button addbtn;


    private FirebaseFirestore mFirestore;
    private static final  int ImageBack=1;
    private StorageReference Folder;
    private Task<Uri> Imageurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopadd);

        Folder= FirebaseStorage.getInstance().getReference().child("ImageFolder");

        mFirestore=FirebaseFirestore.getInstance();

        addshop=findViewById(R.id.addshop);
        imagebox=findViewById(R.id.imagebox);
        image=findViewById(R.id.image);
        addshopname=findViewById(R.id.addshopname);
        Address=findViewById(R.id.Address);
        city=findViewById(R.id.city);
        category=findViewById(R.id.category);
        Mobile=findViewById(R.id.Mobile);
        name=findViewById(R.id.name);
        Addressenter=findViewById(R.id.Addressenter);
        cityenter=findViewById(R.id.cityenter);
        categoryenter=findViewById(R.id.categoryenter);
        Mobileenter=findViewById(R.id.Mobileenter);
        shoptimeview=findViewById(R.id.Shoptimeview);
        shoptime=findViewById(R.id.shoptime);
        addbtn=findViewById(R.id.addbtn);

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shopname= name.getText().toString();
                String Address=Addressenter.getText().toString();
                String city=cityenter.getText().toString();
                String category=categoryenter.getText().toString();
                String mobile=Mobileenter.getText().toString();
                String time=shoptime.getText().toString();
                HashMap<String, String> usermap=new HashMap<>();
                usermap.put("shopname",shopname);
                usermap.put("ShopAddress",Address);
                usermap.put("shopcity",city);
                usermap.put("category",category);
                usermap.put("mobile",mobile);
                usermap.put("shoptime",time);
                usermap.put("Imageurl", String.valueOf(Imageurl));


                mFirestore.collection("shop info").add(usermap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        if(!vallidateshopname() | !vallidateaddress() | !vallidatecity() | !vallidatecategory() | !vallidatemobile() | !vallidatetime()){
                            return;
                        }else{
                            Intent intent=new Intent();
                            intent.setClass(shopadd.this,MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(shopadd.this,"shop added successfully",Toast.LENGTH_SHORT).show();

                        }





                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String error=e.getMessage();
                        Toast.makeText(shopadd.this,"Error :"+error,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
    private boolean vallidateshopname(){
        String  shopnamein=name.getEditableText().toString().trim();

        if (shopnamein.isEmpty()){
            name.setError("Field cant be empty");
            return false;
        } else{
            name.setError(null);
            return true;
        }
    }
    private boolean vallidateaddress(){
        String  addressin=Addressenter.getEditableText().toString().trim();

        if (addressin.isEmpty()){
            Addressenter.setError("Field cant be empty");
            return false;
        } else{
            Addressenter.setError(null);
            return true;
        }
    }
    private boolean vallidatecity(){
        String  cityin=cityenter.getEditableText().toString().trim();

        if (cityin.isEmpty()){
            cityenter.setError("Field cant be empty");
            return false;
        } else{
            cityenter.setError(null);
            return true;
        }
    }
    private boolean vallidatecategory(){
        String  categoryin=categoryenter.getEditableText().toString().trim();

        if (categoryin.isEmpty()){
            categoryenter.setError("Field cant be empty");
            return false;
        } else{
            categoryenter.setError(null);
            return true;
        }
    }
    private boolean vallidatemobile(){
        String  mobilein=Mobileenter.getEditableText().toString().trim();

        if (mobilein.isEmpty()){
            Mobileenter.setError("Field cant be empty");
            return false;
        }else{
            Mobileenter.setError(null);
            return true;
        }
    }
    private boolean vallidatetime(){
        String  timein=shoptime.getEditableText().toString().trim();

        if (timein.isEmpty()){
            shoptime.setError("Field cant be empty");
            return false;
        }else{
            shoptime.setError(null);
            return true;
        }
    }


    public void UploadImage(View view) {
        Intent intent= new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,ImageBack);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ImageBack){
            if(resultCode==RESULT_OK){
                Uri imagedata=data.getData();

                StorageReference Imagename=Folder.child("image"+imagedata.getLastPathSegment());

                Imagename.putFile(imagedata).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Imageurl=Imagename.getDownloadUrl();
                    }
                });
            }
        }
    }

}