
package javapomsystem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class dailySales{
    
    private Item item; // Aggregation: DailySales has an Item object
    private String SalesDate;
    private String ItemID;
    private int Quantity;
    private double Price;
    
    private static String separator = ",";
  
    //Add and update sales
    public dailySales(String date, String itemID, int quantity, double price){
        SalesDate = date;
        ItemID = itemID;
        Quantity = quantity;
        Price = price;
    }
    
    //Display sales
    public dailySales(String date, Item _item, int quantity, double price){
        item = _item;
        ItemID = _item.getItemID();
        SalesDate = date;
        Quantity = quantity;
        Price = price;
    }
    
    public Item getItem(){
        return item;
    }
   
    public String getSalesDate(){
        return SalesDate;
    }
    
    public String getItemID(){
        return ItemID;
    }
    
    public int getQuantity(){
        return Quantity;
    }
    
    public double getSellingPrice(){
        return Price;
    }
    
    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
    
    public void setPrice(double price){
        Price = price;
    }
    
    
    public static void displayDailySales(String value){
        ArrayList<dailySales> dailySalesList = FileAccess.ReadFromTextFile(dailySales.class);
        ArrayList<Stock> stockList = FileAccess.ReadFromTextFile(Stock.class);
        ArrayList<dailySales> filteredList = new ArrayList<>();

        String[] header = {"Date", "Item ID", "Category", "Name", "Specifications", "Price", "Quantity", "Total"};
        int[] spacing = {15, 10, 20, 20, 20, 10, 10, 15};
       
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
              
        for(dailySales sales : dailySalesList){
            // (if no filter: display all) || (if date exist) || (if year/month exist) // dateInfo[filterIndex].equals(value)
            if(value == null || sales.getSalesDate().equals(value) || sales.getSalesDate().substring(0,4).equals(value)
                    || sales.getSalesDate().substring(0,7).equals(value)){
                filteredList.add(sales);
            }
        }
        
        if(!filteredList.isEmpty()){
            Info.generateTable(header,spacing,true);
            for(dailySales sales : filteredList){
                for(Stock stock : stockList){
                    if(sales.getItemID().equals(stock.getItemID())){
                        String[] data = {sales.getSalesDate(),sales.getItemID(),stock.getCategory(),stock.getName(),stock.getSpecification(),
                            "RM " + df.format(sales.getSellingPrice()),String.valueOf(sales.getQuantity()),"RM " + df.format(sales.getSellingPrice()*sales.getQuantity())};
                        Info.generateTable(data,spacing,false);
                    }   
                }
            }
            ActionHistory action = new ActionHistory(User.loggedInUser.getID(),"Viewed list of daily sales.");
            action.recordActionHistory();
        } else{
            System.out.println("\n(SYSTEM) No record found! Please try again.");
        }  
        
    }
    
    public static void addNewDailySales(String date){
        Scanner Sc = new Scanner(System.in);
        
        String choice = "Y";
        if(!date.equals("X")){
            do{
                System.out.println("\n~ ~ ~ A D D   I T E M   S A L E S ~ ~ ~\n");
                Stock.displayStockDetails(true);
                System.out.print("\nPlease enter the item ID \t\t\t: I");
                String itemID = "I" + Sc.nextLine();
                if(Stock.getStockDetails(itemID) == null){
                    System.out.println("\n(SYSTEM) Item does not exist! Please try again.");
                    break;
                }
          
                if(salesExist(date,itemID)){
                    System.out.println("\n(SYSTEM) Sales record for the item already exists! Please try again.");
                    break;
                }

                Stock currentStock = Stock.getStockDetails(itemID);
                double price = currentStock.getSellingPrice();
                int stockQuantity = currentStock.getQuantity();

                int salesQuantity = 0;
                boolean validQuantity = false;
                while (!validQuantity) {
                    System.out.print("Please enter the item sales quantity \t\t: ");
                    String salesQuantityInput = Sc.nextLine();
                    try {
                        salesQuantity = Integer.parseInt(salesQuantityInput);
                        validQuantity = true;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input for quantity. Please enter a valid number.\n");
                    }
                }

                if(salesQuantity <= 0){
                    System.out.println("\n(SYSTEM) Invalid quantity input! Please try again.");
                    break;
                } else if(salesQuantity > stockQuantity){
                    System.out.println("\n(SYSTEM) Quantity input exists stock quantity! Please try again.");
                    break;
                }      

                
                dailySales newSales = new dailySales(date,itemID,salesQuantity,price);

                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(FileAccess.getFileName(dailySales.class),true));
                    writer.write(newSales.getSalesDate() + separator + newSales.getItemID() + separator + newSales.getQuantity() + separator + newSales.getSellingPrice() + "\n");
                    writer.close();

                    //Update stock quantity       
                    Stock.updateStockQuantity(newSales.getItemID(), newSales.getQuantity());

                    System.out.println("(SYSTEM) Item sales added successfully!\n");
                    ActionHistory action = new ActionHistory(User.loggedInUser.getID(),"Added new sales for item " + newSales.getItemID() + " for date: " + newSales.getSalesDate() + ".");
                    action.recordActionHistory();

                    System.out.print("Do you wish to add more item sales? (Enter 'y' to continue): ");
                    choice = Sc.nextLine();
                    System.out.println("");

                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                } 
                

            } while(choice.equalsIgnoreCase("Y"));
        }

    }
    
    public void editRecord(boolean isDelete){
        ArrayList<dailySales> dailySalesList = FileAccess.ReadFromTextFile(dailySales.class);
        ArrayList<dailySales> updatedSalesList = new ArrayList<>();
        
        int oriSalesQuantity = 0, stockChanges;
        
        for(dailySales sales : dailySalesList){
            if(sales.getSalesDate().equals(getSalesDate()) && sales.getItemID().equals(getItemID())){
                oriSalesQuantity = sales.getQuantity();
                sales.setQuantity(getQuantity());
                sales.setPrice(getSellingPrice());
            }
            if(!isDelete){
                updatedSalesList.add(sales);
            } 
        }

        stockChanges = getQuantity() - oriSalesQuantity;

        Stock stock = Stock.getStockDetails(getItemID());
        if(stock.getQuantity() - stockChanges >= 0){
            
            Stock.updateStockQuantity(getItemID(), stockChanges);
            FileAccess.UpdateTextFile(updatedSalesList,dailySales.class); 
            ActionHistory action = new ActionHistory(User.loggedInUser.getID(),"Edited sales details for item " + getItemID() + " of date: " + getSalesDate() + " and updated stock quantity.");
            action.recordActionHistory();

        } else{
            System.out.println("\n(SYSTEM) Insufficient stock quantity! Please try again.");
        }
    }
    

    private static boolean salesExist(String date, String itemID){
        
        ArrayList<dailySales> dailySalesList = FileAccess.ReadFromTextFile(dailySales.class);
        
        String formatPattern = "yyyy.MM.dd";
        SimpleDateFormat ftDate = new SimpleDateFormat(formatPattern);
        ftDate.setLenient(false);
        boolean flag = false;

        try {
            ftDate.parse(date);
        } catch (ParseException e) {
            System.out.println("\n(SYSTEM) Invalid date input! Please try again.");
            return flag;
        }
        
        for(dailySales sales : dailySalesList){
            if(sales.getSalesDate().equals(date) && sales.getItemID().equals(itemID)){
                flag = true;
                return flag;
            }
        }
        
        return flag;
       
    }
    
}   