package com.example.berni.onclick;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Third_Activity extends AppCompatActivity {
    EditText et_phone, et_web;
    ImageButton ib_phone, ib_web, ib_send;
    public static final int CAMERA_SAVE = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_);
        // con este muestra la muestra para retroceder despues se configura en el manifest parentActivityName
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // R = resursos mipmap ests en res
        getSupportActionBar().setIcon(R.mipmap.ic_myicon);
        casting();
        ib_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = et_web.getText().toString();
                String mail = "berna_halo@hotmail.com";
                if (!url.isEmpty() && !url.equals(null)) {
                    Intent intent = new Intent( );
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://"+url));
                    //Contactos
                    Intent i_contacts = new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people"));
                    //email
                    Intent i_mail = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto"+mail));
                    //email completo
                    Intent i_mail_complete = new Intent(Intent.ACTION_SEND, Uri.parse(mail));
                    i_mail_complete.setType("plain/text");
                    i_mail_complete.putExtra(Intent.EXTRA_SUBJECT,"Titulo");
                    i_mail_complete.putExtra(Intent.EXTRA_TEXT,"Parrafo");
                    i_mail_complete.putExtra(Intent.EXTRA_EMAIL, new String[]{"berna_halo@hotmail.com"});
                    // Telefono 2
                    Intent i_tel = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:58702575"));
                    // Abrir Camara



                    startActivity(Intent.createChooser(i_mail_complete,"Elige cliente de correo"));
                    //startActivity(i_mail_complete);

                }else {
                    message("No tiene valor");
                }
            }
        });

        ib_phone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String telephone = et_phone.getText().toString();
                if (!telephone.isEmpty()) {
                    //Comprobar version actual que estamos corriendo
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        //Comprobar si ha aceptado, no ha aceptado o nunca se le ha preguntado
                        if (checkPermision(Manifest.permission.CALL_PHONE)) {
                            // ha aceptado
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telephone));
                            if (ActivityCompat.checkSelfPermission(Third_Activity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            startActivity(intent);
                      }else {

                           if (!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)){
                               // Si no se le ha preguntado aun
                               requestPermissions(new String[]{Manifest.permission.CALL_PHONE},100);
                           }else {
                                // si se ha denegado
                               message("Activa los permisos");
                               Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                               i.addCategory(Intent.CATEGORY_DEFAULT);
                               i.setData(Uri.parse("package:"+getPackageName()));
                               i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                               i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                               i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                               startActivity(i);
                           }
                        }


                  }else {
                      olderVersion(telephone);
                  }
              }else {
                  message("Esta vacio el telefono");
              }
          }

          @SuppressLint("MissingPermission")
          private void olderVersion(String number){
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+number));
            if (checkPermision(Manifest.permission.CALL_PHONE)){
                startActivity(intent);
            }else {
                message("No tienes permisos");
            }
          }

      });

        ib_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, CAMERA_SAVE);
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case CAMERA_SAVE:
                if (requestCode== Activity.RESULT_OK){
                    String result = data.toUri(0);
                    message("result"+ result);
                }else {
                    message("error");
                }

                break;

                default:
                    super.onActivityResult(requestCode, resultCode, data);
        }

    }

    public void casting(){
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_web = (EditText) findViewById(R.id.et_web);
        ib_phone = (ImageButton) findViewById(R.id.ib_phone);
        ib_web = (ImageButton) findViewById(R.id.ib_web);
        ib_send = (ImageButton) findViewById(R.id.btn_send);
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
