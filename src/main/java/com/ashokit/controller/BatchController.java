package com.ashokit.controller;

// Import required Spring and utility classes
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Import Java IO and Time utilities
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Mark as Spring MVC Controller
@Controller
// Base URL mapping for all endpoints in this controller
@RequestMapping("/batch")
public class BatchController {
    // Initialize logger for this class
    private static final Logger logger = LoggerFactory.getLogger(BatchController.class);
    
    // Spring Batch components
    private final JobLauncher jobLauncher;    // Launches batch jobs
    private final Job importCustomerJob;       // The job we configured in BatchConfig
    
    // Inject upload path from application.properties/yml
    @Value("${upload.path}")
    private String uploadPath;

    // Constructor injection of required components
    public BatchController(JobLauncher jobLauncher, Job importCustomerJob) {
        this.jobLauncher = jobLauncher;
        this.importCustomerJob = importCustomerJob;
    }

    // Handle POST requests to /batch/upload
    @PostMapping("/upload")
    public String handleFileUpload(
            // Accept multipart file upload
            @RequestParam("file") MultipartFile file, 
            // For sending messages back to the view
            RedirectAttributes redirectAttributes) {
        
        // Validate file is not empty
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please select a file to upload");
            return "redirect:/";
        }

        // Validate file extension is .xlsx
        if (!file.getOriginalFilename().endsWith(".xlsx")) {
            redirectAttributes.addFlashAttribute("error", "Please upload an Excel file (.xlsx)");
            return "redirect:/";
        }

        try {
            // Create upload directory if it doesn't exist
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                logger.info("Creating upload directory: {}", uploadPath);
                uploadDir.mkdirs();
            }

            // Generate unique filename using timestamp
            String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String originalFilename = file.getOriginalFilename();
            String filename = timestamp + "_" + originalFilename;
            Path filePath = Paths.get(uploadPath, filename);

            // Save the uploaded file to disk
            logger.info("Saving file to: {}", filePath);
            Files.copy(file.getInputStream(), filePath);

            // Create job parameters
            JobParameters params = new JobParametersBuilder()
                    .addString("filePath", filePath.toString())  // Path to uploaded file
                    .addLong("startAt", System.currentTimeMillis())  // Unique parameter for job instance
                    .toJobParameters();

            // Launch the batch job
            logger.info("Starting batch job for file: {}", filePath);
            jobLauncher.run(importCustomerJob, params);

            // Add success message for the user
            redirectAttributes.addFlashAttribute("message", 
                "File uploaded and import started successfully: " + originalFilename);

        } catch (Exception e) {
            // Log error and add error message for the user
            logger.error("Error processing file upload: ", e);
            redirectAttributes.addFlashAttribute("error", 
                "Failed to process file: " + e.getMessage());
        }

        // Redirect back to home page
        return "redirect:/";
    }
}