package app.file_upload;

import io.reactivex.Single;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.PUT;
import retrofit2.http.Part;

/**
 * Created by rikmen00@gmail.com on 25.02.2020.
 */

public interface RestApiService {
    @PUT("cooltool.static/ProjectContact/ProjectContact_238938902/WebEyeTracker/q_238938870/333333.mp4?AWSAccessKeyId=AKIAJK2OK5NFLD3C3BPQ&ContentType=video%2Fmp4&Expires=1582722420&Signature=iRYcjOS6DCqB9HTQOW2Vfo9HpH0%3D")
    Single <ResponseBody> onFileUpload(@Part RequestBody file);
}
