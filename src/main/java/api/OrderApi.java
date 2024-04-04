package api;

import endpoint.EndPoint;
import io.restassured.response.Response;
import json.CreateOrderRequest;
import json.Orders;

import static api.BaseSteps.getRequestSpec;

public class OrderApi {
    public static Response getIngredient(){
        return getRequestSpec()
                .when()
                .get(EndPoint.INGREDIENTS);
    }

    public static Response getOrders(Orders orders){
        return getRequestSpec()
                .body(orders)
                .when()
                .get(EndPoint.ORDER);
    }

    public static Response getOrdersWithToken(CreateOrderRequest orders, String accestoken){
        return getRequestSpec()
                .header("Authorization", accestoken)
                .body(orders)
                .when()
                .get(EndPoint.ORDER);
    }
    public Response getAllOrdersWithToken(String accessToken) {
        return getRequestSpec()
                .header("Authorization", accessToken)
                .when()
                .get(EndPoint.ORDER);
    }

    public Response getAllOrdersWithoutToken() {
        return getRequestSpec()
                .when()
                .get(EndPoint.ORDER);
    }


}
