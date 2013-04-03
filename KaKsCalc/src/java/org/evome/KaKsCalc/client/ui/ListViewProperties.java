/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui;

/**
 *
 * @author nekoko
 */
public class ListViewProperties {
        private String key;
        private String value;
        
        public ListViewProperties(String key, String value){
            this.key = key;
            this.value = value;
        }
        
        public String getKey(){
            return key;
        }
        public String getValue(){
            return value;
        }
        public void setKey(String key){
            this.key = key;     
        }
        public void setValue(String value){
            this.value = value;
        }
}
