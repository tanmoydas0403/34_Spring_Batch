package com.ashokit.batch;

// Import statements for required classes
import com.ashokit.entity.Customer;
import org.apache.poi.ss.usermodel.*; // Apache POI classes for Excel handling
import org.apache.poi.xssf.usermodel.XSSFWorkbook; // Specific to .xlsx files
import org.springframework.batch.item.ItemReader; // Spring Batch interface for reading items
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.slf4j.Logger; // Logging framework
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

// Class implements Spring Batch's ItemReader interface to read Customer objects from Excel
public class ExcelItemReader implements ItemReader<Customer> {
    // Initialize logger for this class
    private static final Logger logger = LoggerFactory.getLogger(ExcelItemReader.class);
    
    // Instance variables to maintain the state of the reader
    private final String filePath;        // Path to the Excel file
    private Iterator<Row> rowIterator;    // Iterator to traverse Excel rows
    private FileInputStream fis;          // File input stream
    private Workbook workbook;           // Excel workbook object
    private int currentRow = 0;          // Tracks current row number being processed

    // Constructor takes file path as parameter
    public ExcelItemReader(String filePath) {
        this.filePath = filePath;
        logger.info("Initialized ExcelItemReader with file: {}", filePath);
    }

    // Required method from ItemReader interface
    // Returns one Customer object per call until no more data
    @Override
    public Customer read() throws Exception {
        try {
            // Initialize reader if first time
            if (rowIterator == null) {
                initialize();
            }

            // Process next row if available
            if (rowIterator != null && rowIterator.hasNext()) {
                Row row = rowIterator.next();
                currentRow++;
                logger.debug("Processing row number: {}", currentRow);
                return mapRowToCustomer(row);
            }

            // No more rows to process, clean up and return null
            closeResources();
            return null;
        } catch (Exception e) {
            logger.error("Error reading row {}: {}", currentRow, e.getMessage());
            throw e;
        }
    }

    // Initialize Excel workbook and get iterator for rows
    private void initialize() throws IOException {
        logger.info("Initializing Excel reader");
        fis = new FileInputStream(filePath);
        workbook = new XSSFWorkbook(fis);     // Create workbook instance
        Sheet sheet = workbook.getSheetAt(0); // Get first sheet
        rowIterator = sheet.iterator();

        // Skip header row
        if (rowIterator.hasNext()) {
            rowIterator.next();
            currentRow++;
        }
        logger.info("Excel reader initialized successfully");
    }

    // Convert an Excel row to a Customer object
    private Customer mapRowToCustomer(Row row) {
        try {
            Customer customer = new Customer();
            
            // Handle ID field (required)
            Cell idCell = row.getCell(0);
            if (idCell == null) {
                logger.error("ID is missing in row {}", currentRow);
                throw new IllegalStateException("ID is required");
            }
            customer.setId((int) idCell.getNumericCellValue());

            // Map remaining fields from Excel columns to Customer properties
            customer.setFirstName(getCellValueAsString(row.getCell(1)));
            customer.setLastName(getCellValueAsString(row.getCell(2)));
            customer.setEmail(getCellValueAsString(row.getCell(3)));
            customer.setGender(getCellValueAsString(row.getCell(4)));
            customer.setContactNo(getCellValueAsString(row.getCell(5)));
            customer.setCountry(getCellValueAsString(row.getCell(6)));
            customer.setDob(getCellValueAsString(row.getCell(7)));

            logger.debug("Mapped row {} to: {}", currentRow, customer);
            return customer;
        } catch (Exception e) {
            logger.error("Error mapping row {}: {}", currentRow, e.getMessage());
            throw new RuntimeException("Error mapping Excel row " + currentRow, e);
        }
    }

    // Utility method to safely get cell value as String regardless of cell type
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        try {
            // Handle different types of cell values
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue();
                case NUMERIC:
                    // Special handling for dates
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getLocalDateTimeCellValue().toString();
                    }
                    return String.valueOf((long) cell.getNumericCellValue());
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    return cell.getCellFormula();
                default:
                    return "";
            }
        } catch (Exception e) {
            logger.warn("Error reading cell value at row {}: {}", currentRow, e.getMessage());
            return "";
        }
    }

    // Clean up method to release resources
    private void closeResources() {
        try {
            if (workbook != null) {
                workbook.close();
            }
            if (fis != null) {
                fis.close();
            }
            logger.info("Closed Excel resources successfully");
        } catch (IOException e) {
            logger.error("Error closing resources: {}", e.getMessage());
        }
    }
}