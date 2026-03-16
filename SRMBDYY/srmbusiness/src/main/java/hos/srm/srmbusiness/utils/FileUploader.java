package hos.srm.srmbusiness.utils;

import hos.srm.srmbusiness.minio.MinioConfig;
import io.minio.*;
import io.minio.errors.*;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class FileUploader {

    /**
     * 上传文件到MinIO存储桶
     *
     * @param objectName   对象名称（即文件名）
     * @param inputStream  文件输入流
     * @param contentType  内容类型
     * @return             是否上传成功
     */
    public boolean uploadFile(String objectName, InputStream inputStream, String contentType) {
        try {
            MinioClient minioClient = MinioConfig.getClient();
            String bucketName = MinioConfig.getBucketName();

            // 检查bucket是否存在，不存在则创建
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            //System.out.println(bucketName);
            // 执行上传操作
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(contentType)
                            .build()
            );

            System.out.println("文件 '" + objectName + "' 已成功上传至存储桶 '" + bucketName + "'");
            return true;
        } catch (Exception e) {
            System.err.println("上传失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}