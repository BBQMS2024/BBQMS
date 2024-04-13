package ba.unsa.etf.si.bbqms;

import ba.unsa.etf.si.bbqms.ws.controllers.AuthController;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {


    @BeforeAll
    public static void setup() {
        // Set the base URI and port for all test cases
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    public void testRegisterEndpoint() {
        AuthController.RegisterRequest registerRequest = new AuthController.RegisterRequest("test@example.com", "password");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(registerRequest)
                .when()
                .post("/api/v1/auth/register");

        int statusCode = response.getStatusCode();

        if(statusCode == 200){
            response.then()
                    .contentType(ContentType.JSON)
                    .body("email", equalTo("test@example.com"));
        }else {
            response.then()
                    .contentType(ContentType.JSON)
                    .statusCode(400)
                    .body("error", Matchers.containsString("Couldn't register user"));
        }
    }

    @Test
    public void testLoginEndpointWithValidCredentials() {
        AuthController.LoginRequest loginRequest = new AuthController.LoginRequest("test@example.com", "password");

        given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/api/v1/auth/login")
                .then()
                .statusCode(200);
    }
}
