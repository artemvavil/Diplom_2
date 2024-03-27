package api;

import endpoint.EndPoint;
import io.restassured.response.Response;
import json.CreatingUser;


public class UserApi extends BaseSteps {

    public static Response create(CreatingUser creatingUser) {
        return getRequestSpec()
                .body(creatingUser)
                .when()
                .post(EndPoint.USER);
    }

    public Response delete(String accestoken) {
        return getRequestSpec()
                .header("Authorization", accestoken)
                .when()
                .delete(EndPoint.ACTIONS_USER);
    }

    public Response login(CreatingUser creatingUser) {
        return getRequestSpec()
                .body(creatingUser)
                .when()
                .post(EndPoint.LOGIN);
    }

//    @Step("обновление информации о пользователе")
//    public Response updateInfUser(CreatingUser creatingUser, String accestoken) {
//        return baseRequest()
//                .header("Authorization", accestoken)
//                .body(creatingUser)
//                .when()
//                .patch(EndPoint.ACTIONS_USER);
//    }
//
//    @Step("выход из системы")
//    public Response logOut(LogOut logOut, String accestoken) {
//        return baseRequest()
//                .header("Authorization", accestoken)
//                .body(logOut)
//                .when()
//                .post(EndPoint.LOGOUT);
//    }
}
