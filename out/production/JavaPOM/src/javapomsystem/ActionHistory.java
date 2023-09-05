
package javapomsystem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ActionHistory {
    
    private String Date;
    private String UserID;
    private String ActionDetails;
    
    //Action creation
    public ActionHistory(String userID, String action){
        SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        Date = ft.format(new Date());
        UserID = userID;
        ActionDetails = action;
    }
    
    //Action Reading
    public ActionHistory(String date, String userID, String action){
        Date = date;
        UserID = userID;
        ActionDetails = action;
    }
    
    public String getDate(){
        return Date;
    }
    
    public String getUserID(){
        return UserID;
    }
    
    public String getActionDetails(){
        return ActionDetails;
    }
    
    public void recordActionHistory(){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FileAccess.getFileName(ActionHistory.class),true));
            writer.write(getDate() + "," + getUserID() + "," + getActionDetails() + "\n");
            writer.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }  
    } 
    
    public static void displayActionHistory(String value){
        ArrayList<ActionHistory> actionHistoryList = FileAccess.ReadFromTextFile(ActionHistory.class);
        ArrayList<ActionHistory> filteredList = new ArrayList<>();

        for(ActionHistory action : actionHistoryList){
            if(value.isEmpty() || action.getDate().substring(0,4).equals(value)|| action.getDate().substring(0,7).equals(value) 
                        || action.getDate().substring(0,10).equals(value) || action.getDate().equals(value)){
                filteredList.add(action);
            }
        }
        
        System.out.println("\n~ ~ A C T I O N   H I S T O R Y ~ ~\n");
        String[] header = {"Date & Time", "User ID", "Action"};
        int[] spacing = {25,15,80};
        Info.generateTable(header,spacing,true);
        
        if(!filteredList.isEmpty()){
            for(ActionHistory action : filteredList){
                String[] data = {action.getDate(),action.getUserID(),action.getActionDetails()};
                Info.generateTable(data,spacing,false);
            }
        } else{
            System.out.println("\n(SYSTEM) No record found for the selected action history!");
        }

    }
    
}   
