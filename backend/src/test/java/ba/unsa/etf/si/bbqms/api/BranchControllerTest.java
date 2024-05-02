package ba.unsa.etf.si.bbqms.api;

import ba.unsa.etf.si.bbqms.ws.controllers.BranchController;
import org.junit.jupiter.api.*;

import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BranchControllerTest extends BaseSetup{
    static private int branchId;
    static private int stationId;
    private final int invalidBranchId = -1;
    private final int invalidStationId = -1;
    private final static String tenantCode = "DFLT";
    private final String otherTenantCode = "OTHR";

    @BeforeAll
    static public void setUp() {
        BranchController.BranchRequest branchRequest =
                new BranchController.BranchRequest("Branch Name", Collections.emptyList());

        branchId = given()
                    .body(branchRequest)
                    .pathParam("tenantCode", tenantCode)
                .when()
                    .header("Authorization", "Bearer " + generateSuperAdminToken())
                    .post("api/v1/branches/{tenantCode}")
                .then()
                    .statusCode(200)
                    .and()
                    .assertThat()
                    .body("name", equalTo("Branch Name"))
                    .body("tellerStations", equalTo(Collections.emptyList()))
                .extract()
                    .path("id");

        BranchController.StationRequest stationRequest =
                new BranchController.StationRequest("Station Name");

        stationId = given()
                    .body(stationRequest)
                    .pathParam("tenantCode", tenantCode)
                    .pathParam("branchId", branchId)
                .when()
                    .header("Authorization", "Bearer " + generateSuperAdminToken())
                    .post("api/v1/branches/{tenantCode}/{branchId}/stations")
                .then()
                    .statusCode(200)
                    .and()
                    .assertThat()
                    .body("name", equalTo("Station Name"))
                .extract()
                    .path("id");
    }

    @AfterAll
    static public void tearDown() {
        given()
            .pathParam("tenantCode", tenantCode)
            .pathParam("branchId", branchId)
        .when()
            .header("Authorization", "Bearer " + generateSuperAdminToken())
            .delete("api/v1/branches/{tenantCode}/{branchId}")
        .then()
            .statusCode(200);
    }

    @Test
    public void branchCreation_WithInvalidParameters_Fails() {
        BranchController.BranchRequest branchRequest =
                new BranchController.BranchRequest("", Collections.emptyList());

        given()
            .body(branchRequest)
            .pathParam("tenantCode", tenantCode)
        .when()
            .header("Authorization", "Bearer " + generateSuperAdminToken())
            .post("api/v1/branches/{tenantCode}")
        .then()
            .statusCode(400);
    }

    @Test
    public void branchListing_ForTenantCode_Succeeds() {
        given()
            .pathParam("tenantCode", tenantCode)
        .when()
            .header("Authorization", "Bearer " + generateSuperAdminToken())
            .get("api/v1/branches/{tenantCode}")
        .then()
            .statusCode(200)
            .body("$",hasSize(greaterThan(0)));
    }

    @Test
    public void branchUpdate_WithValidIdAndParameters_Succeeds() {
        BranchController.BranchRequest branchRequest =
                new BranchController.BranchRequest("New Branch Name", null);

        given()
            .body(branchRequest)
            .pathParam("tenantCode", tenantCode)
            .pathParam("branchId", branchId)
        .when()
            .header("Authorization", "Bearer " + generateSuperAdminToken())
            .put("api/v1/branches/{tenantCode}/{branchId}")
        .then()
            .statusCode(200)
            .and()
            .assertThat()
            .body("name", equalTo("New Branch Name"));
    }

    @Test
    public void branchUpdate_WithInvalidId_Fails() {
        BranchController.BranchRequest branchRequest =
                new BranchController.BranchRequest("Invalid Branch Name", null);

        given()
            .body(branchRequest)
            .pathParam("tenantCode", tenantCode)
            .pathParam("branchId", invalidBranchId)
        .when()
            .header("Authorization", "Bearer " + generateSuperAdminToken())
            .put("api/v1/branches/{tenantCode}/{branchId}")
        .then()
            .statusCode(400);
    }

    @Test
    public void branchUpdate_WithInvalidName_Fails() {
        BranchController.BranchRequest branchRequest =
                new BranchController.BranchRequest("", Collections.emptyList());

        given()
            .body(branchRequest)
            .pathParam("tenantCode", tenantCode)
            .pathParam("branchId", branchId)
        .when()
            .header("Authorization", "Bearer " + generateSuperAdminToken())
            .put("api/v1/branches/{tenantCode}/{branchId}")
        .then()
            .statusCode(400);
    }

    @Test
    public void branchRemoval_WithInvalidTenantCode_Fails() {
        given()
            .pathParam("tenantCode", otherTenantCode)
            .pathParam("branchId", branchId)
        .when()
            .header("Authorization", "Bearer " + generateSuperAdminToken())
            .delete("api/v1/branches/{tenantCode}/{branchId}")
        .then()
            .statusCode(400);
    }

    @Test
    public void branchServicesListing_WithValidIdAndParameters_Succeeds() {
        given()
            .pathParam("tenantCode", tenantCode)
            .pathParam("branchId", branchId)
        .when()
            .header("Authorization", "Bearer " + generateSuperAdminToken())
            .get("api/v1/branches/{tenantCode}/{branchId}/services")
        .then()
            .statusCode(200)
            .and()
            .assertThat()
            .body("$", hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    public void branchServicesListing_WithInvalidBranchId_Fails() {
        given()
            .pathParam("tenantCode", tenantCode)
            .pathParam("branchId", invalidBranchId)
        .when()
            .header("Authorization", "Bearer " + generateSuperAdminToken())
            .get("api/v1/branches/{tenantCode}/{branchId}/services")
        .then()
            .statusCode(400);
    }

    @Test
    public void stationCreation_WithInvalidTenantCode_Fails() {
        BranchController.StationRequest stationRequest =
                new BranchController.StationRequest("New Station");

        given()
            .body(stationRequest)
            .pathParam("tenantCode", otherTenantCode)
            .pathParam("branchId", branchId)
        .when()
            .header("Authorization", "Bearer " + generateSuperAdminToken())
            .post("api/v1/branches/{tenantCode}/{branchId}/stations")
        .then()
            .statusCode(400);
    }

    @Test
    public void stationCreation_WithInvalidBranchId_Fails() {
        BranchController.StationRequest stationRequest =
                new BranchController.StationRequest("New Station");

        given()
            .body(stationRequest)
            .pathParam("tenantCode", tenantCode)
            .pathParam("branchId", invalidBranchId)
        .when()
            .header("Authorization", "Bearer " + generateSuperAdminToken())
            .post("api/v1/branches/{tenantCode}/{branchId}/stations")
        .then()
            .statusCode(400);
    }

    @Test
    public void stationUpdate_WithValidIdAndParameters_Succeeds() {
        BranchController.StationRequest stationRequest =
                new BranchController.StationRequest("Updated Station Name");

        given()
            .body(stationRequest)
            .pathParam("tenantCode", tenantCode)
            .pathParam("branchId", branchId)
            .pathParam("stationId", stationId)
        .when()
            .header("Authorization", "Bearer " + generateSuperAdminToken())
            .put("api/v1/branches/{tenantCode}/{branchId}/stations/{stationId}")
        .then()
            .statusCode(200)
            .and()
            .assertThat()
            .body("name", equalTo("Updated Station Name"));
    }

    @Test
    public void stationUpdate_WithInvalidId_Fails() {
        BranchController.StationRequest stationRequest =
                new BranchController.StationRequest("Updated Station Name");

        given()
            .body(stationRequest)
            .pathParam("tenantCode", tenantCode)
            .pathParam("branchId", branchId)
            .pathParam("stationId", invalidStationId)
        .when()
            .header("Authorization", "Bearer " + generateSuperAdminToken())
            .put("api/v1/branches/{tenantCode}/{branchId}/stations/{stationId}")
       .then()
            .statusCode(400);
    }

    @Test
    public void stationUpdate_WithInvalidTenantCode_Fails() {
        BranchController.StationRequest stationRequest =
                new BranchController.StationRequest("Updated Station Name");

        given()
            .body(stationRequest)
            .pathParam("tenantCode", otherTenantCode)
            .pathParam("branchId", branchId)
            .pathParam("stationId", stationId)
        .when()
            .header("Authorization", "Bearer " + generateSuperAdminToken())
            .put("api/v1/branches/{tenantCode}/{branchId}/stations/{stationId}")
        .then()
            .statusCode(400);
    }

    @Test
    public void stationRemoval_WithInvalidTenantCode_Fails() {
        given()
            .pathParam("tenantCode", otherTenantCode)
            .pathParam("branchId", branchId)
            .pathParam("stationId", stationId)
        .when()
            .header("Authorization", "Bearer " + generateSuperAdminToken())
            .delete("api/v1/branches/{tenantCode}/{branchId}/stations/{stationId}")
        .then()
            .statusCode(400);
    }

    @Test
    public void getBranchQueue_WithValidBranchId_ReturnsQueue() {
        given()
                .pathParam("tenantCode", tenantCode)
                .pathParam("branchId", branchId)
                .when()
                .header("Authorization", "Bearer " + generateSuperAdminToken())
                .get("/api/v1/branches/{tenantCode}/{branchId}/queue")
                .then()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    public void getBranchQueue_WithInvalidBranchId_ReturnsBadRequest() {
        given()
            .pathParam("tenantCode", tenantCode)
            .pathParam("branchId", invalidBranchId)
        .when()
            .header("Authorization", "Bearer " + generateSuperAdminToken())
            .get("/api/v1/branches/{tenantCode}/{branchId}/queue")
        .then()
            .statusCode(400);
    }
}
