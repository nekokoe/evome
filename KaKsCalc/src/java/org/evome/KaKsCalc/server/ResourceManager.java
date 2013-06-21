/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.server;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.evome.KaKsCalc.client.Task;
import org.evome.KaKsCalc.client.Resource;
import org.biojava3.core.sequence.DNASequence;
import org.biojava3.core.sequence.io.FastaReaderHelper;
import org.biojava3.core.sequence.io.FastaWriterHelper;
import java.util.LinkedHashMap;
import java.util.UUID;


/**
 *
 * @author nekoko
 */
public class ResourceManager {
    
    private static DBConnector dbconn = GWTServiceImpl.getDBConn();
    private static SysConfig sysconf = GWTServiceImpl.getSysConfig();

    private static String getResourcePath(String prefix, Resource res){
        //path is built as prefix/parent/.../res
        String path = res.getName();
        UUID child = UUID.fromString(res.getUUID());
        while(true) {
            UUID parent = DatabaseManager.getParentUUID(child);
            if (parent == null){
                break;
            }
            path = parent.toString() + "/" + path;
            child = parent;
        }
        return prefix + "/" + path;
    }        
    
    private static boolean initResourcePath(String prefix, Resource res) {
        String path = getResourcePath(prefix, res);
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
            System.out.println(ResourceManager.class.getName() + ", create task dir : " + path);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, null, ex);            
            return false;
        }
    }

    public static boolean isFastaFile(File file){
        try{
            FastaReaderHelper.readFastaDNASequence(file);
            return true;
        }catch(Exception ex){
            return false;
        }                    
    }
    
    public static boolean tempFileAsResource(File tempfile, String prefix, Resource res){ 
        //copy tempfile to task dir
        String target = getResourcePath(prefix, res);
        if (tempfile.exists()){
            try{
                tempfile.renameTo(new File(target + "/" + res.getName()));
            }catch(Exception ex){
                Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }else{
            return false;
        }
        return true;
    }
    
    public static LinkedHashMap<String, DNASequence> parseFastaDNASeqs(File fasta){
        try{
            return FastaReaderHelper.readFastaDNASequence(fasta);
        }catch(Exception ex){
            Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
