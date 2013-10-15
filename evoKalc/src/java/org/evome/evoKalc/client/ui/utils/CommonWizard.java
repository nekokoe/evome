/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.client.ui.utils;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;


/**
 *
 * @author nekoko
 * 
 * this is a common shared Wizard widget, ba
 *
 */
public class CommonWizard extends Window {
    
    private int step = 0;   //counter of steps, 0 = first
    private ArrayList<Widget> stepWigs = new ArrayList<Widget>();
    private ArrayList<String> stepText = new ArrayList<String>();
    private HashMap<Widget, NextStepHandler> nextHandlers = new HashMap<Widget, NextStepHandler>();
    private HashMap<Widget, PrevStepHandler> prevHandlers = new HashMap<Widget, PrevStepHandler>();
    private HashMap<Widget, InputValidator> inputValidators = new HashMap<Widget, InputValidator>();
    private FinishHandler finishHandler;
    private CancelHandler cancelHandler;
    private EnumMap<ButtonType, Component> buttons = new EnumMap<ButtonType, Component>(ButtonType.class);
    
    
    private enum ButtonType{
        PREV,NEXT,FINISH,CANCEL
    }
    
    
    public CommonWizard(){
        initWizardPanel();
        step = 0;
    }
    
    public void addStepWidget(Widget widget, String headText){
        stepWigs.add(widget);
        stepText.add(headText);
        updateUI();
    }
    
    public void next(){
        step++;
        updateUI();
    }
    
    public void back(){
        step--;
        updateUI();
    }
    public void finish(){
        this.hide();
    }
    
    public void cancel(){
        this.hide();
    }
    
    public boolean jumpTo(Widget w){
        //jump to the given widget
        if (!stepWigs.contains(w)){
            return false;
        }        
        step = stepWigs.indexOf(w);
        updateUI();
        return true;
    }
    
    
    
    protected void updateUI(){
        //alter buttons
        if (step == 0){
            //first    
            buttons.get(ButtonType.PREV).setEnabled(false);
            buttons.get(ButtonType.FINISH).setEnabled(false);
        }else if (step == stepWigs.size() - 1){
            //last
            buttons.get(ButtonType.NEXT).setEnabled(false);
            buttons.get(ButtonType.FINISH).setEnabled(true);                
        }else{
            //other
            buttons.get(ButtonType.PREV).setEnabled(true);            
            buttons.get(ButtonType.NEXT).setEnabled(true);
            buttons.get(ButtonType.FINISH).setEnabled(false);             
        }
        //switch stepwig
        this.setWidget(stepWigs.get(step));
        //set heading text
        this.setHeadingText(stepText.get(step));
        //this is very useful to redraw layout appearance
        this.forceLayout();
    }
    
    //action handlers
    public interface NextStepHandler{
        void onNextStep();
    }
    
    public interface PrevStepHandler{
        void onPrevStep();
    }
    
    public interface FinishHandler{
        void onFinish();
    }
    
    public interface CancelHandler{
        void onCancel();
    }
    
    //validator
    public interface InputValidator{
        boolean onValidate();
    }
    
    public void addNextStepHandler(Widget widget, NextStepHandler next){
        nextHandlers.put(widget, next);
    }
    
    public void addPrevStepHandler(Widget widget, PrevStepHandler prev){
        prevHandlers.put(widget, prev);
    }
    
    public void setFinishHandler(FinishHandler finish){
        this.finishHandler = finish;
    }
    
    public void setCancelHandler(CancelHandler cancel){
        this.cancelHandler = cancel;
    }
    
    public void addInputValidator(Widget widget, InputValidator val){
        this.inputValidators.put(widget, val);
    }
    
    private void initWizardPanel(){
        //set default size
        this.setWidth(500);
        this.setHeight(400);
        //border margins
        this.getElement().setMargins(10);
        //set headbar button
//        this.getHeader().addTool(new ToolButton(ToolButton.CLOSE, new SelectEvent.SelectHandler() {
//            @Override
//            public void onSelect(SelectEvent event) {
//                cancelHandler.onCancel();                
//                cancel();    
//            }
//        }));
        this.addHideHandler(new HideEvent.HideHandler(){
            @Override
            public void onHide(HideEvent event){
                cancel();
                try{
                    cancelHandler.onCancel();
                }catch(Exception ex){
                    
                }                   
            }
        });
        
        //add bottom buttons
        TextButton prev = new TextButton("back");
        TextButton next = new TextButton("next");
        TextButton finish = new TextButton("finish");
        TextButton cancel = new TextButton("cancel");
        this.addButton(prev);
        this.addButton(next);
        this.addButton(finish);
        this.addButton(cancel);
        //and add buttons to buttons map
        buttons.put(ButtonType.PREV, prev);
        buttons.put(ButtonType.NEXT, next);
        buttons.put(ButtonType.FINISH, finish);
        buttons.put(ButtonType.CANCEL, cancel);
        //set button action

        prev.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                Widget w = stepWigs.get(step);
                back();
                try{
                    prevHandlers.get(w).onPrevStep();
                }catch(Exception ex){
                    
                }
            }
        });
        next.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                Widget w = stepWigs.get(step);
                //trigger validators
                try {
                    if (inputValidators.get(w).onValidate()) {
                        next();
                    }
                } catch (Exception ex) {
                    next();
                }
                //trigger nextHandlers
                try {
                    nextHandlers.get(w).onNextStep();
                } catch (Exception ex) {
                }
            }
        });
        finish.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                finish();
                try{
                    finishHandler.onFinish();                
                }catch(Exception ex){
                    
                }

            }
        });
        cancel.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                cancel();
                try{
                    cancelHandler.onCancel();
                }catch(Exception ex){
                    
                }                
            }
        });                
    }

}
