package org.evome.KaKsCalc.client.ui;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import java.util.HashMap;

/**
 *
 * @author wchen
 */
public class AwesomeButton extends Button {

    private HashMap<String, String> styles = new HashMap<String, String>();
    private HashMap<String, String> masterstyles = new HashMap<String, String>();
    /*
     * valid style names are :
     * 
     */
    private String style = "white", size = "large";
    
    public AwesomeButton() {
        super();
        init();
    }

    public AwesomeButton(String msg) {
        super(msg);
        init();
    }

    public AwesomeButton(String msg, String col) {
        super(msg);

        style = col.toLowerCase();
        init();
    }
    
    public void setColor( String col ){
        style = col.toLowerCase();
        init();
    }
    
    public void setSize( String size ){
        this.size = size.equalsIgnoreCase("small") ? "small" : "large";
        init();
    }
    
    public String[] getAvailableStyles(){
        return (String[]) styles.keySet().toArray();
    }
    
    public boolean checkStyleAvailable(String s){
        return styles.containsKey( s );
    }
    
    public String getCurrentStyle(){
        return this.style;
    }
    
    private void init() {
        /*
         * initiate styles and master styles
         */
        styles.put("strike", "awesomeButtonStrike");
        styles.put("black", "awesomeButtonBlack");
        styles.put("white", "awesomeButtonWhite");
        styles.put("orangeyellow", "awesomeButtonOrangyellow");
        styles.put("orange", "awesomeButtonOrange");
        styles.put("green", "awesomeButtonGreen");
        styles.put("blue", "awesomeButtonBlue");
        styles.put("red", "awesomeButtonRed");
        styles.put("magenta", "awesomeButtonMagenta");
        styles.put("google", "googlechromSysconf");
        
        masterstyles.put("strike", "awesomeButton");
        masterstyles.put("black", "awesomeButton");
        masterstyles.put("white", "awesomeButton");
        masterstyles.put("orangeyellow", "awesomeButton");
        masterstyles.put("orange", "awesomeButton");
        masterstyles.put("green", "awesomeButton");
        masterstyles.put("blue", "awesomeButton");
        masterstyles.put("red", "awesomeButton");
        masterstyles.put("magenta", "awesomeButton");
        masterstyles.put("google", "googlechromSysconf");
        
        /*
         * initiate buttonstyle
         */
        if( !styles.containsKey( style ) ){
            style = "black";
        }
        this.setStyleName( masterstyles.get( style ) );
        this.addStyleName( styles.get( style ) );
        
        if( size.equals("large") ){
            this.addStyleName( "large" );
        } else if ( size.equals("small")){
            this.addStyleName( "small" );
        }
        
        /*
         * add mouse handlers to button
         */
        this.addMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
//                DOM.setCapture(getElement());
                addStyleName( styles.get( style ) + "MouseOver" );
            }
        });

        this.addMouseOutHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
//                DOM.releaseCapture(getElement());
                removeStyleName( masterstyles.get( style ) + "MouseDown" );
                removeStyleName( styles.get( style ) + "MouseOver" );
            }
        });

        this.addMouseDownHandler(new MouseDownHandler() {

            @Override
            public void onMouseDown(MouseDownEvent event) {
                addStyleName( masterstyles.get( style ) + "MouseDown" );
            }
        });

        this.addMouseUpHandler(new MouseUpHandler() {

            @Override
            public void onMouseUp(MouseUpEvent event) {
                removeStyleName( masterstyles.get( style ) + "MouseDown" );
            }
        });
        
        this.addBlurHandler(new BlurHandler(){
            @Override
            public void onBlur(BlurEvent event) {
//                DOM.releaseCapture(getElement());
                removeStyleName( masterstyles.get( style ) + "MouseDown" );
                removeStyleName( styles.get( style ) + "MouseOver" );
            }
        });
    }
}
