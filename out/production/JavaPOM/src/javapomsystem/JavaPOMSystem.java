package javapomsystem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

public class JavaPOMSystem {

    public static void main(String[] args) {

        String selection;
        do {
            System.out.println("Welcome to SIGMA SDN BHD (SSB)!");
            System.out.println("""
                    1. Log in
                    2. Testing
                    0. Exit""");

            System.out.print("\nPlease enter a number to continue \t: ");
            Scanner Sc = new Scanner(System.in);
            selection = Sc.nextLine();

            switch (selection) {
                case "1" -> {
                    String userID = User.Login();
                    User.loggedInUser = User.getUserDetails(userID);

                    if (userID != null) {
                        switch (userID.substring(0, 1)) {
                            case "A" -> adminInterface();
                            case "S" -> salesInterface();
                            case "P" -> purchasingInterface();
                            default -> {
                            }
                        }
                    }
                }

                case "2" -> testing();
                case "0" -> {
                }
                default -> System.out.println("\n(SYSTEM) Invalid selection! Please try again!\n");
            }
        } while (!selection.equals("0"));

        System.out.println("\n(SYSTEM) Thanks for visiting our system! Have a nice day!");
    }

    private static void testing() {
        ;
    }


    //--------------------------------------------------------------------ADMIN METHODS--------------------------------------------------------------------//

    private static void adminInterface() {

        String selection;

        do {
            System.out.println("\nHi, " + User.loggedInUser.getName() + "!");
            System.out.println("Welcome to SIGMA SDN BHD (SSB)- Admin Interface!\n");
            System.out.println("""
                    1. Sales Interface 
                    2. Purchasing Interface
                    ---------------------------------
                    3. View user details
                    4. Add new user
                    5. Edit user profile
                    6. Set user status
                    7. View all action history
                    8. View own profile
                    9. Edit own profile
                    0. Log out""");
            System.out.print("\nPlease enter a number to continue \t: ");
            Scanner Sc = new Scanner(System.in);
            selection = Sc.nextLine();

            switch (selection) {
                case "1" -> {
                    ActionHistory action = new ActionHistory(User.loggedInUser.getID(), "Accessing Sales system.");
                    action.recordActionHistory();
                    salesInterface();
                }
                case "2" -> {
                    ActionHistory action = new ActionHistory(User.loggedInUser.getID(), "Accessing Purchasing system.");
                    action.recordActionHistory();
                    purchasingInterface();
                }
                case "3" -> displayUser();
                case "4" -> addNewUser();
                case "5" -> editDetails(false);
                case "6" -> updateStatus();
                case "7" -> viewActionHistory();
                case "8" -> User.viewOwnProfile(User.loggedInUser);
                case "9" -> editDetails(true);
                case "0" -> {
                    System.out.println("");
                    ActionHistory action = new ActionHistory(User.loggedInUser.getID(), "Logged out of the Admin system.");
                    action.recordActionHistory();
                }
                default -> System.out.println("\n(SYSTEM) Invalid selection! Please try again!");
            }
        } while (!selection.equals("0"));

    }

    private static void displayUser() {
        Scanner Sc = new Scanner(System.in);

        System.out.println("\n~ ~ ~ D I S P L A Y   U S E R ~ ~ ~");
        System.out.println("\nPlease select user to display :");
        System.out.println("""
                1. All users
                2. Admin
                3. Sales
                4. Purchasing
                0. Back""");
        System.out.print("\nPlease enter a number to continue \t: ");
        String choice = Sc.nextLine();

        String status = "";
        boolean isValidSelection = true;
        if (choice.equals("1") || choice.equals("2") || choice.equals("3") || choice.equals("4")) {
            System.out.println("\nPlease select user status :");
            System.out.println("""
                    1. All
                    2. Active
                    3. Inactive""");
            System.out.print("\nPlease enter a number to continue \t: ");
            String selection = Sc.nextLine();

            switch (selection) {
                case "1" -> status = "";
                case "2" -> status = "Active";
                case "3" -> status = "Inactive";
                default -> isValidSelection = false;
            }
        }

        if (isValidSelection) {
            switch (choice) {
                case "1" -> Admin.displayUserDetails("", status);
                case "2" -> Admin.displayUserDetails("Admin", status);
                case "3" -> Admin.displayUserDetails("Sales", status);
                case "4" -> Admin.displayUserDetails("Purchasing", status);
                case "0" -> {
                }
                default -> System.out.println("\n(SYSTEM) Invalid user selection! Please try again!");
            }
        } else {
            System.out.println("\n(SYSTEM) Invalid status selection! Please try again!");
        }
    }


    // change add new user + edit to admin / user

