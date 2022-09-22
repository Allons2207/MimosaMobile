package com.vaultits.ifad.logic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.vaultits.ifad.MainActivity;
import com.vaultits.ifad.R;
import com.vaultits.ifad.activities.mytrip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Methods {
    public static void showAlert(String title, String Message, Context context) {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(true);
            builder.setTitle(title);
            builder.setMessage(Message);
            builder.setPositiveButton("Ok",
                    (dialog, which) -> dialog.cancel());
            builder.show();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void showRequestFailedDialog(Context context) {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(true);
            builder.setTitle("Request Failed");
            builder.setMessage("Check your internet connection.");
            builder.setPositiveButton("Ok",
                    (dialog, which) -> dialog.cancel());
            builder.show();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void showDialog(Dialog mdialog, String message, boolean action) {
        try {
            if (action) {
                mdialog.setContentView(R.layout.custom_progress_dialog);
                final TextView textView = (TextView) mdialog.findViewById(R.id.custom_dialog_text);
                textView.setText(message);
                mdialog.setCanceledOnTouchOutside(false);
                mdialog.setOnKeyListener((dialog, keyCode, event) -> {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            dialog.dismiss();
                        }
                        return true;
                    }
                    return false;
                });
                mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mdialog.show();
            } else {
                mdialog.dismiss();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static String removeQoutes(String result) {
        String withoutQoutes = "";
        try {
            if (result.length() >= 2 && result.charAt(0) == '"' && result.charAt(result.length() - 1) == '"') {
                withoutQoutes = result.substring(1, result.length() - 1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return withoutQoutes;
    }

    public static boolean isLogged(Context ctx) {
        boolean res = false;
        try {
            SharedPreferences sharedPreferences = ctx.getSharedPreferences(MainActivity.ACC_PREFERENCES, Context.MODE_PRIVATE);
            //check if the preference is there
            if (sharedPreferences.contains(MainActivity.IsLogged)) {
                if (sharedPreferences.getBoolean(MainActivity.IsLogged, false)) {
                    res = true;
                } else {
                    res = false;
                }
            } else {
                res = false;
            }
        } catch (Exception e) {
            showAlert("Error", e.toString(), ctx);
        }
        return res;
    }

    public static void Alert(String title, String Message, Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    public static String getDateForSqlServer() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getUserId(Context ctx) {
        String  res;
        try {
            SharedPreferences sharedPreferences = ctx.getSharedPreferences(MainActivity.ACC_PREFERENCES, Context.MODE_PRIVATE);
            //check if the preference is there
            if (sharedPreferences.contains(MainActivity.IsLogged)) {
                if (sharedPreferences.getBoolean(MainActivity.IsLogged, false)) {
                    res = sharedPreferences.getString(MainActivity.UserId, "0");
                } else {
                    res = "0";
                }
            } else {
                res = "0";
            }
        } catch (Exception e) {
            Methods.showAlert("Error", e.toString(), ctx);
            res = "0";
            return res;
        }
        return res;
    }

    public static String dobFromYearOfBirth(String year) {
        return year + "-01-01";
    }

    public static String toSqlDate(String date) {
        String[] tokens = date.split("-");
        return tokens[2] + "-" + tokens[1] + "-" + tokens[0];
    }

    /*
     * must perform this operation in thread, in case file os to large UI will hang
     * method takes URI of picked file and sub folder ,then gets the the file from uri
     * and writes it into the base dir + subfolder
     */
    public static File convertUriToFile(Uri uri, String folder, Context ctx) {
        File realFile = null;
        InputStream inputStream = null;
        try {
            inputStream = ctx.getContentResolver().openInputStream(uri);
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + ctx.getString(R.string.base_dir) + folder);
//            if (file.exists() && file.isDirectory()) {
//                String[] children = file.list();
//                for (int i = 0; i < children.length; i++) {
//                    new File(file, children[i]).delete();
//                }
//            } else {
//                file.mkdirs();
//            }

            if (!file.exists()) {
                file.mkdirs();
            }

            File file1 = new File(file + "/" + fileInfo(uri, ctx).get(0) + "." + fileInfo(uri, ctx).get(1));

            if (!file1.exists()) {
                file1.createNewFile();
            } else {
                file1.delete();
            }
            OutputStream outputStream = new FileOutputStream(file1);
            byte[] buffer = new byte[4 * 1024];
            int read;

            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }

            outputStream.flush();
            realFile = file1;

        } catch (Exception e) {
            System.out.println(e);
            try {
                inputStream.close();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }

        return realFile;
    }

    /*
     * gets uri and extracts the file name and extension then returns the info
     * in an List with name at 0 and ext at 1
     */
    public static ArrayList<String> fileInfo(Uri uri, Context ctx) {
        Uri returnUri = uri;
        ArrayList<String> file_info = new ArrayList<>();
        Cursor returnCursor = ctx.getContentResolver().query(returnUri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String mimeType = ctx.getContentResolver().getType(returnUri);
        String name = returnCursor.getString(nameIndex);

        int dot = name.lastIndexOf(".");
        String extension = name.substring(dot + 1);
        String real_name = name.substring(0, dot);
        file_info.add(real_name);
        file_info.add(extension);

        return file_info;
    }

    //check file existence
    public static boolean fileExist(String path, Context ctx) {
        boolean exist;
        try {
            File file = new File(path);
            if (file.exists()) {
                exist = true;
            } else {
                exist = false;
            }
        } catch (Exception e) {
            exist = false;
            Toast.makeText(ctx, e.toString(), Toast.LENGTH_SHORT).show();
        }
        return exist;
    }

    //delete file
    public static boolean deleteFile(String path,Context ctx){
        boolean res;
        try {
            File file = new File(path);
            if (file.exists() && file.delete()) {
                res = true;
            } else {
                res = false;
            }
        } catch (Exception e) {
            res = false;
            Toast.makeText(ctx, e.toString(), Toast.LENGTH_SHORT).show();
        }
        return res;
    }
}
