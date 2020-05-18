package listas;

import java.io.Serializable;
import java.text.CollationKey;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Parte implements Serializable, Comparable<Parte> {

    private String nombreCliente;

    private String direccionCliente;

    private Date fechaCreacion;

    private String trabajador;

    private String estado;

    private Date fechaReparacion;

    private int duracion;

    private List<Material> materiales;

    public Parte(String nombreCliente, String direccionCliente, Date fechaCreacion, String trabajador, String estado, Date fechaReparacion, int duracion, List<Material> materiales) {
        this.nombreCliente = nombreCliente;
        this.direccionCliente = direccionCliente;
        this.fechaCreacion = fechaCreacion;
        this.trabajador = trabajador;
        this.estado = estado;
        this.fechaReparacion = fechaReparacion;
        this.duracion = duracion;
        this.materiales = materiales;
    }

    public Parte(String nombreCliente, String direccionCliente, Date fechaCreacion, String trabajador, String estado, Date fechaReparacion) {
        this.nombreCliente = nombreCliente;
        this.direccionCliente = direccionCliente;
        this.fechaCreacion = fechaCreacion;
        this.trabajador = trabajador;
        this.estado = estado;
        this.fechaReparacion = fechaReparacion;
    }

    public Parte() {
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(String trabajador) {
        this.trabajador = trabajador;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaReparacion() {
        return fechaReparacion;
    }

    public void setFechaReparacion(Date fechaReparacion) {
        this.fechaReparacion = fechaReparacion;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public List<Material> getMateriales() {
        return materiales;
    }

    public void setMateriales(List<Material> materiales) {
        this.materiales = materiales;
    }

    @Override
    public String toString() {
        if (estado.equals("R")) {
            return formatoColumna(nombreCliente, 25) + formatoColumna(direccionCliente, 25) + formatoColumna(fechaCadena(fechaCreacion), 12)
                    + formatoColumna(trabajador, 25) + formatoColumna(estado, 7)
                    + formatoColumna(fechaCadena(fechaReparacion), 12) + formatoColumna(Integer.toString(duracion), 15) + materiales;
        } else {
            return formatoColumna(nombreCliente, 25) + formatoColumna(direccionCliente, 25) + formatoColumna(fechaCadena(fechaCreacion), 12)
                    + formatoColumna(trabajador, 25) + formatoColumna(estado, 7)
                    + formatoColumna(fechaCadena(fechaReparacion), 12);
        }
    }

    public static String formatoColumna(String texto, int largo) {
        int restaLargo = largo - texto.length();
        for (int indice = 0; indice < restaLargo; indice++) {
            texto = texto + " ";
        }
        return texto;
    }

    public static String fechaCadena(Date fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String cadena = sdf.format(fecha);
        return cadena;

    }

    public int compareTo(Parte pa) {

        Collator collator = Collator.getInstance();

        CollationKey key1 = collator.getCollationKey(fechaCadena(pa.getFechaCreacion()));
        CollationKey key2 = collator.getCollationKey(fechaCadena(this.getFechaCreacion()));

        return key1.compareTo(key2);
    }

}
