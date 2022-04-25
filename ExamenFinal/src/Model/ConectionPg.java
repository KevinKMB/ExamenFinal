/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Edison
 */
public class ConectionPg {
    Connection con;
    
    String cadConexion="jdbc:postgresql://localhost:5432/ExamenFinal";
    String pgUser="postgres";
    String pgPass="1234";
    
    public ConectionPg() {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectionPg.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            con= DriverManager.getConnection(cadConexion,pgUser,pgPass);
            System.out.println("Conexion OK.");
        } catch (SQLException ex) {
            Logger.getLogger(ConectionPg.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ResultSet consulta(String sql){
        
        try {
            Statement st=con.createStatement();
            return st.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(ConectionPg.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
            
    }
    
    public boolean accion(String sql){
        boolean correcto;
        try {
            Statement st=con.createStatement();
            st.execute(sql);
            st.close();
            correcto=true;
        } catch (SQLException ex) {
            Logger.getLogger(ConectionPg.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            correcto=false;
        }
        
        return correcto;
    }

    public Connection getCon() {
        return con;
    }
}
