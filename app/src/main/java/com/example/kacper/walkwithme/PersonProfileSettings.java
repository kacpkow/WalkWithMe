package com.example.kacper.walkwithme;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.kacper.walkwithme.LoginActivity.LoginContent;
import com.example.kacper.walkwithme.Model.PasswordForm;
import com.example.kacper.walkwithme.Model.PhotoData;
import com.example.kacper.walkwithme.Model.PhotoDataDeserializer;
import com.example.kacper.walkwithme.Model.UserData;
import com.example.kacper.walkwithme.Model.UserProfileData;
import com.example.kacper.walkwithme.Model.UserProfileDataDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author Kacper Kowalik
 * @version 1.0
 */
public class PersonProfileSettings extends AppCompatActivity{

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    private Button uploadImgButton;
    private ImageView imageView;

    Dialog dialog;

    private TextView firstName;
    private TextView lastName;
    private TextView description;
    private TextView dateOfBirth;
    private TextView email;
    private TextView password;

    private ImageButton changeFirstNameButton;
    private ImageButton changeLastNameButton;
    private ImageButton changeBirthDateButton;
    private ImageButton changeDescriptionButton;
    private ImageButton changeEmailButton;

    private Integer year = 2000;        //default values
    private Integer month = 1;          //default values
    private Integer day = 1;            //default values

    private String yearString;
    private String monthString;
    private String dayString ;

    private Button saveChangesButton;
    private Button updatePasswordButton;
    UserProfileData user;
    PhotoData photo1;
    InputStream inputStream;

    int userId;

    Bitmap image;

    OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_profile_settings);
        client = RequestController.getInstance().getClient();



        imageView = (ImageView)findViewById(R.id.userImage);
        uploadImgButton = (Button)findViewById(R.id.uploadPhotoButton);

        firstName = (TextView)findViewById(R.id.userFirstName);
        lastName = (TextView)findViewById(R.id.userLastName);
        description = (TextView)findViewById(R.id.userDescription);
        dateOfBirth = (TextView)findViewById(R.id.birthDate);
        email = (TextView)findViewById(R.id.userEmail);

        changeFirstNameButton = (ImageButton)findViewById(R.id.firstNameEditButton);
        changeLastNameButton = (ImageButton)findViewById(R.id.lastNameEditButton);
        changeDescriptionButton = (ImageButton)findViewById(R.id.descriptionEditButton);
        changeBirthDateButton = (ImageButton)findViewById(R.id.birthDateEditButton);
        changeEmailButton = (ImageButton) findViewById(R.id.emailEditButton);
        updatePasswordButton = (Button) findViewById(R.id.buttonChangePassword);

        saveChangesButton = (Button) findViewById(R.id.buttonSaveProfileData);

        changeFirstNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeField(firstName);
            }
        });

        changeLastNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeField(lastName);
            }
        });

        changeDescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeField(description);
            }
        });

        changeBirthDateButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                setDate(dateOfBirth);
            }
        });

        changeEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeField(email);
            }
        });

        uploadImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(pickPhoto , 1);
            }
        });

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            saveData();
            }
        });

        updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });

        SharedPreferences settings = getApplicationContext().getSharedPreferences("userId", Context.MODE_PRIVATE);
        userId = settings.getInt("ID", 0);
        getUserData(toString().valueOf(userId));

    }

    public void updatePassword(){
        dialog = new Dialog(PersonProfileSettings.this);
        dialog.setContentView(R.layout.dialog_change_password);
        dialog.setTitle("Change password");

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.textPasswordLabel);
        text.setText("Change your password");

        final EditText oldPasswordEditText = (EditText) dialog.findViewById(R.id.oldPasswordField);
        final EditText newPasswordEditText = (EditText) dialog.findViewById(R.id.newPasswordField);
        final EditText confirmationPasswordEditText = (EditText) dialog.findViewById(R.id.confirmationPasswordField);

        Button dialogButtonChangePassword = (Button) dialog.findViewById(R.id.dialogButtonChangePassword);
        // if button is clicked, close the custom dialog
        dialogButtonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordForm pwdF = new PasswordForm();
                pwdF.setOldPassword(oldPasswordEditText.getText().toString());
                pwdF.setNewPassword(newPasswordEditText.getText().toString());
                pwdF.setConfirmPassword(confirmationPasswordEditText.getText().toString());

                if(pwdF.getNewPassword().equals(pwdF.getConfirmPassword())){
                    requestChangePassword(pwdF);
                }
                else{
                    Toast.makeText(PersonProfileSettings.this, "Password not changed, password confirmation and new password must be the same", Toast.LENGTH_LONG).show();
                }

            }
        });

        Button dialogButtonCancel= (Button) dialog.findViewById(R.id.dialogButtonCancel);
        // if button is clicked, close the custom dialog
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void requestChangePassword(final PasswordForm pwdF){
        String url = getString(R.string.service_address) + "user/updatePassword";

        Gson gson = new Gson();

        MediaType mediaType = MediaType.parse("application/json");
        final PasswordForm pwdForm = pwdF;

        RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(pwdForm));

        final Request request;
        request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("content-type", "application/json")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("password", "failure");
                backgroundThreadShowDialog(getApplicationContext(), "Password not changed, try again");
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {

                Log.e("password", response.body().string());
                if(response.code() == 200){

                   backgroundThreadShowDialog(getApplicationContext(), "Password changed successfully");
                    SharedPreferences preferences = getApplicationContext().getSharedPreferences("CREDENTIALS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    String json = preferences.getString("values", "");
                    if(json != null){
                        Gson gson = new Gson();
                        LoginContent log = gson.fromJson(json, LoginContent.class);
                        log.setPassword(pwdForm.getNewPassword());
                        String jsonSave = gson.toJson(log);
                        editor.putString("values", jsonSave);
                    }
                    backgroundThreadCloseDialog(getApplicationContext());

                }
                else{
                    backgroundThreadShowDialog(getApplicationContext(), "Password not changed, try again");
                }
            }
        });
    }

    public void backgroundThreadShowDialog(final Context context,
                                           final String msg) {
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    AlertDialog.Builder alert = new AlertDialog.Builder(PersonProfileSettings.this);
                    alert.setTitle("Password");
                    alert.setMessage(msg);
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();

                }
            });
        }
    }

    public void loadPhoto(){
        String url = getString(R.string.service_address) + "profile/photo";

        // Configure Gson
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(PhotoData.class, new PhotoDataDeserializer());
        final Gson retGson = gsonBuilder.create();

        final Request request;
        request = new Request.Builder()
                .url(url)
                .addHeader("content-type", "application/json")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //progressDialog.dismiss();
                //backgroundThreadShortToast(loginView.getAppContext(), "Error in logging occured");
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                //progressDialog.dismiss();
                String json = response.body().string();

                if(response.code() == 200){
                    try{
                        PhotoData photos = retGson.fromJson(json, PhotoData.class);
                        photo1 = photos;
                        backgroundThreadSetImage(PersonProfileSettings.this, photos);
                    }catch(Exception ex){
                        Log.e("ex", ex.getLocalizedMessage());
                    }


                }
            }
        });

    }

    public void backgroundThreadSetImage(final Context context,
                                         final PhotoData photoData) {
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {

                    if(photo1.getPhotoId() != 0){
                        Glide.with(getApplicationContext())
                                .load(Base64.decode(photo1.getData(), Base64.DEFAULT))
                                .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.NONE))
                                .into(imageView);
                    }

                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();

                    try{
                        inputStream = getContentResolver().openInputStream(selectedImage);
                        image = BitmapFactory.decodeStream(inputStream);
                        imageView.setImageBitmap(image);
                        savePhoto(image);
                    }
                    catch(FileNotFoundException e){
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();

                    try{
                        if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
                            inputStream = getContentResolver().openInputStream(selectedImage);
                            Bitmap image = BitmapFactory.decodeStream(inputStream);
                            imageView.setImageBitmap(image);
                            savePhoto(image);
                        }

                    }
                    catch(FileNotFoundException e){
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public void saveData(){
        saveUserData();
        saveDescription();
        getUserData(String.valueOf(userId));

    }

    public void saveDescription(){

        String url = getString(R.string.service_address) + "profile/description";

        String userDescription = String.valueOf(description.getText());
        MediaType mediaType = MediaType.parse("application/json");
        Gson gson = new Gson();

        RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(userDescription));

        final Request request;
        request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .addHeader("content-type", "application/json")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("err", e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {

                if(response.code() == 200){

                }
            }
        });
    }

    public void savePhoto(Bitmap img){

        String url = getString(R.string.service_address) + "profile/photo";
        try {
            inputStream.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.reset();
        img.compress(Bitmap.CompressFormat.JPEG, 0, stream);
        byte[] byteArray = stream.toByteArray();

        MediaType mediaType = MediaType.parse("application/json");
        Gson gson = new Gson();

        PhotoData photo1 = new PhotoData();
        photo1.setTook_time("2017-09-01 14:30:45");
        photo1.setData(byteArray);

        RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(photo1));

        final Request request;
        request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .addHeader("content-type", "application/json")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("err", e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Gson retGson = new Gson();
                //progressDialog.dismiss();
                String json = response.body().string();

                if(response.code() == 200){
                    Log.e("ok", "is ok");


                }
            }
        });
    }

    public void saveUserData(){
        String url = getString(R.string.service_address)+ "user/updateData";

        Gson gson = new Gson();

        MediaType mediaType = MediaType.parse("application/json");
        UserData userData = new UserData();
        userData.setId(userId);
        userData.setFirstName(firstName.getText().toString());
        userData.setLastName(lastName.getText().toString());
        userData.setDate(dateOfBirth.getText().toString());

        SharedPreferences settings = getSharedPreferences("userLocation", Context.MODE_PRIVATE);
        userData.setCity(settings.getString("description", ""));
        Log.e("city", userData.getCity());

        RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(userData));

        final Request request;
        request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("content-type", "application/json")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                backgroundThreadShowToast(getApplicationContext(), "user data has not been changed, try again");
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                //progressDialog.dismiss();
                Log.e("log", response.body().string());

                if(response.code() == 200){

                    backgroundThreadShowToast(getApplicationContext(), "user data has been changed");
                    backgroundThreadGetUserData(getApplicationContext());

                }
                else{
                    backgroundThreadShowToast(getApplicationContext(), "user data has not been changed, try again");
                }
            }
        });
    }

    public static void backgroundThreadShowToast(final Context context,
                                                  final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void backgroundThreadCloseDialog(final Context context) {
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    dialog.dismiss();
                }
            });
        }
    }


    public void getUserData(String userId){

        String url = getString(R.string.service_address) + "user/" + userId;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(UserProfileData.class, new UserProfileDataDeserializer());
        final Gson retGson = gsonBuilder.create();

        final Request request;
        request = new Request.Builder()
                .url(url)
                .addHeader("content-type", "application/json")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //progressDialog.dismiss();
                //backgroundThreadShortToast(loginView.getAppContext(), "Error in logging occured");
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                //progressDialog.dismiss();
                String json = response.body().string();
                Log.e("json people", json);

                if(response.code() == 200){
                    try{
                        user = retGson.fromJson(json, UserProfileData.class);
                        backgroundThreadChangeUserData(getApplicationContext());
                        loadPhoto();
                    }catch(Exception ex){
                        Log.e("err", ex.getLocalizedMessage());
                    }

                }
            }
        });
    }

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (PersonProfileSettings) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (PersonProfileSettings) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((PersonProfileSettings) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    Toast.makeText(PersonProfileSettings.this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

    public void backgroundThreadChangeUserData(final Context context) {
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {

                    firstName.setText(user.getFirstName());
                    lastName.setText(user.getLastName());
                    description.setText(user.getDescription());
                    dateOfBirth.setText(user.getBirth_date());
                    email.setText(user.getEmail());
                }
            });
        }
    }

    public void backgroundThreadGetUserData(final Context context) {
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {

                    getUserData(String.valueOf(userId));
                }
            });
        }
    }

    public void changeField(final TextView view){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                PersonProfileSettings.this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.edit_field_dialog, null);
        final EditText editText = (EditText)dialogView.findViewById(R.id.editText);
        editText.setText(view.getText().toString());
        dialogBuilder.setView(dialogView);

        dialogBuilder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                view.setText(editText.getText().toString());
            }
        });
        dialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });


        final AlertDialog alertDialog = dialogBuilder.create();
        if(!PersonProfileSettings.this.isFinishing()){
            alertDialog.show();
        }

    }

    public static void backgroundThreadShortToast(final Context context,
                                                  final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setDate(final TextView textView){
        yearString = user.getBirth_date().substring(0, 4);
        year = Integer.valueOf(yearString);
        monthString = user.getBirth_date().substring(5,7);
        month = Integer.valueOf(monthString);
        dayString = user.getBirth_date().substring(8,10);
        day = Integer.valueOf(dayString);
        DatePickerDialog datePickerDialog = new DatePickerDialog(PersonProfileSettings.this, new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year1, int monthOfYear, int dayOfMonth) {
                year = year1;
                month = monthOfYear;
                day = dayOfMonth;
                String str = String.format("%02d-%02d-%02d", year, month + 1, day);
                textView.setText(str);
            }
        }, year, month, day);

        if(!PersonProfileSettings.this.isFinishing()){
            datePickerDialog.show();
        }
    }
}
