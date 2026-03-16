package hos.srm.srmbusiness.minio;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

public class MinioConfig {
    /*
    private static final String ENDPOINT = "http://172.16.37.112:9099"; // MinIO服务器地址
    private static final String ACCESS_KEY = "minioadmin";          // 访问密钥
    private static final String SECRET_KEY = "minioadmin";          // 秘密密钥
    private static final String BUCKET_NAME = "kysrm";           // 存储桶名称
    */

    private static final String ENDPOINT = "http://175.24.134.85:9000"; // MinIO服务器地址
    private static final String ACCESS_KEY = "adminsrm";          // 访问密钥
    private static final String SECRET_KEY = "456qweQaz";          // 秘密密钥
    private static final String BUCKET_NAME = "kysrm";

    public static MinioClient getClient() {
        return MinioClient.builder()
                .endpoint(ENDPOINT)
                .credentials(ACCESS_KEY, SECRET_KEY)
                .build();
    }

    public static String getBucketName() {
        return BUCKET_NAME;
    }
}
