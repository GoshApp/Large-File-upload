package app.file_upload;

/**
 * Created by rikmen00@gmail.com on 25.02.2020.
 */

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import app.file_upload.task.AsyncTaskResult;
import io.reactivex.disposables.Disposable;
import okhttp3.ConnectionSpec;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FileUploadService extends JobIntentService {
    private static final String TAG = "FileUploadService";
    Disposable mDisposable;
    /**
     * Unique job ID for this service.
     */
    private static final int JOB_ID = 102;
    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, FileUploadService.class, JOB_ID, intent);
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        /**
         * Download/Upload of file
         * The system or framework is already holding a wake lock for us at this point
         */
        // get file file here
        String mFilePath = intent.getStringExtra("mFilePath");
        if (mFilePath == null) {
            Log.e(TAG, "onHandleWork: Invalid file URI");
            return;
        }
        new UploadTask().execute(mFilePath);
    }
    private void onErrors(Throwable throwable) {
        sendBroadcastMeaasge("Error in file upload " + throwable.getMessage());
        Log.e(TAG, "onErrors: ", throwable);
    }
    private void onProgress(Double progress) {
        sendBroadcastMeaasge("Uploading in progress... " + (int) (100 * progress));
        Log.i(TAG, "onProgress: " + progress);
    }
    private void onSuccess() {
        sendBroadcastMeaasge("File uploading successful ");
        Log.i(TAG, "onSuccess: File Uploaded");
    }
    public void sendBroadcastMeaasge(String message) {
        Intent localIntent = new Intent("my.own.broadcast");
        localIntent.putExtra("result", message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

    private class UploadTask extends AsyncTask <String, Void, AsyncTaskResult <Void>> {
        private static final int NETWORK_TIMEOUT_SEC = 60;
        private static final String SERVER_URL = "https://s3.amazonaws.com/cooltool.static/ProjectContact/ProjectContact_238938902/WebEyeTracker/q_238938870/555225.mp4?AWSAccessKeyId=AKIAJK2OK5NFLD3C3BPQ&ContentType=video%2Fmp4&Expires=1582727253&Signature=OiUG1TEERlfQQSJlBABFrPpNuE4%3D";

        @Override
        protected AsyncTaskResult doInBackground(String... params) {
            try {
                // Get pre-signed Amazon url to upload file.
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT))
                        .connectTimeout(NETWORK_TIMEOUT_SEC, TimeUnit.SECONDS)
                        .readTimeout(NETWORK_TIMEOUT_SEC, TimeUnit.SECONDS)
                        .writeTimeout(NETWORK_TIMEOUT_SEC, TimeUnit.SECONDS)
                        .build();

                // Upload file to Amazon.
                String imagePath = params[0];
                Request uploadFileRequest = new Request.Builder()
                        .url(SERVER_URL)
                        .put(RequestBody.create(MediaType.parse("video/mp4"), new File(imagePath))) // !read from uploaded file
                        .build();
                Response uploadResponse = client.newCall(uploadFileRequest).execute();
                if (!uploadResponse.isSuccessful())
                    return new AsyncTaskResult(new Exception("Upload file response code: " + uploadResponse.code()));

                return new AsyncTaskResult(null);

            } catch (Exception e) {
                return new AsyncTaskResult(e);
            }
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            super.onPostExecute(result);
            if (result.hasError()) {
                Log.e(TAG, "UploadTask failed", result.getError());
               // Toast.makeText(MainActivity.this, "UploadTask finished with error", Toast.LENGTH_LONG).show();
            } else {
                Log.d(TAG, "UploadTask finished successfullyd", result.getError());
            }
        }
    }
}