    private static void addNewUser() {
        Scanner Sc = new Scanner(System.in);

        System.out.println("\n~ ~ ~ U S E R   R E G I S T R A T I O N ~ ~ ~");
        System.out.println("\nPlease select user to register:");
        System.out.println("""
                1. Admin
                2. Sales
                3. Purchasing
                0. Back""");
        System.out.print("\nPlease enter a number to continue \t: ");
        String choice = Sc.nextLine();

        String role = "X";
        String title = "U S E R";
        switch (choice) {
            case "1" -> {
                role = "Admin";
                title = "A D M I N";
            }
            case "2" -> {
                role = "Sales";
                title = "S A L E S";
            }
            case "3" -> {
                role = "Purchasing";
                title = "P U R C H A S I N G";
            }
            case "0" -> {
            }
            default -> System.out.println("\n(SYSTEM) Invalid selection! Please try again!");
        }

        if (!choice.equals("0") && !role.equals("X")) {

            System.out.println("\n~ ~ " + title + "    R E G I S T R A T I O N ~ ~");
            String id = Info.generateNewID(role);
            System.out.println("\n" + role + " ID\t\t\t\t: " + id);
            System.out.print("Please enter user name \t\t\t: ");
            String name = Sc.nextLine();

            String ic = null;
            String email = null;
            String contactNumber = null;
            boolean isValidIC = false;
            boolean isValidEmail = false;
            boolean isValidContact = false;
            String ICRegex = "^[0-9]{12}$";
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
            String contactNumberRegex = "^(?:\\+?6?01)[0-9]{8}$";
            Pattern ICPattern = Pattern.compile(ICRegex);
            Pattern emailPattern = Pattern.compile(emailRegex);
            Pattern contactNumberPattern = Pattern.compile(contactNumberRegex);

            while (!isValidIC) {
                System.out.print("Please enter user IC (12 digits) \t: ");
                ic = Sc.nextLine();

                if (ICPattern.matcher(ic).matches()) {
                    isValidIC = true;
                } else {
                    System.out.println("(SYSTEM) Invalid identification number! Please enter a 12-digit number.\n");
                }
            }

            while (!isValidEmail) {
                System.out.print("Please enter user email \t\t: ");
                email = Sc.nextLine();

                if (emailPattern.matcher(email).matches()) {
                    isValidEmail = true;
                } else {
                    System.out.println("(SYSTEM) Invalid email address! Please try again.\n");
                }
            }

            while (!isValidContact) {
                System.out.print("Please enter user contact no.\t\t: ");
                contactNumber = Sc.nextLine();

                if (contactNumberPattern.matcher(contactNumber).matches()) {
                    isValidContact = true;
                } else {
                    System.out.println("(SYSTEM) Invalid contact number! Please try again.\n");
                }
            }


            String password = Admin.generatePassword(id, ic);
            User newUser = new User(id, password, name, ic, email, contactNumber);
            newUser.addEntry();
        }
    }

