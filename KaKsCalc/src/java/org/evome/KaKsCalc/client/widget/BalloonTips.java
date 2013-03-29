package org.evolgenius.client.utils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Last modified: Feb 26, 2013 --
 * @author wchen
 */
public class BalloonTips extends PopupPanel {

	/**
	 * position of the triangles
	 */
	public enum TipPos {
		rightTo, bottomTo, leftTo, topTo;
	}

	public interface Resources extends ClientBundle {
		@Source("resources/BalloonTipsAlert15.png")
		ImageResource tip();

		@Source("resources/BalloonTipsCss.css")
		CssResource css();
	}

	/**
	 * private variables for balloon tips
	 */
	private static final Resources resources = GWT.create(Resources.class);
	private SimplePanel spMessage = new SimplePanel();
	private final int distance2tip = 5, tipheight = 20, tipwidth = 10, space = 2;
	/**
	 * other private variables
	 */
	private Widget widget = null;
	private TipPos pos = null;
	

	/**
	 * constructors
	 */
	public BalloonTips() {
		super();
		initiate();
	}

	public BalloonTips(boolean autoHide) {
		super(autoHide);
		initiate();
	}

	public BalloonTips(boolean autoHide, boolean modal) {
		super(autoHide, modal);
		initiate();
	}

	public void setWidgetToShow(IsWidget w) {
		this.spMessage.setWidget(w);
	}

	/**
	 * set position to and show
	 */
	public void showRelativeToWidget(Widget wgt) {
		showRelativeToWidget(wgt, TipPos.rightTo);
	}

	public void showRelativeToWidget(Widget wgt, TipPos tippos) {
		// first remove all existing styles
		for (TipPos tp : TipPos.values()) {
			this.removeStyleName(tp.name());
		}

		// then add the style name
		this.addStyleName(tippos.name());

		// keep tracking variables
		widget = wgt;
		pos = tippos;

		// March 20, 2013 --
		this.removeFromParent(); // do this first --
		
		// show somewhere
		this.show();
	}

	/**
	 * initiate
	 */
	private void initiate() {
		resources.css().ensureInjected();

		HorizontalPanel hp = new HorizontalPanel();
		Image tip = new Image(resources.tip());
		hp.add( tip );
		hp.setCellVerticalAlignment( tip, HasVerticalAlignment.ALIGN_MIDDLE );

		hp.add(this.spMessage);
		hp.setCellVerticalAlignment(this.spMessage, HasVerticalAlignment.ALIGN_MIDDLE);
		DOM.setStyleAttribute(this.spMessage.getElement(), "paddingLeft", "5px");

		// set css styles
		this.setStyleName("balloon");

		// use HP as the managing widget 
		this.setWidget(hp);
	}

	@Override
	protected void onLoad() {
		if (pos != null && widget != null && isVisible()) {
			// then decide the position of current widget relative to the input widget
			int x = 0, y = 0;

			switch (pos) {
			case rightTo:
				x = widget.getAbsoluteLeft() + widget.getOffsetWidth() + tipwidth + space;
				y = widget.getAbsoluteTop() + (distance2tip + tipheight / 2 - widget.getOffsetHeight() / 2);
				break;
			case bottomTo:
				x = widget.getAbsoluteLeft();
				y = widget.getAbsoluteTop() + widget.getOffsetHeight() + tipwidth + space;
				break;
			case leftTo:
				x = widget.getAbsoluteLeft() - getOffsetWidth() - tipwidth - space;
				y = widget.getAbsoluteTop() + (distance2tip + tipheight / 2 - widget.getOffsetHeight() / 2);
				break;
			case topTo:
				x = widget.getAbsoluteLeft();
				y = widget.getAbsoluteTop() - getOffsetHeight() - tipwidth - space;
				break;
			default:
				break;
			}
			setPopupPosition(x, y);
		}
	}
}
