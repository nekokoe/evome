/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.server;


import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.evome.KaKsCalc.client.Task;
import org.evome.KaKsCalc.client.Job;
import org.evome.KaKsCalc.client.Project;
import org.evome.KaKsCalc.client.Calculation;
import java.util.Date;

/**
 *
 * @author nekoko
 */
public class DatabaseManager {
    
    private static DBConnector dbconn = new DBConnector();
    private static SysConfig sysconf = new SysConfig();
    
    public Project getProject(int project_id){
        Project pj = new Project();
        String sql = "SELECT * FROM `project` WHERE id = " + project_id;
        try{
            ResultSet rs = dbconn.execQuery(sql);
            if (rs.next()){
                pj.setId(project_id);
                pj.setName(rs.getString("name"));
                pj.setOwner(rs.getInt("owner"));
                pj.setOwnerText(sql);
            }else{
                return null;    //return null if no this project or else
            }
        }catch(Exception ex){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);            
        }        
    }
    
    public Calculation getCalculation(int calc_id){
        
    }
    
    public Task getTask(int task_id){
        
    }
    
}
