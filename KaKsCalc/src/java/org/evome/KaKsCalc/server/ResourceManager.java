/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.server;

import java.io.File;
import java.nio.file.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.evome.KaKsCalc.client.Task;
import org.evome.KaKsCalc.client.Resource;
import org.biojava3.core.sequence.DNASequence;
import org.biojava3.core.sequence.io.FastaReaderHelper;
import org.biojava3.core.sequence.io.FastaWriterHelper;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.ArrayList;
import org.evome.KaKsCalc.client.Account;
import org.evome.KaKsCalc.client.shared.UploadInfo;



/**
 *
 * @author nekoko
 */
public class ResourceManager {
    
    private static DBConnector dbconn = GWTServiceImpl.getDBConn();
    private static SysConfig sysconf = GWTServiceImpl.getSysConfig();

    private static String getResourcePath(String prefix, Resource res){
        //Path is build as prefix/ParentUUID/resName
        res = DatabaseManager.getResource(res.getId()); //read database
        File path = new File(prefix + File.separator + res.getParentUUID());
        if (!path.exists()){
            path.mkdirs();
            System.out.println("ResourceManager : create task dir : " + path);            
        }
        return path.getPath() + File.separator + res.getName();
    }        
    
    
    public static Resource uploadAsResource(UploadInfo info){
        Resource res = null;
        Path source = Paths.get(info.path);
        if (Files.exists(source)) {
            try {
                res = new Resource();
                res.setParentUUID(info.UUID);
                res.setName(info.name);
                res.setComment("");
                res.setType(checkResourceType(source));
                res.setOwner(info.account);
                res = DatabaseManager.getResource(DatabaseManager.addResource(res));
                Path target = Paths.get(getResourcePath(sysconf.DATA_ROOT_PATH, res));
                Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);                
            } catch (Exception ex) {
                Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, null, ex);
                res = null;
            }
        }
        return res;        
    }
    
    public static boolean delResourceFile(Resource res) {
        String path = getResourcePath(sysconf.DATA_ROOT_PATH, res);
        try{
            return Files.deleteIfExists(Paths.get(path));
        }catch(Exception ex){
            Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, null, ex);            
            return false;
        }
    }
    
    public static Resource.ResType checkResourceType(Path file) {
        Resource.ResType type = Resource.ResType.REGULAR;
        if (Files.exists(file)) {
            if (isDNAFastaFile(file.toFile())) {
                type = Resource.ResType.DNA;
            }
        } else {
            type = Resource.ResType.UNKNOW;
        }
        return type;
    }
    
    public static Resource.ResType checkResourceType(Resource res){
        Path file = Paths.get(getResourcePath(sysconf.DATA_ROOT_PATH, res) + res.getName());
        return checkResourceType(file);
    }

    public static boolean isDNAFastaFile(File file){
        try{
            FastaReaderHelper.readFastaDNASequence(file);
            return true;
        }catch(Exception ex){
            System.out.println(ex.toString());
            return false;
        }catch(Error err){
            System.out.println(err.toString());
            return false;
        }
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
