package steps;

import api.OrderApi;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import json.CreateOrderRequest;
import json.Orders;

import java.util.List;

public class OrderSteps {
    private final OrderApi orderApi;

    public OrderSteps(OrderApi orderApi) {
        this.orderApi = orderApi;
    }

    @Step("получение ингредиентов")
    public ValidatableResponse getIngredient() {
        return orderApi.getIngredient()
                .then();
    }

    @Step("создание заказа")
    public ValidatableResponse createOrder(List<String> ingredient) {
        Orders requestBody = new Orders();
        requestBody.setIngredients(ingredient);
        return OrderApi.getOrders(requestBody)
                .then();
    }

    @Step("создание заказа с авторизацией")
    public ValidatableResponse createOrdersWithToken(List<String> ingredient, String accestoken) {
        CreateOrderRequest requestBody = new CreateOrderRequest();
        requestBody.setIngredients(ingredient);
        return orderApi.getOrdersWithToken(requestBody,accestoken)
                .then();
    }

    @Step("получение заказа без авторизации")
    public ValidatableResponse getOrderWithoutAuthorization() {
        return orderApi.getAllOrdersWithoutToken()
                .then();
    }

    @Step("получение заказа без авторизации")
    public ValidatableResponse getOrderWithAuthorization(String accessToken) {
        return orderApi.getAllOrdersWithToken(accessToken)
                .then();
    }

}
