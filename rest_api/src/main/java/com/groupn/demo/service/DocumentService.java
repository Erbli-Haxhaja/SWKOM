package com.groupn.demo.service;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentService {

    private final MinioClient minioClient;
    private final RabbitTemplate rabbitTemplate;
    private final String bucketName = "documents";

    public DocumentService(MinioClient minioClient, RabbitTemplate rabbitTemplate) {
        this.minioClient = minioClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    public String uploadDocument(MultipartFile file) throws Exception {
        // Check if the bucket exists, if not create it
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        String objectName = file.getOriginalFilename();  // Generate object name

        // Upload document to MinIO
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)  // Use document name as the object index
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );

        // Send message to RabbitMQ OCR_QUEUE
        sendOCRJobMessage(objectName);

        return objectName;
    }

    private void sendOCRJobMessage(String objectName) {
        String message = "{\"documentId\":\"" + objectName + "\"}";
        rabbitTemplate.convertAndSend("OCR_QUEUE", message);  // Send to OCR_QUEUE
    }
}
