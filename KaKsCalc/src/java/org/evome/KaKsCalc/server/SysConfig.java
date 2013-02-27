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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SysConfig extends RemoteServiceServlet{
    public String 
            SERVERLET_REAL_PATH = "",
            DATA_ROOT_PATH = "",
            WORK_ROOT_PATH = "",
            ACCOUNT_DB = "",
            DB_NAME = "",
            DB_USER = "",
            DB_PASS = "",
            DB_URL = "",
            EMAIL_SERVER = "",
            EMAIL_USER = "",
            EMAIL_PASS = "",
            EMAIL_FROM = "";
  
    public int 
            EMAIL_PORT = 25,
            MAX_CPU = 8,
            MAX_MEM = 1073741824; //1T = 1073741824k
    
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
        readConfig();
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
            
            //resolve setting hash
            //db connection
            this.DB_NAME = config.get("database");
            this.ACCOUNT_DB = config.get("accountdb");
            this.DB_USER = config.get("username");
            this.DB_PASS = config.get("password");
            this.DB_URL = config.get("serverurl");
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

            isSuccess = true;

        } catch (FileNotFoundException ex) {
            Logger.getLogger(SysConfig.class.getName()).log(Level.SEVERE, null, ex);
            isSuccess = false;
        }
        return isSuccess;
    }
}
