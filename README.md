Base URL: http://localhost:8080/batch-app

Available Endpoints:

1. Home Page
   URL: http://localhost:8080/batch-app/
   or
   URL: http://localhost:8080/batch-app/home

2. Excel File Upload
   URL: http://localhost:8080/batch-app/batch/upload
   Method: POST
   Content-Type: multipart/form-data

3. PDF Generation
   URL: http://localhost:8080/batch-app/pdf/generate
   Method: GET

4. H2 Database Console
   URL: http://localhost:8080/batch-app/h2-console
   Credentials:
   - Username: sa
   - Password: [empty]
   - JDBC URL: jdbc:h2:mem:testdb
   - Driver Class: org.h2.Driver

5. Error Page
   URL: http://localhost:8080/batch-app/error

Security Note:
- Application requires authentication
- Username: tanmoydas0403
- Password: password

File Paths:
1. Excel Uploads: [user.home]/batch-app/uploads/excel
2. PDF Exports: [user.home]/batch-app/exports/pdf
3. CSV Exports: [user.home]/batch-app/exports/csv
4. Log File: [user.home]/batch-app/logs/batch-app.log#   3 4 _ S p r i n g _ B a t c h  
 