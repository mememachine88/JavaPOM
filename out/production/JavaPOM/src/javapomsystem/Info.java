
package javapomsystem;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

//Utility Class

public class Info {
    
    private static String separator = ",";
    
    public static enum Status{
        Active,
        Inactive,
        Pending,
        Approved,
        Rejected
    }

    public static String generateNewID(String type){
        
        Class objectType = getClass(type);
        int newIDNum = 0;
        String newID;
        
        int beginIndex = 1;
        if(type.equals("Supplier") || type.equals("PurchaseRequisition") || type.equals("PurchaseOrder")){
            beginIndex = 2;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(FileAccess.getFileName(objectType)));

            String line;
            while((line = reader.readLine()) != null && line.length() >0){
                String[] info = line.split(separator);
                if (info[0].substring(0,1).equals(type.substring(0,1))){
                    int readID = Integer.parseInt(info[0].substring(beginIndex,5));
                    if(readID > newIDNum){
                        newIDNum = readID;
                    }
                }
            }
            newIDNum = newIDNum + 1;
            newID = switch (type) {
                case "Supplier" -> String.format("S-%03d", newIDNum);
                case "PurchaseRequisition" -> String.format("PR%03d", newIDNum);
                case "PurchaseOrder" -> String.format("PO%03d", newIDNum);
                default -> type.substring(0, 1) + String.format("%04d", newIDNum);
            };
            reader.close();
            return newID;
            
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        
        System.out.println("(SYSTEM) New ID generation failed.");
        return "X0000";
    }
    
    public static void generateTable(String[] data, int[] spacing, boolean isHeader){
        
        if (isHeader){
            for (int i = 0; i < spacing.length; i++) {
                System.out.print("-".repeat(spacing[i]));
                if (i != spacing.length - 1) {
                    System.out.print("--");
                } else {
                    System.out.print("\n");
                }
            }
        } 
        
        for (int i = 0; i < spacing.length; i++) {
            System.out.print(Info.formatDataLength(data[i], spacing[i]));
            if (i != spacing.length - 1) {
                System.out.print("| ");
            } else {
                System.out.print("\n");
            }
        }
        
        if (isHeader){
            for (int i = 0; i < spacing.length; i++) {
                System.out.print("-".repeat(spacing[i]));
                if (i != spacing.length - 1) {
                    System.out.print("|-");
                } else {
                    System.out.print("\n");
                }
            }
        }    
    }
    
    public static String formatDataLength(String value, int length) {
        return String.format("%-" + length + "s", value);
    }
    
    public static Class<?> getClass(String type){
        
        Class<?> className = null;
        
        switch(type){
            case "User" -> className = User.class;
            case "Admin" -> className = User.class;
            case "Sales" -> className = User.class;
            case "Purchasing" -> className = User.class;
            case "Item" -> className = Item.class;
            case "Stock" -> className = Stock.class;
            case "Supplier" -> className = Supplier.class;
            case "dailySales" -> className = dailySales.class;
            case "ActionHistory" -> className = ActionHistory.class;
            case"PurchaseRequisition"-> className = PurchaseRequisition.class;
            case "PurchaseOrder"-> className = PurchaseOrder.class;
        }  
        return className;
    }
  
    public static int getStatusCount(Class<?> objectType, boolean isActive){
        
        ArrayList<?> objectList = FileAccess.ReadFromTextFile(objectType);
        
        int count = 0;
        for (Object object : objectList) {
            switch (objectType.getSimpleName()) {
                case "User" ->{
                    User user = (User) object;
                    if(user.getStatus().equals(getStatusString(isActive))){
                        count++;
                    }
                }
                case "Stock" -> {
                    Stock stock = (Stock) object;
                    if(stock.getStatus().equals(getStatusString(isActive))){
                        count++;
                    }
                }
                case "Item" -> {
                    Item item = (Item) object;
                    if(item.getStatus().equals(getStatusString(isActive))){
                        count++;
                    }
                }
                case "Supplier" ->{
                    Supplier supplier = (Supplier) object;
                    if(supplier.getStatus().equals(getStatusString(isActive))){
                        count++;
                    }
                }
                default -> {}
            }
        }
        
        return count;
    }
    
    public static String getStatusString(boolean isActive){
        if(isActive){
            return "Active";
        } else{
            return "Inactive";
        }
    }
        

    
    
    
    public static String promptDateFilter(String filter){
        
        Scanner Sc = new Scanner(System.in);
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd");
        
        String value = "";
        String invalidValue = "X";
        String year,month,day;

        if(filter.equals("Date") || filter.equals("Month") || filter.equals("Year")){
            System.out.print("\nPlease enter a year\t\t: ");
            year = Sc.nextLine();
            if(!isValidYear(year)){
                return invalidValue;
            }
            value = year;
            
            if(filter.equals("Date") || filter.equals("Month") ){
                System.out.print("Please enter a month\t\t: ");
                month = Sc.nextLine();
                if(!isValidMonth(month)){
                    return invalidValue;
                }
                value += "." + String.format("%02d", Integer.valueOf(month));
                
                if (filter.equals("Date")){
                    System.out.print("Please enter a day\t\t: ");
                    day = Sc.nextLine();
                    if(!isValidDate(year,month,day)){
                        return invalidValue;
                    }
                    value += "." + String.format("%02d", Integer.valueOf(day));
                }
            }
        }  else if(filter.equals("Today")){
            Date date = new Date();
            value = ft.format(date);
        }
        
        return value;
    }
    
    private static boolean isValidYear(String year) {
        try {
            int yearValue = Integer.parseInt(year);
            return (yearValue >= 0 && year.length() == 4); // Year should be non-negative and 4 digits long
        } catch (NumberFormatException e) {
            System.out.println("\n(SYSTEM) Invalid year input! Please try again.");
            return false; // Year is not a valid number
        }
    }

    private static boolean isValidMonth(String month) {
        try {
            int monthValue = Integer.parseInt(month);
            return (monthValue >= 1 && monthValue <= 12); // Month should be between 1 and 12
        } catch (NumberFormatException e) {
            System.out.println("\n(SYSTEM) Invalid month input! Please try again.");
            return false; // Month is not a valid number
        }
    }

    private static boolean isValidDate(String year, String month, String day) {
        try {
            int yearValue = Integer.parseInt(year);
            int monthValue = Integer.parseInt(month);
            int dayValue = Integer.parseInt(day);

            Calendar calendar = Calendar.getInstance();
            calendar.setLenient(false);
            calendar.set(yearValue, monthValue - 1, 1); // Set the calendar to the first day of the month
            int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            if(dayValue >= 1 && lastDayOfMonth >= dayValue){ // Day should be between 1 and last day of the month
                return true;
            } else{
                System.out.println("\n(SYSTEM) Invalid day input! Please try again.");
                return false;
            }

            
        } catch (NumberFormatException e) {
            System.out.println("\n(SYSTEM) Invalid date input! Please try again.");
            return false; // Day is not a valid number
        }
    }

}
