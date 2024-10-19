package com.paperlessservice.swkom;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class OcrService {

    private final Tesseract tesseract;

    public OcrService() {
        tesseract = new Tesseract();
        tesseract.setDatapath("/usr/share/tesseract-ocr/4.00/tessdata");  // Path to Tesseract data files
        tesseract.setLanguage("eng");
    }

    public String extractText(File file) throws TesseractException {
        return tesseract.doOCR(file);
    }
}