package org.example.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.junit.Test;

/**
 * date api test
 */
public class DateApiTest {
    
    @Test
    public void testDateToLocalDateTime() {
        // Create a java.util.Date object
        Date date = new Date();
        
        // Convert Date to LocalDateTime
        LocalDateTime localDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        
        System.out.println("Original Date: " + date);
        System.out.println("Converted LocalDateTime: " + localDateTime);
    }
    
    @Test
    public void testDateToLocalDate() {
        // Create a java.util.Date object
        Date date = new Date();
        
        // Convert Date to LocalDate
        LocalDate localDate = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        
        System.out.println("Original Date: " + date);
        System.out.println("Converted LocalDate: " + localDate);
    }

    @Test
    public void testDateStringFormatting() {
        // Create a java.util.Date object
        Date date = new Date();
        
        // Format using SimpleDateFormat (legacy approach)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(date);
        System.out.println("Formatted Date (SimpleDateFormat): " + formattedDate);
        
        // Format using DateTimeFormatter (modern approach)
        LocalDateTime localDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        
        // Different format patterns
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        DateTimeFormatter formatter4 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        System.out.println("Formatted DateTime (yyyy-MM-dd HH:mm:ss): " + 
                localDateTime.format(formatter1));
        System.out.println("Formatted Date (dd/MM/yyyy): " + 
                localDateTime.format(formatter2));
        System.out.println("Formatted Date (MMMM dd, yyyy): " + 
                localDateTime.format(formatter3));
        System.out.println("Formatted Date (yyyy-MM-dd): " + 
                localDateTime.format(formatter4));
    }
}
