package com.jschool.ejb.controller;

import com.jschool.ejb.JsonResponse;
import com.jschool.ejb.ejb.DriverStatusBean;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by infinity on 18.03.16.
 */
@ManagedBean
public class DriverController {

    private Integer number;
    private String message;
    private String curentStatus;

    @EJB
    private DriverStatusBean driverStatusBean;

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

    public String getCurentStatus() {
        return curentStatus;
    }

    public void setCurentStatus(String curentStatus) {
        this.curentStatus = curentStatus;
    }

    public String redirectDriver(){
        return "driver?faces-redirect=true";
    }

    public void beforePhase(PhaseEvent event) throws IOException {
        HttpSession session = getSession();
        String driver = (String) session.getAttribute("driver");
        if (driver == null){
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect("login.xhtml");
        }else {
            number = Integer.parseInt(driver);
            curentStatus = (String) session.getAttribute("status");
        }
    }

    public void beforeLogin(PhaseEvent event) throws IOException {
        HttpSession session = getSession();
        String driver = (String) session.getAttribute("driver");
        if (driver != null){
            number = Integer.parseInt(driver);
            curentStatus = (String) session.getAttribute("status");
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect("select.xhtml");
        }
    }

    public String logoutDriver(){
        HttpSession session = getSession();
        String number = (String)session.getAttribute("driver");
        driverStatusBean.logoutDriver(Integer.valueOf(number));
        session.invalidate();
        return "login?faces-redirect=true";
    }

    public String loginDriver(){
        JsonResponse jsonResponse = driverStatusBean.loginDriver(number);
        if (jsonResponse.getStatus().equals("ok")){
            HttpSession session = getSession();
            session.setAttribute("driver", String.valueOf(number));
            session.setAttribute("status", "shift");
            return "select?faces-redirect=true";
        }else {
            message = jsonResponse.getResult();
            return "";
        }
    }

    public void setDriverStatusDriving() throws IOException {
        HttpSession session = getSession();
        String number = (String)session.getAttribute("driver");
        JsonResponse jsonResponse = driverStatusBean.setStatus(String.valueOf(number),"driving");
        message = jsonResponse.getResult();
        if (jsonResponse.getStatus().equals("ok")){
            session.setAttribute("status", "driving");
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect("select.xhtml");
        }
    }

    public void setDriverStatusShift() throws IOException {
        HttpSession session = getSession();
        String number = (String) session.getAttribute("driver");
        JsonResponse jsonResponse = driverStatusBean.setStatus(String.valueOf(number),"shift");
        message = jsonResponse.getResult();
        if (jsonResponse.getStatus().equals("ok")){
            session.setAttribute("status", "shift");
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect("select.xhtml");
        }
    }

    private HttpSession getSession(){
        return (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
    }
}
