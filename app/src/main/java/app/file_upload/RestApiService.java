package app.file_upload;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by rikmen00@gmail.com on 25.02.2020.
 */

public interface RestApiService {
    @Multipart
    @POST("ProjectContact/ProjectContact_238934631/WebEyeTracker/q_238285786/calibration_capture.mp4?AWSAccessKeyId=AKIAJK2OK5NFLD3C3BPQ&Expires=1582634813&Signature=VUlkC3hN%2F0W5q7MRjCefPLfcIeM%3D/")
    Single <ResponseBody> onFileUpload(@Part MultipartBody.Part file);
}
