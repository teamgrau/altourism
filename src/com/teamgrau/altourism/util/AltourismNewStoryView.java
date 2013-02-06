package com.teamgrau.altourism.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.teamgrau.altourism.R;
import com.teamgrau.altourism.util.data.AltourismLocation;
import com.teamgrau.altourism.util.data.StoryArchivist;
import com.teamgrau.altourism.util.data.StoryArchivistLocalDB;
import com.teamgrau.altourism.util.data.model.Story;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class AltourismNewStoryView extends Activity implements View.OnClickListener {
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final int MEDIA_TYPE_VIDEO = 2;

    private static LinkedList<Uri> fileUris;
    private Location mLocation;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );

        mLocation = new AltourismLocation ( "NewStory",
                                            getIntent ().getDoubleExtra ( "lat", 0.0 ),
                                            getIntent ().getDoubleExtra ( "lng", 0.0 ));

        fileUris = new LinkedList<Uri> (  );

        setContentView ( R.layout.new_story_view );

        TextView title = (TextView) findViewById ( R.id.new_story_title );
        title.setTypeface ( Typeface.createFromAsset ( getAssets (), "fonts/miso-bold.otf" ) );
        title.setPaintFlags(title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        EditText et = (EditText) findViewById ( R.id.new_story_headline );
        et.setTypeface ( Typeface.createFromAsset ( getAssets (), "fonts/miso-bold.otf" ) );
        et = (EditText) findViewById ( R.id.new_story_body );
        et.setTypeface ( Typeface.createFromAsset ( getAssets (), "fonts/miso-bold.otf" ) );

        Button b = (Button) findViewById ( R.id.nextButton );
        b.setTypeface ( Typeface.createFromAsset ( getAssets (), "fonts/miso-bold.otf" ) );


        findViewById ( R.id.nextButton ).setOnClickListener ( this );
        findViewById ( R.id.nextImageButton ).setOnClickListener ( this );
        findViewById ( R.id.new_story_picture ).setOnClickListener ( this );
        findViewById ( R.id.new_story_movie ).setOnClickListener ( this );
        findViewById ( R.id.new_story_audio ).setOnClickListener ( this );
    }

    @Override
    protected void onRestart () {
        super.onRestart ();
    }

    @Override
    protected void onResume () {
        super.onResume ();
        for (Uri fileUri : fileUris ) {
            ImageView i = new ImageView ( this );
            i.setBackground ( getResources ().getDrawable ( R.drawable.altourism_new_story_bubble ) );
            Log.d ( "NewStoryView", findViewById ( R.id.ref_layout_view ).getLayoutParams ().toString () );
            i.setLayoutParams ( findViewById ( R.id.ref_layout_view ).getLayoutParams () );

            i.setScaleType ( ImageView.ScaleType.CENTER_CROP );
            i.setVisibility ( View.VISIBLE );
            setPic ( i, fileUri );

            LinearLayout ll = (LinearLayout) findViewById ( R.id.picture_container );
            ll.addView ( i, ll.getChildCount ()-1 );
        }
    }

    private void setPic( ImageView v, Uri filePath ) {
        // Get the dimensions of the View
        int targetW = 93; //v.getWidth();
        int targetH = 90; //v.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath.toString (), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        String imageType = bmOptions.outMimeType;

        Bitmap bitmap = BitmapFactory.decodeFile(filePath.toString (), bmOptions);
        v.setImageBitmap(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
//                Toast.makeText(this, "Image saved to:\n" +
//                        data.getData(), Toast.LENGTH_LONG).show();
/*                Bundle extras = data.getExtras();
                Bitmap thumbnail = (Bitmap) extras.get("data");
                //mImageView.setImageBitmap(mImageBitmap);
                //Uri fileUri = fileUris.getLast ();
                ImageView i = new ImageView ( this );
                //i.setBackground ( getResources ().getDrawable ( R.drawable.altourism_new_story_bubble ) );
                i.setLayoutParams ( new ViewGroup.LayoutParams ( findViewById ( R.id.ref_layout_view ).getLayoutParams () ) );
                //i.setImageURI ( fileUri );
                i.setImageBitmap ( thumbnail );
                i.setScaleType ( ImageView.ScaleType.CENTER_CROP );
                i.setVisibility ( View.VISIBLE );

                LinearLayout ll = (LinearLayout) findViewById ( R.id.new_story_media_container );
                ll.addView ( i, ll.getChildCount ()-1 );  */

            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }

        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Video captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Video saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the video capture
            } else {
                // Video capture failed, advise user
            }
        }
    }

    @Override
    protected void onPause () {
        super.onPause ();
    }

    @Override
    public void onClick ( View view ) {
        if ( view.getId () == R.id.nextButton || view.getId () == R.id.nextImageButton ) {
            EditText et = (EditText) findViewById ( R.id.new_story_headline );
            String headline = et.getText ().toString ();
            //Toast.makeText ( this, headline, Toast.LENGTH_LONG ).show ();
            et = (EditText) findViewById ( R.id.new_story_body );
            String body = et.getText ().toString ();
            //Toast.makeText ( this, body, Toast.LENGTH_LONG ).show ();
            if ( body.length ()==0 || headline.length ()==0 ) {
                Toast.makeText ( this, "Please enter some text to proceed", Toast.LENGTH_LONG ).show ();
                return;
            }
            StoryArchivist sa = new StoryArchivistLocalDB ( this );
            sa.storeGeschichte ( new Location ( mLocation ),
                                 new Story ( body ) );
            setResult ( RESULT_OK );
            finish ();
        }
        else if ( view.getId () == R.id.new_story_picture ) {
            // create Intent to take a picture and return control to the calling application
            Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);

            Uri fileUri = getOutputMediaFileUri ( MEDIA_TYPE_IMAGE ); // create a file to save the image

            fileUris.add ( fileUri );

            intent.putExtra( MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

            // start the image capture Intent
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
        else if ( view.getId () == R.id.new_story_movie ) {
            //create new Intent
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

            Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);  // create a file to save the video

            fileUris.add ( fileUri );

            intent.putExtra ( MediaStore.EXTRA_OUTPUT, fileUri );  // set the image file name
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high

            // start the Video Capture Intent
            startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
        }
        else if ( view.getId () == R.id.new_story_cancel ) {
            setResult ( RESULT_CANCELED );
            finish ();
        }
        else {
            Toast.makeText ( this, "not implemnented yet", Toast.LENGTH_SHORT ).show ();
        }


    }

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile ( getOutputMediaFile ( type ) );
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File( Environment.getExternalStoragePublicDirectory (
                Environment.DIRECTORY_PICTURES ), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d ( "MyCameraApp", "failed to create directory" );
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat ("yyyyMMdd_HHmmss").format(new Date ());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
}
