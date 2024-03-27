package api;

import endpoint.EndPoint;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public abstract class BaseSteps {
    protected static RequestSpecification getRequestSpec() {
        return given()
                .baseUri(EndPoint.BaseURL)
                .contentType(ContentType.JSON);
    }
}
