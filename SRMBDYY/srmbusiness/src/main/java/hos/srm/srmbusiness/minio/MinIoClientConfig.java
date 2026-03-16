package hos.srm.srmbusiness.minio;


import hos.srm.srmbusiness.exception.ErrorException;
import io.minio.MinioClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaobo
 * @date 2022/8/5
 */
@Data
@Configuration
@Slf4j
public class MinIoClientConfig {

    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.accessKey}")
    private String accessKey;
    @Value("${minio.secretKey}")
    private String secretKey;


    //private String endpoint = "http://172.16.37.112:9000"; // MinIO服务器地址
    //private  String accessKey = "minioadmin";          // 访问密钥
    //private  String secretKey = "minioadmin";          // 秘密密钥
    //private String BUCKET_NAME = "kysrm";
    /**
     * 注入minio 客户端
     *
     * @return MinioClient
     */
    @Bean
    public MinioClient minioClient() {

        try {
            return MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
        } catch (Exception e) {
            throw new ErrorException("-----创建Minio客户端失败-----", e.getMessage());
        }
    }

}