    private static void editDetails(boolean isPersonal) {
        Scanner Sc = new Scanner(System.in);

        String userID;
        System.out.println("\nWelcome to SIGMA SDN BHD (SSB)- Edit Details Interface!\n");

        if (isPersonal) {
            System.out.print("\nUser ID \t\t\t\t: " + User.loggedInUser.getID() + "\n");
            userID = User.loggedInUser.getID();
        } else {
            Admin.displayUserDetails("", "Active");
            System.out.print("\nPlease enter user ID to edit\t\t: ");
            userID = Sc.nextLine();
        }

        User editUser = User.getUserDetails(userID);

        if (editUser != null) {
            if (editUser.getStatus().equals(Info.Status.Active.name())) {

                String password = null;
                String ic = null;
                String email = null;
                String contactNumber = null;
                boolean isValidPassword = false;
                boolean isValidIC = false;
                boolean isValidEmail = false;
                boolean isValidContact = false;
                String passwordRegex = "^(?=.*[A-Z])(?=.*\\d).{8,}$";
                String ICRegex = "^[0-9]{12}$";
                String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
                String contactNumberRegex = "^(?:\\+?6?01)[0-9]{8}$";
                Pattern passwordPattern = Pattern.compile(passwordRegex);
                Pattern ICPattern = Pattern.compile(ICRegex);
                Pattern emailPattern = Pattern.compile(emailRegex);
                Pattern contactNumberPattern = Pattern.compile(contactNumberRegex);

                while (!isValidPassword) {
                    System.out.print("Please enter user password \t\t: ");
                    password = Sc.nextLine();

                    if (!password.isEmpty()) {
                        if (passwordPattern.matcher(password).matches()) {
                            isValidPassword = true;
                        } else {
                            System.out.println("(SYSTEM) Invalid password. \nPassword must be at least 8 characters long and contain at least one uppercase letter and one digit.\n");
                        }
                    } else {
                        isValidPassword = true;
                        password = editUser.getPassword();
                    }
                }

                System.out.print("Please enter new name \t\t\t: ");
                String name = Sc.nextLine();
                if (name.isEmpty()) {
                    name = editUser.getName();
                }

                while (!isValidIC) {
                    System.out.print("Please enter user IC (12 digits) \t: ");
                    ic = Sc.nextLine();

                    if (!ic.isEmpty()) {
                        if (ICPattern.matcher(ic).matches()) {
                            isValidIC = true;
                        } else {
                            System.out.println("(SYSTEM) Invalid identification number! Please enter a 12-digit number.\n");
                        }
                    } else {
                        isValidIC = true;
                        ic = editUser.getIC();
                    }
                }

                while (!isValidEmail) {
                    System.out.print("Please enter user email \t\t: ");
                    email = Sc.nextLine();

                    if (!email.isEmpty()) {
                        if (emailPattern.matcher(email).matches()) {
                            isValidEmail = true;
                        } else {
                            System.out.println("(SYSTEM) Invalid email address! Please try again.\n");
                        }
                    } else {
                        isValidEmail = true;
                        email = editUser.getEmail();
                    }
                }

                while (!isValidContact) {
                    System.out.print("Please enter user contact no.\t\t: ");
                    contactNumber = Sc.nextLine();

                    if (!contactNumber.isEmpty()) {
                        if (contactNumberPattern.matcher(contactNumber).matches()) {
                            isValidContact = true;
                        } else {
                            System.out.println("(SYSTEM) Invalid contact number! Please try again.\n");
                        }
                    } else {
                        isValidContact = true;
                        contactNumber = editUser.getContactNumber();
                    }
                }

                User user = new User(userID, password, name, ic, email, contactNumber);
                user.editRecord();

                if (User.loggedInUser.getID().equals(userID)) {
                    User.loggedInUser = user;
                }

            } else {
                System.out.println("\n(SYSTEM) Account status is not active. Please try again!");
            }
        } else {
            System.out.println("\n(SYSTEM) Account not found. Please try again!");
        }

    }

    private static void updateStatus() {
        Scanner Sc = new Scanner(System.in);

        System.out.println("\n~ ~ ~ U S E R   S T A T U S ~ ~ ~");
        System.out.println("\nPlease select user to edit status :");
        System.out.println("""
                1. Admin
                2. Sales
                3. Purchasing
                0. Back""");
        System.out.print("\nPlease enter a number to continue \t: ");
        String choice = Sc.nextLine();

        switch (choice) {
            case "1" -> editStatus("Admin");
            case "2" -> editStatus("Sales");
            case "3" -> editStatus("Purchasing");
            case "0" -> {
            }
            default -> System.out.println("\n(SYSTEM) Invalid selection! Please try again!");
        }
    }

    private static void viewActionHistory() {

        Scanner Sc = new Scanner(System.in);

        System.out.println("\n~ ~ ~ V I E W   A C T I O N   H I S T O R Y ~ ~ ~");
        System.out.println("\nPlease select action history to display:");
        System.out.println("""
                1. All action history
                2. Current date
                3. Filter by year
                4. Filter by month
                5. Filter by date
                6. Filter by user ID
                0. Back""");
        System.out.print("\nPlease enter a number to continue \t: ");
        String choice = Sc.nextLine();

        String filter = "";
        boolean isValidChoice = true;

        switch (choice) {
            case "1" -> filter = "";
            case "2" -> filter = Info.promptDateFilter("Today");
            case "3" -> filter = Info.promptDateFilter("Year");
            case "4" -> filter = Info.promptDateFilter("Month");
            case "5" -> filter = Info.promptDateFilter("Date");
            case "6" -> {
                System.out.print("\nPlease enter an user ID \t\t: ");
                filter = Sc.nextLine();
            }
            case "0" -> isValidChoice = false;
            default -> {
                isValidChoice = false;
                System.out.println("\n(SYSTEM) Invalid selection! Please try again!");
            }
        }

        if (isValidChoice) {
            ActionHistory.displayActionHistory(filter);
        }

    }


    //--------------------------------------------------------------------ADMIN METHODS--------------------------------------------------------------------//


    //--------------------------------------------------------------------SALES METHODS--------------------------------------------------------------------//

