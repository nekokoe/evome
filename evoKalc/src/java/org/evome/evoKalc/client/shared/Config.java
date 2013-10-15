/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.client.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;

/**
 *   this class wraps the kaks config data
 *   config is one type of Resource
 * @author nekoko
 */
public class Config implements IsSerializable{
    //basic data
    private int id;
    private String uuid, name, comment;
    private Date create, modify;
    private Account owner;
    
    //kaks calculation params
    private Gencode kaks_code;
    private ArrayList<Method> kaks_methods = new ArrayList<Method>();
    private PairMode pairmode;
    //config related resource
    private Resource pairlist;
    private Resource result;
    
    
    //define KaKs method
    public static enum Method{
        NG,LWL,LPB,MLWL,MLPB,YN,MYN,GY,MS,MA,GNG,GLWL,GMLWL,GLPB,GMLPB,GYN,GMYN,ALL
    }
    //define genetic code
    //refer to: http://www.ncbi.nlm.nih.gov/Taxonomy/Utils/wprintgc.cgi?mode=c
    public static enum Gencode{
        u0,Standard,VM,YM,MPCM,IM,CDHN,u7,u8,EFM,EN,BAPP,AYN,AM,AFM,BN,CM,u17,u18,u19,u20,TM,SOM,TM2,PM,CDSG
    }
    
    //define pairmode
    public static enum PairMode{
        Select,List,Sequential
    }
    
    //method description provider
    public static HashMap<Method, String> methodMap = new HashMap<Method, String>(){
        {
            this.put(Method.NG, "");    
        }
    };
    //Gencode description provider
    public static HashMap<Gencode, String> codeMap = new HashMap<Gencode, String>(){
        {
            this.put(Gencode.u0, "");
            this.put(Gencode.Standard, "1 - The Standard Code");
            this.put(Gencode.VM, "2 - The Vertebrate Mitochondrial Code");
            this.put(Gencode.YM, "3 - The Yeast Mitochondrial Code");
            this.put(Gencode.MPCM, "4 - The Mold, Protozoan, and Coelenterate Mitochondrial Code and the Mycoplasma/Spiroplasma Code");
            this.put(Gencode.IM, "5 - The Invertebrate Mitochondrial Code");
            this.put(Gencode.CDHN, "6 - The Ciliate, Dasycladacean and Hexamita Nuclear Code");
            this.put(Gencode.u7, "");
            this.put(Gencode.u8, "");
            this.put(Gencode.EFM, "9 - The Echinoderm and Flatworm Mitochondrial Code");
            this.put(Gencode.EN, "10 - The Euplotid Nuclear Code");
            this.put(Gencode.BAPP, "11 - The Bacterial, Archaeal and Plant Plastid Code");
            this.put(Gencode.AYN, "12 - The Alternative Yeast Nuclear Code");
            this.put(Gencode.AM, "13 - The Ascidian Mitochondrial Code");
            this.put(Gencode.AFM, "14 - The Alternative Flatworm Mitochondrial Code");
            this.put(Gencode.BN, "15 - Blepharisma Nuclear Code");
            this.put(Gencode.CM, "16 - Chlorophycean Mitochondrial Code");
            this.put(Gencode.u17, "");
            this.put(Gencode.u18, "");
            this.put(Gencode.u19, "");
            this.put(Gencode.u20, "");
            this.put(Gencode.TM, "21 - Trematode Mitochondrial Code");
            this.put(Gencode.SOM, "22 - Scenedesmus obliquus mitochondrial Code");
            this.put(Gencode.TM2, "23 - Thraustochytrium Mitochondrial Code");
            this.put(Gencode.PM, "24 - Pterobranchia mitochondrial code");
            //this.put(Gencode.CDSG, "25 - Candidate Division SR1 and Gracilibacteria Code");
        }
    };
    
    public Config(){
        this.kaks_code = Gencode.Standard;
        this.string2Methods("MA");
    }
    
    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id = id;
    }
    
    public String getUUID(){
        return this.uuid;
    }
    public void setUUID(String uuid){
        this.uuid = uuid;
    }
    
    public Account getOwner(){
        return this.owner;
    }
    public void setOwner(Account a){
        this.owner = a;
    }
    
    
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    
    public String getComment(){
        return this.comment;
    }
    public void setComment(String comment){
        this.comment = comment;
    }
    
    public Date getCreateDate() {
        return this.create;
    }
    public Date getModifyDate() {
        return this.modify;
    }
    
    public void setCreateDate(Date create){
        this.create= create;
    }
    public void setModifyDate(Date modify){
        this.modify = modify;
    }    
    
    public Resource getPairlist(){
        return this.pairlist;
    }    
    public void setPairlist(Resource pairlist){
        this.pairlist = pairlist;
    }

    
    public Gencode getKaKsGeneticCode(){
        return this.kaks_code;
    }    
    public void setKaKsGeneticCode(Gencode code){
        this.kaks_code = code;
    }
    
    public ArrayList<Method> getKaKsMethods(){
        return this.kaks_methods;
    }
    public void addKaKsMethod(Method method){
        this.kaks_methods.add(method);
    }
    public void clearKaKsMethods(){
        this.kaks_methods.clear();
    }
    
    
    public PairMode getPairMode(){
        return this.pairmode;
    }
    public void setPairMode(PairMode mode){
        this.pairmode = mode;
    }
    
    public Resource getResult(){
        return this.result;
    }
    public void setResult(Resource res){
        this.result = res;
    }
        
    //m2s to store meths to DB
    public final String methods2String() {
        String strmethods = "";
        for (Iterator<Method> it = this.getKaKsMethods().iterator(); it.hasNext();) {
            strmethods = strmethods + it.next().name() + ",";
        }
        if (strmethods.isEmpty()){
            return "";
        }else{
            return strmethods.substring(0, strmethods.length() - 1);    //kill the ending ','
        }
    }
    
    //s2m, to recover meths from DB
    public final void string2Methods(String str){
        if (str.isEmpty()){
            return;     //do nothing if no methods
        }
        String[] metstrs = str.split(",");
        this.kaks_methods.clear();
        for (String m : metstrs){
            this.kaks_methods.add(Method.valueOf(m));
        }
    }
    
    
    
    public Config sampleData(){
        Config sample = new Config();
        sample.setKaKsGeneticCode(Gencode.Standard);
        sample.addKaKsMethod(Method.MA);
        return sample;
    }
    
}
