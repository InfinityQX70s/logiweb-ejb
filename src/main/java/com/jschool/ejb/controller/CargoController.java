package com.jschool.ejb.controller;

import com.jschool.ejb.JsonResponse;
import com.jschool.ejb.ejb.CargoStatusBean;
import com.jschool.ejb.ejb.DriverStatusBean;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by infinity on 18.03.16.
 */
@ManagedBean
public class CargoController {

    private Integer number;
    private String city;
    private String name;
    private String type;
    private String message;

    @EJB
    private CargoStatusBean cargoStatusBean;
    @EJB
    private DriverStatusBean driverStatusBean;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String redirectCargo(){
        return "cargo?faces-redirect=true";
    }

    public void setCargoStatus() throws IOException {
        cargoStatusBean.setCargoStatus(String.valueOf(number),type);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    public void beforePhase(PhaseEvent event) throws IOException {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
        String driver = (String) session.getAttribute("driver");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        if ( driver == null){
            ec.redirect("login.xhtml");
        }
        JsonResponse jsonResponse = driverStatusBean.getResponse(driver,"order");
        if (jsonResponse.getStatus().equals("done")){
            ec.redirect("select.xhtml");
        }else {
            number = jsonResponse.getCargoNumber();
            name = jsonResponse.getCargoName();
            city = jsonResponse.getCity();
            type = jsonResponse.getType();
        }
    }
}
