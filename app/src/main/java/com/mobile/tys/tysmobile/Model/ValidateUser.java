package com.mobile.tys.tysmobile.Model;

import java.util.Date;

public class ValidateUser {

    private Integer usr_int_id ;
    private String usr_str_red;
    private boolean usr_int_pwdvalido;
    private Integer usr_int_rolinvalido;
    private String usr_str_recordarpwd;
    private Integer usr_int_aprobado;
    private boolean usr_int_bloqueado;
    private Date usr_dat_fecvctopwd;
    private Integer usr_int_numintentos;

    private String token ;


    public String getToken() { return token; }

    public void setToken(String token) { this.token = token ;}

    public Integer getUsr_int_id() {
        return usr_int_id;
    }

    public void setUsr_int_id(Integer usr_int_id) {
        this.usr_int_id = usr_int_id;
    }

    public String getUsr_str_red() {
        return usr_str_red;
    }

    public void setUsr_str_red(String usr_str_red) {
        this.usr_str_red = usr_str_red;
    }

    public boolean getUsr_int_pwdvalido() {
        return usr_int_pwdvalido;
    }

    public void setUsr_int_pwdvalido(boolean usr_int_pwdvalido) {
        this.usr_int_pwdvalido = usr_int_pwdvalido;
    }

    public Integer getUsr_int_rolinvalido() {
        return usr_int_rolinvalido;
    }

    public void setUsr_int_rolinvalido(Integer usr_int_rolinvalido) {
        this.usr_int_rolinvalido = usr_int_rolinvalido;
    }

    public String getUsr_str_recordarpwd() {
        return usr_str_recordarpwd;
    }

    public void setUsr_str_recordarpwd(String usr_str_recordarpwd) {
        this.usr_str_recordarpwd = usr_str_recordarpwd;
    }

    public Integer getUsr_int_aprobado() {
        return usr_int_aprobado;
    }

    public void setUsr_int_aprobado(Integer usr_int_aprobado) {
        this.usr_int_aprobado = usr_int_aprobado;
    }

    public boolean getUsr_int_bloqueado() {
        return usr_int_bloqueado;
    }

    public void setUsr_int_bloqueado(boolean usr_int_bloqueado) {
        this.usr_int_bloqueado = usr_int_bloqueado;
    }

    public Date getUsr_dat_fecvctopwd() {
        return usr_dat_fecvctopwd;
    }

    public void setUsr_dat_fecvctopwd(Date usr_dat_fecvctopwd) {
        this.usr_dat_fecvctopwd = usr_dat_fecvctopwd;
    }

    public Integer getUsr_int_numintentos() {
        return usr_int_numintentos;
    }

    public void setUsr_int_numintentos(Integer usr_int_numintentos) {
        this.usr_int_numintentos = usr_int_numintentos;
    }

    public Date getUsr_dat_ultfeclogin() {
        return usr_dat_ultfeclogin;
    }

    public void setUsr_dat_ultfeclogin(Date usr_dat_ultfeclogin) {
        this.usr_dat_ultfeclogin = usr_dat_ultfeclogin;
    }

    private Date usr_dat_ultfeclogin ;

    public ValidateUser(Integer usr_int_id, String usr_str_red, boolean usr_int_pwdvalido, Integer usr_int_rolinvalido, String usr_str_recordarpwd, Integer usr_int_aprobado, boolean usr_int_bloqueado, Date usr_dat_fecvctopwd, Integer usr_int_numintentos, Date usr_dat_ultfeclogin) {
        this.usr_int_id = usr_int_id;
        this.usr_str_red = usr_str_red;
        this.usr_int_pwdvalido = usr_int_pwdvalido;
        this.usr_int_rolinvalido = usr_int_rolinvalido;
        this.usr_str_recordarpwd = usr_str_recordarpwd;
        this.usr_int_aprobado = usr_int_aprobado;
        this.usr_int_bloqueado = usr_int_bloqueado;
        this.usr_dat_fecvctopwd = usr_dat_fecvctopwd;
        this.usr_int_numintentos = usr_int_numintentos;
        this.usr_dat_ultfeclogin = usr_dat_ultfeclogin;
    }
}