    private static void salesInterface() {

        String selection;

        do {
            System.out.println("\nHi, " + User.loggedInUser.getName() + "!");
            System.out.println("Welcome to SIGMA SDN BHD (SSB)- Sales Interface!\n");
            System.out.println("""
                    1. Item entry
                    2. Supplier entry
                    3. Daily item-wise sales entry 
                    x. (Pending) Summary of specific item
                    4. (Pending) Check items low on stock and requires purchase requisition(PR) 
                    5. (Pending) Create purchase requisition(PR)
                    6. (Pending) Display purchase requisitions(PR)
                    7. (Pending) List of purchase orders(PO)
                    8. View own profile
                    9. Edit own profile
                    0. Log out""");
            System.out.print("\nPlease enter a number to continue \t: ");
            Scanner Sc = new Scanner(System.in);
            selection = Sc.nextLine();

            switch (selection) {
                case "1" -> itemEntry();
                case "2" -> supplierEntry();
                case "3" -> dailySalesEntry();
                case "4" -> PurchaseRequisition.displayLowStockItems();
                case "5" -> PurchaseRequisition.createPurchaseRequisition();
                case "6" -> {
                }
                case "7" -> {
                }
                case "8" -> User.viewOwnProfile(User.loggedInUser);
                case "9" -> editDetails(true);
                case "0" -> {
                    System.out.println("");
                    ActionHistory action = new ActionHistory(User.loggedInUser.getID(), "Logged out of the Sales system.");
                    action.recordActionHistory();
                }
                default -> System.out.println("\n(SYSTEM) Invalid selection! Please try again!");
            }
        } while (!selection.equals("0"));

    }


    private static void itemEntry() {

        Scanner Sc = new Scanner(System.in);

        System.out.println("\n~ ~ ~ I T E M   E N T R Y ~ ~ ~\n");
        System.out.println("""
                1. View all items
                2. View all stocks
                3. Add new item
                4. Add new supplier to existing item
                5. Update item cost
                6. Edit stock details
                7. Set item status
                8. Set stock status
                0. Back""");
        System.out.print("\nPlease enter a number to continue: ");
        String choice = Sc.nextLine();

        switch (choice) {
            case "1" -> Item.displayItemsDetails();
            case "2" -> Stock.displayStockDetails();
            case "3" -> addNewItem();
            case "4" -> Item.addNewItemSupplier();
            case "5" -> updateItemCost();
            case "6" -> editStockDetails();
            case "7" -> editStatus("Item");
            case "8" -> editStatus("Stock");
            case "0" -> {
            }
            default -> System.out.println("\n(SYSTEM) Invalid selection! Please try again!");
        }
    }

    //Prompt Category, Brand, Name, Specs, SupplierID, Cost, Selling Price, ROL. ( expiry date, quantity null )
    private static void addNewItem() {
        Scanner Sc = new Scanner(System.in);

        String category = Item.getItemCategory();
        System.out.print("Please enter the brand of item \t\t\t: ");
        String brand = Sc.nextLine();
        System.out.print("Please enter the item name \t\t\t: ");
        String name = Sc.nextLine();
        System.out.print("Please enter the item specification \t\t: ");
        String specification = Sc.nextLine();

        boolean itemExists = false;
        ArrayList<Stock> stockList = FileAccess.ReadFromTextFile(Stock.class);
        for (Stock stock : stockList) {
            if (stock.getCategory().equals(category) && stock.getBrand().equals(brand)
                    && stock.getName().equals(name) && stock.getSpecification().equals(specification)) {
                itemExists = true;
                System.out.println("\n(SYSTEM) Item already exist as " + stock.getItemID() + ". Please try again!");
                break;
            }
        }

        if (!itemExists) {
            Supplier.displaySupplierDetails(true);
            System.out.print("\nPlease enter a supplier ID \t\t\t: S-");
            String supplierID = "S-" + Sc.nextLine();

            if (Supplier.getSupplierDetails(supplierID) != null) {
                double cost = 0.0;
                boolean validCost = false;
                while (!validCost) {
                    System.out.print("Please enter the item cost \t\t\t: RM ");
                    String costInput = Sc.nextLine();
                    try {
                        cost = Double.parseDouble(costInput);
                        validCost = true;
                    } catch (NumberFormatException e) {
                        System.out.println("(SYSTEM) Invalid input for cost. Please enter a valid number.\n");
                    }
                }

                double price = 0.0;
                boolean validPrice = false;
                while (!validPrice) {
                    System.out.print("Please enter the item selling price \t\t: RM ");
                    String priceInput = Sc.nextLine();
                    try {
                        price = Double.parseDouble(priceInput);
                        validPrice = true;
                    } catch (NumberFormatException e) {
                        System.out.println("(SYSTEM) Invalid input for price. Please enter a valid number.\n");
                    }
                }

                int ROL = 0;
                boolean validROL = false;
                while (!validROL) {
                    System.out.print("Please enter the item reorder level(ROL)\t: ");
                    String ROLInput = Sc.nextLine();
                    try {
                        ROL = Integer.parseInt(ROLInput);
                        validROL = true;
                    } catch (NumberFormatException e) {
                        System.out.println("(SYSTEM) Invalid input for ROL. Please enter a valid number.\n");
                    }
                }

                Stock newItem = new Stock(Info.generateNewID("Item"), category, brand, name, specification, supplierID, cost, "-", price, 0, ROL, "Active", "Active");
                newItem.addEntry();

            } else {
                System.out.println("\n(SYSTEM) Supplier ID does not exist. Please try again!");
            }

        }
    }

