/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.daemon.server;

/**
 *
 * @author nekoko 2012/9/13
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SysConfig {

    public String DAEMON_CLASS_PATH,
            DATA_ROOT_PATH,
            WORK_ROOT_PATH,
            ACCOUNT_DB,
            ACCOUNT_DB_USER,
            ACCOUNT_DB_PASS,
            ACCOUNT_DB_URL,
            DB_NAME,
            DB_USER,
            DB_PASS,
            DB_URL,
            EMAIL_SERVER,
            EMAIL_USER,
            EMAIL_PASS,
            EMAIL_FROM;
    public int EMAIL_PORT,
            MAX_CPU,
            MAX_MEM,
            SESSION_TIME_OUT,
            EXPIRE_DAYS,
            DAEMON_INTERVAL;
    
    public String evomeConfigPath = "";      //path to the evome config file;

    public SysConfig() {
        setDefaults();
        init();
        if (!readConfig()) {
            System.err.println("[SysConfig] failed to read sysconfig. To solve this, you could try: ");
            System.err.println("1.Suppose the daemon is in EVOME_ROOT/bin, put the \"sys.conf\" in EVOME_ROOT/conf");
            System.err.println("2.Set the environment variable EVOME_CONFIG_PATH to the full path where contains the \"sys.conf\" file");
            System.exit(2);
        }        
    }
    private HashMap<String, String> defaults = new HashMap<String, String>();

    private void setDefaults() {
        //defaults values is set here
        defaults.put("serverurl", "jdbc:mysql://127.0.0.1:3306/");
        defaults.put("accountdb_url", "jdbc:mysql://127.0.0.1:3306/");
        defaults.put("emailport", "25");
        defaults.put("maxcpu", "8");
        defaults.put("maxmem", "16384");
        defaults.put("sessiontimeout", "86400");    //86400 sec
        defaults.put("interval", "5000");    //5000 ms
    }

    public final void init() {
        //try to determine the path to the config file
        Map<String, String> env = System.getenv();        
        if (env.containsKey("EVOME_CONFIG_PATH")){
            evomeConfigPath = env.get("EVOME_CONFIG_PATH");
        }else{
            evomeConfigPath = "./../conf";
        }
    }

    private boolean readConfig() {
        HashMap<String, String> config = new HashMap<>();
        String conf_file = evomeConfigPath + File.separator + "sys.conf";
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
            for (Iterator<String> it = defaults.keySet().iterator(); it.hasNext();) {
                String key = it.next();
                if (!config.containsKey(key) || config.get(key).isEmpty()) {
                    config.put(key, defaults.get(key));
                }
            }

            //resolve setting hash
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
            //anonymous account
            this.EXPIRE_DAYS = Integer.parseInt(config.get("expire_days"));
            //daemon refresh intervals
            this.DAEMON_INTERVAL = Integer.parseInt(config.get("interval"));
            
            isSuccess = checkConfig();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            isSuccess = false;
        } catch (Exception ex) {
            Logger.getLogger(SysConfig.class.getName()).log(Level.SEVERE, null, ex);
            isSuccess = false;
        }
        return isSuccess;
    }

    private boolean checkConfig() throws Exception {
        //check userspace path\
        File fck = new File(this.DATA_ROOT_PATH);
        if (!fck.exists() || !fck.isDirectory()) {
            throw new Exception("Data folder does not exists! Check config file");
        }
        fck = new File(this.WORK_ROOT_PATH);
        if (!fck.exists() || !fck.isDirectory()) {
            throw new Exception("Work folder does not exists! Check config file");
        }

        return true;
    }
}
