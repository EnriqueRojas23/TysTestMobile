package com.mobile.tys.tysmobile.Model;

public class GuiaRechazada {
    public int getGuiaremisioncliente() {
        return guiaremisioncliente;
    }

    public void setGuiaremisioncliente(int guiaremisioncliente) {
        this.guiaremisioncliente = guiaremisioncliente;
    }

    public int getIdordentrabajo() {
        return idordentrabajo;
    }

    public void setIdordentrabajo(int idordentrabajo) {
        this.idordentrabajo = idordentrabajo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }


    private int guiaremisioncliente ;
    private int idordentrabajo ;
    private int cantidad;

    public String getNumeroGuia() {
        return NumeroGuia;
    }

    public void setNumeroGuia(String numeroGuia) {
        NumeroGuia = numeroGuia;
    }

    public GuiaRechazada( int idordentrabajo, int cantidad, String numeroGuia) {

        this.idordentrabajo = idordentrabajo;
        this.cantidad = cantidad;
        NumeroGuia = numeroGuia;
    }

    private String NumeroGuia ;

}
