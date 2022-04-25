/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 *
 * @author Edison
 */
public class ModelSocios extends Socios{
    
    ConectionPg cpg = new ConectionPg();

    public ModelSocios(String cedula, String nombres, String direccion, String telefono, Image foto, String disco, String placa, String marca) {
        super(cedula, nombres, direccion, telefono, foto, disco, placa, marca);
    }
    
    public ModelSocios(){
        
    }
    
    public List<Socios> listarSocios() {
        ArrayList<Socios> lista = new ArrayList<Socios>();
        try {
            String sql = "select * from socios";
            ResultSet rs = cpg.consulta(sql);
            byte[] bytea;
            while (rs.next()) {
                Socios socios = new Socios();
                socios.setCedula(rs.getString("cedula"));
                socios.setNombres(rs.getString("nombres"));
                socios.setDireccion(rs.getString("direccion"));
                socios.setTelefono(rs.getString("telefono"));
                socios.setDisco(rs.getString("disco"));
                socios.setPlaca(rs.getString("placa"));
                socios.setMarca(rs.getString("marca"));
                bytea = rs.getBytes("foto");
                if (bytea != null) {
//                    bytea = Base64.decode(bytea, 0, bytea.length);
                    try {
                        socios.setFoto(obtenerImagen(bytea));
                    } catch (IOException ex) {
                        Logger.getLogger(ModelSocios.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                lista.add(socios);
            }
            rs.close();
            return lista;
        } catch (SQLException ex) {
            Logger.getLogger(ModelSocios.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    private Image obtenerImagen(byte[] bytes) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Iterator it = ImageIO.getImageReadersByFormatName("jpeg");
        ImageReader reader = (ImageReader) it.next();
        Object source = bis;
        ImageInputStream iis = ImageIO.createImageInputStream(source);
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        param.setSourceSubsampling(1, 1, 0, 0);
        return reader.read(0, param);
    }
    
    public boolean crearSocioByte(){
        try {
            String sql;
            sql = "INSERT INTO socios (cedula,nombres,direccion,telefono,disco,placa,marca,foto)";
            sql+="VALUES(?,?,?,?,?,?,?,?)";
            PreparedStatement ps = cpg.getCon().prepareStatement(sql);
            ps.setString(1, getCedula());
            ps.setString(2, getNombres());
            ps.setString(3, getDireccion());
            ps.setString(4, getTelefono());
            ps.setString(5, getDisco());
            ps.setString(6, getPlaca());
            ps.setString(7, getMarca());
            ps.setBinaryStream(8, getImagen(), getLargo());

            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModelSocios.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean editarSocio() {
        String sql;
        sql = "UPDATE socios set nombres= '" + getNombres() + "', direccion= '" + getDireccion()+ "', telefono= '" + getTelefono()+ "', disco= '" + getDisco()+ "', placa= '" + getPlaca()+ "', marca= '" + getMarca()+ "', foto= '" + getFoto() + "' WHERE cedula= '" + getCedula()+ "';";
        return cpg.accion(sql);
    }

    public boolean eliminarSocio(String cedula) {
        String sql = "DELETE FROM socios WHERE cedula= '" + cedula + "'";
        return cpg.accion(sql);
    }
}
