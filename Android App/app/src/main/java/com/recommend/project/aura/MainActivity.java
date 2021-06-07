package com.recommend.project.aura;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    int PICK_IMAGE=1;
    String selectedImagePath;
    ImageView IVPreviewImage;
    Button recommendBtn;
    String ourEmotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.connect);
        Button b=findViewById(R.id.select);
        recommendBtn = findViewById(R.id.recommendBtn);
        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView responseText = findViewById(R.id.responseText);
                responseText.setText("...");
                checkPermission(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        0);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectServer();

            }
        });

        recommendBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPopUp();
            }
        }));
    }

    void connectServer(){
//        EditText ipv4AddressView = findViewById(R.id.IPAddress);
//        String ipv4Address = ipv4AddressView.getText().toString();
//        EditText portNumberView = findViewById(R.id.portNumber);
//        String portNumber = portNumberView.getText().toString();
        String postUrl= "http://d4e2498bcf58.ngrok.io";
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath, options);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        RequestBody postBodyImage = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "androidFlask.jpg", RequestBody.create(MediaType.parse("image/*jpg"), byteArray))
                .build();

        TextView responseText = findViewById(R.id.responseText);
        responseText.setText("waiting for server..");

        postRequest(postUrl, postBodyImage);
    }

    void postRequest(String postUrl, RequestBody postBody) {


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView responseText = findViewById(R.id.responseText);
                        responseText.setText("Failed to Connect to Server");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView responseText = findViewById(R.id.responseText);


                        //put recommend button enable here
                        try {

                            responseText.setText(response.body().string());
//                            ourEmotion = response.body().string();
                            recommendBtn.setEnabled(true);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(reqCode, resCode, data);
        try {
            if (reqCode == PICK_IMAGE && resCode == RESULT_OK && data != null) {
                Uri uri = data.getData();
                selectedImagePath = getPath(getApplicationContext(), uri);
//                Uri selectedImageUri = data.getData();
                if (null != uri) {
                    // update the preview image in the layout
                    IVPreviewImage.setImageURI(uri);
                }
//                EditText text=findViewById(R.id.imgPath);
//                text.setText(selectedImagePath);
                //Toast.makeText(getApplicationContext(), selectedImagePath, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "You haven't Picked any Image.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something Went Wrong.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, requestCode);
        }
        else {
            selectImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            }
            else {
                Toast.makeText(MainActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public static String getPath(Context context,Uri uri) {
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String type = split[0];

        Uri contentUri = null;
        if ("image".equals(type)) {
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        final String selection = "_id=?";
        final String[] selectionArgs = new String[]{
                split[1]
        };
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            assert contentUri != null;
            cursor = context.getContentResolver().query(contentUri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private void openPopUp() {
        Intent i = new Intent(getApplicationContext(), PopActivity.class);
        TextView responseText = findViewById(R.id.responseText);
        String emo = responseText.getText().toString();
        i.putExtra("emotion", emo);
        startActivity(i);
    }
}



