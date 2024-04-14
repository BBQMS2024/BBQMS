package ba.unsa.etf.si.bbqms.api;

import ba.unsa.etf.si.bbqms.ws.controllers.AuthController;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.nullValue;

public class BaseSetup {
    @BeforeAll
    public static void setup() {
        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(8080)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();

        ResponseSpecification responseSpecification = new ResponseSpecBuilder()
                .expectResponseTime(lessThan (200000L))
                .build();

        RestAssured.requestSpecification = requestSpecification;
        RestAssured.responseSpecification = responseSpecification;
    }

    public static String generateSuperAdminToken() {
        AuthController.RegisterRequest registerRequest = new AuthController.RegisterRequest("test@example.com", "password");

        return  given()
                .body(registerRequest)
                .when()
                .post("/api/v1/auth/login")
                .then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("token", Matchers.not (nullValue()))
                .extract()
                .path("token");
    }
}
