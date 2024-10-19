package com.paperlessservice.swkom;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import net.sourceforge.tess4j.TesseractException;
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

    @RabbitListener(queues = RabbitMQConfig.OCR_QUEUE)
    public void processOcrJob(String message) {
        try {
            String documentId = new JSONObject(message).getString("documentId");

            // Fetch the document from MinIO
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

            // Send OCR result to RESULT_QUEUE
            rabbitMQSender.sendOcrResult(documentId, ocrText);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
