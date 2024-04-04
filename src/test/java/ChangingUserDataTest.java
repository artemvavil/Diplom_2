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

import static org.apache.http.HttpStatus.*;

public class ChangingUserDataTest {
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
    @DisplayName("изменение данных Email c авторизацией")
    public void updateDataUserEmailWithAuthorization() {
        String email = RandomStringUtils.randomAlphabetic(7).toLowerCase() + "@"
                + RandomStringUtils.randomAlphanumeric(5).toLowerCase() + "."
                + RandomStringUtils.randomAlphabetic(3).toLowerCase();
        String name = RandomStringUtils.randomAlphabetic(5);
        String password = RandomStringUtils.randomAlphabetic(10);

        userSteps.create(email, name, password);
        String accessToken = userSteps.login(email, password)
                .statusCode(SC_OK)
                .extract().path("accessToken");

        String newEmail = RandomStringUtils.randomAlphabetic(4).toLowerCase();

        userSteps.updateDataUser(newEmail, null, null, accessToken)
                .statusCode(SC_OK)
                .body("success", Matchers.is(true))
                .body("user.email", Matchers.is(newEmail))
                .body("user.name", Matchers.is(name));

        accessTokens.add(accessToken);
    }

    @Test
    @DisplayName("изменение данных Name c авторизацией")
    public void updateDataUserNamelWithAuthorization() {
        String email = RandomStringUtils.randomAlphabetic(7).toLowerCase() + "@"
                + RandomStringUtils.randomAlphanumeric(5).toLowerCase() + "."
                + RandomStringUtils.randomAlphabetic(3).toLowerCase();
        String name = RandomStringUtils.randomAlphabetic(5);
        String password = RandomStringUtils.randomAlphabetic(10);

        userSteps.create(email, name, password);
        String accessToken = userSteps.login(email, password)
                .statusCode(SC_OK)
                .extract().path("accessToken");

        String newName = RandomStringUtils.randomAlphabetic(4).toLowerCase();

        userSteps.updateDataUser(null, newName, null, accessToken)
                .statusCode(SC_OK)
                .body("success", Matchers.is(true))
                .body("user.email", Matchers.is(email))
                .body("user.name", Matchers.is(newName));

        accessTokens.add(accessToken);
    }

    @Test
    @DisplayName("изменение данных Password c авторизацией")
    public void updateDataUserPasswordlWithAuthorization() {
        String email = RandomStringUtils.randomAlphabetic(7).toLowerCase() + "@"
                + RandomStringUtils.randomAlphanumeric(5).toLowerCase() + "."
                + RandomStringUtils.randomAlphabetic(3).toLowerCase();
        String name = RandomStringUtils.randomAlphabetic(5);
        String password = RandomStringUtils.randomAlphabetic(10);

        userSteps.create(email, name, password);
        String accessToken = userSteps.login(email, password)
                .statusCode(SC_OK)
                .extract().path("accessToken");

        String newPassword = RandomStringUtils.randomAlphabetic(4).toLowerCase();

        userSteps.updateDataUser(null, null, newPassword, accessToken)
                .statusCode(SC_OK)
                .body("success", Matchers.is(true))
                .body("user.email", Matchers.is(email))
                .body("user.name", Matchers.is(name));

        accessToken = userSteps.login(email, newPassword)
                .statusCode(SC_OK)
                .body("success", Matchers.is(true))
                .body("user.email", Matchers.is(email))
                .body("user.name", Matchers.is(name))
                .extract().path("accessToken");

        accessTokens.add(accessToken);

    }

    @Test
    @DisplayName("изменение данных Email без авторизации")
    public void updateDataUserEmailWithoutAuthorization() {
        String email = RandomStringUtils.randomAlphabetic(7).toLowerCase() + "@"
                + RandomStringUtils.randomAlphanumeric(5).toLowerCase() + "."
                + RandomStringUtils.randomAlphabetic(3).toLowerCase();
        String name = RandomStringUtils.randomAlphabetic(5);
        String password = RandomStringUtils.randomAlphabetic(10);

        String accessToken = userSteps.create(email, name, password)
                .statusCode(SC_OK)
                .extract().path("accessToken");

        accessTokens.add(accessToken);

        String refreshToken = userSteps.login(email, password)
                .statusCode(SC_OK)
                .extract().path("refreshToken");

        userSteps.logOut(accessToken, refreshToken)
                .statusCode(SC_OK)
                .body("message", Matchers.is("Successful logout"));

        String newName = RandomStringUtils.randomAlphabetic(1);
        String newEmail = RandomStringUtils.randomAlphabetic(1).toLowerCase();

        userSteps.updateDataUser(newEmail, newName, null, refreshToken)
                .statusCode(SC_UNAUTHORIZED)
                .body("success", Matchers.is(false))
                .body("message", Matchers.is("You should be authorised"));
    }

    @Test
    @DisplayName("изменение Email на ту что уже используется")
    public void updateUserEmailWithExisted() {
        String email = RandomStringUtils.randomAlphabetic(7).toLowerCase() + "@"
                + RandomStringUtils.randomAlphanumeric(5).toLowerCase() + "."
                + RandomStringUtils.randomAlphabetic(3).toLowerCase();
        String name = RandomStringUtils.randomAlphabetic(5);
        String password = RandomStringUtils.randomAlphabetic(10);

        userSteps.create(email, name, password)
                .statusCode(SC_OK)
                .body("success", Matchers.is(true));


        String anotherEmail = RandomStringUtils.randomAlphabetic(7).toLowerCase() + "@"
                + RandomStringUtils.randomAlphanumeric(5).toLowerCase() + "."
                + RandomStringUtils.randomAlphabetic(3).toLowerCase();
        ;
        String anotherName = RandomStringUtils.randomAlphabetic(5);
        String anotherPassword = RandomStringUtils.randomAlphabetic(10);

        String anotherAccessToken = userSteps.create(anotherEmail, anotherName, anotherPassword)
                .statusCode(SC_OK)
                .body("success", Matchers.is(true))
                .extract().path("accessToken");

        accessTokens.add(anotherAccessToken);

        String accessToken = userSteps.login(email, password)
                .statusCode(SC_OK)
                .body("success", Matchers.is(true))
                .extract().path("accessToken");

        accessTokens.add(accessToken);

        userSteps.updateDataUser(anotherEmail, null, null, accessToken)
                .statusCode(SC_FORBIDDEN)
                .body("success", Matchers.is(false))
                .body("message", Matchers.is("User with such email already exists"));
    }

}
