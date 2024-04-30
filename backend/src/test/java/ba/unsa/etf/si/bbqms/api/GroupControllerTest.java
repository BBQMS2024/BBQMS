package ba.unsa.etf.si.bbqms.api;

import ba.unsa.etf.si.bbqms.ws.controllers.BranchController;
import ba.unsa.etf.si.bbqms.ws.models.BranchGroupCreateDto;
import ba.unsa.etf.si.bbqms.ws.models.BranchGroupUpdateDto;
import ba.unsa.etf.si.bbqms.ws.models.ServiceRequestDto;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.greaterThan;

public class GroupControllerTest extends BaseSetup {
    private int branchGroupId;
    private static Integer branchId;
    private static Integer serviceId;
    private final static String tenantCode = "DFLT";
    private final static String invalidTenantCode = ".";

    @BeforeAll
    public static void createResources() {
        final BranchController.BranchRequest branchRequest =
                new BranchController.BranchRequest("Dummy branch", null);

        branchId = given()
                            .contentType(ContentType.JSON)
                            .pathParam("tenantCode", tenantCode)
                            .body(branchRequest)
                        .when()
                            .header("Authorization", "Bearer " + generateSuperAdminToken())
                            .post("api/v1/branches/{tenantCode}")
                        .then()
                            .statusCode(200)
                        .extract()
                            .path("id");

        final ServiceRequestDto serviceRequestDto =
                new ServiceRequestDto("Dummy service");

        serviceId = given()
                            .contentType(ContentType.JSON)
                            .pathParam("code", tenantCode)
                            .body(serviceRequestDto)
                        .when()
                            .header("Authorization", "Bearer " + generateSuperAdminToken())
                            .post("api/v1/tenants/{code}/services")
                        .then()
                            .statusCode(200)
                        .extract()
                            .path("id");
    }

    @AfterAll
    public static void deleteResources() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("tenantCode", tenantCode)
                .pathParam("branchId", branchId)
            .when()
                .header("Authorization", "Bearer " + generateSuperAdminToken())
                .delete("api/v1/branches/{tenantCode}/{branchId}")
            .then()
                .statusCode(200);

