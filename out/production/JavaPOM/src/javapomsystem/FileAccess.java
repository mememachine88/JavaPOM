
package javapomsystem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Scanner;


public class FileAccess {

    public static enum FileName {
        user,
        itemList,
        stock,
        supplier,
        dailySales,
        actionHistory,
        purchaseRequisition,
        purchaseOrder,
    }

    //Reads file and adds items into a list
    public static ArrayList<String> ReadFile(String fileName) {
        ArrayList<String> result = new ArrayList<>();
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                result.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
        return result;
    }


    public static <T> ArrayList<T> ReadFromTextFile(Class<T> objectType) {

        ArrayList<String> contentString = FileAccess.ReadFile(getFileName(objectType));
        ArrayList<T> objectList = new ArrayList<>();

        for (String line : contentString) {
            String[] info = line.split(",");
            try {
                switch (objectType.getSimpleName()) {
                    case "User" ->
                            objectList.add(objectType.getConstructor(String.class, String.class, String.class, String.class, String.class, String.class, String.class)
                                    .newInstance(info[0], info[1], info[2], info[3], info[4], info[5], info[6]));
                    case "Item" ->
                            objectList.add(objectType.getConstructor(String.class, String.class, double.class, String.class)
                                    .newInstance(info[0], info[1], Double.parseDouble(info[2]), info[3]));
                    case "Stock" ->
                            objectList.add(objectType.getConstructor(String.class, String.class, String.class, String.class, String.class, String.class, double.class, int.class, int.class, String.class)
                                    .newInstance(info[0], info[1], info[2], info[3], info[4], info[5], Double.parseDouble(info[6]), Integer.parseInt(info[7]), Integer.parseInt(info[8]), info[9]));
                    case "Supplier" ->
                            objectList.add(objectType.getConstructor(String.class, String.class, String.class, double.class, String.class)
                                    .newInstance(info[0], info[1], info[2], Double.parseDouble(info[3]), info[4]));
                    case "dailySales" ->
                            objectList.add(objectType.getConstructor(String.class, String.class, int.class, double.class)
                                    .newInstance(info[0], info[1], Integer.parseInt(info[2]), Double.parseDouble(info[3])));
                    case "ActionHistory" ->
                            objectList.add(objectType.getConstructor(String.class, String.class, String.class)
                                    .newInstance(info[0], info[1], info[2]));
                    default -> {
                    }
                }

            } catch (IllegalAccessException | IllegalArgumentException | InstantiationException |
                     NoSuchMethodException | SecurityException | InvocationTargetException e) {

            }

        }
        return objectList;
    }

    public static void UpdateTextFile(ArrayList<?> objectList, Class<?> objectType) {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getFileName(objectType)));
            for (Object object : objectList) {
                switch (objectType.getSimpleName()) {
                    case "User" -> {
                        User user = (User) object;
                        writer.write(user.getID() + "," + user.getPassword() + "," + user.getName() + "," + user.getIC() + "," +
                                user.getEmail() + "," + user.getContactNumber() + "," + user.getStatus());
                    }
                    case "Stock" -> {
                        Stock stock = (Stock) object;
                        writer.write(stock.getItemID() + "," + stock.getCategory() + "," + stock.getBrand() + "," + stock.getName() + "," +
                                stock.getSpecification() + "," + stock.getExpiryDate() + "," + stock.getSellingPrice() + "," +
                                stock.getQuantity() + "," + stock.getReorderLevel() + "," + stock.getStatus());
                    }
                    case "Item" -> {
                        Item item = (Item) object;
                        writer.write(item.getItemID() + "," + item.getSupplierID() + "," + item.getCost() + "," + item.getStatus());
                    }
                    case "Supplier" -> {
                        Supplier supplier = (Supplier) object;
                        writer.write(supplier.getSupplierID() + "," + supplier.getName() + "," + supplier.getAddress() + "," + supplier.getDeliveryFee() + "," + supplier.getStatus());
                    }
                    case "dailySales" -> {
                        dailySales sales = (dailySales) object;
                        writer.write(sales.getSalesDate() + "," + sales.getItemID() + "," + sales.getQuantity() + "," + sales.getSellingPrice());
                    }
                    case "PurchaseRequisition" -> {
                        PurchaseRequisition purchaseRequisition = (PurchaseRequisition) object;

                    }

                    default -> {
                    }

                }
                writer.newLine();
            }
            System.out.println("\n(SYSTEM) Update Successfully!");
            writer.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static String getFileName(Class<?> objectType) {

        FileName fileName;

        if (objectType == User.class) {
            fileName = FileName.user;
        } else if (objectType == Item.class) {
            fileName = FileName.itemList;
        } else if (objectType == Stock.class) {
            fileName = FileName.stock;
        } else if (objectType == Supplier.class) {
            fileName = FileName.supplier;
        } else if (objectType == dailySales.class) {
            fileName = FileName.dailySales;
        } else if (objectType == ActionHistory.class) {
            fileName = FileName.actionHistory;
        } else if (objectType == PurchaseRequisition.class) {
            fileName = FileName.purchaseRequisition;
        } else if (objectType == PurchaseOrder.class) {
            fileName = FileName.purchaseOrder;
        } else {
            fileName = null;
        }

        return System.getProperty("user.dir") + "\\JavaPOMSystem(NewestVer)"+"\\JavaPOMSystem\\" + fileName + ".txt";
    }

}
