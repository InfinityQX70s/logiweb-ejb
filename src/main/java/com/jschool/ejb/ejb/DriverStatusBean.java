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
public class DriverStatusBean {

    private static Client client = ClientBuilder.newClient();

    public JsonResponse getResponse(String number, String status) {
        String uri = "http://logiweb.herokuapp.com/rs";
        WebTarget target = client.target(uri);
        if (status.isEmpty())
            target = target.path("driver/" + number);
        else
            target = target.path("driver/" + number + "/" + status);
        Invocation.Builder builder = target.request();
        return builder.get(JsonResponse.class);
    }

    public JsonResponse setStatus(String number, String status) {
        switch (status) {
            case "shift":
                return setShiftStatus(number);
            case "rest":
                return setRestStatus(number);
            case "driving":
                return setDrivingStatus(number);
            default:
                return null;
        }
    }

    public JsonResponse logoutDriver(Integer number) {
        return setRestStatus(String.valueOf(number));
    }

    public JsonResponse loginDriver(Integer number) {
        JsonResponse jsonResponse = getResponse(String.valueOf(number), "");
        if (jsonResponse.getStatus().equals("ok")) {
            jsonResponse = setShiftStatus(String.valueOf(number));
            return jsonResponse;
        } else
            return jsonResponse;
    }

    private JsonResponse setShiftStatus(String number) {
        return getResponse(number, "shift");
    }

    private JsonResponse setRestStatus(String number) {
        return getResponse(number, "rest");
    }

    private JsonResponse setDrivingStatus(String number) {
        return getResponse(number, "driving");
    }

}
