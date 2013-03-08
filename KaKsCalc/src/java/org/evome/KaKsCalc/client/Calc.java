/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client;

/**
 *
 * @author nekoko
 */
public class Calc {

    private int calc_id, calc_project, calc_owner;
    private String calc_name, calc_comment;

    public void setId(int id) {
        this.calc_id = id;
    }

    public void setProject(int project) {
        this.calc_project = project;
    }

    public void setOwner(int owner) {
        this.calc_owner = owner;
    }

    public void setName(String name) {
        this.calc_name = name;
    }

    public void setComment(String comment) {
        this.calc_comment = comment;
    }

    public int getId() {
        return this.calc_id;
    }

    public int getProject() {
        return this.calc_project;
    }

    public int getOwner() {
        return this.calc_owner;
    }

    public String getName() {
        return this.calc_name;
    }

    public String getComment() {
        return this.calc_comment;
    }
}
