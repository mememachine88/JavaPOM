
package javapomsystem;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Admin{
 
    public static void displayUserDetails(String role, String status){
        ArrayList<User> userList = FileAccess.ReadFromTextFile(User.class);

        String title;
        switch(role){
            case "Admin" -> title = " A D M I N ";
            case "Sales" -> title = " S A L E S ";
            case "Purchasing" -> title = " P U R C H A S I N G ";
            case "" -> title = " S Y S T E M ";
            default -> title = " E R R O R ";    
        }
        
        if(status.equals(Info.Status.Active.name())){
            System.out.println("\n~ ~ ~ A C T I V E  " + title + "  U S E R S ~ ~ ~\n");
        } else if (status.equals(Info.Status.Inactive.name())){
            System.out.println("\n~ ~ ~ I N A C T I V E  " + title + "  U S E R S ~ ~ ~\n");
        } else{
            System.out.println("\n~ ~ ~ A L L  " + title + "  U S E R S ~ ~ ~\n");
        }

        String [] header = {"User ID","Password","Name","IC","Email","Contact","Status"};
        int[] spacing = {10,15,20,15,30,15,10};
        Info.generateTable(header,spacing,true);
        
        int count = 0;
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        
        for(User user : userList){
            boolean isRoleMatch = role.isEmpty() || user.getID().substring(0, 1).equals(role.substring(0, 1));
            boolean isStatusMatch = status.isEmpty() || user.getStatus().equals(status);

            if (isRoleMatch && isStatusMatch){
                String[] data = {user.getID(),user.getPassword(),user.getName(),user.getIC(),user.getEmail(),user.getContactNumber(),user.getStatus()};
                Info.generateTable(data,spacing,false);
                count++;
            }
        }
        
        if(status.isEmpty()){
            status = "System";
        }
        System.out.println("\nTotal " + status + " " + role + " users: " + count);

        if(role.isEmpty()){
            role = "All users";
        }
        //ActionHistory action = new ActionHistory(User.loggedInUser.getID(),"Viewed list of " + role + " details.");
        //action.recordActionHistory();
    }

    
    public static String generatePassword(String id, String ic){
        String password = id + "@" + ic.substring(2, 6);
        return password;
    }
    
}
