/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.ModelSocios;
import View.MenuPrincipal;
import View.ViewSocios;

/**
 *
 * @author Edison
 */
public class ControllerMenuPrincipal {

    MenuPrincipal vista;

    public ControllerMenuPrincipal(MenuPrincipal vista) {
        this.vista = vista;
        vista.setVisible(true);
    }
    
    public void iniciaControl(){
        vista.getMniSocios().addActionListener(l->crudSocios());
        vista.getMnSocios().addActionListener(l->crudSocios());
    }
    
    private void crudSocios(){
        ModelSocios modeloCSocios = new ModelSocios();
        ViewSocios vistaCSocios = new ViewSocios();

        vista.getDskMenu().add(vistaCSocios);
        ControllerSocios controladorCSocios = new ControllerSocios(modeloCSocios, vistaCSocios);
        controladorCSocios.iniciaControl();
    }
}
