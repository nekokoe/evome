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
    
    public static Project getProject(int project_id){
        Project pj = new Project();
        String sql = "SELECT * FROM `project` WHERE id = " + project_id;
        try{
            ResultSet rs = dbconn.execQuery(sql);
            if (rs.next()){
                pj.setId(project_id);
                pj.setName(rs.getString("name"));
                pj.setOwner(AccountManager.getAccount(rs.getInt("owner")));
                pj.setCreateDate(rs.getDate("create"));
                pj.setModifyDate(rs.getDate("modify"));                
            }else{
                pj = null;    //return null if no this project or else
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);            
            pj = null;
        }
        return pj;
    }
    
    public static Calculation getCalculation(int calc_id){
        Calculation calc = new Calculation();
        String sql = "SELECT * FROM `calculation` WHERE id = " + calc_id;
        try{
            ResultSet rs = dbconn.execQuery(sql);
            if (rs.next()){
                calc.setComment(rs.getString("comment"));
                calc.setId(rs.getInt("id"));
                calc.setName(rs.getString("name"));
                calc.setOwner(AccountManager.getAccount(rs.getInt("owner")));
                calc.setProject(DatabaseManager.getProject(rs.getInt("project")));
                calc.setCreateTime(rs.getDate("create"));
                calc.setModifyTime(rs.getDate("modify"));
            }else{
                calc = null;
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            calc = null;
        }
        return calc;
    }
    
    public Task getTask(int task_id){
        
    }
    
}
