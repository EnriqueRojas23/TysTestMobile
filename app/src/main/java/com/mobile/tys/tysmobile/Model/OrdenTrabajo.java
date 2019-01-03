package com.mobile.tys.tysmobile.Model;


import java.util.Date;

public class    OrdenTrabajo {
    public OrdenTrabajo(long idordentrabajo, String numcp, String razonsocial, String fecharegistro, String estado, String origen, String destino, int idestado) {
        this.idordentrabajo = idordentrabajo;
        this.numcp = numcp;
        int found = razonsocial.indexOf("'");
        if(found>0)
        {
            razonsocial = razonsocial.replace("'", "");
        }

        this.razonsocial = razonsocial;
        this.fecharegistro = fecharegistro;
        this.estado = estado;
        this.origen = origen;
        this.destino = destino;
        this.idestado = idestado;
    }

    private long idordentrabajo;
    private String numcp ;
    private String razonsocial ;
    private String fecharegistro;
    private String estado ;
    private String origen ;
    private String destino ;
    private String file;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }



    public int getIdestado() {
        return idestado;
    }

    public void setIdestado(int idestado) {
        this.idestado = idestado;
    }

    private int idestado;

    public long getIdordentrabajo() {
        return idordentrabajo;
    }

    public void setIdordentrabajo(long idordentrabajo) {
        this.idordentrabajo = idordentrabajo;
    }

    public String getNumcp() {
        return numcp;
    }

    public void setNumcp(String numcp) {
        this.numcp = numcp;
    }

    public String getRazonsocial() {
        return razonsocial;
    }

    public void setRazonsocial(String razonsocial) {
        this.razonsocial = razonsocial;
    }

    public String getFecharegistro() {
        return fecharegistro;
    }

    public void setFecharegistro(String fecharegistro) {
        this.fecharegistro = fecharegistro;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }




}
