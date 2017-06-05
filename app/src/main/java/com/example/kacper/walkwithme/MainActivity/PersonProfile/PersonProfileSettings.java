package com.example.kacper.walkwithme.MainActivity.PersonProfile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

public class PersonProfileSettings extends Fragment {

    private Button uploadImgButton;
    private ImageView imageView;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_person_profile_settings, container, false);

        imageView = (ImageView)rootView.findViewById(R.id.userImage);
        uploadImgButton = (Button) rootView.findViewById(R.id.uploadPhotoButton);

        uploadImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(pickPhoto , 1);
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        return rootView;
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
                        inputStream = getActivity().getContentResolver().openInputStream(selectedImage);
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
                        inputStream = getActivity().getContentResolver().openInputStream(selectedImage);
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
