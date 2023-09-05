
package javapomsystem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Scanner;

public class Item implements DataOperations{
    
    private String ItemID;
    private String Category;
    private String Brand;
    private String Name;
    private String Specification;
    private String SupplierID;
    private double Cost;
    private Info.Status Status;
    private int quantity;
    private static String separator = "/";
    public enum category{
        Meat,
        Seafoods,
        Vegetables,
        Fruits,
        DairyProducts,
        Beverages,
        Snacks,
        Biscuits,
        FrozenFoods,
        PersonalCare,
        HealthCare,
        CleaningSupplies,
        Bakery,
        Condiments,
        Others
    }
    
    
    //Full item object
    public Item(String itemID, String itemType, String brand, String name, String specification, String supplierID, double cost, String status){
        ItemID = itemID;
        Category = itemType;
        Brand = brand;
        Name = name;
        Specification = specification;
        SupplierID = supplierID;
        Cost = cost;
        Status = Info.Status.valueOf(status);
    }
    
    //Item object in item list
    public Item(String itemID, String supplierID, double cost, String status){
        ItemID = itemID;
        SupplierID = supplierID;
        Cost = cost;
        Status = Info.Status.valueOf(status);
    }
    
    //Stock Inheritance
    public Item(String itemID, String itemType, String brand, String name, String specification, String status){
        ItemID = itemID;
        Category = itemType;
        Brand = brand;
        Name = name;
        Specification = specification;
        Status = Info.Status.valueOf(status);
    }
    
    //Stock Inheritance for editing stock details
    public Item(String itemID, String itemType, String brand, String name, String specification){
        ItemID = itemID;
        Category = itemType;
        Brand = brand;
        Name = name;
        Specification = specification;
    }
    public Item(String itemID, String itemName, String itemBrand, double cost, int quantity){
        this.ItemID= itemID;
        this.Name=itemName;
        this.Brand=itemBrand;
        this.Cost=cost;
        this.quantity = quantity;

    }
    public Item(String itemID, double cost) {
        this.ItemID = itemID;
        this. Cost = cost;
    }

    public String getItemID() {
        return ItemID;
    }

    public String getCategory() {
        return Category;
    }
    
    public String getBrand() {
        return Brand;
    }

    public String getName() {
        return Name;
    }
    
    public String getSupplierID(){
        return SupplierID;
    }

    public double getCost(){
        return Cost;
    }
    
    public String getSpecification() {
        return Specification;
    }
    
    public String getStatus(){
        return Status.name();
    }
    
    public void setCategory(String itemType){
        Category = itemType;
    }
    
    public void setBrand(String brand){
        Brand = brand;
    }
    
    public void setName(String name){
        Name = name;
    }
    
    public void setSpecification(String specification){
        Specification = specification;
    }
    
    public void setCost(double cost){
        Cost = cost;
    }
    
    public void setStatus(String status){
        Status = Info.Status.valueOf(status);
    }
    public void setQuantity(int quantity){this.quantity= quantity;}
    public int getQuantity(){return quantity;}
    public static void displayItemsDetails(){
        ArrayList<Item> itemList = FileAccess.ReadFromTextFile(Item.class);
        ArrayList<Stock> stockList = FileAccess.ReadFromTextFile(Stock.class);
        
        Collections.sort(itemList, Comparator
        .comparing(Item::getSupplierID)
        .thenComparing(Item::getItemID));
        
        System.out.println("\n~ ~ I T E M   D E T A I L S ~ ~\n");
        String [] header = {"Supplier ID","Item ID","Category","Brand","Name","Specifications","Cost","Status"};
        int[] spacing = {12,10,20,15,20,20,12,15};
        Info.generateTable(header,spacing,true);
        
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        
        for(Item item : itemList){
            for(Stock stock : stockList){
                if(item.getItemID().equals(stock.getItemID())){
                    String[] data = {item.getSupplierID(),item.getItemID(),stock.getCategory(),stock.getBrand(),stock.getName(),stock.getSpecification(),"RM " + df.format(item.getCost()),item.getStatus()};
                    Info.generateTable(data,spacing,false);
                }          
            }
        }
        
        ActionHistory action = new ActionHistory(User.loggedInUser.getID(),"Viewed list of item details."); 
        action.recordActionHistory();
    }
    
