package com.paperlessservice.swkom;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.json.JSONObject;

@Service
public class RabbitMQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendOcrResult(String documentId, String ocrText) {
        JSONObject result = new JSONObject();
        result.put("documentId", documentId);
        result.put("ocrText", ocrText);
        rabbitTemplate.convertAndSend(RabbitMQConfig.RESULT_QUEUE, result.toString());
    }
}
