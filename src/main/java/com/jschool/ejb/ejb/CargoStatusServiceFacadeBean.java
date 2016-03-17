package com.jschool.ejb.ejb;

import com.jschool.ejb.JsonResponse;

import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;

/**
 * Created by infinity on 16.03.16.
 */
@Stateless
public class CargoStatusServiceFacadeBean {

    private Client client = ClientBuilder.newClient();

    private JsonResponse getResponse(String number, String status){
        String uri = "http://localhost:8081/rs";
        WebTarget target = client.target(uri);
        target = target.path("cargo/"+number+"/"+status);
        Invocation.Builder builder = target.request();
        return builder.get(JsonResponse.class);
    }

    public JsonResponse setCargoStatus(String number,String status){
        switch (status){
            case "loaded":
                return setLoadedStatus(number);
            case "delivered":
                return setDeliveredStatus(number);
            default:
                return null;
        }
    }

    private JsonResponse setLoadedStatus(String number){
        return getResponse(number,"loaded");
    }

    private JsonResponse setDeliveredStatus(String number){
        return getResponse(number,"delivered");
    }
}
