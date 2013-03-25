package org.evome.KaKsCalc.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author wchen
 */
public class NotificationCenter extends PopupPanel {

    private int height = 28, elapsed_time = 5000;
    private String message = "";
    private Label label = new Label();
    private VerticalPanel vp = new VerticalPanel();

    public NotificationCenter() {
        super(true, false); // auto hide
        DOM.setStyleAttribute(label.getElement(), "color", "black");
        DOM.setStyleAttribute(label.getElement(), "fontWeight", "bold");

        vp.setHeight(height + "px");
        vp.add(label);
        vp.setCellHorizontalAlignment(label, HasHorizontalAlignment.ALIGN_CENTER);
        vp.setCellVerticalAlignment(label, HasVerticalAlignment.ALIGN_MIDDLE);

        setWidget(vp);
        DOM.setStyleAttribute(this.getElement(), "boxShadow", "2px 2px 2px grey");
        DOM.setStyleAttribute(this.getElement(), "backgroundColor", "#F9EDBE"); //D1ECFF
        DOM.setStyleAttribute(this.getElement(), "padding", "5px 20px 5px 20px");
        DOM.setStyleAttribute(this.getElement(), "opacity", "0.8");
        DOM.setStyleAttribute(this.getElement(), "border", "1px solid #F0C36D");
        DOM.setStyleAttribute(this.getElement(), "borderRadius", "4px");
    }

    public void setMessage(String msg) {
        this.message = msg;
    }
    
    public void setMessageAndShow(String msg) {
        setMessage(msg);
        show();
    }
    
    public void setMessageAndShow(String msg, int milisecs) {
        this.elapsed_time = milisecs;
        setMessage(msg);
        show();
    }

    @Override
    public void show() {
        this.label.setText(message);

        this.setPopupPosition(10, Window.getClientHeight() - height - 20 - 24);
        super.show();

        Timer t = new Timer() {
            @Override
            public void run() {
                hide();
            }
        };
        // Schedule the timer to run once in 5 seconds.
        t.schedule( elapsed_time );
    }
}
