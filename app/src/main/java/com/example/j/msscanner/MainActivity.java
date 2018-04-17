package com.example.j.msscanner;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private ZXingScannerView scannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
             ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 1);
        else showMessageOKCancel( getString(R.string.Welcome), null);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        final SpannableString s = new SpannableString(message);
        Linkify.addLinks(s, Linkify.ALL);
        final AlertDialog d = new AlertDialog.Builder(MainActivity.this)
                .setMessage(s)
                .setPositiveButton("OK", okListener)
                .create();
        d.show();
        TextView messageView = d.findViewById(android.R.id.message);
        messageView.setMovementMethod(LinkMovementMethod.getInstance());
        messageView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
            ClipboardManager cm = (ClipboardManager)getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", "https://goo.gl/oBo7hZ");
            cm.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), "Link Copied!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 1 && grantResults.length > 0) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                Snackbar.make(scannerView, "Permission Denied, cannot access camera! ", Snackbar.LENGTH_INDEFINITE).show();
            else showMessageOKCancel( getString(R.string.Welcome), null);
        }
    }

    @Override
    public void handleResult(Result result) {
        final String myResult = result.getText();
        Intent i = new Intent(this, ResultActivity.class).putExtra("myResult", myResult);
        startActivity(i);
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }
    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }
}