    public static void displayItemsDetails(boolean isActive){
        ArrayList<Item> itemList = FileAccess.ReadFromTextFile(Item.class);
        ArrayList<Stock> stockList = FileAccess.ReadFromTextFile(Stock.class);
        
        if(isActive){
            System.out.println("\n~ ~ A C T I V E   I T E M S   D E T A I L S ~ ~\n");   
        } else{
            System.out.println("\n~ ~ I N A C T I V E   I T E M S   D E T A I L S ~ ~\n");
        }
        
        Collections.sort(itemList, Comparator
        .comparing(Item::getItemID)
        .thenComparing(Item::getSupplierID));
        
        System.out.println("\n~ ~ I T E M   D E T A I L S ~ ~\n");
        String [] header = {"Item ID","Supplier ID","Category","Brand","Name","Specifications","Cost","Status"};
        int[] spacing = {10,12,20,15,20,20,12,15};
        Info.generateTable(header,spacing,true);
        
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        
        for(Item item : itemList){
            for(Stock stock : stockList){
                if(item.getStatus().equals(Info.getStatusString(isActive))){
                    if(item.getItemID().equals(stock.getItemID())){
                        String[] data = {item.getItemID(),item.getSupplierID(),stock.getCategory(),stock.getBrand(),stock.getName(),stock.getSpecification(),"RM " + df.format(item.getCost()),item.getStatus()};
                        Info.generateTable(data,spacing,false);
                    }   
                }        
            }
        }      
    }
    
    public static String displayItemSuppliers(String itemID){

        ArrayList<Item> itemList = FileAccess.ReadFromTextFile(Item.class);
        ArrayList<Stock> stockList = FileAccess.ReadFromTextFile(Stock.class);
        ArrayList<Supplier> supplierList = FileAccess.ReadFromTextFile(Supplier.class);
        ArrayList<Item> filteredList = new ArrayList<>();
        
        for(Item item : itemList){
            if(item.getItemID().equals(itemID)){
                filteredList.add(item);
            }
        }
        
        if (!filteredList.isEmpty()){
            for(Stock stock : stockList){
                if(stock.getItemID().equals(itemID)){
                    System.out.println("\n~ ~ I T E M   D E T A I L S ~ ~\n");
                    System.out.println("Item ID \t\t: " + stock.getItemID());
                    System.out.println("Category \t\t: " + stock.getCategory());
                    System.out.println("Brand \t\t\t: " + stock.getBrand());
                    System.out.println("Name \t\t\t: " + stock.getName());
                    System.out.println("Specifications \t\t: " + stock.getSpecification());
                }
            }
            System.out.println("\n~ ~ S U P P L I E R S   D E T A I L S ~ ~\n");
            String [] header = {"Supplier ID","Name","Cost","Address","Delivery Fee"};
            int[] spacing = {15,25,10,20,15};
            Info.generateTable(header,spacing,true);
            
            DecimalFormat df = new DecimalFormat("0.00");
            df.setMaximumFractionDigits(2);
            
            for(Item item : filteredList){
                
                for(Supplier supplier : supplierList){
                    if(supplier.getSupplierID().equals(item.getSupplierID()) && supplier.getStatus().equals(Info.Status.Active.name())){
                        String[] data = {supplier.getSupplierID(),supplier.getName(),"RM " + df.format(item.getCost()),supplier.getAddress(),"RM " + df.format(supplier.getDeliveryFee())};
                        Info.generateTable(data,spacing,false);
                    }
                }
            }
            System.out.println("");
            
        } else{
            System.out.println("\n(SYSTEM) Item does not exist! Please try again.");
        }
        
        ActionHistory action = new ActionHistory(User.loggedInUser.getID(),"Viewed list of suppliers for item " + itemID + "."); 
        action.recordActionHistory();
        return itemID;
    }
    
