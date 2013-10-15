/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.daemon.server;

/**
 *
 * @author nekoko
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.evome.evoKalc.daemon.shared.Shared;

public class DBConnector {

    private static SysConfig conf = Shared.sysconf;
    
    private Connection conn;
    
    public DBConnector(){
        this.conn = getConn();
    }
    
    public ResultSet execQuery(String sql) {
        ResultSet rs = null;
        try {
            PreparedStatement query = conn.prepareStatement(sql);
            rs = query.executeQuery();
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
    
    public int execUpdate(String sql){
        //for update, insert, delete
        //System.out.println(sql);//FOR DEBUG
        try {
            PreparedStatement query = conn.prepareStatement(sql);
            return query.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return 0;
        } 
    }
    
    public ResultSet execUpdateReturnGeneratedKeys(String sql){
        //System.out.println(sql);//FOR DEBUG                
        ResultSet rs = null;
        try {
            PreparedStatement query = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            query.executeUpdate();
            rs = query.getGeneratedKeys();
        } catch (Exception ex) { //debug out output this way
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    private Connection getConn() {
        Connection connect = null;
        String driver = "com.mysql.jdbc.Driver";
        try {
            Class.forName(driver).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);            
        }

        try {
            connect = DriverManager.getConnection(conf.DB_URL + conf.DB_NAME, conf.DB_USER, conf.DB_PASS);
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return connect;
    }

    public String getSQLTime() {
        return getSQLTime(new Date());
    }
    
    public String getSQLTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
    
    public boolean isAlive(){
        try{
            return conn.isValid(0);
        }catch(SQLException ex){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }    
}
