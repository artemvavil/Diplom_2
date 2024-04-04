import api.OrderApi;
import api.UserApi;
import io.qameta.allure.junit4.DisplayName;
import json.GetIngredientsResponse;
import json.Ingredient;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;
import steps.UserSteps;
import org.hamcrest.Matchers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.apache.http.HttpStatus.*;

public class CreateOrderTest {
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
    @DisplayName("создание заказа без авторизации с ингредиентами")
    public void createOrderWithoutAuthWithIngredientsTest() {
        GetIngredientsResponse ingredients;
        ingredients = orderSteps.getIngredient().extract().as(GetIngredientsResponse.class);
        List<String> ingredientsHash = new ArrayList<>();
        for (Ingredient ingredient : ingredients.getData()) {
            ingredientsHash.add(ingredient.get_id());
        }

        int randomNum = ThreadLocalRandom.current().nextInt(1, ingredientsHash.size() + 1);

        orderSteps.createOrder(ingredientsHash.subList(0, randomNum))
                .statusCode(SC_UNAUTHORIZED)
                .body("success", Matchers.is(false));
    }

    @Test
    @DisplayName("создание заказа с авторизацией")
    public void createOrderWithAuthTest() {
        String email = RandomStringUtils.randomAlphabetic(10).toLowerCase() + "@" + RandomStringUtils.randomAlphabetic(6).toLowerCase() + '.' + RandomStringUtils.randomAlphabetic(3).toLowerCase();
        String name = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);

        userSteps.create(email, name, password)
                .statusCode(SC_OK);

        String accessToken = userSteps.login(email, password)
                .statusCode(SC_OK)
                .extract().path("accessToken");

        accessTokens.add(accessToken);

        GetIngredientsResponse ingredients;
        ingredients = orderSteps.getIngredient().extract().as(GetIngredientsResponse.class);
        List<String> ingredientsHash = new ArrayList<>();
        for (Ingredient ingredient : ingredients.getData()) {
            ingredientsHash.add(ingredient.get_id());
        }

        int randomNum = ThreadLocalRandom.current().nextInt(1, ingredientsHash.size() + 1);

        orderSteps.createOrdersWithToken(ingredientsHash.subList(0, randomNum), accessToken)
                .statusCode(SC_OK)
                .body("success", Matchers.is(true));
    }

    @Test
    @DisplayName("создание заказа с авторизацией и без ингредиентов")
    public void createOrderWithoutAuthWithoutIngredientsTest() {
        List<String> ingredientsHash = new ArrayList<>();

        orderSteps.createOrder(ingredientsHash)
                .statusCode(SC_UNAUTHORIZED)
                .body("success", Matchers.is(false));
    }

    @Test
    @DisplayName("создание заказа без авторизации и без ингредиентов")
    public void createOrderWithoutAuthWithWrongIngredientsTest() {
        List<String> ingredientsHash = new ArrayList<>();
        ingredientsHash.add("wrong_ingredient_1");
        ingredientsHash.add("wrong_ingredient_2");

        orderSteps.createOrder(ingredientsHash)
                .statusCode(SC_UNAUTHORIZED)
                .body("message", Matchers.is("You should be authorised"));

    }
}
