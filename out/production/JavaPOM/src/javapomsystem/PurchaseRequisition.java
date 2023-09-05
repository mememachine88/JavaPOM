
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javapomsystem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author hoe
 */
public class PurchaseRequisition {
    private String prID;
    private String salesManagerID;
    private Supplier supplier;//get supplier ID, delivery fee and address
    private String date;
    private ArrayList<Item> itemList; //consists of itemid,brand and item name and cost stores it as individual object
    public String GetPRID() {
        return prID;
    }

    public void SetPRID(String prID) {
        this.prID = prID;
    }

    public void SetDate(String Date) {
        this.date = Date;
    }

    public void setStockList(ArrayList<Item> itemList) {
        this.itemList = itemList;
    }

    public PurchaseRequisition(String prID, Supplier supplier, ArrayList<Item> item, String date) {
        this.prID = prID;
        this.supplier = supplier;
        this.date = date;
        this.itemList = item;
    }

    public static void DisplayLowStockItems() {
        ArrayList<Item> itemList = FileAccess.ReadFromTextFile(Item.class);
        ArrayList<Stock> stockList = FileAccess.ReadFromTextFile(Stock.class);

        Collections.sort(itemList, Comparator.comparing(Item::getSupplierID).thenComparing(Item::getItemID));

        System.out.println("\n~ ~ I T E M   D E T A I L S ~ ~\n");
        String[] header = {"Supplier ID", "Item ID", "Category", "Brand", "Name", "Specifications","Supplier ID", "Cost", "Status"};
        int[] spacing = {12, 10, 20, 15, 20, 20, 12, 12, 15};
        Info.generateTable(header, spacing, true);

        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);

        for (Item item : itemList) {
            for (Stock stock : stockList) {
                if (item.getItemID().equals(stock.getItemID()) && stock.getQuantity() <= stock.getReorderLevel()) {
                    String[] data = {item.getSupplierID(), item.getItemID(), stock.getCategory(), stock.getBrand(), stock.getName(), item.getSupplierID(),item.getSpecification(), "RM " + df.format(item.getCost()), item.getStatus()};
                    Info.generateTable(data, spacing, false);
                }
            }
        }
    }


    public static void DisplayItems() {
        ArrayList<Item> itemList = FileAccess.ReadFromTextFile(Item.class);
        String[] header = {"Item ID", "Category", "Name", "Supplier ID", "Cost", "Specifications", "Status"};
        int[] spacing = {10, 20, 20, 12, 10, 20, 10};
        System.out.println("\n~ ~ I T E M   D E T A I L S ~ ~\n");
        Info.generateTable(header, spacing, true);
        for (Item item : itemList) {
            DecimalFormat df = new DecimalFormat("0.00");
            df.setMaximumFractionDigits(2);
            String[] data = {item.getItemID(), item.getCategory(), item.getName(), item.getSupplierID(), "RM " + df.format(item.getCost()), item.getSpecification(), item.getStatus()};
            Info.generateTable(data, spacing, false);

        }

    }

    public static void CreatePurchaseRequisition(User loggedInUser) {
        DisplayLowStockItems();
        String itemID=Item.displayItemSuppliers();
        ArrayList<Item> itemList = FileAccess.ReadFromTextFile(Item.class);
        ArrayList<Stock> stockList = FileAccess.ReadFromTextFile(Stock.class);
        ArrayList<Supplier> supplierList = FileAccess.ReadFromTextFile(Supplier.class);
        System.out.print("Please Enter Supplier ID:\t S-");
        Scanner Sc = new Scanner(System.in);
        String supplierID = "S-" + Sc.nextLine();
        PurchaseRequisition purchaseRequisition = null;
        boolean isSupplierFound = false;
        for (Item item : itemList) {
            if (item.getSupplierID().equals(supplierID) && item.getStatus().equals("Active")) {
                isSupplierFound = true;
                String supplierName = null;
                double deliveryFee = 0;
                String supplierAddress = null;
                String itemName = null;
                String itemBrand = null;

                for (Supplier supplier : supplierList) {
                    if(supplier.getSupplierID().equals(supplierID)
                    ) {
                        supplierAddress = supplier.getAddress();
                        deliveryFee = supplier.getDeliveryFee();
                        supplierName = supplier.getName();
                    }

                }
                for (Stock stock : stockList
                ){
                    if (itemID.equals(stock.getItemID())
                    ){
                        itemBrand = stock.getBrand();
                        itemName = stock.getName();
                    }
                }
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String date = dtf.format(now);
                System.out.println("Please enter the amount you wish to purchase:\t\t");
                String quantity = Sc.nextLine();

                int orderQuantity = Integer.parseInt(quantity);
                String prID = Info.generateNewID("PurchaseRequisition");
                ArrayList<Item> itemsList = new ArrayList<Item>();
                itemsList.add(new Item(item.getItemID(), itemName, itemBrand, item.getCost(),orderQuantity));
                Supplier supplier1 = new Supplier(supplierID, supplierName, supplierAddress, deliveryFee);
                purchaseRequisition = new PurchaseRequisition(prID, supplier1, itemsList, date);
                break;
            }
        }
        if (isSupplierFound == false) {
            System.out.println("Supplier not found or Supplier is Inactive!");
        }
        String itemLine = "[";
        for (Item item : purchaseRequisition.itemList){
            itemLine+=item.toString();
        }
        itemLine +="]";



        String seperator = ",";
        String line = purchaseRequisition.GetPRID()+seperator+loggedInUser+seperator+purchaseRequisition.supplier.getSupplierID()+seperator+purchaseRequisition.supplier.getName()+seperator+purchaseRequisition.supplier.getAddress()+seperator+Double.toString(purchaseRequisition.supplier.getDeliveryFee())+seperator+ purchaseRequisition.date+ itemLine;

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FileAccess.getFileName(PurchaseRequisition.class),true));
            writer.write(line +"\n");
            System.out.println("\n(SYSTEM) Item successfully added!");
            writer.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

//PR001, S0001,S-001,SupplierName, Address, 50, 2023.08.09[I0002#itemName#brand1#30#3.5/]



    }
}
