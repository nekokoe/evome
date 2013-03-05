/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kakscalcdaemon;

/**
 *
 * @author nekoko 2012/9/13
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SysConfig {
    public String 
            SERVERLET_REAL_PATH = "",
            DAEMON_CLASS_PATH = "",
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
    
   
    public SysConfig(){
        init();
    }
    
    public final void init(){
        //tell me where I am ?
        File classpath = new File(System.getProperty("java.class.path"));
        if (classpath.isFile()){
            this.DAEMON_CLASS_PATH = classpath.getParent();
        }else{
            this.DAEMON_CLASS_PATH = classpath.getPath();
        }
        if (!readConfig(this.DAEMON_CLASS_PATH)){
            System.err.println("[SysConfig] failed to read sysconfig, make sure put me in EVOME_ROOT/bin , and the config file is put in EVOME_ROOT/conf");
            System.exit(2);
        }        
    }
    
    private boolean readConfig(String path) {
        HashMap<String, String> config = new HashMap<String, String>();
        String conf_file = path + "/../conf/sys.conf";
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

            isSuccess = checkConfig();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            isSuccess = false;
        }
        return isSuccess;
    }
    
    private boolean checkConfig() {
        //check userspace path\
        File fck = new File(this.DATA_ROOT_PATH);
        if (!fck.exists() || !fck.isDirectory()) {
            return false;
        }
        fck = new File(this.WORK_ROOT_PATH);
        if (!fck.exists() || !fck.isDirectory()) {
            return false;
        }
        return true;
    }    
}
