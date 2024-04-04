package steps;

import api.UserApi;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import json.CreatingUser;
import json.LogOut;

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

    @Step("удаление пользователя")
    public ValidatableResponse delete(String accessToken) {
        return userApi.delete(accessToken)
                .then();
    }

    @Step("авторизация пользователя")
    public ValidatableResponse login(String email, String password) {
        CreatingUser creatingUser = new CreatingUser();
        creatingUser.setPassword(password);
        creatingUser.setEmail(email);
        return userApi.login(creatingUser)
                .then();
    }

    @Step("выход из системы")
    public ValidatableResponse logOut(String accessToken, String refreshToken) {
        LogOut logOut  = new LogOut();
        logOut.setToken(refreshToken);
        return userApi.logOut(logOut,refreshToken)
                .then();
    }

    @Step("изменение данных пользователя")
    public ValidatableResponse updateDataUser(String email, String name, String password, String accessToken){
        CreatingUser creatingUser = new CreatingUser();
        creatingUser.setEmail(email);
        creatingUser.setName(name);
        creatingUser.setPassword(password);
        return userApi.updateInfoUser(creatingUser,accessToken)
                .then();
    }

}
