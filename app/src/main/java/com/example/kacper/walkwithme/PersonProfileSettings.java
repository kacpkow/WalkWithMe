package com.example.kacper.walkwithme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import com.example.kacper.walkwithme.R;
import java.io.FileNotFoundException;
import java.io.InputStream;


import static android.app.Activity.RESULT_OK;

public class PersonProfileSettings extends AppCompatActivity {

    private Button uploadImgButton;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_profile_settings);
        imageView = (ImageView)findViewById(R.id.userImage);
        uploadImgButton = (Button)findViewById(R.id.uploadPhotoButton);

        uploadImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(pickPhoto , 1);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream inputStream;

                    try{
                        inputStream = getContentResolver().openInputStream(selectedImage);
                        Bitmap image = BitmapFactory.decodeStream(inputStream);
                        //imageView.setImageBitmap(image);
                    }
                    catch(FileNotFoundException e){
                        e.printStackTrace();
                    }

                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream inputStream;

                    try{
                        inputStream = getContentResolver().openInputStream(selectedImage);
                        Bitmap image = BitmapFactory.decodeStream(inputStream);
                        //imageView.setImageBitmap(image);
                    }
                    catch(FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

}
