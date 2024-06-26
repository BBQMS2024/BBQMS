package ba.unsa.etf.si.bbqms.api;

import ba.unsa.etf.si.bbqms.ws.controllers.TicketController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TicketControllerTest extends BaseSetup {
    private int ticketId;

    @BeforeEach
    public void Should_CreateTicketSuccessfully_When_ValidParametersProvided() {
        TicketController.NewTicketRequest newTicketRequest =
                new TicketController.NewTicketRequest(1,1,"ExponentPushToken[*********]");

        ticketId = given()
                    .body(newTicketRequest)
                .when()
                    .header("Authorization", "Bearer " + generateSuperAdminToken())
                    .post("api/v1/tickets")
                .then()
                    .statusCode(200)
                    .and()
                    .assertThat()
                    .body("ticket", notNullValue())
                    .body("ticket.service.id" ,equalTo(1))
                    .body("ticket.branch.id", equalTo(1))
                .extract()
                    .path("ticket.id");
    }

    @AfterEach
    public void Should_CancelTicketSuccessfully_When_GivenValidTicketId() {
        given()
            .pathParam("ticketId",ticketId)
        .when()
            .header("Authorization", "Bearer " + generateSuperAdminToken())
            .delete("api/v1/tickets/{ticketId}")
        .then()
            .statusCode(200);
    }

    @Test
    public void Should_RetrieveTicketsForDeviceToken_When_SuccessfulResponse_And_JsonContainsRequiredKeyValuePair() {
        given()
            .pathParam("deviceToken","ExponentPushToken[*********]")
        .when()
            .header("Authorization", "Bearer " + generateSuperAdminToken())
            .get("api/v1/tickets/devices/{deviceToken}")
        .then()
            .statusCode(200)
            .body("$",hasSize(greaterThan(0)))
            .body("ticket.id", hasItem(ticketId));
    }

    @Test
    public void Should_RetrieveTicketById_When_ValidTicketIdProvided() {
        given()
            .pathParam("ticketId",ticketId)
        .when()
            .header("Authorization", "Bearer " + generateSuperAdminToken())
            .get("api/v1/tickets/{ticketId}")
        .then()
            .statusCode(200)
            .body("id", equalTo(ticketId));
    }
}