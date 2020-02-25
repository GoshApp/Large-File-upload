package app.file_upload;

/**
 * Created by rikmen00@gmail.com on 25.02.2020.
 */

public enum MIMEType {
    IMAGE("image/"), VIDEO("video/");
    public String value;

    MIMEType(String value) {
        this.value = value;
        }
}