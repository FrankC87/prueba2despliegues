/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listas;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * FRANCISCO JAVIER CRUZ REDONDO
 */
public class Listas {

    private static List<Parte> partes = new ArrayList<Parte>();
    private static File fichero = new File("partes.dat");

    public static void main(String[] args) {
        if (fichero.exists()) {
            deserializa();
        }

        int o;
        do {
            Scanner entrada = new Scanner(System.in);
            imprimeMenu();
            o = entrada.nextInt();
            opcionesMenu(o);
        } while (o != 99);

        serializa();
    }

    //MOSTRAMOS MENU POR PANTALLA
    public static void imprimeMenu() {

        System.out.println("******************************************************");
        System.out.println("* 1 .-  DAR ALTA PARTE");
        System.out.println("* 2 .-  CERRAR PARTE PENDIENTE");
        System.out.println("* 3 .-  DAR BAJA PARTE");
        System.out.println("* 4 .-  BUSCAR PARTE POR TRABAJADOR");
        System.out.println("* 5 .-  ORDENAR PARTES POR FECHA");
        System.out.println("* 6 .-  MOSTRAR PARTES PENDIENTES CON MAS DE 10 DIAS");
        System.out.println("* 7 .-  LISTAR PARTES");
        System.out.println("* 99.-  SALIR DEL PROGRAMA");
        System.out.println("******************************************************");
        System.out.print("*Opcion:");

    }

    //OPCIONES DEL MENU
    public static void opcionesMenu(int o) {
        switch (o) {
            case 1:
                System.out.println("**ALTA**");
                if (nuevoParte()) {
                    System.out.print("ALTA VALIDA\n");
                } else {
                    System.out.print("ALTA NO VALIDA\n");
                }
                break;
            case 2:
                System.out.println("**CERRAR PARTE**");
                if (cerrarParte()) {
                    System.out.print("PARTE CERRADO\n");
                } else {
                    System.out.print("PARTE NO SE PUEDE CERRAR\n");
                }
                break;
            case 3:
                System.out.println("**BAJA**");
                if(borrarParte()) {
                    System.out.print("PARTE BORRADO\n");
                } else {
                    System.out.print("PARTE NO BORRADO\n");
                }

                break;
            case 4:
                System.out.println("**BUSCAR PARTE**");
                System.out.print(buscarPartes());
                break;
            case 5:
                System.out.println("**ORDENAR PARTES**");
                Collections.sort(partes,Collections.reverseOrder());
                System.out.print(muestraPartes());
                break;
            case 6:
                System.out.println("**PENDIENTES**");
                System.out.print(muestraPartesPendientes());
                break;
            case 7:
                System.out.println("**LISTA DE PARTES**");
                System.out.print(muestraPartes());
                break;

        }
    }

    //SERIALIZAMOS
    public static void serializa() {
        FileOutputStream mifil = null;
        ObjectOutputStream miobj = null;
        try {
            mifil = new FileOutputStream(fichero);
            miobj = new ObjectOutputStream(mifil);
            miobj.writeObject(partes);
            miobj.close();
            mifil.close();
            System.out.println("DATOS GUARDADOS");
        } catch (Exception e) {
            System.out.println("Error entrada/salida\n" + e.toString());
        }

    }

    //DESERIALIZAMOS
    public static void deserializa() {
        FileInputStream mifil = null;
        ObjectInputStream miobj = null;
        try {
            mifil = new FileInputStream(fichero);
            miobj = new ObjectInputStream(mifil);
            boolean seguir = true;
            while (seguir) {
                try {
                    partes = (List<Parte>) miobj.readObject();
                } catch (ClassNotFoundException ex) {
                    System.out.println("Clase no encontrada");
                } catch (EOFException ex) {
                    seguir = false;
                    System.out.println("DATOS CARGADOS");
                }
            }

            miobj.close();
            mifil.close();
        } catch (Exception e) {
            System.out.println("Error entrada/salida\n" + e.toString());
        }
    }

    //METODO PARA MOSTRAR TODOS LOS PARTES
    public static String muestraPartes() {

        String retorno = Cabecera();
        int i = 1;
        Iterator itera = partes.iterator();
        while (itera.hasNext()) {
            String cad = itera.next().toString();
            retorno = retorno + Parte.formatoColumna(Integer.toString(i), 6) + cad + "\n";
            i++;
        }

        return retorno;
    }

