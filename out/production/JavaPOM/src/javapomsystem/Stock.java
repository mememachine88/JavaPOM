
package javapomsystem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Stock extends Item{
  
    private int Quantity;
    private double SellingPrice;
    private String ExpiryDate; // Assumption : only the most near expiry date recorded
    private int ReorderLevel;
    private Info.Status Status;
    
    private static String separator = "/";

 
    //Stock object in text file
    public Stock(String itemID, String category, String brand, String name, String specification, String expiryDate, double price, int quantity, int ROL, String status){
        super(itemID,category,brand,name,specification,status);
        ExpiryDate = expiryDate;
        SellingPrice = price;
        Quantity = quantity;
        ReorderLevel = ROL;
        Status = Info.Status.valueOf(status);
    }
    //Creating new item object
    public Stock(String itemID, String category, String brand, String name, String specification, String supplierID, double cost, String expiryDate, double price, int quantity, int ROL, String itemStatus, String stockStatus){
        super(itemID,category,brand,name,specification,supplierID,cost,itemStatus);
        ExpiryDate = expiryDate;
        SellingPrice = price;
        Quantity = quantity;
        ReorderLevel = ROL;
        Status = Info.Status.valueOf(stockStatus);
    }
    
    //Editable stock details
    public Stock(String itemID,String category,String brand,String name,String specification,String expiryDate,double price,int ROL){
        super(itemID,category,brand,name,specification);
        ExpiryDate = expiryDate;
        SellingPrice = price;
        ReorderLevel = ROL;
    }
    public Stock(String itemID, int quantity, double cost){
        super(itemID,cost);
        this.Quantity= quantity;

    }
    public String getExpiryDate(){
        return ExpiryDate;
    }
    
    public double getSellingPrice(){
        return SellingPrice;
    }
    
    public int getQuantity(){
        return Quantity;
    }
    
    public int getReorderLevel(){
        return ReorderLevel;
    }
   
    @Override
    public String getStatus(){
        return this.Status.name();
    }
    
    public void setExpiryDate(String expiryDate){
        ExpiryDate = expiryDate;
    }
    
    public void setSellingPrice(double sellingPrice){
        SellingPrice = sellingPrice;
    }
    
    public void setQuantity(int quantity){
        Quantity = quantity;
    }
    
    public void setReorderLevel(int ROL){
        ReorderLevel = ROL;
    }
    public String toString(){
        return getItemID() +"#" + getCost()+"#"+ getQuantity() ;
    }
    @Override
    public void setStatus(String status){
        this.Status = Info.Status.valueOf(status);
    }
    
    public static void displayStockDetails(){
        ArrayList<Stock> stockList = FileAccess.ReadFromTextFile(Stock.class);
        
        System.out.println("\n~ ~ I T E M / S T O C K   D E T A I L S ~ ~\n");
        String [] header = {"Item ID","Category","Brand","Name","Specification","Expiry Date","Price","Quantity","Reorder Level","Status"};
        int[] spacing = {10,20,15,20,20,15,12,10,15,15};
        Info.generateTable(header,spacing,true);
        
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
              
        for(Stock stock : stockList){
            String[] data = {stock.getItemID(),stock.getCategory(),stock.getBrand(),stock.getName(),stock.getSpecification(),stock.getExpiryDate(),df.format(stock.getSellingPrice()),
                Integer.toString(stock.getQuantity()),Integer.toString(stock.getReorderLevel()),stock.getStatus()};
            Info.generateTable(data,spacing,false);
        }
        
        ActionHistory action = new ActionHistory(User.loggedInUser.getID(),"Viewed list of stock details.");
        action.recordActionHistory();
    }
    
    
    public static void displayStockDetails(boolean isActive){
        ArrayList<Stock> stockList = FileAccess.ReadFromTextFile(Stock.class);

        String status;
        if(isActive){
            System.out.println("\n~ ~ A C T I V E   S T O C K   D E T A I L S ~ ~\n");   
        } else{
            System.out.println("\n~ ~ I N A C T I V E   S T O C K   D E T A I L S ~ ~\n");
        }
        
        String [] header = {"Item ID","Category","Brand","Name","Specification","Expiry Date","Price","Quantity","Reorder Level"};
        int[] spacing = {10,20,15,20,20,15,12,10,15};
        Info.generateTable(header,spacing,true);
        
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
              
        for(Stock stock : stockList){
            if(stock.getStatus().equals(Info.getStatusString(isActive))){
                String[] data = {stock.getItemID(),stock.getCategory(),stock.getBrand(),stock.getName(),stock.getSpecification(),stock.getExpiryDate(),df.format(stock.getSellingPrice()),
                Integer.toString(stock.getQuantity()),Integer.toString(stock.getReorderLevel())};
                Info.generateTable(data,spacing,false);
            }
        }
    }
    
    //Prompt Category, Brand, Name, Specs, SupplierID, Cost, Selling Price, ROL. ( expiry date, quantity null )
    @Override
    public void addEntry() {
        try {
            BufferedWriter stockWriter = new BufferedWriter(new FileWriter(FileAccess.getFileName(Stock.class),true));
            stockWriter.write(getItemID() + separator + getCategory() + separator + getBrand() + separator + getName() + separator + getSpecification() + separator + "-" + separator 
                    + getSellingPrice() + separator + 0 + separator + getReorderLevel() + separator + getStatus() + "\n");
            stockWriter.close();
            
            BufferedWriter itemWriter = new BufferedWriter(new FileWriter(FileAccess.getFileName(Item.class),true));
            itemWriter.write(getItemID() + separator + getSupplierID() + separator + getCost() + separator + getStatus() + "\n");
            itemWriter.close();
            
            ActionHistory action = new ActionHistory(User.loggedInUser.getID(),"Added new item " + getItemID() + ".");
            action.recordActionHistory();
            System.out.println("\n(SYSTEM) Item successfully added!");
            
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }   
    }
    
    @Override
    public void editRecord(){
        ArrayList<Stock> stockList = FileAccess.ReadFromTextFile(Stock.class);
        ArrayList<Stock> updatedList = new ArrayList<>();
        
        for(Stock stock : stockList){
            if(stock.getItemID().equals(getItemID())){
                stock.setCategory(getCategory());
                stock.setBrand(getBrand());
                stock.setName(getName());
                stock.setSpecification(getSpecification());
                stock.setExpiryDate(getExpiryDate());
                stock.setSellingPrice(getSellingPrice());
                stock.setReorderLevel(getReorderLevel());
            }
            updatedList.add(stock);
        }
        FileAccess.UpdateTextFile(updatedList,Stock.class);
        ActionHistory action = new ActionHistory(User.loggedInUser.getID(),"Edited stock details for item " + getItemID() + ".");
        action.recordActionHistory();
    }
    
    public static void updateStatus(String itemID, String updatedStatus){
        ArrayList<Stock> stockList = FileAccess.ReadFromTextFile(Stock.class);
        ArrayList<Stock> updatedList = new ArrayList<>();
        
        for(Stock stock : stockList){
            if(stock.getItemID().equals(itemID)){
                stock.setStatus(updatedStatus);
            }
            updatedList.add(stock);
        }
        FileAccess.UpdateTextFile(updatedList,Stock.class);   
    }
    
    //tested
    public static void updateStockQuantity(String itemID, int quantity){
        ArrayList<Stock> stockList = FileAccess.ReadFromTextFile(Stock.class);
        ArrayList<Stock> updatedList = new ArrayList<>();
        
        for(Stock stock : stockList){
            if(stock.getItemID().equals(itemID)){
                int balance = stock.getQuantity() - quantity;
                stock.setQuantity(balance);
            }
            updatedList.add(stock);
        }
        
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(FileAccess.getFileName(Stock.class)));
            for(Stock stock : updatedList){
                writer.write(stock.getItemID() + "," + stock.getCategory() + "," + stock.getBrand() + "," + stock.getName() + "," +
                        stock.getSpecification() + "," + stock.getExpiryDate() + "," + stock.getSellingPrice() + "," +
                        stock.getQuantity() + "," + stock.getReorderLevel() + "," + stock.getStatus());
                writer.newLine();
            }
            writer.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }       
    } 
    
    public static Stock getStockDetails(String itemID){
        
        ArrayList<Stock> stockList = FileAccess.ReadFromTextFile(Stock.class);
        
        for(Stock stock : stockList){
            if(stock.getItemID().equals(itemID)){
                return stock;
            }
        }
        return null;  
    }
    
}