    public static void updateItemCost() {
        Scanner Sc = new Scanner(System.in);

        Item.displayItemsDetails();
        System.out.println("\n~ ~ ~ U P D A T E   I T E M   C O S T ~ ~ ~\n");
        System.out.print("Please enter supplier ID of item \t\t\t: S-");
        String supplierID = "S-" + Sc.nextLine();

        Supplier supplierDetails = Supplier.getSupplierDetails(supplierID);
        if (supplierDetails != null) {
            System.out.print("Please enter an item ID from the supplier to edit \t: I");
            String itemID = "I" + Sc.nextLine();

            Item itemDetails = Item.getItemDetails(itemID, supplierID);
            if (itemDetails != null) {
                double cost = 0.0;
                boolean validCost = false;
                while (!validCost) {
                    System.out.print("Please enter the item cost \t\t\t\t: RM ");
                    String costInput = Sc.nextLine();
                    try {
                        cost = Double.parseDouble(costInput);
                        validCost = true;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input for cost. Please enter a valid number.\n");
                    }
                }

                itemDetails.setCost(cost);
                itemDetails.editRecord();

            } else {
                System.out.println("\n(SYSTEM) Item from the supplier is not found. Please try again!");
            }

        } else {
            System.out.println("\n(SYSTEM) Supplier does not exist. Please try again!");
        }
    }


    private static void editStockDetails() {
        Scanner Sc = new Scanner(System.in);

        Stock.displayStockDetails();
        System.out.print("\nPlease enter an item ID to continue \t: I");
        String itemID = "I" + Sc.nextLine();

        Stock oriStock = Stock.getStockDetails(itemID);

        if (oriStock != null && oriStock.getStatus().equals(Info.Status.Active.name())) {
            String category = Item.getItemCategory();

            System.out.print("Please enter the brand of item \t\t\t: ");
            String brand = Sc.nextLine();
            if (brand.isEmpty()) {
                brand = oriStock.getBrand();
            }

            System.out.print("Please enter the item name \t\t\t: ");
            String name = Sc.nextLine();
            if (name.isEmpty()) {
                name = oriStock.getName();
            }

            System.out.print("Please enter the item specification \t\t: ");
            String specification = Sc.nextLine();
            if (specification.isEmpty()) {
                specification = oriStock.getSpecification();
            }

            System.out.print("\nEnter 'y' to edit expiry date of item\t\t: ");
            String updateDate = Sc.nextLine();
            String expiryDate;
            if (updateDate.equals("y") || updateDate.equals("Y")) {
                expiryDate = Info.promptDateFilter("Date");
                if (!expiryDate.equals("X")) {
                    expiryDate = oriStock.getExpiryDate();
                }
            } else {
                expiryDate = oriStock.getExpiryDate();
            }

            double sellingPrice = 0.0;
            boolean validPrice = false;
            while (!validPrice) {
                System.out.print("Please enter the item selling price \t\t: RM ");
                String priceInput = Sc.nextLine();
                if (!priceInput.isEmpty()) {
                    try {
                        sellingPrice = Double.parseDouble(priceInput);
                        validPrice = true;
                    } catch (NumberFormatException e) {
                        System.out.println("(SYSTEM) Invalid input for price. Please enter a valid number.\n");
                    }
                } else {
                    validPrice = true;
                    sellingPrice = oriStock.getSellingPrice();
                }
            }

            int ROL = 0;
            boolean validROL = false;
            while (!validROL) {
                System.out.print("Please enter the item reorder level(ROL) \t: ");
                String ROLInput = Sc.nextLine();
                if (!ROLInput.isEmpty()) {
                    try {
                        ROL = Integer.parseInt(ROLInput);
                        validROL = true;
                    } catch (NumberFormatException e) {
                        System.out.println("(SYSTEM) Invalid input for ROL. Please enter a valid number.\n");
                    }
                } else {
                    validROL = true;
                    ROL = oriStock.getReorderLevel();
                }
            }

            Stock updatedStock = new Stock(itemID, category, brand, name, specification, expiryDate, sellingPrice, ROL);
            updatedStock.editRecord();

        } else {
            System.out.println("\n(SYSTEM) Please enter a valid and active item ID.");
        }


    }


