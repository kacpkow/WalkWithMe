package com.example.kacper.walkwithme.RegistrationActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.kacper.walkwithme.Model.RegistrationForm;
import com.example.kacper.walkwithme.R;
import com.example.kacper.walkwithme.RequestController;
import com.google.gson.Gson;

import java.io.IOException;

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

public class RegistrationPresenterImpl implements RegistrationPresenter{

    private RegistrationView registrationView;
    ProgressDialog progressDialog;

    OkHttpClient client;

    public RegistrationPresenterImpl(RegistrationView registrationView) {
        this.registrationView = registrationView;
        progressDialog = new ProgressDialog(registrationView.getActivityContext());
        RequestController.getInstance().newClient();
        client = RequestController.getInstance().getClient();
    }

    @Override
    public void register(RegistrationForm form) {
        if(validateFields(form)){
            progressDialog = new ProgressDialog(registrationView.getActivityContext());
            String url = registrationView.getAppContext().getString(R.string.service_address) + "registration";
            progressDialog.setTitle("Account registering, please wait ...");
            progressDialog.show();
            OkHttpClient client = new OkHttpClient();
            Log.e("form", form.getNick().toString() + " " + form.getFirstName().toString() + " "+ form.getLastName().toString());

            Gson gson = new Gson();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(form));

            Log.e("form", gson.toJson(form));

            Request request;
            request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("content-type", "application/json")
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    progressDialog.dismiss();
                    backgroundThreadShowDialog(registrationView.getAppContext(),"Connection error, try again...");
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    progressDialog.dismiss();
                    String json = response.body().string();
                    Log.e("resp code", String.valueOf(response.code()));

                    if(response.code() == 409){
                        backgroundThreadShowDialog(registrationView.getAppContext(), "Login busy. Your account has not been registered. Please try again.");
                    }
                    else{
                        backgroundThreadCloseActivity(registrationView.getAppContext(), registrationView);
                        backgroundThreadShortToast(registrationView.getAppContext(), "Your account has been registered.");
                    }

                }
            });

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

    public static void backgroundThreadCloseActivity(final Context context, final RegistrationView view) {
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    view.finishActivity();
                }
            });
        }
    }

    public void backgroundThreadShowDialog(final Context context, final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                            registrationView.getAppContext());

                    dialogBuilder.setTitle(msg);

                    dialogBuilder.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();
                }
            });
        }
    }


    public Boolean validateFields(RegistrationForm form){
        if(TextUtils.isEmpty(form.getNick())){
            registrationView.showToast("Empty nick field\nTry again");
            return false;
        }
        if (TextUtils.isEmpty(form.getPassword()) || (form.getPassword().length() < 6) || form.getPassword().length() > 15){
            registrationView.showToast("Your password does not match to the requirements\nTry again");
            return false;
        }

        if(!isValidEmail(form.getMail())){
            registrationView.showToast("Your email is not correct\nTry again");
            return false;
        }
        return true;
    }

    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public void onDestroy() {
        registrationView = null;
    }

}
