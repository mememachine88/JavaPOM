
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
    private String status;

    public String GetPRID() {
        return prID;
    }

    public void setPRID(String prID) {
        this.prID = prID;
    }

    public void setDate(String Date) {
        this.date = Date;
    }

    public void setStockList(ArrayList<Item> itemList) {
        this.itemList = itemList;
    }
    public ArrayList<Item> getItemList(){return itemList;}

    public PurchaseRequisition(String prID, Supplier supplier, ArrayList<Item> item, String date, String status) {
        this.prID = prID;
        this.supplier = supplier;
        this.date = date;
        this.itemList = item;
        this.status = status;
    }

    public static void displayLowStockItems() {
        ArrayList<Item> itemList = FileAccess.ReadFromTextFile(Item.class);
        ArrayList<Stock> stockList = FileAccess.ReadFromTextFile(Stock.class);

        Collections.sort(itemList, Comparator.comparing(Item::getSupplierID).thenComparing(Item::getItemID));

        System.out.println("\n~ ~ I T E M   D E T A I L S ~ ~\n");
        String[] header = {"Supplier ID", "Item ID", "Category", "Brand", "Name", "Specifications", "Supplier ID",
                "Cost", "Status"};
        int[] spacing = {12, 10, 20, 15, 20, 20, 12, 12, 15};
        Info.generateTable(header, spacing, true);

        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);

        for (Item item : itemList) {
            for (Stock stock : stockList) {
                if (item.getItemID().equals(stock.getItemID()) && stock.getQuantity() <= stock.getReorderLevel()) {
                    String[] data = {item.getSupplierID(), item.getItemID(), stock.getCategory(), stock.getBrand(),
                            stock.getName(), item.getSupplierID(), item.getSpecification(), "RM " +
                            df.format(item.getCost()), item.getStatus()};
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
            String[] data = {item.getItemID(), item.getCategory(), item.getName(), item.getSupplierID(), "RM " +
                    df.format(item.getCost()), item.getSpecification(), item.getStatus()};
            Info.generateTable(data, spacing, false);

        }

    }

    public static void createPurchaseRequisition() {
        displayLowStockItems();
        String itemID = Item.displayItemSuppliers();
        ArrayList<Item> itemList = FileAccess.ReadFromTextFile(Item.class);
        ArrayList<Item> prItemList = new ArrayList<>();
        ArrayList<Stock> stockList = FileAccess.ReadFromTextFile(Stock.class);
        ArrayList<Supplier> supplierList = FileAccess.ReadFromTextFile(Supplier.class);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        boolean flag = false;
        for (Item item : itemList
        ) {
            if (item.getItemID().equals(itemID)) {
                flag = true;
            }
        }
        if (flag) {

            System.out.print("Please Enter Supplier ID:\t S-");
            Scanner Sc = new Scanner(System.in);
            String supplierID = "S-" + Sc.nextLine();
            boolean isSupplierFound = false;
            for (Item item : itemList) {
                if (item.getSupplierID().equals(supplierID) && item.getStatus().equals("Active") && item.getItemID().equals(itemID)) {
                    isSupplierFound = true;
                    System.out.println("Please enter the amount you wish to purchase:\t\t");
                    String quantity = Sc.nextLine();

                    int orderQuantity = Integer.parseInt(quantity);
                    Stock stockDetails = Stock.getStockDetails(itemID);
                    prItemList.add(new Item(item.getItemID(), stockDetails.getName(), stockDetails.getBrand(), stockDetails.getCost(), orderQuantity));

                    break;
                }
            }
            if (isSupplierFound == false) {
                System.out.println("Supplier not found or Supplier is Inactive!");
            }
            Supplier supplierDetails = Supplier.getSupplierDetails(supplierID);
            PurchaseRequisition purchaseRequisition =
                    new PurchaseRequisition(Info.generateNewID("PurchaseRequisition"), supplierDetails,
                            prItemList,date,Info.Status.Pending.name());

            String seperator = ",";
            String line = purchaseRequisition.GetPRID() + seperator + User.loggedInUser.getID() +
                    seperator + purchaseRequisition.supplier.getSupplierID() + seperator +
                    purchaseRequisition.supplier.getName() +
                    seperator + purchaseRequisition.supplier.getAddress() + seperator +
                    Double.toString(purchaseRequisition.supplier.getDeliveryFee()) + seperator +
                    purchaseRequisition.date +seperator+
                    purchaseRequisition.getItemList()+ seperator+purchaseRequisition.status;
            System.out.println(line);
            System.out.println(prItemList);

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(FileAccess.getFileName
                        (PurchaseRequisition.class),
                        true));
                writer.write(line + "\n");
                System.out.println("\n(SYSTEM) Item successfully added!");
                writer.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

//PR001, S0001,S-001,SupplierName, Address, 50, 2023.08.09[I0002#itemName#brand1#30#3.5/]


    }
}
