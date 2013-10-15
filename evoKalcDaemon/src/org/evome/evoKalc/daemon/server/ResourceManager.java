/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.daemon.server;

import java.io.File;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.biojava3.core.sequence.DNASequence;
import org.biojava3.core.sequence.RNASequence;
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.template.AbstractSequence;
import org.biojava3.core.sequence.io.FastaReaderHelper;
import org.biojava3.core.sequence.io.FastaWriterHelper;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.evome.evoKalc.daemon.shared.*;


/**
 *
 * @author nekoko
 */
public class ResourceManager {
    
    private static SysConfig sysconf = Shared.sysconf;
    
    private static LinkedHashMap<String, LinkedHashMap<String, Sequence>> seq_cache = new LinkedHashMap<>();

    public static String getWorkPath(Task t){
        File path = new File(sysconf.WORK_ROOT_PATH + 
                File.separator + t.getOwner().getUUID() +
                File.separator + t.getUUID());
        if (!path.exists()){
            path.mkdirs();
            System.out.println("ResourceManager : create task dir : " + path);            
        }
        return path.getPath();
    }
    
    public static String getResourcePath(String prefix, Resource res){
        //Path is build as prefix/accountUUID/resource/resUUID
        res = DatabaseManager.getResource(res.getId()); //read database
        
        File path = new File(prefix + 
                File.separator + res.getOwner().getUUID() + 
                File.separator + "resoucre");
        if (!path.exists()){
            path.mkdirs();
            System.out.println("ResourceManager : create task dir : " + path);            
        }
        return path.getPath() + File.separator + res.getUUID();
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
    
    public static boolean removeAccountFolder(String prefix, Account a) {
        //for the beta version, to delete DATADIR/UUID folders if need
        //in the future, should be matained as uniformed RESOURCE
        Path path = Paths.get(prefix + File.separator + a.getUUID());
        if (Files.notExists(path)){
            return true;
        }
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {
                    Files.deleteIfExists(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException e)
                        throws IOException {
                    if (e == null) {
                        Files.deleteIfExists(dir);
                        return FileVisitResult.CONTINUE;
                    } else {
                        // directory iteration failed
                        throw e;
                    }
                }
            });
            System.out.println("Account folder "+ a.getUUID() + " has been deleted successfully.");
            return true;
        } catch (IOException ex) {
            Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public static boolean isFastaDNA(File file){
        if (file.exists()) {
            //is Protein?
            try {
                FastaReaderHelper.readFastaDNASequence(file);
                return true;
            } catch (Exception ex) {
                System.out.println(ex.toString());
            } catch (Error err) {
                System.out.println(err.toString());
            }
        }
        return false;
    }
    
    public static boolean isFastaDNA(Resource res){
        Path file = Paths.get(getResourcePath(sysconf.DATA_ROOT_PATH, res));
        return isFastaDNA(file.toFile());
    }
    
    public static boolean isFastaProtein(File file){
        if (file.exists()) {
            //is Protein?
            try {
                FastaReaderHelper.readFastaProteinSequence(file);
                return true;
            } catch (Exception ex) {
                System.out.println(ex.toString());
            } catch (Error err) {
                System.out.println(err.toString());
            }
        }
        return false;
    }
    
    public static boolean isFastaProtein(Resource res){
        Path file = Paths.get(getResourcePath(sysconf.DATA_ROOT_PATH, res));
        return isFastaProtein(file.toFile());
    }
     
    public static LinkedHashMap<String, ? extends AbstractSequence> parseFastaSeqs(Resource res){
        if (isFastaDNA(res)) {
            return parseFastaDNASeqs(res);
        } else if (isFastaProtein(res)) {
            return parseFastaProteinSeqs(res);
        } else{
            return null;
        }        
    }
    
    public static LinkedHashMap<String, ProteinSequence> parseFastaProteinSeqs(Resource res){
        Path file = Paths.get(getResourcePath(sysconf.DATA_ROOT_PATH, res));
        try{
            return FastaReaderHelper.readFastaProteinSequence(file.toFile());
        }catch(Exception ex){
            Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static LinkedHashMap<String, DNASequence> parseFastaDNASeqs(Resource res){
        Path file = Paths.get(getResourcePath(sysconf.DATA_ROOT_PATH, res));
        try{
            return FastaReaderHelper.readFastaDNASequence(file.toFile());
        }catch(Exception ex){
            Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    } 
    
    public static ArrayList<Sequence> parseSeqIDs(Resource res) {
        ArrayList<Sequence> id_list = new ArrayList<Sequence>();
        Set<String> keyset = null;
        if (isFastaDNA(res)) {
            keyset = parseFastaDNASeqs(res).keySet();
        } else if (isFastaProtein(res)) {
            keyset = parseFastaProteinSeqs(res).keySet();
        }

        if (keyset != null) {       //seems a fasta file
            for (Iterator<String> it = keyset.iterator(); it.hasNext();) {
                id_list.add(new Sequence(it.next(), "", res.getUUID())); //seq is ignored for remote transfer, real seq should be obtained by other means
            }
        } else {                      //seems not a fasta file
            id_list.add(new Sequence(res.getName() + " is not a fasta file, or some error(s) occured", "", res.getUUID())); //tricky...
        }
        return id_list;
    }
    
    public static Resource savePairList(Account a, ArrayList<Pair> pairlist){
        //prepare resource
        Resource res = null;
        res = new Resource();
        res.setName("pairlist");    //or else?
        res.setComment("");
        res.setType(Resource.Type.Pairlist);
        res.setOwner(a);
        res = DatabaseManager.getResource(DatabaseManager.addResource(res));
        Path file = Paths.get(getResourcePath(sysconf.DATA_ROOT_PATH, res));      
        
        //write pairlist
        try{
            //pairlist file format:
            //pairname \t parent_res_uuid,id_a \t parent_res_uuid,id_b
            BufferedWriter writer = new BufferedWriter(new FileWriter(file.toFile()));
            for (Iterator<Pair> it = pairlist.iterator(); it.hasNext();){
                Pair pair = it.next();
                String str = pair.getGroup() + "\t";    //default group is set inside class Pair as "default"
                str += pair.getName() + "\t";
                str += pair.getA().getParent() + "\t" ;
                str += pair.getA().getId() + "\t";
                str += pair.getB().getParent() + "\t";
                str += pair.getB().getId() + "\n";
                writer.write(str);
            }
            writer.close();
        }catch(Exception ex){
            Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, null, ex);
            res = null;
        }
        return res;
    }
    
    public static Sequence getSequence(String parent, String id){
        //check and update seq cache
        if (!seq_cache.containsKey(parent)){
            Resource res = DatabaseManager.getResource(parent);
            LinkedHashMap<String, ? extends AbstractSequence> seqs = new LinkedHashMap<>();;
            if (isFastaDNA(res)){
                seqs = parseFastaDNASeqs(res);
            }else if (isFastaProtein(res)){
                seqs = parseFastaProteinSeqs(res);
            }
            seq_cache.put(parent, new LinkedHashMap<String, Sequence>());
            for(Iterator<String> it = seqs.keySet().iterator(); it.hasNext();){
                String key = it.next();
                seq_cache.get(parent).put(key, new Sequence(key, seqs.get(key).getSequenceAsString(), parent));
            }            
        }        
        return seq_cache.get(parent).get(id);
    }    
    
}
