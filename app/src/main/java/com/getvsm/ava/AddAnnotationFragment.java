package com.getvsm.ava;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by fasal on 28-06-2015.
 */
public class AddAnnotationFragment extends Fragment {
    Activity activity;
    Button chose,comment;
    HashMap<String,String>  params;
    String text,time,type,file;
    Uri URI = null;
    private static final int PICK_FROM_GALLERY = 101;
    int columnIndex;
    Bitmap bp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_annotation,container,false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        params=new HashMap<String, String>();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button add = (Button) activity.findViewById(R.id.add_annonation);
         chose = (Button) activity.findViewById(R.id.btChoose);

        chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == chose) {
                    openGallery();
                }
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params=new HashMap<String, String>();
                text=((EditText)activity.findViewById(R.id.etMessageAnnotation)).getText().toString();
              //  text=((EditText)activity.findViewById(R.id.etMessage)).getText().toString();
                time=((EditText)activity.findViewById(R.id.etTime)).getText().toString();
                type=((EditText)activity.findViewById(R.id.etType)).getText().toString();

                try {
                    text= URLEncoder.encode(text,"UTF-8");
                    params.put("text", text);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                params.put("time",time);
                params.put("type", type);
                params.put("video_id",AnnotationAsyncTask.videoId);
                params.put("oauth_key",activity.getSharedPreferences("Cache", Context.MODE_PRIVATE).getString("oauth", null));

                Log.e("text", text);
                Log.e("text",time);
                Log.e("text",type);
                Log.e("text",AnnotationAsyncTask.videoId);
                AddAnnotationAsyncTask  addAsyncTask = new AddAnnotationAsyncTask(getActivity(),bp);

                addAsyncTask.execute(params);
            }
        });



    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == PICK_FROM_GALLERY && resultCode ==activity.RESULT_OK) {
        if (resultCode ==activity.RESULT_OK && null!=data) {
//            /**

//             * Get Path
//             */
            //  Uri selectedImage = data.getData();

//            Log.e("select",selectedImage.toString());
//            String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//            Cursor cursor = activity.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
//            cursor.moveToFirst();
//            columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            file = cursor.getString(columnIndex);
//            Log.e("Path:", file);
            //  URI = Uri.parse("file://" + file);
//            cursor.close();
//
//            ImageView imageView = (ImageView)activity.findViewById(R.id.imgView);
//           // bp=BitmapFactory.decodeFile(file);
//            bp=decodeSampledBitmapFromFile(URI.toString(),250,250);
//
//            imageView.setImageBitmap(bp);
//
//
//
//
//
//        }

            Uri selectedImage = data.getData();
            file = getPath(getActivity(), selectedImage);
            ImageView imageView = (ImageView) activity.findViewById(R.id.imgView);
            // bp=BitmapFactory.decodeFile(file);
            bp = decodeSampledBitmapFromFile(file.toString(), 150, 150);
            Log.e("bp", bp.toString());
            imageView.setImageBitmap(bp);
        }

    }
    public void openGallery() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.putExtra("return-data", true);
            startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1);
        }
        catch (Exception e){
            Toast toast = Toast
                    .makeText(activity, "This device doesn't support the chooser option!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    public static Bitmap decodeSampledBitmapFromFile(String path,
                                                     int reqWidth, int reqHeight) { // BEST QUALITY MATCH

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }

        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }


        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }




    }


