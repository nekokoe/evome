/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.shared;

/**
 *
 * @author nekoko
 */
public class TreeViewItem {
        private int id;
        private String uuid;
        private String value;
        private Type type;
        
        
        public enum Type{
            HOME,PROJECT,CALCULATION,TASK //HOME = home page
        }
        
        public TreeViewItem(Type type, int id, String value){
            this.id = id;
            this.value = value;
            this.type = type;
        }
        
        public String getKey(){
            return type + "_" + id;
        }
        public void setKey(String key){
            String[] strs = key.split("_");
            this.type = Type.valueOf(strs[0]);
            this.id = Integer.parseInt(strs[1]);
        }
        
        public String getValue(){
            return value;
        }        
        public void setValue(String value){
            this.value = value;
        }

        public Type getType(){
            return type;
        }        
        public void setType(Type type){
            this.type = type;
        }
        public int getId(){
            return id;
        }
        public void setId(int id){
            this.id = id;
        }
}
