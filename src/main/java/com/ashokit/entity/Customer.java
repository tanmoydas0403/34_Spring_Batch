package com.ashokit.entity;

// Import JPA annotations from Jakarta Persistence
import jakarta.persistence.*;

// Mark this class as a JPA entity
@Entity
// Specify the database table name
@Table(name = "CUSTOMER_INFO")
public class Customer {
    
    // Primary key field
    @Id
    // Map to database column "customer_id"
    @Column(name = "customer_id")
    private Integer id;
    
    // Map to database column "first_name"
    @Column(name = "first_name")
    private String firstName;
    
    // Map to database column "last_name"
    @Column(name = "last_name")
    private String lastName;
    
    // Map to database column "email"
    @Column(name = "email")
    private String email;
    
    // Map to database column "gender"
    @Column(name = "gender")
    private String gender;
    
    // Map to database column "contact_no"
    @Column(name = "contact_no")
    private String contactNo;
    
    // Map to database column "country"
    @Column(name = "country")
    private String country;
    
    // Map to database column "dob" (Date of Birth)
    @Column(name = "dob")
    private String dob;

    // Getter and Setter methods for all fields
    // Getter for id
    public Integer getId() { return id; }
    // Setter for id
    public void setId(Integer id) { this.id = id; }
    
    // Getter for firstName
    public String getFirstName() { return firstName; }
    // Setter for firstName
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    // Getter for lastName
    public String getLastName() { return lastName; }
    // Setter for lastName
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    // Getter for email
    public String getEmail() { return email; }
    // Setter for email
    public void setEmail(String email) { this.email = email; }
    
    // Getter for gender
    public String getGender() { return gender; }
    // Setter for gender
    public void setGender(String gender) { this.gender = gender; }
    
    // Getter for contactNo
    public String getContactNo() { return contactNo; }
    // Setter for contactNo
    public void setContactNo(String contactNo) { this.contactNo = contactNo; }
    
    // Getter for country
    public String getCountry() { return country; }
    // Setter for country
    public void setCountry(String country) { this.country = country; }
    
    // Getter for dob (Date of Birth)
    public String getDob() { return dob; }
    // Setter for dob
    public void setDob(String dob) { this.dob = dob; }

    // Override toString method for debugging and logging
    @Override
    public String toString() {
        // Create a string representation of the Customer object
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", country='" + country + '\'' +
                ", dob='" + dob + '\'' +
                '}';
    }
}