
package javapomsystem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class User implements DataOperations{
       
    private String UserID;
    private String Password;
    private String Name;
    private String IC;
    private String Email;
    private String ContactNumber;
    private Info.Status UserStatus;
    
    private static String separator = ",";
    
    public static User loggedInUser;
 
    // For general purposes
    public User(String userID, String password, String name, String ic, String email, String contactNumber){
        UserID = userID;
        Password = password;
        Name = name;
        IC = ic;
        Email = email;
        ContactNumber = contactNumber;
        UserStatus = Info.Status.Active;
    }
    
    // For edit / delete purposes
    public User(String userID, String password, String name, String ic, String email, String contactNumber, String status){
        UserID = userID;
        Password = password;
        Name = name;
        IC = ic;
        Email = email;
        ContactNumber = contactNumber;
        UserStatus = Info.Status.valueOf(status);
    }
    public String toString(){ return loggedInUser.toString();}
    
    public String getID(){
        return UserID;
    }
    
    public String getName(){
        return Name;
    }
    
    public String getPassword(){
        return Password;
    }
    
    public String getIC(){
        return IC;
    }
    
    public String getEmail(){
        return Email;
    }
    
    public String getContactNumber(){
        return ContactNumber;
    }
    
    public String getStatus(){
        return UserStatus.name();
    }
    
    public void setPassword(String password){
        Password = password;
    }

    public void setName(String name){
        Name = name;
    }
    
    public void setIC(String ic){
        IC = ic;
    }
    
    public void setEmail(String email){
        Email = email;
    }
    
    public void setContactNumber(String contactNumber){
        ContactNumber = contactNumber;
    }
    
    
    public void setStatus(String status){
        UserStatus = Info.Status.valueOf(status);
    }
    
    public static String Login(){
        Scanner Sc = new Scanner(System.in);
        
        System.out.println("\n~ ~ ~ L O G I N ~ ~ ~");
        System.out.print("\nPlease enter your user ID\t\t: ");
        String ID = Sc.nextLine();
        System.out.print("Please enter your password\t\t: ");
        String password = Sc.nextLine();
        
        ArrayList<User> userList = FileAccess.ReadFromTextFile(User.class);
        boolean flag = false;
        
        for(User user : userList){
            if(user.getID().equals(ID)){
                flag = true;
                if(user.getPassword().equals(password)){
                    if(user.getStatus().equals(Info.Status.Active.name())){
                        ActionHistory action = new ActionHistory(ID,"Logged into the system.");
                        action.recordActionHistory();
                        return ID;
                    } else{
                        System.out.println("\nAccount status is not active. Please contact system administrators for further information.");
                    } 
                } else{
                    System.out.println("\n(SYSTEM) Incorrect Password. Please try again!\n");
                }
            }
        }
        
        if(!flag){
            System.out.println("\n(SYSTEM) Username not found. Please try again!\n");
        }
        
        return null;

    }
       
    @Override
    public void addEntry() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FileAccess.getFileName(User.class),true));
            writer.write(UserID + separator + Password + separator + Name + separator + IC + separator + Email + separator + ContactNumber + separator + UserStatus + "\n");
            writer.close();
            System.out.println("\n(SYSTEM) User " + getID() +" Successfully Registered!");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }   
        ActionHistory action = new ActionHistory(User.loggedInUser.getID(),"Registered new user " + getID() + ".");
        action.recordActionHistory();
    }
      
    @Override
    public void editRecord(){
        ArrayList<User> userList = FileAccess.ReadFromTextFile(User.class);
        ArrayList<User> updatedList = new ArrayList<>();
        
        for(User user : userList){
            if(user.getID().equals(getID())){
                user.setPassword(getPassword());
                user.setName(getName());
                user.setIC(getIC());
                user.setEmail(getEmail());
                user.setContactNumber(getContactNumber());
            }
            updatedList.add(user);
        }
        FileAccess.UpdateTextFile(updatedList,User.class);
        ActionHistory action = new ActionHistory(User.loggedInUser.getID(),"Edited user details for " + getID() + ".");
        action.recordActionHistory();
    }
    
    public static void updateStatus(String userID, String updatedStatus){
        ArrayList<User> userList = FileAccess.ReadFromTextFile(User.class);
        ArrayList<User> updatedList = new ArrayList<>();
        
        for(User user : userList){
            if(user.getID().equals(userID)){
                user.setStatus(updatedStatus);
            }
            updatedList.add(user);
        }
        FileAccess.UpdateTextFile(updatedList,User.class);   
    }
    
    // to String?
    public static void viewOwnProfile(User user){
        System.out.println("\n~ ~ ~ P E R S O N A L   D E T A I L S ~ ~ ~\n");
        System.out.println("UserID \t\t: " + user.getID());
        System.out.println("Password \t: " + user.getPassword());
        System.out.println("Name \t\t: " + user.getName());
        System.out.println("IC \t\t: " + user.getIC());
        System.out.println("Email \t\t: " + user.getEmail());
        System.out.println("Contact Number \t: " + user.getContactNumber());
        
        ActionHistory action = new ActionHistory(loggedInUser.getID(),"Viewed own profile."); 
        action.recordActionHistory();
    }
    
    public static User getUserDetails(String userID){
        ArrayList<User> userList = FileAccess.ReadFromTextFile(User.class);
        
        for(User user : userList){
            if(user.getID().equals(userID)){
                return user;
            }
        }
        return null;  
    }

}
