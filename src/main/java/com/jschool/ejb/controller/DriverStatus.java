package com.jschool.ejb.controller;

import com.jschool.ejb.JsonResponse;
import com.jschool.ejb.ejb.DriverStatusServiceFacadeBean;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 * Created by infinity on 16.03.16.
 */
@ManagedBean
public class DriverStatus {

    @EJB
    private DriverStatusServiceFacadeBean driverStatusServiceFacadeBean;
    private String message;

    public void setDriverStatusDriving(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
        String number = (String)session.getAttribute("driver");
        JsonResponse jsonResponse = driverStatusServiceFacadeBean.setStatus(String.valueOf(number),"driving");
        message = jsonResponse.getResult();
    }

    public void setDriverStatusShift(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
        String number = (String) session.getAttribute("driver");
        JsonResponse jsonResponse = driverStatusServiceFacadeBean.setStatus(String.valueOf(number),"shift");
        message = jsonResponse.getResult();
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }
}
