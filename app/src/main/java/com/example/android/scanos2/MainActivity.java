package com.example.android.scanos2;



import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ExampleDialog.ExampleDialogListener, SearchDialog.SearchDialogListener {
public String UserInput;
public String Keysearch="Abhi";
private Button btn;

    @Override
    public void applyTexts(String filename) {
        modify_string(filename);
        openCamera();

    }

    @Override
    public void applySearch(String keyword) {
       modify_keysearch(keyword);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        btn= (Button) findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   openDialog2(v);
            }
        });
        FloatingActionButton fab =
                (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click.
                openDialog();

            }
        });

    }

    public void modify_string(String b){
        UserInput=b;
    }

    public void modify_keysearch(String t){Keysearch=t; openScanos();}

    public void openDialog2(View v) {
        SearchDialog searchDialog = new SearchDialog();
        searchDialog.show(getSupportFragmentManager(), "search dialog");
    }

    public void openScanos(){
        Uri selectedUri = Uri.parse(Environment
                .getExternalStorageDirectory()
                .toString()+"/Scanos/"+Keysearch+".jpg");
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(selectedUri,"*/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void openCamera(){

        int REQUEST_CODE = 99;
        int preference = ScanConstants.OPEN_CAMERA;
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void openDialog(){
        ExampleDialog exampleDialog= new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(),"example dialog");

    }

    private void checkPermission() {
        //ask for permission
        PermissionListener permissionListener= new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Please grant access", Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.with(MainActivity.this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET)
                .check();
    }

    private void saveImage(Bitmap finalBitmap, String a) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Scanos");
        myDir.mkdirs();

        String fname =  a +".jpg";

         File file = new File(myDir, fname);

        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99 && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                getContentResolver().delete(uri, null, null);

                saveImage(bitmap,UserInput);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        }

}