    private static void supplierEntry() {

        Scanner Sc = new Scanner(System.in);

        System.out.println("\n~ ~ ~ S U P P L I E R   E N T R Y ~ ~ ~\n");
        System.out.println("""
                1. View all suppliers
                2. Add new supplier
                3. Edit supplier details
                4. Set supplier status
                0. Back""");
        System.out.print("\nPlease enter a number to continue: ");
        String choice = Sc.nextLine();

        switch (choice) {
            case "1" -> Supplier.displaySupplierDetails();
            case "2" -> addNewSupplier();
            case "3" -> editSupplier();
            case "4" -> editStatus("Supplier");
            case "0" -> {
            }
            default -> System.out.println("\n(SYSTEM) Invalid selection! Please try again!");
        }
    }

    private static void addNewSupplier() {
        Scanner Sc = new Scanner(System.in);

        System.out.println("\n~ ~ A D D   N E W   S U P P L I E R ~ ~ \n");
        String supplierID = Info.generateNewID("Supplier");
        System.out.println("\nSupplier ID\t\t\t\t: " + supplierID);
        System.out.print("Please enter the supplier name \t\t: ");
        String name = Sc.nextLine();
        System.out.print("Please enter the supplier address \t: ");
        String address = Sc.nextLine();

        double deliveryFee = 0.0;
        boolean validFee = false;
        while (!validFee) {
            System.out.print("Please enter the delivery fee \t\t: RM ");
            String deliveryFeeInput = Sc.nextLine();
            try {
                deliveryFee = Double.parseDouble(deliveryFeeInput);
                validFee = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input for delivery fee. Please enter a valid number.");
            }
        }

        Supplier newSupplier = new Supplier(supplierID, name, address, deliveryFee, Info.Status.Active.name());
        newSupplier.addEntry();
    }

    private static void editSupplier() {
        Scanner Sc = new Scanner(System.in);

        Supplier.displaySupplierDetails(true);
        System.out.println("\n~ ~ E D I T   S U P P L I E R ~ ~ \n");
        System.out.print("Please enter the supplier ID to edit \t: S-");
        String supplierID = "S-" + Sc.nextLine();

        Supplier oriSupplier = Supplier.getSupplierDetails(supplierID);

        if (oriSupplier != null && oriSupplier.getStatus().equals(Info.Status.Active.name())) {
            System.out.print("Please enter the supplier name \t\t: ");
            String name = Sc.nextLine();
            if (name.isEmpty()) {
                name = oriSupplier.getName();
            }

            System.out.print("Please enter the supplier address \t: ");
            String address = Sc.nextLine();
            if (address.isEmpty()) {
                address = oriSupplier.getAddress();
            }

            double deliveryFee = 0.0;
            boolean validFee = false;
            while (!validFee) {
                System.out.print("Please enter the delivery fee \t\t: RM ");
                String deliveryFeeInput = Sc.nextLine();
                if (!deliveryFeeInput.isEmpty()) {
                    try {
                        deliveryFee = Double.parseDouble(deliveryFeeInput);
                        validFee = true;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input for delivery fee. Please enter a valid number.");
                    }
                } else {
                    validFee = true;
                    deliveryFee = oriSupplier.getDeliveryFee();
                }
            }

            Supplier updatedSupplier = new Supplier(supplierID, name, address, deliveryFee);
            updatedSupplier.editRecord();

        } else {
            System.out.println("\n(SYSTEM) Please enter a valid and active supplier ID.");
        }

    }


