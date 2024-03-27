package steps;

import api.UserApi;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import json.CreatingUser;

public class UserSteps {
    private final UserApi userApi;

    public UserSteps(UserApi userApi) {
        this.userApi = userApi;
    }

    @Step("создание пользователя")
    public ValidatableResponse create(String email, String name, String password) {
        CreatingUser creatingUser = new CreatingUser();
        creatingUser.setEmail(email);
        creatingUser.setName(name);
        creatingUser.setPassword(password);
        return UserApi.create(creatingUser)
                .then();
    }

}
