
package javapomsystem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Supplier implements DataOperations {
    
    private String SupplierID;
    private String Name;
    private String Address;
    private double DeliveryFee;
    private Info.Status Status;
    
    private static String separator = "/";
    
    public Supplier(String supplierID, String name, String address, double deliveryFee){
        SupplierID = supplierID;
        Name = name;
        Address = address;
        DeliveryFee = deliveryFee;
    }
    
    public Supplier(String supplierID, String name, String address, double deliveryFee, String status){
        SupplierID = supplierID;
        Name = name;
        Address = address;
        DeliveryFee = deliveryFee;
        Status = Info.Status.valueOf(status);
    }
    
    public String getSupplierID(){
        return SupplierID;
    }
    
    public String getName(){
        return Name;
    }
    
    public String getAddress(){
        return Address;
    }
    
    public double getDeliveryFee(){
        return DeliveryFee;
    }
    
    public String getStatus(){
        return Status.name();
    }
    
    public void setName(String name){
        Name = name;
    }
    
    public void setAddress(String address){
        Address = address;
    }
    
    public void setDeliveryFee(double deliveryFee){
        DeliveryFee = deliveryFee;
    }
    
    public void setStatus(String status){
        Status = Info.Status.valueOf(status);
    }
    
    public static void displaySupplierDetails(){
        ArrayList<Supplier> supplierList = FileAccess.ReadFromTextFile(Supplier.class);
        
        System.out.println("\n~ ~ S U P P L I E R S   D E T A I L S ~ ~\n");
        String [] header = {"Supplier ID","Name","Address","Delivery Fee","Status"};
        int[] spacing = {15,25,20,15,10};
        Info.generateTable(header,spacing,true);
        
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        
        for(Supplier supplier : supplierList){
            String[] data = {supplier.getSupplierID(),supplier.getName(),supplier.getAddress(),"RM " + df.format(supplier.getDeliveryFee()),supplier.getStatus()};
            Info.generateTable(data,spacing,false);
        }
        
        ActionHistory action = new ActionHistory(User.loggedInUser.getID(),"Viewed list of supplier details.");
        action.recordActionHistory();
        
    }
    
    public static void displaySupplierDetails(boolean isActive){
        ArrayList<Supplier> supplierList = FileAccess.ReadFromTextFile(Supplier.class);
        
        System.out.println("\n~ ~ S U P P L I E R S   D E T A I L S ~ ~\n");
        String [] header = {"Supplier ID","Name","Address","Delivery Fee","Status"};
        int[] spacing = {15,25,20,15,10};
        Info.generateTable(header,spacing,true);
        
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
              
        for(Supplier supplier : supplierList){
            if (supplier.getStatus().equals(Info.getStatusString(isActive))){
                String[] data = {supplier.getSupplierID(),supplier.getName(),supplier.getAddress(),"RM " + df.format(supplier.getDeliveryFee()),supplier.getStatus()};
                Info.generateTable(data,spacing,false);
            }
        }
        
    }
    
    @Override
    public void addEntry() {
        
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FileAccess.getFileName(Supplier.class),true));
            writer.write(SupplierID + separator + Name + separator + Address + separator + DeliveryFee + separator + Status + "\n");
            System.out.println("\n(SYSTEM) Supplier successfully added!");
            ActionHistory action = new ActionHistory(User.loggedInUser.getID(),"Added new supplier " + SupplierID + ".");
            action.recordActionHistory();
            writer.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }   
        
        ActionHistory action = new ActionHistory(User.loggedInUser.getID(),"Added new supplier " + getSupplierID() + ".");
        action.recordActionHistory();
    }

    @Override
    public void editRecord(){
        ArrayList<Supplier> supplierList = FileAccess.ReadFromTextFile(Supplier.class);
        ArrayList<Supplier> updatedList = new ArrayList<>();
        
        for(Supplier supplier : supplierList){
            if(supplier.getSupplierID().equals(getSupplierID())){
                supplier.setName(getName());
                supplier.setAddress(getAddress());
                supplier.setDeliveryFee(getDeliveryFee());
            }
            updatedList.add(supplier);
        }
        FileAccess.UpdateTextFile(updatedList,Supplier.class);
        ActionHistory action = new ActionHistory(User.loggedInUser.getID(),"Edited supplier details for supplier " + getSupplierID() + ".");
        action.recordActionHistory();
    }
    
    public static void updateStatus(String supplierID, String updatedStatus){
        ArrayList<Supplier> supplierList = FileAccess.ReadFromTextFile(Supplier.class);
        ArrayList<Supplier> updatedList = new ArrayList<>();
        
        for(Supplier supplier : supplierList){
            if(supplier.getSupplierID().equals(supplierID)){
                supplier.setStatus(updatedStatus);
            }
            updatedList.add(supplier);
        }
        FileAccess.UpdateTextFile(updatedList,Supplier.class);   
    }
    
    public static Supplier getSupplierDetails(String supplierID){
        ArrayList<Supplier> supplierList = FileAccess.ReadFromTextFile(Supplier.class);
        
        for(Supplier supplier : supplierList){
            if(supplier.getSupplierID().equals(supplierID)){
                return supplier;
            }
        }
        return null;  
    }
    
}