    //METODO PARA CREAR PARTES NUEVOS E INTRODUCIRLOS EN LA LISTA
    private static boolean nuevoParte() {

        Scanner entrada = new Scanner(System.in);
        Scanner entradaNum = new Scanner(System.in);
        String nombreC, direccionC, trabajador, estado, nombreMa, opcion;
        String fecha1, fecha2;
        Date fechaC, fechaR;
        int duracion;
        double cantidad;
        List<Material> materiales;
        Parte realizado;
        boolean control = true;

        try {

            System.out.print("Nombre del Cliente:");
            nombreC = entrada.nextLine();
            System.out.print("Direccion del Cliente:");
            direccionC = entrada.nextLine();
            System.out.print("Fecha Creacion (DD/MM/YYYY):");
            fecha1 = entrada.nextLine();
            fechaC = validaFecha(fecha1);
            System.out.print("Trabajador:");
            trabajador = entrada.nextLine();
            //COMPROBAMOS Y RESTRINGIMOS QUE EL ESTADO DEL PARTE SEA EL ADECUADO
            do {
                System.out.print("Estado (P/R):");
                estado = entrada.nextLine();
                if (!estado.equals("P") && !estado.equals("R")) {
                    System.out.print("AVISO: Estado no valido\n");
                }
            } while (!estado.equals("P") && !estado.equals("R"));
            //COMPROBAMOS Y RESTRINGIMOS QUE LA FECHA DE REPARACION NO SEA ANTERIOR A LA DE CREACION
            do {
                System.out.print("Fecha Reparacion (DD/MM/YYYY):");
                fecha2 = entrada.nextLine();
                fechaR = validaFecha(fecha2);
                if (fechaR.before(fechaC)) {
                    System.out.print("AVISO: La fecha de reparacion debe de ser posterior a la de creacion\n");
                }
            } while (fechaR.before(fechaC));

            //SI LA INCIDENCIA ESTA REALIZADA SE NECESITAN DATOS ADICIONALES SINO CREAMOS EL PARTE CON LA INFORMACION OBTENIDA
            if (estado.equals("R")) {
                materiales = new ArrayList<>();
                System.out.print("Duracion (min):");
                duracion = entradaNum.nextInt();
                System.out.println("-Materiales empleados");
                do {
                    Scanner entradaM = new Scanner(System.in);
                    Scanner entradaMNum = new Scanner(System.in);
                    System.out.print("  Material:");
                    nombreMa = entradaM.nextLine();
                    System.out.print("  Cantidad:");
                    cantidad = entradaMNum.nextDouble();
                    Material nuevo = new Material(nombreMa, cantidad);
                    materiales.add(nuevo);
                    System.out.print("  Nuevo material(s/n):");
                    opcion = entrada.next();
                } while (opcion.equals("s"));

                realizado = new Parte(nombreC, direccionC, fechaC, trabajador, estado, fechaR, duracion, materiales);
            } else {
                realizado = new Parte(nombreC, direccionC, fechaC, trabajador, estado, fechaR);
            }

            partes.add(realizado);

        } catch (Exception ex) {
            System.out.println("LOS DATOS NO SON VALIDOS");
            control = false;
        }

        return control;
    }

    //METODO PARA VALIDAR Y TRANSFORMAR LAS FECHAS INTRODUCIDAS
    public static Date validaFecha(String fecha) {
        Pattern patron = Pattern.compile("([0-9]{2})/([0-9]{2})/([0-9]{4})");
        Date fechafinal;
        int dia = 0, mes = 0, anio = 0;
        boolean control = false;

        Scanner entrada = new Scanner(System.in);
        do {
            Matcher m = patron.matcher(fecha);
            if (m.matches()) {
                dia = Integer.parseInt(m.group(1));
                mes = Integer.parseInt(m.group(2));
                anio = Integer.parseInt(m.group(3));
                control = true;
            } else {
                System.out.print("Fecha no valida, Introduzca fecha valida(DD/MM/YYYY):");
                fecha = entrada.next();
            }
        } while (!control);

        fechafinal = new Date(anio - 1900, mes - 1, dia);

        return fechafinal;
    }

