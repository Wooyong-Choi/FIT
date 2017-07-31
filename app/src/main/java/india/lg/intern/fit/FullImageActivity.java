package india.lg.intern.fit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

public class FullImageActivity extends AppCompatActivity {

    String imagePath;
    Bitmap bmp;

    String crlf = "\r\n";
    String twoHyphens = "--";
    String boundary =  "*****";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        // get intent data
        Intent i = getIntent();

        // Selected image id
        imagePath = i.getStringExtra("bmp");
        bmp = (Bitmap) BitmapFactory.decodeFile(imagePath);

        ImageView imageView = (ImageView) findViewById(R.id.full_image_view);
        imageView.setRotation(getCameraPhotoOrientation(imagePath));
        imageView.setImageBitmap(bmp);

        DownloadTask downloadTask = new DownloadTask();

        downloadTask.execute();
    }

    public int getCameraPhotoOrientation(String imagePath) {
        int rotate = 0;
        try {
            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            Log.i("RotateImage", "Exif orientation: " + orientation);
            Log.i("RotateImage", "Rotate value: " + rotate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    private String hashTagGenerate() {
        String data = "";
        HttpURLConnection urlConnection = null;
        InputStream iStream = null;

        try {
            URL url = new URL("https://westcentralus.api.cognitive.microsoft.com/vision/v1.0/tag");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setUseCaches(false);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Ocp-Apim-Subscription-Key", "8e3a68b027484550a72bf8e4eb98f3fc");
            urlConnection.connect();

            OutputStream output = urlConnection.getOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, output);
            output.close();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            data = br.readLine();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = hashTagGenerate();
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }

            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ((TextView) (findViewById(R.id.hashTagView))).setText(result);
        }
    }
}
