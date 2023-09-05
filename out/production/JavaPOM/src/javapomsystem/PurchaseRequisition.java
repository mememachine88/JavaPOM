
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
import java.text.SimpleDateFormat;
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
    private ArrayList<Stock> itemList; //consists of itemid,brand and item name and cost stores it as individual object
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

    public void setStockList(ArrayList<Stock> itemList) {
        this.itemList = itemList;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<Stock> getItemList() {
        return itemList;
    }

    public PurchaseRequisition(String prID, Supplier supplier, ArrayList<Stock> item, String date, String status) {
        this.prID = prID;
        this.supplier = supplier;
        this.date = date;
        this.itemList = item;
        this.status = status;
    }

    public static void displayLowStockItems() {
        ArrayList<Stock> stockList = FileAccess.ReadFromTextFile(Stock.class);

        Collections.sort(stockList, Comparator.comparing(Stock::getItemID));

        System.out.println("\n~ ~ L O W   S T O C K   I T E M   D E T A I L S ~ ~\n");
        String[] header = {"Item ID", "Category", "Brand", "Name", "Specification", "Price", "Quantity", "Reorder Level", "Status"};
        int[] spacing = {10, 20, 15, 20, 20, 12, 10, 15, 15};
        Info.generateTable(header, spacing, true);

        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);

        for (Stock stock : stockList) {
            if (stock.getReorderLevel() >= stock.getQuantity()) {
                String[] data = {stock.getItemID(), stock.getCategory(), stock.getBrand(), stock.getName(), stock.getSpecification(), df.format(stock.getSellingPrice()),
                        Integer.toString(stock.getQuantity()), Integer.toString(stock.getReorderLevel()), stock.getStatus()};
                Info.generateTable(data, spacing, false);
            }

        }
    }

    public static void displaySuppliers(String itemID, int quantity) {
        ArrayList<Item> itemList = FileAccess.ReadFromTextFile(Item.class);
        ArrayList<Stock> stockList = FileAccess.ReadFromTextFile(Stock.class);
        ArrayList<Supplier> supplierList = FileAccess.ReadFromTextFile(Supplier.class);
        ArrayList<Item> filteredList = new ArrayList<>();

        for (Item item : itemList) {
            if (item.getItemID().equals(itemID)) {
                filteredList.add(item);
            }
        }

        if (!filteredList.isEmpty()) {
            for (Stock stock : stockList) {
                if (stock.getItemID().equals(itemID)) {
                    System.out.println("\n~ ~ I T E M   D E T A I L S ~ ~\n");
                    System.out.println("Item ID \t\t: " + stock.getItemID());
                    System.out.println("Category \t\t: " + stock.getCategory());
                    System.out.println("Brand \t\t\t: " + stock.getBrand());
                    System.out.println("Name \t\t\t: " + stock.getName());
                    System.out.println("Specifications \t\t: " + stock.getSpecification());
                }
            }
            System.out.println("\n~ ~ S U P P L I E R S   D E T A I L S ~ ~\n");
            String[] header = {"Supplier ID", "Name", "Address", "Delivery Fee", "Cost", "Order Quantity", "Total Amount"};
            int[] spacing = {15, 25, 20, 15, 10, 15, 15};
            Info.generateTable(header, spacing, true);

            DecimalFormat df = new DecimalFormat("0.00");
            df.setMaximumFractionDigits(2);

            for (Item item : filteredList) {

                for (Supplier supplier : supplierList) {
                    if (supplier.getSupplierID().equals(item.getSupplierID()) &&
                            supplier.getStatus().equals(Info.Status.Active.name())) {
                        double totalAmount = item.getCost() + quantity + supplier.getDeliveryFee();
                        String[] data = {supplier.getSupplierID(), supplier.getName(), supplier.getAddress(), "RM "
                                + df.format(supplier.getDeliveryFee()), "RM " + df.format(item.getCost()), Integer.toString(quantity), "RM " + df.format(totalAmount)};
                        Info.generateTable(data, spacing, false);
                    }
                }
            }
            System.out.println("");

        } else {
            System.out.println("\n(SYSTEM) Item does not exist! Please try again.");
        }

        ActionHistory action = new ActionHistory(User.loggedInUser.getID(), "Viewed list of suppliers for item " + itemID + ".");
        action.recordActionHistory();

    }

    public static void createPurchaseRequisition() {
        Scanner Sc = new Scanner(System.in);
        ArrayList<Item> itemList = FileAccess.ReadFromTextFile(Item.class);
        ArrayList<Stock> prItemList = new ArrayList<>();
        ArrayList<Stock> stockList = FileAccess.ReadFromTextFile(Stock.class);
        ArrayList<Supplier> supplierList = FileAccess.ReadFromTextFile(Supplier.class);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);

        boolean flag = false;
        boolean addItem = false;
        boolean isSupplierFound = false;
        String supplierID = null;
        String choice = "Y";

        displayLowStockItems();
        do{
            if(isSupplierFound){
                Item.displaySpecificSupplierItems(supplierID);
            }

            System.out.print("Please enter the item that you wish to restock: I");
            String itemID = "I" + Sc.nextLine();

            for (Stock stock : stockList) {
                if (stock.getItemID().equals(itemID) && stock.getStatus().equals("Active")) {
                    flag = true;
                }
            }
            if(!flag){
                System.out.println("\n(SYSTEM) Item not found or Item is Inactive");
                break;
            }

            boolean validQuantity = false;
            int quantity = 0;
            while (!validQuantity) {
                System.out.print("Please enter the amount you wish to purchase:\t");
                String inputQuantity = Sc.nextLine();
                try {
                    quantity = Integer.parseInt(inputQuantity);
                    validQuantity = true;
                } catch (NumberFormatException e) {
                    System.out.println("(SYSTEM) Invalid input for cost. Please enter a valid number.\n");
                }
            }
            displaySuppliers(itemID, quantity);
            if (!addItem) {
                System.out.print("Please Enter Supplier ID:\t S-");
                supplierID = "S-" + Sc.nextLine();
                addItem = true;
            }
            for (Item item : itemList) {
                if (item.getSupplierID().equals(supplierID) && item.getStatus().equals("Active")
                        && item.getItemID().equals(itemID)) {
                    isSupplierFound = true;
                    Stock stockDetails = Stock.getStockDetails(itemID);
                    System.out.println(quantity);
                    prItemList.add(new Stock(item.getItemID(), quantity, item.getCost()));
                }
            }
            System.out.println("Do you wish to add new item? Y/N");
            choice = Sc.nextLine();

        } while(choice.equalsIgnoreCase("Y"));

        if (!isSupplierFound) {
            System.out.println("\n(SYSTEM) Supplier not found or Supplier is Inactive!");

        } else {
            Supplier supplierDetails = Supplier.getSupplierDetails(supplierID);
            PurchaseRequisition purchaseRequisition =
                    new PurchaseRequisition(Info.generateNewID("PurchaseRequisition"), supplierDetails,
                            prItemList, date, Info.Status.Pending.name());

            String seperator = ",";
            String line = purchaseRequisition.GetPRID() + seperator + User.loggedInUser.getID() +
                    seperator + purchaseRequisition.supplier.getSupplierID() + seperator +
                    Double.toString(purchaseRequisition.supplier.getDeliveryFee()) + seperator +
                    purchaseRequisition.date + seperator +
                    purchaseRequisition.getItemList() + seperator + purchaseRequisition.status;
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
    }
}
