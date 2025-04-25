package com.ashokit.service;

// Import necessary classes
import com.ashokit.entity.Customer;
import com.ashokit.repository.CustomerRepository;
// iText PDF library imports
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;  // Spring service annotation
import org.slf4j.Logger;        // Logging framework
import org.slf4j.LoggerFactory;

// Java utilities
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

// Mark this class as a Spring service
@Service
public class PdfGeneratorService {
    // Initialize logger for this class
    private static final Logger logger = LoggerFactory.getLogger(PdfGeneratorService.class);
    
    // Repository for accessing customer data
    private final CustomerRepository customerRepository;

    // Constructor injection of repository
    public PdfGeneratorService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Main method to generate PDF
    public byte[] generatePdf() throws DocumentException {
        // Fetch all customers from database
        List<Customer> customers = customerRepository.findAll();
        
        // Check if data exists
        if (customers.isEmpty()) {
            throw new IllegalStateException("No customer data available to generate PDF");
        }

        // Create output stream for PDF data
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Create new PDF document with A4 size
        Document document = new Document(PageSize.A4);
        
        try {
            // Initialize PDF writer
            PdfWriter.getInstance(document, baos);
            document.open();

            // Add title to PDF
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Customer Data Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);  // Add space after title

            // Add timestamp to PDF
            Font timestampFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            Paragraph timestampPara = new Paragraph(
                "Generated on: " + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), 
                timestampFont
            );
            timestampPara.setAlignment(Element.ALIGN_RIGHT);
            document.add(timestampPara);
            document.add(Chunk.NEWLINE);

            // Create table with 8 columns
            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);  // Use full page width
            // Define relative column widths
            float[] columnWidths = {0.5f, 1f, 1f, 1.5f, 0.8f, 1f, 1f, 1f};
            table.setWidths(columnWidths);

            // Add table headers
            addTableHeader(table);

            // Add customer data rows
            for (Customer customer : customers) {
                addCustomerToTable(table, customer);
            }

            // Add completed table to document
            document.add(table);
        } finally {
            // Ensure document is closed
            document.close();
        }

        // Return PDF as byte array
        return baos.toByteArray();
    }

    // Helper method to add table headers
    private void addTableHeader(PdfPTable table) {
        // Define column headers
        String[] headers = {"ID", "First Name", "Last Name", "Email", "Gender", "Contact", "Country", "DOB"};
        // Set header font
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        
        // Create header cells
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setPadding(5);
            table.addCell(cell);
        }
    }

    // Helper method to add a customer row to the table
    private void addCustomerToTable(PdfPTable table, Customer customer) {
        // Set font for data cells
        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        
        // Add customer data cells
        addCell(table, String.valueOf(customer.getId()), dataFont);
        addCell(table, customer.getFirstName(), dataFont);
        addCell(table, customer.getLastName(), dataFont);
        addCell(table, customer.getEmail(), dataFont);
        addCell(table, customer.getGender(), dataFont);
        addCell(table, customer.getContactNo(), dataFont);
        addCell(table, customer.getCountry(), dataFont);
        addCell(table, customer.getDob(), dataFont);
    }

    // Helper method to add a cell to the table
    private void addCell(PdfPTable table, String content, Font font) {
        // Create cell with content, handling null values
        PdfPCell cell = new PdfPCell(new Phrase(content != null ? content : "", font));
        cell.setPadding(4);
        table.addCell(cell);
    }
}