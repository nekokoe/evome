package org.evome.KaKsCalc.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AlertWidget extends PopupPanel {

	/**
	 */
	public enum AlertWidgetType {
		info, success, warning, error, busy
	}

	public interface AlertWidgetResources extends ClientBundle {
		@Source("resources/Alertwidget/ajax-loader-transparent_background_blue.gif")
		ImageResource busy();

		@Source("resources/Alertwidget/error.png")
		ImageResource error();

		@Source("resources/Alertwidget/info.png")
		ImageResource info();

		@Source("resources/Alertwidget/success.png")
		ImageResource success();

		@Source("resources/Alertwidget/warning.png")
		ImageResource warning();

		@Source("resources/AlertWidget.css")
		CssResource css();
	}

	private static final AlertWidgetResources resources = GWT.create(AlertWidgetResources.class);
	private HorizontalPanel hp = new HorizontalPanel();
	private SimplePanel spImage = new SimplePanel(), spMessage = new SimplePanel();
	private HTML hmsg = new HTML();
	private Image busy = new Image(resources.busy()), error = new Image(resources.error()), info = new Image(resources.info()), success = new Image(
			resources.success()), warning = new Image(resources.warning());

	public AlertWidget() {
		super();
		init(false);
	}

	// a new constructor; Feb 17, 2013 --
	public AlertWidget(boolean modular) {
		super();
		init(modular);
	}

	public void setMessageAndShow(String msg) {
		hmsg.setHTML(msg);
		setStyleAssembleVPandShow(AlertWidget.AlertWidgetType.info.toString());
	}
	public void setMessageAndShow(SafeHtml msg, AlertWidget.AlertWidgetType astyle) {
		hmsg.setHTML(msg);
		setStyleAssembleVPandShow( astyle.toString() );
	}

	public void setMessageAndShow(String msg, AlertWidget.AlertWidgetType astyle) {
		hmsg.setHTML(msg);
		setStyleAssembleVPandShow( astyle.toString() );
	}

	/**
	 * Feb 20, 2013 --
	 * set style name,
	 * assembleVP
	 * and show
	 * @param style
	 */
	private void setStyleAssembleVPandShow(String style) {
		// set style name
		this.setStyleName("AW" + style );
		if (style.equalsIgnoreCase("info")) {
			this.spImage.setWidget(info);
		} else if (style.equalsIgnoreCase("success")) {
			this.spImage.setWidget(success);
		} else if (style.equalsIgnoreCase("warning")) {
			this.spImage.setWidget(warning);
		} else if (style.equalsIgnoreCase("error")) {
			this.spImage.setWidget(error);
		} else if (style.equalsIgnoreCase("busy")) {
			this.spImage.setWidget(busy);
		} else {
			this.spImage.setWidget(info);
		}
		
		this.spMessage.setWidget( this.hmsg );
		super.center();
	}

	/**
	 * Feb 17, 2013 --
	 * Feb 20, 2013 --
	 */
	protected void init(boolean modular) {
		this.setModal(modular);
		this.setGlassEnabled(modular);
		this.setAutoHideEnabled(!modular);
		
		// ensure css is enjected properly
		resources.css().ensureInjected();

		hp.setSpacing(20);
		hp.add(this.spImage);
		hp.setCellVerticalAlignment(this.spImage, HasVerticalAlignment.ALIGN_MIDDLE);

		hp.add(this.spMessage);
		hp.setCellVerticalAlignment(this.spMessage, HasVerticalAlignment.ALIGN_MIDDLE);

		AwesomeButton btnDismiss = new AwesomeButton("Dismiss", "magenta", "small");

		VerticalPanel vp = new VerticalPanel();
		vp.add(hp);
		vp.add(btnDismiss);
		vp.setCellHorizontalAlignment(btnDismiss, HasHorizontalAlignment.ALIGN_RIGHT);
		vp.setSpacing(5);

		btnDismiss.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});

		this.setWidget(vp);
	}

}