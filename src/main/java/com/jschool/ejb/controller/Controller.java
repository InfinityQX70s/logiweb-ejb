package com.jschool.ejb.controller;

import com.jschool.ejb.JsonResponse;
import com.jschool.ejb.ejb.DriverStatusServiceFacadeBean;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@ManagedBean
@SessionScoped
public class Controller {

    @EJB
    private DriverStatusServiceFacadeBean driverStatusServiceFacadeBean;
    private Integer number;
    private String message;

    public String driver(){
        return "driver?faces-redirect=true";
    }

    public String cargo(){
        return "cargo?faces-redirect=true";
    }


    public String logoutDriver(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
        String number = (String)session.getAttribute("driver");
        driverStatusServiceFacadeBean.logoutDriver(Integer.valueOf(number));
        session.invalidate();
        return "login?faces-redirect=true";
    }

    public String loginDriver(){
        JsonResponse jsonResponse = driverStatusServiceFacadeBean.loginDriver(number);
        if (jsonResponse.getStatus().equals("ok")){
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                    .getExternalContext().getSession(false);
            session.setAttribute("driver", String.valueOf(number));
            return "select?faces-redirect=true";
        }else {
            message = jsonResponse.getResult();
            return "";
        }
    }

    public void beforePhase(PhaseEvent event) throws IOException {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
        String driver = (String) session.getAttribute("driver");
        if (driver == null){
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect("login.xhtml");
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
