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
    @BeforeEach
    public void Should_CreateBranchGroupSuccessfully_When_ValidParametersProvided() {
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
    public void Should_DeleteBranchGroupSuccessfully_When_GivenValidBranchGroupId() {
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
    public void Should_RetrieveAllBranchGroupsFromTenant_When_ValidTenantCodeIsProvided() {
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
    public void Should_AddServiceToBranchGroup_When_ServiceIdIsProvided() {
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
    public void Should_AddBranchToBranchGroup_When_BranchIdIsProvided() {
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
    public void Should_UpdateBranchGroupName_When_NameIsProvided() {
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
    public void Should_RemoveServiceFromBranchGroup_WhenServiceIdIsProvided() {
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
    public void Should_RemoveBranchFromBranchGroup_WhenBranchIdIsProvided() {
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
}
