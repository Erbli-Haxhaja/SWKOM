package com.groupn.demo.minio;
import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint("http://minio:9000")  // MinIO URL
                .credentials("minioadmin", "minioadmin")  // MinIO credentials
                .build();
    }
}