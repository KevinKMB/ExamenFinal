/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.ConectionPg;
import Model.ModelSocios;
import Model.Socios;
import View.ViewSocios;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.xml.ws.Holder;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Edison
 */
public class ControllerSocios {

    private ModelSocios modelo;
    private ViewSocios vista;
    private JFileChooser jfc;

    public ControllerSocios(ModelSocios modelo, ViewSocios vista) {
        this.modelo = modelo;
        this.vista = vista;
        vista.setVisible(true);//Visibilar la vista.
        cargarSocios();
    }
    
    public void iniciaControl() {

        vista.getBtnCrear().addActionListener(l -> abrirDialogo(1));
        vista.getBtnModificar().addActionListener(l -> abrirDialogo(2));

        vista.getBtnAceptar().addActionListener(l -> crearEditarPersona());
        vista.getBtnEliminar().addActionListener(l -> eliminarSocio());
        vista.getBtnCancelar().addActionListener(l -> cancelar());
        vista.getBtnExaminar().addActionListener(l-> examinaFoto());
        vista.getBtnImprimir().addActionListener(l -> imprimirListaClientes());
    }

    private void examinaFoto() {
        jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int estado = jfc.showOpenDialog(vista);
        if (estado == JFileChooser.APPROVE_OPTION) {
            try {
                Image imagen = ImageIO.read(jfc.getSelectedFile()).getScaledInstance(vista.getLblFoto().getWidth(),
                        vista.getLblFoto().getHeight(), Image.SCALE_DEFAULT);

                Icon icono = new ImageIcon(imagen);
                vista.getLblFoto().setIcon(icono);
                vista.getLblFoto().updateUI();
            } catch (IOException ex) {
                Logger.getLogger(ControllerSocios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void abrirDialogo(int ce) {
        String title;
        if (ce == 1) {
            title = "Crear nuevo Socio";
            vista.getDlgSocios().setName("crear");
        } else {
            title = "Editar Socio";
            vista.getDlgSocios().setName("editar");
            modificacion();
        }

        vista.getDlgSocios().setLocationRelativeTo(vista);
        vista.getDlgSocios().setSize(700, 500);
        vista.getDlgSocios().setTitle(title);
        vista.getDlgSocios().setVisible(true);
    }
    
    private void crearEditarPersona() {
        if ("crear".equals(vista.getDlgSocios().getName())) {
            //INSERTAR
            String cedula = vista.getTxtCedula().getText();
            String nombres = vista.getTxtNombres().getText();
            String direccion = vista.getTxtDireccion().getText();
            String telefono = vista.getTxtTelefono().getText();
            String disco = vista.getTxtDisco().getText();
            String placa = vista.getTxtPlaca().getText();
            String marca = vista.getTxtMarca().getText();

            ModelSocios socios = new ModelSocios();
                socios.setCedula(cedula);
                socios.setNombres(nombres);
                socios.setDireccion(direccion);
                socios.setTelefono(telefono);
                socios.setDisco(disco);
                socios.setPlaca(placa);
                socios.setMarca(marca);
            
            try {
                FileInputStream img = new FileInputStream(jfc.getSelectedFile());
                int largo = (int)jfc.getSelectedFile().length();
                socios.setImagen(img);
                socios.setLargo(largo);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ControllerSocios.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if (socios.crearSocioByte()) {
                vista.getDlgSocios().setVisible(false);
                JOptionPane.showMessageDialog(vista, "Socio Creado Satisfactoriamente");
                cargarSocios();
            } else {
                JOptionPane.showMessageDialog(vista, "No se pudo crear la persona");
            }

        } else if ("editar".equals(vista.getDlgSocios().getName())) {
            String cedula = vista.getTxtCedula().getText();
            String nombres = vista.getTxtNombres().getText();
            String direccion = vista.getTxtDireccion().getText();
            String telefono = vista.getTxtTelefono().getText();
            String disco = vista.getTxtDisco().getText();
            String placa = vista.getTxtPlaca().getText();
            String marca = vista.getTxtMarca().getText();

            ModelSocios editarSocio = new ModelSocios();

            editarSocio.setCedula(cedula);
            editarSocio.setNombres(nombres);
            editarSocio.setDireccion(direccion);
            editarSocio.setTelefono(telefono);
            editarSocio.setDisco(disco);
            editarSocio.setPlaca(placa);
            editarSocio.setMarca(marca);
            if (editarSocio.editarSocio()) {
                vista.getDlgSocios().setVisible(false);
                JOptionPane.showMessageDialog(vista, "Persona modificada correctamente");
            } else {
                JOptionPane.showMessageDialog(vista, "No se pudo modificar la persona");
            }
        }

    }
    
    private void eliminarSocio() {
        ModelSocios elisocio = new ModelSocios();
        int fila = vista.getTblSocios().getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Por favor, seleccione una fila");
        } else {
            String cedula = vista.getTblSocios().getValueAt(fila, 0).toString();
            elisocio.eliminarSocio(cedula);
            JOptionPane.showMessageDialog(vista, "Usuario Eliminado");
        }
    }
    
    private void modificacion() {
        int fila = vista.getTblSocios().getSelectedRow();
        if (fila < -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione una fila");
        } else {
            String datos = vista.getTblSocios().getValueAt(fila, 0).toString();

            List<Socios> milista = modelo.listarSocios();
            for (int i = 0; i < milista.size(); i++) {
                if (milista.get(i).getCedula().equals(datos)) {

                    vista.getTxtCedula().setText(milista.get(i).getCedula());
                    vista.getTxtNombres().setText(milista.get(i).getNombres());
                    vista.getTxtDireccion().setText(milista.get(i).getDireccion());
                    vista.getTxtTelefono().setText(milista.get(i).getTelefono());
                    vista.getTxtDisco().setText(milista.get(i).getDisco());
                    vista.getTxtPlaca().setText(milista.get(i).getPlaca());
                    vista.getTxtMarca().setText(milista.get(i).getMarca());
                    //vista.getLblFoto().set(milista.get(i).getFoto());

                    cargarSocios();
                }
            }
        }

    }
    
    private void cargarSocios() {
        vista.getTblSocios().setDefaultRenderer(Object.class, new ImageTabla());
        vista.getTblSocios().setRowHeight(100);

        DefaultTableModel tblModel;
        tblModel = (DefaultTableModel) vista.getTblSocios().getModel();
        tblModel.setNumRows(0);
        List<Socios> listap = modelo.listarSocios();
        Holder<Integer> i = new Holder<>(0);
        listap.stream().forEach(pe -> {

            tblModel.addRow(new Object[6]);
            vista.getTblSocios().setValueAt(pe.getCedula(), 0, 0);
            vista.getTblSocios().setValueAt(pe.getNombres(), 0, 1);
            vista.getTblSocios().setValueAt(pe.getDireccion(), 0, 2);
            vista.getTblSocios().setValueAt(pe.getTelefono(), 0, 3);
            vista.getTblSocios().setValueAt(pe.getDisco(), 0, 4);
            vista.getTblSocios().setValueAt(pe.getPlaca(), 0, 5);
            vista.getTblSocios().setValueAt(pe.getMarca(), 0, 6);
            Image foto=pe.getFoto();
            if(foto!=null){
                Image nimg= foto.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                ImageIcon icono = new ImageIcon(nimg);
                DefaultTableCellRenderer renderer= new DefaultTableCellRenderer();
                renderer.setIcon(icono);
                vista.getTblSocios().setValueAt(new JLabel(icono), i.value, 7);
            }else{
                vista.getTblSocios().setValueAt(null, i.value, 7);
            }
            i.value++;
        });

    }
    
    private void cancelar() {
        vista.getDlgSocios().dispose();
    }
    
    private void imprimirListaClientes(){
        ConectionPg connection = new ConectionPg();
                
     try{
         JasperReport jr = (JasperReport)JRLoader.loadObject(getClass().getResource("/view/reportes/Socios.jasper"));
         
         JasperPrint jp = JasperFillManager.fillReport(jr, null, connection.getCon());
         

         
     }catch(JRException ex){
         Logger.getLogger(ControllerSocios.class.getName()).log(Level.SEVERE, null, ex);
     }   
    }
}
