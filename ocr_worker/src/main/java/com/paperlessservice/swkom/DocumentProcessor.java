package com.paperlessservice.swkom;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

@Service
public class DocumentProcessor {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private OcrService ocrService;

    @Autowired
    private RabbitMQSender rabbitMQSender;

    @Autowired
    private ElasticsearchService elasticsearchService;

    @RabbitListener(queues = RabbitMQConfig.OCR_QUEUE)
    public void processOcrJob(String message) {
        try {
            JSONObject jsonMessage = new JSONObject(message);
            String documentId = jsonMessage.getString("documentId");

            // Fetch the document from MinIO (same as before)
            InputStream documentStream = minioClient.getObject(
                    GetObjectArgs.builder().bucket("documents").object(documentId).build());

            // Save to local file
            File tempFile = new File("/tmp/" + documentId);
            FileOutputStream fos = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = documentStream.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            fos.close();

            // Perform OCR on the file
            String ocrText = ocrService.extractText(tempFile);

            // Index the document in Elasticsearch
            elasticsearchService.indexDocument(documentId, ocrText);

            // Send OCR result to RabbitMQ RESULT_QUEUE
            rabbitMQSender.sendOcrResult(documentId, ocrText);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
