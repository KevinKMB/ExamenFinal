/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Controller.ControllerMenuPrincipal;
import View.MenuPrincipal;

/**
 *
 * @author Edison
 */
public class Menu {
    public static void main(String[] args) {
        //Instancio las clases del Modelo y la Vista.
        MenuPrincipal vista = new MenuPrincipal();
        ControllerMenuPrincipal controller = new ControllerMenuPrincipal(vista);
        controller.iniciaControl();

    }
}
