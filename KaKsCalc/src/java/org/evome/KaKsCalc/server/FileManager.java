/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.server;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.evome.KaKsCalc.client.Task;
import org.biojava3.core.sequence.DNASequence;
import org.biojava3.core.sequence.io.FastaReaderHelper;
import org.biojava3.core.sequence.io.FastaWriterHelper;


/**
 *
 * @author nekoko
 */
public class FileManager {
    
    private static DBConnector dbconn = GWTServiceImpl.getDBConn();
    private static SysConfig sysconf = GWTServiceImpl.getSysConfig();

    
    private static String getSubDir(String prefix, Task task) {
        return prefix
                + "/" + task.getOwner()
                + "/" + task.getProject()
                + "/" + task.getCalculation()
                + "/" + task.getId();
    }
    
    public static String getDataDir(Task task){
        return getSubDir(sysconf.DATA_ROOT_PATH, task);
    }
    
    public static String getWorkDir(Task task){
        return getSubDir(sysconf.WORK_ROOT_PATH, task);
    }
    
    private static boolean initSubDir(String prefix, Task task) {
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
            System.out.println(FileManager.class.getName() + ", create task dir : " + path);
            return true;
        } catch (Exception ex) {
            System.err.println(FileManager.class.getName() + ", Cannot create dir: " + path);
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);            
            return false;
        }
    }

    public static boolean initDataDir(Task task){
        return initSubDir(sysconf.DATA_ROOT_PATH, task);
    }
    
    public static boolean initWorkDir(Task task){
        return initSubDir(sysconf.WORK_ROOT_PATH, task);
    }
    
    public static boolean isFastaFile(File file){
        try{
            FastaReaderHelper.readFastaDNASequence(file);
            return true;
        }catch(Exception ex){
            return false;
        }                    
    }
    
    public static boolean postFileUpload(File tempfile, Task task){
        //copy tempfile to task dir
        String target = getDataDir(task);
        if (tempfile.exists()){
            try{
                tempfile.renameTo(new File(target + "/" + tempfile.getName()));
            }catch(Exception ex){
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }else{
            return false;
        }
        return true;
    }
}
