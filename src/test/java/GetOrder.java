import api.OrderApi;
import api.UserApi;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import json.GetIngredientsResponse;
import json.GetOrdersResponse;
import json.Ingredient;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;
import steps.UserSteps;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

public class GetOrder {
    OrderSteps orderSteps;
    UserSteps userSteps;
    private static List<String> accessTokens = new ArrayList<>();

    @Before
    public void setUp() {

        userSteps = new UserSteps(new UserApi());
        orderSteps = new OrderSteps(new OrderApi());

    }

    @After
    public void cleanUp() {
        for (String accessToken : accessTokens) {
            userSteps.delete(accessToken);
        }
    }

    @Test
    @DisplayName("получение заказа без авторизации")
    public void getOrderWithoutAuthorization(){
         orderSteps.getOrderWithoutAuthorization()
                .statusCode(SC_UNAUTHORIZED)
                .body("message", Matchers.is("You should be authorised"));
    }

    @Test
    @DisplayName("получение заказа c авторизацией")
    public void getOrderWithAuthorization(){
        String email = RandomStringUtils.randomAlphabetic(7).toLowerCase() + "@"
                + RandomStringUtils.randomAlphanumeric(5).toLowerCase() + "."
                + RandomStringUtils.randomAlphabetic(3).toLowerCase();
        String name = RandomStringUtils.randomAlphabetic(5);
        String password = RandomStringUtils.randomAlphabetic(10);

        String accessToken = userSteps.create(email, name, password)
                .statusCode(SC_OK)
                .extract().path("accessToken");

        accessTokens.add(accessToken);

        userSteps.login(email,password)
                .statusCode(SC_OK);

        GetIngredientsResponse ingredients;
        ingredients = orderSteps.getIngredient().extract().as(GetIngredientsResponse.class);
        List<String> ingredientsHash = new ArrayList<>();
        for (Ingredient ingredient: ingredients.getData()) {
            ingredientsHash.add(ingredient.get_id());
        }

        int randomNum = ThreadLocalRandom.current().nextInt(1, ingredientsHash.size() + 1);

        int number = orderSteps.createOrdersWithToken(ingredientsHash.subList(0, randomNum), accessToken)
                .statusCode(SC_OK)
                .body("success", Matchers.is(true))
                .extract().path("orders.number");

        GetOrdersResponse orders = orderSteps.getOrderWithAuthorization(accessToken)
                .statusCode(SC_OK)
                .extract().as(GetOrdersResponse.class);

        Assert.assertEquals(number, (int)orders.getOrders().get(orders.getOrders().size() - 1).getNumber());

        orderSteps.getOrderWithAuthorization(accessToken)
                .statusCode(SC_OK)
                .body("total", Matchers.is(orders.getOrders().size()));
    }
}
