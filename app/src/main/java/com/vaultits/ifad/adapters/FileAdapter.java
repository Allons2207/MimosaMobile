package com.vaultits.ifad.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vaultits.ifad.R;
import com.vaultits.ifad.database.captured_data.activities_helper;
import com.vaultits.ifad.logic.Methods;
import com.vaultits.ifad.models.file;
import com.vaultits.ifad.retrofit.APIClient;
import com.vaultits.ifad.retrofit.ApiInterface;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.File_Viewholder> {
    private Context mContext;
    private ArrayList<file> mFiles;
    private Dialog mDialog;
    private activities_helper activities_helper;
    //retrofit
    private ApiInterface apiInterface;

    public FileAdapter(Context mContext, ArrayList<file> files) {
        try {
            this.mContext = mContext;
            this.mFiles = files;
            this.mDialog = new Dialog(mContext);
            this.activities_helper = new activities_helper(mContext, "", null);
            this.apiInterface = APIClient.getApiClient().create(ApiInterface.class);
        } catch (Exception e) {
            Methods.showAlert("Error", e.toString(), mContext);
        }
    }

    @NonNull
    @Override
    public File_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        try {
            view = LayoutInflater.from(mContext).inflate(R.layout.file_row_item, parent, false);
        } catch (Exception e) {
            Toast.makeText(mContext, "Error adapter create view holder", Toast.LENGTH_SHORT).show();
        }
        return new FileAdapter.File_Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull File_Viewholder holder, int position) {
        try {
            final file file = mFiles.get(position);
            holder.title.setText("Title : " + file.getmTitle());
            holder.author.setText("Author : " + file.getmAuthor());
            File file1 = new File(file.getmPath());
            String name = file1.getName();
            holder.filename.setText("Filename : " + name);
            holder.date.setText("Date : " + file.getDate());

            Button upload = (Button) holder.mUpload;
            Button delete = (Button) holder.mDelete;
            ImageButton synced = (ImageButton) holder.mSynced;
            ImageButton un_synced = (ImageButton) holder.mUnSynced;

            //show synced button if status is yes else show show
            if (file.getmStatus().equalsIgnoreCase("Yes")) {
                synced.setVisibility(View.VISIBLE);
                un_synced.setVisibility(View.GONE);
            } else {
                synced.setVisibility(View.GONE);
                un_synced.setVisibility(View.VISIBLE);
            }

            upload.setOnClickListener(v -> {
                try {
                    String topic = null;
                    String start_date = "";
                    String end_date = "";
                    String facilitator = "";
                    String comment = "";
                    String activity_id = "";
                    String site = "";
                    String status = "";
                    String project = "";
                    String org = "";
                    String dis = "";
                    String scheme = "";
                    //file
                    String f_type = "";
                    String title = "";
                    String author = "";
                    String des = "";
                    String date = "";
                    String author_org = "";
                    String created_by = "";
                    String file_path = null;
                    String file_uri = "";
                    //get the file info along with appointment info
                    Cursor cursor = activities_helper.getFileInfo(file.getmID());
                    while (cursor.moveToNext()) {
                        //APPOINTMENT DETAILS
                        topic = cursor.getString(cursor.getColumnIndex("TOPIC"));
                        start_date = cursor.getString(cursor.getColumnIndex("START"));
                        end_date = cursor.getString(cursor.getColumnIndex("ENDDATE"));
                        //Methods.toSqlDate(start_date)
                        //Methods.toSqlDate(end_date)
                        facilitator = cursor.getString(cursor.getColumnIndex("FACILITATOR"));
                        comment = cursor.getString(cursor.getColumnIndex("COMMENT"));
                        activity_id = cursor.getString(cursor.getColumnIndex("ACTIVITY"));
                        site = cursor.getString(cursor.getColumnIndex("SITE"));
                        status = cursor.getString(cursor.getColumnIndex("STATUS"));
                        project = cursor.getString(cursor.getColumnIndex("PROJECT"));
                        org = cursor.getString(cursor.getColumnIndex("ORG"));
                        dis = cursor.getString(cursor.getColumnIndex("DIS"));
                        scheme = cursor.getString(cursor.getColumnIndex("SCHEME"));
                        //FILE DETAILS
                        f_type = cursor.getString(cursor.getColumnIndex("FILETYPEID"));
                        title = cursor.getString(cursor.getColumnIndex("TITLE"));
                        author = cursor.getString(cursor.getColumnIndex("AUTHOR"));
                        des = cursor.getString(cursor.getColumnIndex("DESCRIPTION"));
                        date = cursor.getString(cursor.getColumnIndex("DATE"));
                        author_org = cursor.getString(cursor.getColumnIndex("AUTHORORG"));
                        created_by = cursor.getString(cursor.getColumnIndex("CREATEDBY"));
                        file_path = cursor.getString(cursor.getColumnIndex("DOCPATH"));
                        file_uri = cursor.getString(cursor.getColumnIndex("DOCURL"));
                    }

                    //check file exist
                    File file2 = new File(file_path);
                    if (file2.exists()) {
                        //upload
                        postActivityFile(topic, start_date, end_date, facilitator, comment, activity_id, site, status, project, org, dis, f_type, title, author, des, date, author_org, created_by, file_path, file_uri, file.getmID(), scheme,synced,un_synced);
                    } else {
                        //do not upload
                        Toast.makeText(mContext, "File does not exist.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
                }
            });

            delete.setOnClickListener(v -> {
                try {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setCancelable(true);
                    builder.setTitle("Confirm Delete");
                    builder.setMessage("Are you sure you want to delete?");
                    builder.setPositiveButton("YES",
                            (dialog, which) -> {
                                //delete database record
                                activities_helper.deleteFile(file.getmPath(), mContext);
                                //delete the file from file folder
                                Methods.deleteFile(file.getmPath(), mContext);
                                //update list by removing
                                mFiles.remove(position);
                                //update UI
                                notifyItemRemoved(position);
                            });
                    builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.cancel());
                    builder.show();

                } catch (Exception e) {
                    Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Methods.showAlert("Error", e.toString(), mContext);
        }
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    public class File_Viewholder extends RecyclerView.ViewHolder {

        public TextView title, author, filename, date;
        public Button mUpload;
        public Button mDelete;
        public ImageButton mSynced;
        public ImageButton mUnSynced;

        public File_Viewholder(@NonNull View itemView) {
            super(itemView);
            try {
                title = itemView.findViewById(R.id.title);
                author = itemView.findViewById(R.id.author);
                filename = itemView.findViewById(R.id.filename);
                date = itemView.findViewById(R.id.date);
                mUpload = itemView.findViewById(R.id.upload_file);
                mDelete = itemView.findViewById(R.id.delete_file);
                mSynced = itemView.findViewById(R.id.synced);
                mUnSynced = itemView.findViewById(R.id.un_synced);
            } catch (Exception e) {
                Methods.showAlert("Error", e.toString(), mContext);
            }
        }
    }

    //upload file method
    private void postActivityFile(String topic, String start, String end, String faci, String activity_des, String activity, String site, String status, String proj, String org, String dis, String ftype, String title, String author, String file_description, String date, String author_org, String created, String path, String uri, String id, String scheme,ImageButton synced,ImageButton un_synced) {
        try {
            File file = new File(path);
            RequestBody the_file = RequestBody.create(
                    MediaType.parse(mContext.getContentResolver().getType(Uri.parse(uri))),
                    file
            );

            MultipartBody.Part actual_file = MultipartBody.Part.createFormData("file", file.getName(), the_file);
            String extension = mContext.getContentResolver().getType(Uri.parse(uri));

            RequestBody the_act_topic = RequestBody.create(MultipartBody.FORM, topic);
            RequestBody the_act_start = RequestBody.create(MultipartBody.FORM, Methods.toSqlDate(start));
            RequestBody the_act_end = RequestBody.create(MultipartBody.FORM, Methods.toSqlDate(end));
            RequestBody the_act_faci = RequestBody.create(MultipartBody.FORM, faci);
            RequestBody the_act_des = RequestBody.create(MultipartBody.FORM, activity_des);
            RequestBody the_act_site = RequestBody.create(MultipartBody.FORM, site);
            RequestBody the_act_status = RequestBody.create(MultipartBody.FORM, status);
            RequestBody the_act_proj = RequestBody.create(MultipartBody.FORM, proj);
            RequestBody the_act_org = RequestBody.create(MultipartBody.FORM, org);
            RequestBody the_act_dis = RequestBody.create(MultipartBody.FORM, dis);
            RequestBody the_act_id = RequestBody.create(MultipartBody.FORM, activity);
            RequestBody the_act_scheme = RequestBody.create(MultipartBody.FORM, scheme);

            RequestBody file_ext = RequestBody.create(MultipartBody.FORM, extension);
            RequestBody file_type = RequestBody.create(MultipartBody.FORM, ftype);
            RequestBody file_title = RequestBody.create(MultipartBody.FORM, title);
            RequestBody file_author = RequestBody.create(MultipartBody.FORM, author);
            RequestBody file_author_org = RequestBody.create(MultipartBody.FORM, author_org);
            RequestBody file_date = RequestBody.create(MultipartBody.FORM, date);
            RequestBody file_created_by = RequestBody.create(MultipartBody.FORM, created);
            RequestBody file_des = RequestBody.create(MultipartBody.FORM, file_description);

            Call<ResponseBody> upload = apiInterface.PostFile(the_act_topic, the_act_start, the_act_end, the_act_faci, the_act_des, the_act_id, the_act_site, the_act_status, the_act_proj, the_act_org, the_act_dis, file_type, file_title, file_author, file_des, file_date, file_author_org, file_created_by, file_ext, actual_file, the_act_scheme);
            Methods.showDialog(mDialog, "Saving file...", true);
            upload.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Methods.showDialog(mDialog, "Saving file...", false);
                        String result = response.body().string();

                        String message = Methods.removeQoutes(result);
                        String [] tokens = message.split(":");
                        //tokens[0] - activity info
                        //tokens[1] - binary file
                        //tokens[2] - file info
                        //tokens[3] - doc object
                        if (tokens[2].equalsIgnoreCase("Success")) {
                            Methods.showAlert("Response", "File uploaded successfully.", mContext);
                            //update sync status
                            if(activities_helper.updateFileSyncStatus(id, mContext)){
                                synced.setVisibility(View.VISIBLE);
                                un_synced.setVisibility(View.GONE);
                            }

                        } else if (tokens[2].equalsIgnoreCase("Failed")) {
                            Methods.showAlert("Response", "File upload failed.", mContext);
                        }  else {
                            Methods.showAlert("Response", message, mContext);
                        }
                    } catch (Exception e) {
                        Toast.makeText(mContext, "Error " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        Methods.showDialog(mDialog, "dismiss", false);
                        Methods.showAlert("Request failed", "Request failed..Check your network connection.", mContext);
                    } catch (Exception e) {
                        Toast.makeText(mContext, "Error " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
