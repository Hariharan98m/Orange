package io.hasura.orange;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by HARIHARAN on 14-10-2017.
 */

public class BaseActivity extends AppCompatActivity{
    public AlertDialog AD;
    public ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog= new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dot_progress_bar, null);
        dialogBuilder.setView(dialogView);

        AD= dialogBuilder.create();
        AD.setCancelable(false);
    }

    public void showProgressDialog(Boolean shouldShow) {
        if (shouldShow) {
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }
}