    //CABECERA UTILIZADA A LA HORA DE MOSTRAR PARTES
    public static String Cabecera() {
        return Parte.formatoColumna("ORDEN", 6) + Parte.formatoColumna("CLIENTE", 25) + Parte.formatoColumna("DIREC.CLI.", 25) + Parte.formatoColumna("FECH.CRE.", 12)
                + Parte.formatoColumna("TRABAJADOR", 25) + Parte.formatoColumna("ESTADO", 7)
                + Parte.formatoColumna("FECH.REP.", 12) + Parte.formatoColumna("DURACION(min)", 15) + Parte.formatoColumna("MATERIALES", 12) + "\n";
    }

    //MOSTRAMOS PARTES PENDIENTES CON MAS DE 10 DIAS
    public static String muestraPartesPendientes() {
        String retorno = Cabecera();
        int i = 1, dias;
        Date actual = new Date();
        for (Parte parte : partes) {
            //Calculamos el numero de dias entre la fecha de creacion y el dia actual
            dias = (int) ((parte.getFechaCreacion().getTime() - actual.getTime()) / 86400000) * (-1);
            //Si el parte esta pendiente y han pasado 10 o mas dias lo mostramos
            if (parte.getEstado().equals("P") && dias >= 10) {
                String cad = parte.toString();
                retorno = retorno + Parte.formatoColumna(Integer.toString(i), 6) + cad + "\n";
            }
            i++;
        }
        return retorno;
    }

    //METODO PARA BUSCAR PARTES SEGUN EL TRABAJADOR
    public static String buscarPartes() {
        Scanner entrada = new Scanner(System.in);
        String retorno = Cabecera();
        int i = 1;
        String nombre;
        System.out.print("Introduzca nombre del trabajador:");
        nombre = entrada.nextLine();
        for (Parte parte : partes) {

            if (parte.getTrabajador().equals(nombre)) {
                String cad = parte.toString();
                retorno = retorno + Parte.formatoColumna(Integer.toString(i), 6) + cad + "\n";
            }
            i++;
        }
        return retorno;
    }

    //METODO PARA CERRAR UN PARTE PENDIENTE SEGUN SU POSICION
    public static boolean cerrarParte() {
        Scanner entrada = new Scanner(System.in);
        Scanner entradaNum = new Scanner(System.in);
        String nombreMa;
        double cantidad;
        String opcion;
        int i = 1, orden;
        boolean control = false;

        System.out.print("Introduzca orden del parte:");
        orden = entradaNum.nextInt();

        for (Parte parte : partes) {

            if (parte.getEstado().equals("P") && i == orden) {
                control = true;

                List<Material> materiales = new ArrayList<>();

                try {

                    System.out.print("Duracion (min):");
                    int duracion = entradaNum.nextInt();
                    System.out.println("-Materiales empleados");
                    do {
                        Scanner entradaM = new Scanner(System.in);
                        Scanner entradaMNum = new Scanner(System.in);
                        System.out.print("  Material:");
                        nombreMa = entradaM.nextLine();
                        System.out.print("  Cantidad:");
                        cantidad = entradaMNum.nextDouble();
                        Material nuevo = new Material(nombreMa, cantidad);
                        materiales.add(nuevo);
                        System.out.print("  Nuevo material(s/n):");
                        opcion = entrada.next();
                    } while (opcion.equals("s"));

                    parte.setEstado("R");
                    parte.setDuracion(duracion);
                    parte.setMateriales(materiales);

                } catch (Exception ex) {
                    System.out.println("LOS DATOS NO SON VALIDOS");
                    control = false;
                }
            }
            i++;
        }
        return control;
    }
    
    //METODO PARA BORRAR UN PARTE
    public static boolean borrarParte() {
        
        Scanner entrada = new Scanner(System.in);

        boolean control = false;
        int orden, i = 1;
        
        try {
            System.out.print("Introduzca orden del parte:");
            orden = entrada.nextInt();

            if (orden < partes.size() && orden > 0) {
                partes.remove(orden - 1);
                control = true;
            }
        } catch (Exception ex) {
            System.out.println("LOS DATOS NO SON VALIDOS");
            control = false;
        }

        return control;
    }

}
