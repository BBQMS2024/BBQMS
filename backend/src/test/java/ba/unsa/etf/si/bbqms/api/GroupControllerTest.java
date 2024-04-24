package ba.unsa.etf.si.bbqms.api;

import ba.unsa.etf.si.bbqms.ws.controllers.GroupController;
import ba.unsa.etf.si.bbqms.ws.models.BranchGroupCreateDto;
import ba.unsa.etf.si.bbqms.ws.models.BranchGroupUpdateDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.greaterThan;

public class GroupControllerTest extends BaseSetup {
    private int branchGroupId;
    private final String tenantCode = "DFLT";
    private final String invalidTenantCode = ".";

    @BeforeEach
    public void createBranchGroup_ValidNameBranchIdsAndServiceIds_ReturnsOkResponse() {
        final String name = "Dummy Branch Group";
        final Set<Long> branchIds = Set.of(2L);
        final Set<Long> serviceIds = Set.of(1L);

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
                .body("branches.id", hasItem(2))
                .body("services.id", hasItem(1))
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
        final Set<Long> branchIds = Set.of(2L);
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
        final Set<Long> branchIds = Set.of(2L);
        final Set<Long> serviceIds = Set.of(1L);

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
        final Set<Long> serviceIds = Set.of(2L);

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
        final int serviceId = 2;

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
        final int serviceId = 2;

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
        final int branchId = 1;

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
                .pathParam("serviceId", branchId)
            .when()
                .header("Authorization", "Bearer " + generateSuperAdminToken())
                .put("api/v1/groups/{tenantCode}/{groupId}/branches/{serviceId}")
            .then()
                .statusCode(400);
    }

    @Test
    public void addBranchToBranchGroup_InvalidTenantCode_ReturnsBadRequest() {
        final int branchId = 1;

        given()
                .pathParam("tenantCode", invalidTenantCode)
                .pathParam("groupId", branchGroupId)
                .pathParam("serviceId", branchId)
            .when()
                .header("Authorization", "Bearer " + generateSuperAdminToken())
                .put("api/v1/groups/{tenantCode}/{groupId}/branches/{serviceId}")
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
        final int serviceId = 1;

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
        final int serviceId = 1;

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
        final int branchId = 2;

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
        final int branchId = 1;

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
