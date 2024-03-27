import api.UserApi;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

public class LoginUserTest {
    UserSteps userSteps;
    private static List<String> accessTokens = new ArrayList<>();

    @Before
    public void setUp() {
        userSteps = new UserSteps(new UserApi());
    }

    @After
    public void cleanUp() {
        for (String accessToken : accessTokens) {
            userSteps.delete(accessToken);
        }
    }

    @Test
    @DisplayName("логин под существующим пользователем")
    public void loginUser() {
        String email = RandomStringUtils.randomAlphabetic(7).toLowerCase() + "@"
                + RandomStringUtils.randomAlphanumeric(5).toLowerCase() + "."
                + RandomStringUtils.randomAlphabetic(3).toLowerCase();
        String name = RandomStringUtils.randomAlphabetic(5);
        String password = RandomStringUtils.randomAlphabetic(10);

        userSteps.create(email, name, password)
                .statusCode(SC_OK)
                .body("success", Matchers.is(true))
                .body("user.email", Matchers.is(email))
                .body("user.name", Matchers.is(name))
                .extract().path("accessToken");

        String accessToken = userSteps.login(email, password)
                .statusCode(SC_OK)
                .body("success", Matchers.is(true))
                .body("user.email", Matchers.is(email))
                .body("user.name", Matchers.is(name))
                .extract().path("accessToken");

        accessTokens.add(accessToken);
    }

    @Test
    @DisplayName("логин с неверным логином")
    public void loginUserIncorrectEmailTest() {
        String email = RandomStringUtils.randomAlphabetic(7).toLowerCase() + "@"
                + RandomStringUtils.randomAlphanumeric(5).toLowerCase() + "."
                + RandomStringUtils.randomAlphabetic(3).toLowerCase();
        String name = RandomStringUtils.randomAlphabetic(5);
        String password = RandomStringUtils.randomAlphabetic(10);

        String accessToken = userSteps.create(email, name, password)
                .statusCode(SC_OK)
                .body("success", Matchers.is(true))
                .body("user.email", Matchers.is(email))
                .body("user.name", Matchers.is(name))
                .extract().path("accessToken");
        accessTokens.add(accessToken);

        String incorrectEmail = RandomStringUtils.randomAlphabetic(5);

        userSteps.login(incorrectEmail, password)
                .statusCode(SC_UNAUTHORIZED)
                .body("success", Matchers.is(false))
                .body("message", Matchers.is("email or password are incorrect"));

    }

    @Test
    @DisplayName("логин с неверным паролем")
    public void loginUserIncorrectPasswordTest() {
        String email = RandomStringUtils.randomAlphabetic(7).toLowerCase() + "@"
                + RandomStringUtils.randomAlphanumeric(5).toLowerCase() + "."
                + RandomStringUtils.randomAlphabetic(3).toLowerCase();
        String name = RandomStringUtils.randomAlphabetic(5);
        String password = RandomStringUtils.randomAlphabetic(10);

        String accessToken = userSteps.create(email, name, password)
                .statusCode(SC_OK)
                .body("success", Matchers.is(true))
                .body("user.email", Matchers.is(email))
                .body("user.name", Matchers.is(name))
                .extract().path("accessToken");
        accessTokens.add(accessToken);

        String incorrectPassword = RandomStringUtils.randomAlphabetic(5);

        userSteps.login(incorrectPassword, password)
                .statusCode(SC_UNAUTHORIZED)
                .body("success", Matchers.is(false))
                .body("message", Matchers.is("email or password are incorrect"));
    }

}