        given()
                .contentType(ContentType.JSON)
                .pathParam("code", tenantCode)
                .pathParam("id", serviceId)
            .when()
                .header("Authorization", "Bearer " + generateSuperAdminToken())
                .delete("api/v1/tenants/{code}/services/{id}")
            .then()
                .statusCode(200);
    }

    @BeforeEach
    public void createBranchGroup_ValidNameBranchIdsAndServiceIds_ReturnsOkResponse() {
        final String name = "Dummy Branch Group";
        final Set<Long> branchIds = Set.of(branchId.longValue());
        final Set<Long> serviceIds = Set.of(serviceId.longValue());

        final BranchGroupCreateDto newBranchGroupRequest =
                new BranchGroupCreateDto(name, serviceIds, branchIds);

        branchGroupId = given()
                .pathParam("tenantCode", tenantCode)
                .body(newBranchGroupRequest)
            .when()
                .header("Authorization", "Bearer " + generateSuperAdminToken())
                .post("api/v1/groups/{tenantCode}")
            .then()
                .statusCode(200)
                .and()
            .assertThat()
                .body("name" ,equalTo(name))
                .body("branches.id", hasItem(branchId))
                .body("services.id", hasItem(serviceId))
            .extract()
                .path("id");
    }

    @AfterEach
    public void deleteBranchGroup_ValidBranchGroupId_ReturnsOkResponse() {
        given()
                .pathParam("tenantCode", tenantCode)
                .pathParam("branchGroupId", branchGroupId)
            .when()
                .header("Authorization", "Bearer " + generateSuperAdminToken())
                .delete("api/v1/groups/{tenantCode}/{branchGroupId}")
            .then()
                .statusCode(200);
    }

    @Test
    public void getAllBranchGroups_ValidTenantCode_ReturnsOkResponse() {
        given()
                .pathParam("tenantCode", tenantCode)
            .when()
                .header("Authorization", "Bearer " + generateSuperAdminToken())
                .get("api/v1/groups/{tenantCode}")
            .then()
                .statusCode(200)
                .body("$",hasSize(greaterThan(0)));
    }

    @Test
    public void addBranchGroup_NonExistentService_ReturnsBadRequest() {
        final String name = "Dummy Branch Group";
        final Set<Long> branchIds = Set.of(branchId.longValue());
        final Set<Long> serviceIds = Set.of(-1L);

        final BranchGroupCreateDto newBranchGroupRequest =
                new BranchGroupCreateDto(name, serviceIds, branchIds);

       given()
                .pathParam("tenantCode", tenantCode)
                .body(newBranchGroupRequest)
            .when()
                .header("Authorization", "Bearer " + generateSuperAdminToken())
                .post("api/v1/groups/{tenantCode}")
            .then()
                .statusCode(400);
    }

    @Test
    public void addBranchGroup_InvalidTenantCode_ReturnsBadRequest() {
        final String name = "Dummy Branch Group";
        final Set<Long> branchIds = Set.of(branchId.longValue());
        final Set<Long> serviceIds = Set.of(serviceId.longValue());

        final BranchGroupCreateDto newBranchGroupRequest =
                new BranchGroupCreateDto(name, serviceIds, branchIds);

        given()
                .pathParam("tenantCode", invalidTenantCode)
                .body(newBranchGroupRequest)
            .when()
                .header("Authorization", "Bearer " + generateSuperAdminToken())
                .post("api/v1/groups/{tenantCode}")
            .then()
                .statusCode(400);
    }

    @Test
    public void addBranchGroup_NonExistentBranch_ReturnsBadRequest() {
        final String name = "Dummy Branch Group";
        final Set<Long> branchIds = Set.of(-1L);
        final Set<Long> serviceIds = Set.of(serviceId.longValue());

        final BranchGroupCreateDto newBranchGroupRequest =
                new BranchGroupCreateDto(name, serviceIds, branchIds);

        given()
                .pathParam("tenantCode", tenantCode)
                .body(newBranchGroupRequest)
            .when()
                .header("Authorization", "Bearer " + generateSuperAdminToken())
                .post("api/v1/groups/{tenantCode}")
            .then()
                .statusCode(400);
    }

    @Test
    public void addServiceToBranchGroup_ValidServiceId_ReturnsOkResponse() {
        given()
                .pathParam("tenantCode", tenantCode)
                .pathParam("groupId", branchGroupId)
                .pathParam("serviceId", serviceId)
            .when()
                .header("Authorization", "Bearer " + generateSuperAdminToken())
                .put("api/v1/groups/{tenantCode}/{groupId}/services/{serviceId}")
            .then()
                .statusCode(200)
                .body("services.id", hasItem(serviceId));
    }

    @Test
    public void addServiceToBranchGroup_NonExistentServiceId_ReturnsBadRequest() {
        final int serviceId = -1;

        given()
                .pathParam("tenantCode", tenantCode)
                .pathParam("groupId", branchGroupId)
                .pathParam("serviceId", serviceId)
            .when()
                .header("Authorization", "Bearer " + generateSuperAdminToken())
                .put("api/v1/groups/{tenantCode}/{groupId}/services/{serviceId}")
            .then()
                .statusCode(400);
    }

    @Test
    public void addServiceToBranchGroup_InvalidTenantCode_ReturnsBadRequest() {
        given()
                .pathParam("tenantCode", invalidTenantCode)
                .pathParam("groupId", branchGroupId)
                .pathParam("serviceId", serviceId)
            .when()
                .header("Authorization", "Bearer " + generateSuperAdminToken())
                .put("api/v1/groups/{tenantCode}/{groupId}/services/{serviceId}")
            .then()
                .statusCode(400);
    }

    @Test
    public void addBranchToBranchGroup_ValidBranchId_ReturnsOkResponse() {
        given()
                .pathParam("tenantCode", tenantCode)
                .pathParam("groupId", branchGroupId)
                .pathParam("branchId", branchId)
            .when()
                .header("Authorization", "Bearer " + generateSuperAdminToken())
                .put("api/v1/groups/{tenantCode}/{groupId}/branches/{branchId}")
            .then()
                .statusCode(200)
                .body("branches.id", hasItem(branchId));
    }

    @Test
    public void addBranchToBranchGroup_NonExistentBranchId_ReturnsBadRequest() {
        final int branchId = -1;

        given()
                .pathParam("tenantCode", tenantCode)
                .pathParam("groupId", branchGroupId)
                .pathParam("branchId", branchId)
            .when()
                .header("Authorization", "Bearer " + generateSuperAdminToken())
                .put("api/v1/groups/{tenantCode}/{groupId}/branches/{branchId}")
            .then()
                .statusCode(400);
    }

    @Test
    public void addBranchToBranchGroup_InvalidTenantCode_ReturnsBadRequest() {
        given()
                .pathParam("tenantCode", invalidTenantCode)
                .pathParam("groupId", branchGroupId)
                .pathParam("branchId", branchId)
            .when()
                .header("Authorization", "Bearer " + generateSuperAdminToken())
                .put("api/v1/groups/{tenantCode}/{groupId}/branches/{branchId}")
            .then()
                .statusCode(400);
    }

    @Test
    public void updateBranchGroupName_Name_ReturnsOkResponse() {
        final String newName = "New Branch Group";

        final BranchGroupUpdateDto branchGroupUpdateDto =
                new BranchGroupUpdateDto(newName);

        given()
                .pathParam("tenantCode", tenantCode)
                .pathParam("branchGroupId", branchGroupId)
                .body(branchGroupUpdateDto)
            .when()
                .header("Authorization", "Bearer " + generateSuperAdminToken())
                .put("api/v1/groups/{tenantCode}/{branchGroupId}")
            .then()
                .statusCode(200)
                .body("name", equalTo(newName));
    }

    @Test
    public void updateBranchGroupName_InvalidTenantCode_ReturnsBadRequest() {
        final String newName = "New Branch Group";

        final BranchGroupUpdateDto branchGroupUpdateDto =
                new BranchGroupUpdateDto(newName);

        given()
                .pathParam("tenantCode", invalidTenantCode)
                .pathParam("branchGroupId", branchGroupId)
                .body(branchGroupUpdateDto)
            .when()
                .header("Authorization", "Bearer " + generateSuperAdminToken())
                .put("api/v1/groups/{tenantCode}/{branchGroupId}")
            .then()
                .statusCode(400);
    }

    @Test
    public void removeServiceFromBranchGroup_ValidServiceId_ReturnsOkResponse() {
        given()
                .pathParam("tenantCode", tenantCode)
                .pathParam("branchGroupId", branchGroupId)
                .pathParam("serviceId", serviceId)
            .when()
                .header("Authorization", "Bearer " + generateSuperAdminToken())
                .delete("api/v1/groups/{tenantCode}/{branchGroupId}/services/{serviceId}")
            .then()
                .statusCode(200)
                .body("services.id", not(hasItem(serviceId)));
    }

    @Test
    public void removeServiceFromBranchGroup_InvalidServiceId_ReturnsBadRequest() {
        final int serviceId = -1;

        given()
                .pathParam("tenantCode", tenantCode)
                .pathParam("branchGroupId", branchGroupId)
                .pathParam("serviceId", serviceId)
            .when()
                .header("Authorization", "Bearer " + generateSuperAdminToken())
                .delete("api/v1/groups/{tenantCode}/{branchGroupId}/services/{serviceId}")
            .then()
                .statusCode(400);
    }

    @Test
    public void removeServiceFromBranchGroup_InvalidTenantCode_ReturnsBadRequest() {
        given()
                .pathParam("tenantCode", invalidTenantCode)
                .pathParam("branchGroupId", branchGroupId)
                .pathParam("serviceId", serviceId)
            .when()
                .header("Authorization", "Bearer " + generateSuperAdminToken())
                .delete("api/v1/groups/{tenantCode}/{branchGroupId}/services/{serviceId}")
            .then()
                .statusCode(400);
    }

    @Test
    public void removeBranchFromBranchGroup_ValidBranchId_ReturnsOkResponse() {
        given()
                .pathParam("tenantCode", tenantCode)
                .pathParam("branchGroupId", branchGroupId)
                .pathParam("branchId", branchId)
            .when()
                .header("Authorization", "Bearer " + generateSuperAdminToken())
                .delete("api/v1/groups/{tenantCode}/{branchGroupId}/branches/{branchId}")
            .then()
                .statusCode(200)
                .body("branches.id", not(hasItem(branchId)));
    }

    @Test
    public void removeBranchFromBranchGroup_InvalidBranchId_ReturnsBadRequest() {
        final int branchId = -1;

        given()
                .pathParam("tenantCode", tenantCode)
                .pathParam("branchGroupId", branchGroupId)
                .pathParam("branchId", branchId)
            .when()
                .header("Authorization", "Bearer " + generateSuperAdminToken())
                .delete("api/v1/groups/{tenantCode}/{branchGroupId}/branches/{branchId}")
            .then()
                .statusCode(400);
    }

    @Test
    public void removeBranchFromBranchGroup_InvalidTenantCode_ReturnsBadRequest() {
        given()
                .pathParam("tenantCode", invalidTenantCode)
                .pathParam("branchGroupId", branchGroupId)
                .pathParam("branchId", branchId)
            .when()
                .header("Authorization", "Bearer " + generateSuperAdminToken())
                .delete("api/v1/groups/{tenantCode}/{branchGroupId}/branches/{branchId}")
            .then()
                .statusCode(400);
    }
}
