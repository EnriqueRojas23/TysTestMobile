package com.mobile.tys.tysmobile.Model;

import java.util.Date;

public class ConfirmarEntrega {


     public ConfirmarEntrega()
     {}
    public ConfirmarEntrega(String numcp,int idmaestroetapa, int idordentrabajo, String descripcion, String horaetapa, String recurso, String documento, Date fechaetapa, String fecharegistro, int idusuarioregistro, boolean visible, int idestado, int idusuarioentrega
            , long idetapa, String razonsocial, String file) {

        this.idmaestroetapa = idmaestroetapa;
        this.idordentrabajo = idordentrabajo;
        this.descripcion = descripcion;
        this.horaetapa = horaetapa;
        this.recurso = recurso;
        this.documento = documento;
        this.fechaetapa = fechaetapa;
        this.fecharegistro = fecharegistro;
        this.idusuarioregistro = idusuarioregistro;
        this.visible = visible;
        this.idestado = idestado;
        this.idusuarioentrega = idusuarioentrega;
        this.idetapa = idetapa;
        this.razonsocial = razonsocial;
        this.numcp = numcp;
        this.file  = file;
    }

    public int idmaestroetapa ;
    public long idordentrabajo ;
    public String descripcion ;
    public String horaetapa ;
    public String recurso ;
    public String documento ;
    public Date fechaetapa ;
    public String fecharegistro ;
    public int idusuarioregistro;
    public boolean visible ;
    public int idestado ;
    public int idusuarioentrega ;
    public String file ;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }



    public String getNumcp() {
        return numcp;
    }

    public void setNumcp(String numcp) {
        this.numcp = numcp;
    }

    public String numcp;

    public String getRazonsocial() {
        return razonsocial;
    }

    public void setRazonsocial(String razonsocial) {
        this.razonsocial = razonsocial;
    }

    public String razonsocial;



    public long idetapa ;

    public long getIdetapa() {
        return idetapa;
    }

    public void setIdetapa(long idetapa) {
        this.idetapa = idetapa;
    }

    public int getIdmaestroetapa() {
        return idmaestroetapa;
    }

    public void setIdmaestroetapa(int idmaestroetapa) {
        this.idmaestroetapa = idmaestroetapa;
    }

    public long getIdordentrabajo() {
        return idordentrabajo;
    }

    public void setIdordentrabajo(long idordentrabajo) {
        this.idordentrabajo = idordentrabajo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getHoraetapa() {
        return horaetapa;
    }

    public void setHoraetapa(String horaetapa) {
        this.horaetapa = horaetapa;
    }

    public String getRecurso() {
        return recurso;
    }

    public void setRecurso(String recurso) {
        this.recurso = recurso;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Date getFechaetapa() {
        return fechaetapa;
    }

    public void setFechaetapa(Date fechaetapa) {
        this.fechaetapa = fechaetapa;
    }

    public String getFecharegistro() {
        return fecharegistro;
    }

    public void setFecharegistro(String fecharegistro) {
        this.fecharegistro = fecharegistro;
    }

    public int getIdusuarioregistro() {
        return idusuarioregistro;
    }

    public void setIdusuarioregistro(int idusuarioregistro) {
        this.idusuarioregistro = idusuarioregistro;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getIdestado() {
        return idestado;
    }

    public void setIdestado(int idestado) {
        this.idestado = idestado;
    }

    public int getIdusuarioentrega() {
        return idusuarioentrega;
    }

    public void setIdusuarioentrega(int idusuarioentrega) {
        this.idusuarioentrega = idusuarioentrega;
    }


}