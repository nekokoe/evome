package org.evome.KaKsCalc.client.widget;



import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import java.util.HashMap;

/**
 *
 * created on 2011 Sep 4, 2012: improve visual effects Feb 6, 2013: improve
 * visual effects Feb 6, 2013: check if mouse over manually; this is necessary
 * because mouse over / out doesn't work nicely while mouse is down NOTE:
 * mouseDown effects for non-google style are removed Feb 11, 2013: css
 * injection
 */
public class AwesomeButton extends Button {
    
    /**
     * an interface to load resources needed
     * Feb 11, 2013 --
     */
    public interface Resources extends ClientBundle {
        public static final Resources INSTANCE = GWT.create(Resources.class);
        @Source("resources/Awesomebuttons.css")
        public CssResource cssAwesomeButtons();
    }
    
    public enum AwesomeButtonColor {
    	strike, black, white, orangeyellow, orange, green, blue, red, magenta, google
	}
    
    private HashMap<String, String> styles = new HashMap<String, String>();
    private HashMap<String, String> masterstyles = new HashMap<String, String>();
    /*
     * valid style names are :
     */
    private String style = "white", size = "large";
    private boolean bMouseOver = false, bMouseDown = false;

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
    
    public AwesomeButton(String msg, String col, String size ) {
        super(msg);

        style = col.toLowerCase();
        setSize( size );
        init();
    }
    
    public AwesomeButton(String msg, AwesomeButton.AwesomeButtonColor col) {
        super(msg);

        style = col.toString().toLowerCase();
        init();
    }
    
    public AwesomeButton(String msg, AwesomeButton.AwesomeButtonColor col, String size ) {
        super(msg);

        style = col.toString().toLowerCase();
        setSize( size );
        init();
    }

    public void setColor(String col) {
        style = col.toLowerCase();
        init();
    }
    
    public void setColor(AwesomeButton.AwesomeButtonColor col) {
    	style = col.toString().toLowerCase();
        init();
    }


    public final void setSize(String size) {
        this.size = size.equalsIgnoreCase("small") ? "small" : "large";
        init();
    }

    public String[] getAvailableStyles() {
        return (String[]) styles.keySet().toArray();
    }

    public boolean checkStyleAvailable(String s) {
        return styles.containsKey(s);
    }

    public String getCurrentStyle() {
        return this.style;
    }

    private void init() {

        // Feb 11, 2013 --
        Resources.INSTANCE.cssAwesomeButtons().ensureInjected();
        
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
        if (!styles.containsKey(style)) {
            style = "black";
        }
        this.setStyleName(masterstyles.get(style));
        this.addStyleName(styles.get(style));

        if (size.equals("large")) {
            this.addStyleName("large");
        } else if (size.equals("small")) {
            this.addStyleName("small");
        }

        /*
         * add mouse handlers to button
         */
        this.addMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                bMouseOver = true;
                addStyleName(styles.get(style) + "MouseOver");
                if (bMouseDown) {
                    addStyleName(masterstyles.get(style) + "MouseDown");
                }
            }
        });

        this.addMouseOutHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                bMouseOver = false;
                removeStyleName(styles.get(style) + "MouseOver");
                if (!bMouseDown) {
                    removeStyleName(styles.get(style) + "MouseDown");
                }
            }
        });

        this.addMouseDownHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent event) {
                bMouseDown = true;
                addStyleName(masterstyles.get(style) + "MouseDown");
                addStyleName(styles.get(style) + "MouseOver");
                DOM.setCapture(getElement());
            }
        });

        this.addMouseUpHandler(new MouseUpHandler() {
            @Override
            public void onMouseUp(MouseUpEvent event) {
                bMouseDown = false;
                removeStyleName(masterstyles.get(style) + "MouseDown");

                /**
                 * Feb 6, 2013: check if mouse over; this is necessary because
                 * mouse over / out doesn't work nicely while mouse is down.
                 */
                int left = getAbsoluteLeft();
                int top = getAbsoluteTop();
                int right = left + getOffsetHeight();
                int bottom = top + getOffsetWidth();

                int mousex = event.getClientX();
                int mousey = event.getClientY();

                if (mousex >= left && mousex <= right && mousey >= top && mousey <= bottom) {
                    bMouseOver = true;
                } else {
                    bMouseOver = false;
                }

                if (!bMouseOver) {
                    removeStyleName(styles.get(style) + "MouseOver"); // Sep 4, 2012; also remove mouse over effect
                }
                DOM.releaseCapture(getElement());
            }
        });
    }

    @Override
    protected void onLoad() {
        removeStyleName(styles.get(style) + "MouseOver"); // 
        removeStyleName(masterstyles.get(style) + "MouseDown");//
    }
}