/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui;

/**
 *
 * @author nekoko
 */
public class TreeViewItem {
        private int id;
        private String value;
        private String type;
        
        public TreeViewItem(String type, int id, String value){
            this.id = id;
            this.value = value;
            this.type = type;
        }
        
        public String getKey(){
            return type + "_" + id;
        }
        public void setKey(String key){
            String[] strs = key.split("_");
            this.type = strs[0];
            this.id = Integer.parseInt(strs[1]);
        }
        public String getValue(){
            return value;
        }        
        public void setValue(String value){
            this.value = value;
        }

        public String getType(){
            return type;
        }        
        public void setType(String type){
            this.type = type;
        }
        public int getId(){
            return id;
        }
        public void setId(int id){
            this.id = id;
        }
}
