package com.example.berni.onclick;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Third_Activity extends AppCompatActivity {
    EditText et_phone, et_web;
    ImageButton ib_phone, ib_web;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_);
        casting();

      ib_phone.setOnClickListener(new View.OnClickListener() {

          @Override
          public void onClick(View v) {
              String telephone = et_phone.getText().toString();
              if (!telephone.isEmpty()){
                  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                      requestPermissions(new String[]{Manifest.permission.CALL_PHONE},100);
                  }else {
                      olderVersion(telephone);
                  }
              }else {
                  message("Esta vacio el telefono");
              }
          }

          @SuppressLint("MissingPermission")
          private void olderVersion(String number){
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
            if (checkPermision(Manifest.permission.CALL_PHONE)){
                startActivity(intent);
            }else {
                message("No tienes permisos");
            }
          }

      });




    }
    public void casting(){
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_web = (EditText) findViewById(R.id.et_web);
        ib_phone = (ImageButton) findViewById(R.id.ib_phone);
        ib_web = (ImageButton) findViewById(R.id.ib_web);
    }



    public void message(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();

    }

    private boolean checkPermision(String permission){
        int result = this.checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Caso del telefono
        if (requestCode == 100) {
            String permission = permissions[0];
            int result = grantResults[0];
            if (permission.equals(Manifest.permission.CALL_PHONE)){
                //comprobar si ha sido aceptado o denegado la peticion de permiso
                if (result == PackageManager.PERMISSION_GRANTED){
                    // Acepto su permiso
                    String telephone = et_phone.getText().toString();
                    Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+telephone));
                    startActivity(intent);
                }else {
                    // No acepto su permiso
                }
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
