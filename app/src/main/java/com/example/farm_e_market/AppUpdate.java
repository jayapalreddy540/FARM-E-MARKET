package com.example.farm_e_market;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import androidx.core.content.FileProvider;


public class AppUpdate extends FileProvider {

    ProgressDialog mProgressDialog;

    public void updateApp(Context context, Uri uri) {

        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("App Update Available");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startActivity(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:com.example.farm_e_market")));
                        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                        intent.setDataAndType(uri,"application/vnd.android.package-archive");
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        context.startActivity(intent);
            }
            else {
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                install.setDataAndType(uri, "application/vnd.android.package-archive");
                context.startActivity(install);
            }
        } catch (Exception e) {
            Log.d("Install Exception",e.toString());
        }
        finally {
            mProgressDialog.dismiss();
        }
    }
}