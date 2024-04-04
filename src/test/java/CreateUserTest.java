import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import api.UserApi;
import steps.UserSteps;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;

public class CreateUserTest {
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
    @DisplayName("создать уникального пользователя")
    public void creatingUser() {
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
    }

    @Test
    @DisplayName("создать пользователя, который уже зарегистрирован")
    public void createExistUser() {
        String email = RandomStringUtils.randomAlphabetic(7).toLowerCase() + "@"
                + RandomStringUtils.randomAlphanumeric(5).toLowerCase() + "."
                + RandomStringUtils.randomAlphabetic(3).toLowerCase();
        String name = RandomStringUtils.randomAlphabetic(5);
        String password = RandomStringUtils.randomAlphabetic(10);

        String accessToken = userSteps.create(email, name, password)
                .statusCode(SC_OK)
                .body("success", Matchers.is(true))
                .extract().path("accessToken");

        userSteps.create(email, name, password)
                .statusCode(SC_FORBIDDEN)
                .body("success", Matchers.is(false))
                .body("message", Matchers.is("User already exists"));

        accessTokens.add(accessToken);
    }

    @Test
    @DisplayName("создать пользователя и не заполнить одно из обязательных полей - Email")
    public void createUserWithoutEmail() {
        String email = null;
        String name = RandomStringUtils.randomAlphabetic(5);
        String password = RandomStringUtils.randomAlphabetic(10);

        userSteps.create(email, name, password)
                .statusCode(SC_FORBIDDEN)
                .body("success", Matchers.is(false))
                .body("message", Matchers.is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("создать пользователя и не заполнить одно из обязательных полей - Name")
    public void createUserWithoutName() {
        String email = RandomStringUtils.randomAlphabetic(7).toLowerCase() + "@"
                + RandomStringUtils.randomAlphanumeric(5).toLowerCase() + "."
                + RandomStringUtils.randomAlphabetic(3).toLowerCase();
        String name = null;
        String password = RandomStringUtils.randomAlphabetic(10);

        userSteps.create(email, name, password)
                .statusCode(SC_FORBIDDEN)
                .body("success", Matchers.is(false))
                .body("message", Matchers.is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("создать пользователя и не заполнить одно из обязательных полей - Password")
    public void createUserWithoutPassword() {
        String email = RandomStringUtils.randomAlphabetic(7).toLowerCase() + "@"
                + RandomStringUtils.randomAlphanumeric(5).toLowerCase() + "."
                + RandomStringUtils.randomAlphabetic(3).toLowerCase();
        String name = RandomStringUtils.randomAlphabetic(5);
        String password = null;

        userSteps.create(email, name, password)
                .statusCode(SC_FORBIDDEN)
                .body("success", Matchers.is(false))
                .body("message", Matchers.is("Email, password and name are required fields"));
    }

}