    public static void displaySpecificSupplierItems(String supplierID){
        
       /* Scanner Sc = new Scanner(System.in);
        Supplier.displaySupplierDetails(true);
        System.out.print("\nPlease enter a supplier ID to continue \t: S-");
        String supplierID = "S-" + Sc.nextLine();*/
        
        ArrayList<Item> itemList = FileAccess.ReadFromTextFile(Item.class);
        ArrayList<Stock> stockList = FileAccess.ReadFromTextFile(Stock.class);
        ArrayList<Supplier> supplierList = FileAccess.ReadFromTextFile(Supplier.class);
        ArrayList<Item> filteredList = new ArrayList<>();
        
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        
        String [] header = {"Item ID","Category","Brand","Name","Specifications","Cost","Status"};
        int[] spacing = {10,20,15,20,20,12,15};
        
        boolean flag = false;
        for(Supplier supplier : supplierList){
            if(supplier.getSupplierID().equals(supplierID)){
                flag = true;
                if(supplier.getStatus().equals(Info.Status.Active.name())){
                    System.out.println("\n~ ~ ~ S U P P L I E R   I T E M S   L I S T ~ ~ ~\n");
                    System.out.println("Supplier ID \t: " + supplier.getSupplierID());
                    System.out.println("Name \t\t: " + supplier.getName());
                    System.out.println("Address \t: " + supplier.getAddress());
                    System.out.println("Delivery Fee \t: RM " + supplier.getDeliveryFee());
                    System.out.println("Status \t\t: " + supplier.getStatus() + "\n");

                    Info.generateTable(header,spacing,true);
                    filteredList.clear();
                    ActionHistory action = new ActionHistory(User.loggedInUser.getID(),"Viewed list of items from supplier " + supplierID + "."); 
                    action.recordActionHistory();

                    for(Item item : itemList){
                        if(item.getSupplierID().equals(supplier.getSupplierID())){
                            filteredList.add(item);
                        }
                    }

                    if(!filteredList.isEmpty()){
                        for(Item item : filteredList){
                            for(Stock stock : stockList){
                                if(item.getItemID().equals(stock.getItemID())){
                                    String[] data = {item.getItemID(),stock.getCategory(),stock.getBrand(),stock.getName(),stock.getSpecification(),"RM " + df.format(item.getCost()),item.getStatus()};
                                    Info.generateTable(data,spacing,false);
                                }   
                            }
                        }       
                    } else{
                        System.out.println("\n(SYSTEM) No item record found.\n\n");
                    }  
                } else{
                    System.out.println("\n(SYSTEM) Selected supplier is not active. Please try again!");
                }
            }
        } 
        
        if (!flag) {
            System.out.println("\n(SYSTEM) Supplier ID not found. Please enter a valid supplier ID.");
        }
    }
    
