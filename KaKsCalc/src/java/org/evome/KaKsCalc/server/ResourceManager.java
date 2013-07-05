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
import org.biojava3.core.sequence.RNASequence;
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.io.FastaReaderHelper;
import org.biojava3.core.sequence.io.FastaWriterHelper;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.ArrayList;
import org.evome.KaKsCalc.client.Account;
import org.evome.KaKsCalc.client.shared.UploadInfo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


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
                res.setType(checkFileType(source));
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
    
    public static Resource textAsResource(UploadInfo info){
        //save text on client side as a plain text file and reg. as Resource
        //text is transferred as info.text
        Resource res = null;
        try{
            res = new Resource();
            res.setParentUUID(info.UUID);
            res.setName(info.name);
            res.setComment("");
            res.setType(Resource.ResType.REGULAR);
            res.setOwner(info.account);
            res = DatabaseManager.getResource(DatabaseManager.addResource(res)); 
            Path file = Paths.get(getResourcePath(sysconf.DATA_ROOT_PATH, res));
            BufferedWriter writer = new BufferedWriter(new FileWriter(file.toFile()));
            String text = info.getText();
            text = text.replaceAll("\r", "");
            text = text.replaceAll("\n", System.getProperty("line.separator"));
            writer.write(text);
            writer.close();
        }catch(Exception ex){
            Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, null, ex);
            res = null;
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
    
    public static Resource.ResType checkFileType(Path path){
        if (!Files.exists(path)) {
            return Resource.ResType.UNKNOW;
        }
        //is DNA?
        try{
            FastaReaderHelper.readFastaDNASequence(path.toFile());
            return Resource.ResType.DNA;
        }catch(Exception ex){
            System.out.println(ex.toString());            
        }catch(Error err){
            System.out.println(err.toString());
        }
        //is RNA?
        try{
            FastaReaderHelper.readFastaProteinSequence(path.toFile());
            return Resource.ResType.PROTEIN;
        }catch(Exception ex){
            System.out.println(ex.toString());            
        }catch(Error err){
            System.out.println(err.toString());
        }
        //is dir?
        if (Files.isDirectory(path)){
            return Resource.ResType.DIRECTORY;
        }
        //is regular file?
        if (Files.isRegularFile(path)){
            return Resource.ResType.REGULAR;
        }
        //don't know what is this
        return Resource.ResType.UNKNOW;
    }
    
    
    public static Resource.ResType checkResourceType(Resource res) {
        Path file = Paths.get(getResourcePath(sysconf.DATA_ROOT_PATH, res) + res.getName());
        return checkFileType(file);    
    }
    
    public static LinkedHashMap<String, DNASequence> parseFastaDNASeqs(Resource res){
        Path file = Paths.get(getResourcePath(sysconf.DATA_ROOT_PATH, res) + res.getName());
        try{
            return FastaReaderHelper.readFastaDNASequence(file.toFile());
        }catch(Exception ex){
            Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
