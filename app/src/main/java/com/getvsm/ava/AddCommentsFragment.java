package com.getvsm.ava;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by fasal on 28-06-2015.
 */
public class AddCommentsFragment extends Fragment {
    Activity activity;
    Button chose,choseVideo;
    HashMap<String, String> params;
    String text, time, type, file, ext;
    Uri URI = null;
    private String videoPaht=new String();
    private static final int PICK_FROM_GALLERY = 101;
    int columnIndex;
    Bitmap bp;

    // String annotation_id;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_comments, container, false);
    }
//    public AddCommentsFragment() {
//        // Required empty public constructor
//        annotation_id=new String();
//    }
//    public void setAnnotationId(String annotation_id){
//        this.annotation_id=annotation_id;
//        Log.e("id",annotation_id);
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        params = new HashMap<String, String>();
    }

    //
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button add = (Button) activity.findViewById(R.id.add_comment);
        chose = (Button) activity.findViewById(R.id.btChoose);
        choseVideo = (Button) activity.findViewById(R.id.btChooseVideo);
        chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == chose) {
                    openGallery();
                }
            }
        });
choseVideo.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(v==choseVideo){
            openVideo();
        }

    }
});

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text = ((EditText) activity.findViewById(R.id.etMessage)).getText().toString();

                //String image = null;
                if(ext!=null) {
                    if (ext.contains("image/")) {
                        params.put("file_type", "image");
                        Log.e("file_type", "image");
                    }
                    if (ext.contains("video/")) {
                        //String video = null;
                        params.put("file_type", "video");
                        Log.e("file_type", "video");
                    }
                }
                try {
                    text= URLEncoder.encode(text, "UTF-8");
                    params.put("text", text);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                params.put("text", text);

                params.put("annotation_id", AnnonationAdapter.annotation_id);
                params.put("oauth_key", activity.getSharedPreferences("Cache", Context.MODE_PRIVATE).getString("oauth", null));

                Log.e("text", text);

                Log.e("ann_id", AnnonationAdapter.annotation_id);
                AddCommentsAsyncTask addAsyncTask = new AddCommentsAsyncTask(getActivity(), bp,videoPaht);

                addAsyncTask.execute(params);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( resultCode ==activity.RESULT_OK && null!=data) {
            /*
            get the file path;
            get the mime type
            if-> vidoe/*
               filePath =set file
               bp=null;
            if->image/*
               bp=bitmap(file)
               file=null;
             */

            Uri selectedImage = data.getData();
            file = getPath(getActivity(), selectedImage);
            Log.e("vid",file);
            URI = Uri.parse("file://" + file);
            videoPaht=URI.getPath();
            ext = getMimeType(URI.toString());
//            Log.e("ext", ext);
            if(ext!=null) {
                if (ext.contains("image/")) {

                    bp = decodeSampledBitmapFromFile(file.toString(), 250, 250);

                    ((ImageView) activity.findViewById(R.id.imgView)).setImageBitmap(bp);
                    videoPaht = null;
                }
                if (ext.contains("video/")) {
                    VideoView videoView = (VideoView) activity.findViewById(R.id.vd_comment);
                    videoView.setVideoPath(file);
                    MediaController vidControl = new MediaController(activity);
                    vidControl.setAnchorView(videoView);
                    videoView.setMediaController(vidControl);
                    //videoView.start();
                    bp = null;
                }

            }





        }
    }

    public void openVideo(){
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1);

    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1);

    }

    public static String getMimeType(String URI) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(URI);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
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
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }

        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
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
                final String[] selectionArgs = new String[]{
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