    @Override
    public void addEntry() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FileAccess.getFileName(Item.class),true));
            writer.write(getItemID() + separator + getSupplierID() + separator + getCost() + separator + getStatus() + "\n");
            System.out.println("\n(SYSTEM) Item successfully added!");
            writer.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }   
        ActionHistory action = new ActionHistory(User.loggedInUser.getID(),"Added new item " + getItemID() + "from supplier " + getSupplierID() +".");
        action.recordActionHistory();
    }
    
    @Override
    public void editRecord(){
        ArrayList<Item> itemList = FileAccess.ReadFromTextFile(Item.class);
        ArrayList<Item> updatedList = new ArrayList<>();
        
        for(Item item : itemList){
            if(item.getItemID().equals(getItemID()) && item.getSupplierID().equals(getSupplierID())){
                item.setCost(getCost());
            }
            updatedList.add(item);
        }
        FileAccess.UpdateTextFile(updatedList,Item.class);
        ActionHistory action = new ActionHistory(User.loggedInUser.getID(),"Edited item cost for item " + getItemID() + " from supplier " + getSupplierID() + ".");
        action.recordActionHistory();
    }
    
    public static void updateStatus(String itemID, String supplierID, String updatedStatus){
        ArrayList<Item> itemList = FileAccess.ReadFromTextFile(Item.class);
        ArrayList<Item> updatedList = new ArrayList<>();
        
        for(Item item : itemList){
            if(item.getItemID().equals(itemID) && item.getSupplierID().equals(supplierID)){
                item.setStatus(updatedStatus);
            }
            updatedList.add(item);
        }
        FileAccess.UpdateTextFile(updatedList,Item.class);   
    }
    
    //validate same supplierID, itemID
    public static void addNewItemSupplier(){
        Scanner Sc = new Scanner(System.in);
        
        Stock.displayStockDetails();
        System.out.println("\n~ ~ ~ A D D   N E W   I T E M   S U P P L I E R ~ ~ ~\n");
        System.out.print("Please enter the item ID to add new supplier \t: I");
        String itemID = "I" + Sc.nextLine();
        
        ArrayList<Item> itemList = FileAccess.ReadFromTextFile(Item.class);
        ArrayList<Stock> stockList = FileAccess.ReadFromTextFile(Stock.class);
        ArrayList<Stock> filteredList = new ArrayList<>();
        
        for(Stock stock : stockList){
            if(stock.getItemID().equals(itemID)){
                filteredList.add(stock);
            }
        }
        
        if (!filteredList.isEmpty()){
            Supplier.displaySupplierDetails(true);
            System.out.print("\nPlease enter a supplier ID \t\t\t: S-");
            String supplierID = "S-" + Sc.nextLine();

            if(Supplier.getSupplierDetails(supplierID) != null){
                if(getItemDetails(itemID,supplierID) == null){
                    double cost = 0.0;
                    boolean validCost = false;
                    while (!validCost) {
                        System.out.print("Please enter the item's cost from the supplier \t: RM ");
                        String costInput = Sc.nextLine();
                        try {
                            cost = Double.parseDouble(costInput);
                            validCost = true;
                        } catch (NumberFormatException e) {
                            System.out.println("(SYSTEM) Invalid input for cost. Please enter a valid number.\n");
                        }
                    }

                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(FileAccess.getFileName(Item.class),true));

                        Item newItem = new Item(itemID,supplierID,cost,Info.Status.Active.name());
                        writer.write(newItem.getItemID() + separator + newItem.getSupplierID() + separator + newItem.getCost() + separator + newItem.getStatus() + "\n");

                        System.out.println("\n(SYSTEM) Item successfully added!");
                        ActionHistory action = new ActionHistory(User.loggedInUser.getID(),"Added new supplier " + newItem.SupplierID + " to item " + newItem.ItemID + ".");
                        action.recordActionHistory();
                        writer.close();

                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }

                } else{
                    System.out.println("\n(SYSTEM) Item from the supplier already exists. Please try again!");
                }
            } else{
                System.out.println("\n(SYSTEM) Supplier ID does not exist. Please try again!");
            }
            
        } else{
            System.out.println("\n(SYSTEM) Item not found. Please try again!\n");
        }
    }
    
    public static String getItemCategory(){
        Scanner Sc = new Scanner(System.in);

        String Category = "X";
        boolean isValidInput = false;
        
        do{
            System.out.println("\n~ ~ C A T E G O R I E S ~ ~ \n");
        
            int count = 0;
            for (category info : EnumSet.allOf(category.class)) {
                count++;
                System.out.println(count + ". " + info);
            } 
            System.out.print("\nPlease enter a category number \t\t\t: ");
            try{
                int selection = Integer.parseInt(Sc.nextLine());
                Category = category.values()[selection - 1].name();
                isValidInput = true;
            } catch(Exception ex){
                System.out.println("\n(SYSTEM) Invalid category selection! Please try again.");
            }
        } while(!isValidInput);
        
        return Category;
    }
   
    public static Item getItemDetails(String itemID, String supplierID){
        
        ArrayList<Item> itemList = FileAccess.ReadFromTextFile(Item.class);
        
        for(Item item : itemList){
            if(item.getItemID().equals(itemID) && item.getSupplierID().equals(supplierID)){
                return item;
            }
        }
        return null;  
    }

    /*
    //Necessary? - replaced by displayItemsDetails
    public static void displaySupplierItems(){
              
        ArrayList<Item> itemList = FileAccess.ReadFromTextFile(Item.class);
        ArrayList<Stock> stockList = FileAccess.ReadFromTextFile(Stock.class);
        ArrayList<Supplier> supplierList = FileAccess.ReadFromTextFile(Supplier.class);
        ArrayList<Item> filteredList = new ArrayList<>();
        
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        
        String [] header = {"Item ID","Category","Brand","Name","Specifications","Cost","Status"};
        int[] spacing = {10,20,15,20,20,12,15};
        
        for(Supplier supplier : supplierList){
            System.out.println("\n~ ~ ~ S U P P L I E R   I T E M S   L I S T ~ ~ ~\n");
            System.out.print("Supplier ID \t: " + supplier.getSupplierID());
            System.out.print("\t\tName \t\t: " + supplier.getName());
            System.out.print("\nAddress \t: " + supplier.getAddress());
            System.out.print("\tDelivery Fee \t: RM " + supplier.getDeliveryFee());
            System.out.println("\t\tStatus \t: " + supplier.getStatus() + "\n");
            
            Info.generateTable(header,spacing,true);
            filteredList.clear();
            
            for(Item item : itemList){
                if(item.getSupplierID().equals(supplier.getSupplierID())){
                    filteredList.add(item);
                }
            }
            
            if(!filteredList.isEmpty()){
                for(Item item : filteredList){
                    for(Stock stock : stockList){
                        if(item.getItemID().equals(stock.getItemID())){
                            String[] data = {item.getItemID(),stock.getCategory(),stock.getBrand(),stock.getName(),stock.getSpecification(),"RM " + df.format(item.getCost()),item.getStatus()};
                            Info.generateTable(data,spacing,false);
                        }   
                    }
                }       
            } else{
                System.out.println("\n(SYSTEM) No item record found.\n\n");
            }  
        }      
    }
    */
      
}
