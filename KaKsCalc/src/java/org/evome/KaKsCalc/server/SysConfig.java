/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.server;

/**
 *
 * @author nekoko 2012/9/13
 */

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Iterator;

public class SysConfig extends RemoteServiceServlet{
    public String 
            SERVERLET_REAL_PATH, DATA_ROOT_PATH, WORK_ROOT_PATH, 
            ACCOUNT_DB, ACCOUNT_DB_USER, ACCOUNT_DB_PASS, ACCOUNT_DB_URL,
            DB_NAME, DB_USER, DB_PASS, DB_URL,
            EMAIL_SERVER, EMAIL_USER, EMAIL_PASS, EMAIL_FROM;
  
    public int 
            EMAIL_PORT = 25,
            MAX_CPU = 8,
            MAX_MEM = 1073741824, //1T = 1073741824k
            SESSION_TIME_OUT = 86400;
    
        // name of tables;
    public final String 
            userTable   = "user",
            sessionTable     = "session",
            projectsTable    = "projects",
            treesTable       = "trees",
            datasetsTable    = "datasets",
            datasetinfoTable = "datasetinfo",
            kaksProjects     = "kaksprojects",
            kaksCalculation  = "kakscalculation",
            kaksResults      = "pairsinfo";
    

    
    public SysConfig(){
        setDefaults();
        readConfig();
    }
    
    private HashMap<String, String> defaults = new HashMap<String, String>();
    private void setDefaults(){
        //defaults values is set here
        defaults.put("serverurl", "jdbc:mysql://127.0.0.1:3306/");
        defaults.put("accountdb_url", "jdbc:mysql://127.0.0.1:3306/");
        defaults.put("datapath", this.SERVERLET_REAL_PATH + "/data");
        defaults.put("workpath", this.SERVERLET_REAL_PATH = "/work");
        defaults.put("emailport", "25");
        defaults.put("maxcpu", "8");
        defaults.put("maxmem", "16384");
        defaults.put("sessiontimeout", "86400");
    }
    
    private boolean readConfig() {
        HashMap<String, String> config = new HashMap<String, String>();
        this.SERVERLET_REAL_PATH = getServletContext().getRealPath("/");
        String conf_file = this.SERVERLET_REAL_PATH + "conf/sys.conf";
        boolean isSuccess;
        try {
            Scanner scanner = new Scanner(new FileInputStream(conf_file));
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split("\\s+");
                //skip comment lines
                if (parts[0].startsWith("#")) {
                    continue;
                }
                //put settings to hashmap
                if (parts.length == 2) {
                    config.put(parts[0].toLowerCase(), parts[1]);
                }
            }
            scanner.close();
            //check if defaults is need, replace with defaults if so
            for (Iterator<String> it = defaults.keySet().iterator(); it.hasNext();){
                String key = it.next();
                if (!config.containsKey(key) || config.get(key).isEmpty()){
                    config.put(key, defaults.get(key));
                }
            }
            
            //resolve setting
            //db connection
            this.DB_NAME = config.get("database");
            this.DB_USER = config.get("username");
            this.DB_PASS = config.get("password");
            this.DB_URL = config.get("serverurl");
            this.ACCOUNT_DB = config.get("accountdb");
            this.ACCOUNT_DB_USER = config.get("accountdb_user");
            this.ACCOUNT_DB_PASS = config.get("accountdb_pass");
            this.ACCOUNT_DB_URL = config.get("accountdb_url");
            //activation
            this.EMAIL_SERVER = config.get("emailserver");
            this.EMAIL_PORT = Integer.parseInt(config.get("emailport"));
            this.EMAIL_USER = config.get("emailuser");
            this.EMAIL_PASS = config.get("emailpass");
            this.EMAIL_FROM = config.get("emailaddress");
            //userspace
            this.DATA_ROOT_PATH = config.get("datapath");
            this.WORK_ROOT_PATH = config.get("workpath");
            //resource limits
            this.MAX_CPU = Integer.parseInt(config.get("maxcpu"));
            this.MAX_MEM = Integer.parseInt(config.get("maxmem"));
            //session
            this.SESSION_TIME_OUT = Integer.parseInt(config.get("sessiontimeout"));
            
            isSuccess = true;

        } catch (FileNotFoundException ex) {
            Logger.getLogger(SysConfig.class.getName()).log(Level.SEVERE, null, ex);
            isSuccess = false;
        }
        return isSuccess;
    }
    
    private boolean checkConfig(){
        //check userspace path\
        File fck = new File(this.DATA_ROOT_PATH);
        if (!fck.exists() || !fck.isDirectory()){
            return false;
        }
        fck = new File(this.WORK_ROOT_PATH);
        if (!fck.exists() || !fck.isDirectory()){
            return false;
        }
        
        return true;
    }    
}
