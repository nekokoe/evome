/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.server;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.evome.KaKsCalc.client.Task;

/**
 *
 * @author nekoko
 */
public class FileManager {
    
    private static DBConnector dbconn = new DBConnector();
    private static SysConfig sysconf = new SysConfig();

    
    
    
    public String getSubDir(String prefix, Task task){
        return prefix 
                + "/" + task.getOwner() 
                + "/" + task.getProject() 
                + "/" + task.getId();
    }
    
    private boolean initSubDir(String prefix, Task task) {
        String path = getSubDir(prefix, task);
        try {
            File dir = new File(path);
            if (dir.exists()) {
                //task dir exists, check if empty
                if (dir.isDirectory()) {
                    File[] list = dir.listFiles();
                    if (list.length > 0) {
                        //cannot use this dir
                        return false;
                    }
                } else {
                    //is here, but regular file
                    return false;
                }
                //now safe to remove it
                dir.delete();
            }
            dir.mkdirs();
            System.out.println(this.getClass().getName() + ", create task dir : " + path);
            return true;
        } catch (Exception ex) {
            System.err.println(this.getClass().getName() + ", Cannot create dir: " + path);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);            
            return false;
        }
    }

    public boolean initDataDir(Task task){
        return initSubDir(sysconf.DATA_ROOT_PATH, task);
    }
    
    public boolean initWorkDir(Task task){
        return initSubDir(sysconf.WORK_ROOT_PATH, task);
    } 
}