    private static void editStatus(String type) {

        Class objectType = Info.getClass(type);

        if (type.equals("Admin") || type.equals("Sales") || type.equals("Purchasing")) {
            objectType = User.class;
        }

        System.out.println("\n~ ~ ~ E D I T   S T A T U S ~ ~ ~\n");
        System.out.println("""
                1. Set status to Active
                2. Set status to Inactive
                0. Back
                """);
        System.out.print("Please enter a number to continue: ");
        Scanner Sc = new Scanner(System.in);
        String selection = Sc.nextLine();

        boolean isActiveStatus = false;
        boolean changedStatus = true;

        boolean isValidSelection = true;
        switch (selection) {
            case "1" -> {
                isActiveStatus = false;
                changedStatus = true;
            }
            case "2" -> {
                isActiveStatus = true;
                changedStatus = false;
            }
            case "0" -> {
                isValidSelection = false;
            }
            default -> {
                System.out.println("\n(SYSTEM) Invalid selection! Please try again!");
                isValidSelection = false;
            }
        }

        String status = Info.getStatusString(isActiveStatus);
        String updatedStatus = Info.getStatusString(changedStatus);

        if (isValidSelection) {
            if (Info.getStatusCount(objectType, isActiveStatus) > 0) {
                String supplierID = "";
                switch (type) {
                    case "Item" -> Item.displayItemsDetails(isActiveStatus);
                    case "Stock" -> Stock.displayStockDetails(isActiveStatus);
                    case "Supplier" -> Supplier.displaySupplierDetails(isActiveStatus);
                    case "Admin" -> Admin.displayUserDetails("Admin", status);
                    case "Sales" -> Admin.displayUserDetails("Sales", status);
                    case "Purchasing" -> Admin.displayUserDetails("Purchasing", status);
                    default ->
                            System.out.println("\n(SYSTEM) Invalid type for displaying details! Please try again!\n");
                }

                String prefix = type.substring(0, 1);
                if (objectType.equals(Item.class) || objectType.equals(Stock.class)) {
                    prefix = "I";
                } else if (objectType.equals(Stock.class)) {
                    prefix = "S-";
                }

                System.out.print("\nPlease enter an " + objectType.getSimpleName() + " ID to edit status \t: " + prefix);
                String ID = prefix + Sc.nextLine();

                if (type.equals("Item")) {
                    System.out.print("Please enter the supplier ID \t\t: S-");
                    supplierID = "S-" + Sc.nextLine();
                }

                switch (objectType.getSimpleName()) {
                    case "User" -> User.updateStatus(ID, updatedStatus);
                    case "Stock" -> Stock.updateStatus(ID, updatedStatus);
                    case "Item" -> Item.updateStatus(ID, supplierID, updatedStatus);
                    case "Supplier" -> Supplier.updateStatus(ID, updatedStatus);
                    default -> {
                    }
                }
            }
        }
    }


    private static void dailySalesEntry() {

        Scanner Sc = new Scanner(System.in);

        System.out.println("\n~ ~ ~ S U P P L I E R   E N T R Y ~ ~ ~\n");
        System.out.println("""
                1. View daily sales
                2. Add new daily sales
                3. Edit daily sales
                4. Delete daily sales
                0. Back""");
        System.out.print("\nPlease enter a number to continue: ");
        String choice = Sc.nextLine();

        switch (choice) {
            case "1" -> viewDailySales();
            case "2" -> addDailySales();
            case "3" -> editDailySales(false);
            case "4" -> editDailySales(true);
            case "0" -> {
            }
            default -> System.out.println("\n(SYSTEM) Invalid selection! Please try again!");
        }
    }

    private static void viewDailySales() {
        Scanner Sc = new Scanner(System.in);

        System.out.println("\n~ ~ ~ V I E W   D A I L Y   S A L E S ~ ~ ~");
        System.out.println("\nPlease select number to filter sales:");
        System.out.println("""
                1. Display all sales
                2. Current date
                3. Filter by Date
                4. Filter by Month
                5. Filter by Year
                0. Back""");
        System.out.print("\nPlease enter a number to continue \t: ");
        String choice = Sc.nextLine();

        switch (choice) {
            case "1" -> dailySales.displayDailySales(null);
            case "2" -> {
                Date date = new Date();
                SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd");
                dailySales.displayDailySales(ft.format(date));
            }
            case "3" -> dailySales.displayDailySales(Info.promptDateFilter("Date"));
            case "4" -> dailySales.displayDailySales(Info.promptDateFilter("Month"));
            case "5" -> dailySales.displayDailySales(Info.promptDateFilter("Year"));
            case "0" -> {
            }
            default -> System.out.println("\n(SYSTEM) Invalid selection! Please try again!");
        }

    }

    private static void addDailySales() {
        Scanner Sc = new Scanner(System.in);

        System.out.println("\n~ ~ ~ A D D   N E W   D A I L Y   S A L E S ~ ~ ~\n");
        System.out.println("1. Add to current date\n2. Add to specific date\n");
        System.out.print("Please enter a number to continue \t\t: ");
        String selection = Sc.nextLine();

        String date = "";
        SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd");

        boolean isValidSelection = false;
        switch (selection) {
            case "1" -> {
                Date currentDate = new Date();
                date = ft.format(currentDate);
                isValidSelection = true;
            }
            case "2" -> {
                try {
                    date = Info.promptDateFilter("Date");
                    isValidSelection = true;
                } catch (Exception e) {
                    System.out.println("Invalid date input. Please try again!");
                }

            }
            default -> System.out.println("\n(SYSTEM) Invalid selection! Please try again!");
        }

        if (isValidSelection) {
            dailySales.addNewDailySales(date);
        }
    }

