package com.ashokit.controller;

// Import necessary classes
import com.ashokit.service.PdfGeneratorService;  // Custom service for PDF generation
import org.springframework.http.HttpHeaders;      // For HTTP response headers
import org.springframework.http.MediaType;        // For setting content type
import org.springframework.http.ResponseEntity;   // For flexible HTTP responses
import org.springframework.stereotype.Controller;  // Spring MVC controller annotation
import org.springframework.web.bind.annotation.GetMapping;     // For handling GET requests
import org.springframework.web.bind.annotation.RequestMapping; // For base URL mapping
import org.slf4j.Logger;        // For logging
import org.slf4j.LoggerFactory; // For creating logger instance

// Java time utilities for timestamp generation
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Mark as Spring MVC Controller
@Controller
// Base URL mapping for all endpoints in this controller
@RequestMapping("/pdf")
public class PdfController {
    // Initialize logger for this class
    private static final Logger logger = LoggerFactory.getLogger(PdfController.class);
    
    // Inject PDF generator service
    private final PdfGeneratorService pdfGeneratorService;

    // Constructor injection of PDF generator service
    public PdfController(PdfGeneratorService pdfGeneratorService) {
        this.pdfGeneratorService = pdfGeneratorService;
    }

    // Handle GET requests to /pdf/generate
    @GetMapping("/generate")
    public ResponseEntity<byte[]> generateAndDownloadPdf() {
        try {
            // Generate PDF content using the service
            byte[] pdfContent = pdfGeneratorService.generatePdf();
            
            // Generate timestamp for unique filename
            String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            // Create filename with timestamp
            String filename = "customers_report_" + timestamp + ".pdf";

            // Set up HTTP headers for PDF download
            HttpHeaders headers = new HttpHeaders();
            // Set content type to PDF
            headers.setContentType(MediaType.APPLICATION_PDF);
            // Set filename and attachment disposition
            headers.setContentDispositionFormData("attachment", filename);
            // Set cache control headers
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            // Log successful PDF generation
            logger.info("Generated PDF report: {}", filename);
            
            // Return PDF content with appropriate headers
            return ResponseEntity.ok()  // HTTP 200 OK
                .headers(headers)       // Add configured headers
                .body(pdfContent);      // Add PDF content

        } catch (Exception e) {
            // Log error if PDF generation fails
            logger.error("Error generating PDF: ", e);
            // Return HTTP 500 Internal Server Error
            return ResponseEntity.internalServerError().build();
        }
    }
}