    public static void editDailySales(boolean isDelete) {
        Scanner Sc = new Scanner(System.in);

        ArrayList<dailySales> dailySalesList = FileAccess.ReadFromTextFile(dailySales.class);
        ArrayList<dailySales> filteredList = new ArrayList<>();

        System.out.println("\n~ ~ ~ E D I T   D A I L Y   S A L E S ~ ~ ~");
        System.out.println("\nPlease select number to edit sales:");
        System.out.println("""
                1. Current date
                2. Specific date
                0. Back""");
        System.out.print("\nPlease enter a number to continue \t: ");
        String choice = Sc.nextLine();
        boolean isValidSelection = true;

        String date = null;
        switch (choice) {
            case "1" -> {
                SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd");
                date = ft.format(new Date());
            }
            case "2" -> date = Info.promptDateFilter("Date");
            case "0" -> isValidSelection = false;
            default -> {
                isValidSelection = false;
                System.out.println("\n(SYSTEM) Invalid selection! Please try again!");
            }
        }

        if (isValidSelection) {
            for (dailySales sales : dailySalesList) {
                if (sales.getSalesDate().equals(date)) {
                    filteredList.add(sales);
                }
            }

            if (!filteredList.isEmpty()) {
                dailySales.displayDailySales(date);
                System.out.print("\nPlease enter an item ID \t\t: I");
                String itemID = "I" + Sc.nextLine();

                int salesQuantity = 0;
                double price = 0;
                if (!isDelete) {
                    for (dailySales sales : filteredList) {
                        if (sales.getItemID().equals(itemID)) {
                            boolean validQuantity = false;
                            while (!validQuantity) {
                                System.out.print("Please enter the updated sales quantity : ");
                                String salesQuantityInput = Sc.nextLine();
                                try {
                                    salesQuantity = Integer.parseInt(salesQuantityInput);
                                    validQuantity = true;
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid input for quantity. Please enter a valid number.\n");
                                }
                            }

                            boolean validPrice = false;
                            while (!validPrice) {
                                System.out.print("Please enter updated item unit price \t: RM ");
                                String priceInput = Sc.nextLine();
                                try {
                                    price = Double.parseDouble(priceInput);
                                    validPrice = true;
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid input for price. Please enter a valid number.\n");
                                }
                            }
                        }
                    }
                }

                dailySales updatedSales = new dailySales(date, itemID, salesQuantity, price);
                updatedSales.editRecord(isDelete);
            }
        }
    }


    //--------------------------------------------------------------------SALES METHODS--------------------------------------------------------------------//


    //------------------------------------------------------------------PURCHASING METHODS----------------------------------------------------------------//


    private static void purchasingInterface() {

        String selection;

        do {
            System.out.println("\nHi, " + User.loggedInUser.getName() + "!");
            System.out.println("Welcome to SIGMA SDN BHD (SSB)- Purchasing Interface!\n");
            System.out.println("""
                    1. List of stocks
                    2. List of items
                    3. List of items by specific supplier
                    4. List of specific item's suppliers
                    5. List of suppliers
                    6. (Pending) List of purchase requisition(PR)
                    7. (Pending) Generate purchase order(PO)
                    8. (Pending) List of purchase orders(PO)
                    9. View own profile
                    10. Edit own profile
                    0. Log out""");
            System.out.print("\nPlease enter a number to continue \t: ");
            Scanner Sc = new Scanner(System.in);
            selection = Sc.nextLine();

            switch (selection) {
                case "1" -> Stock.displayStockDetails();
                case "2" -> Item.displayItemsDetails();
                case "3" -> Item.displaySpecificSupplierItems(null);
                case "4" -> {
                    Stock.displayStockDetails();
                    System.out.print("\nPlease enter an item ID to continue \t: I");
                    String itemID = "I" + Sc.nextLine();
                    Item.displayItemSuppliers(itemID);
                }
                case "5" -> Supplier.displaySupplierDetails();
                case "6" -> {
                }
                case "7" -> {
                }
                case "8" -> {
                }
                case "9" -> User.viewOwnProfile(User.loggedInUser);
                case "10" -> editDetails(true);
                case "0" -> {
                    System.out.println("");
                    ActionHistory action = new ActionHistory(User.loggedInUser.getID(), "Logged out of the Purchasing system.");
                    action.recordActionHistory();
                }
                default -> System.out.println("\n(SYSTEM) Invalid selection! Please try again!");
            }
        } while (!selection.equals("0"));

    }


}
