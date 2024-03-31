package ba.unsa.etf.si.bbqms;

import ba.unsa.etf.si.bbqms.domain.Tenant;
import ba.unsa.etf.si.bbqms.domain.TenantLogo;
import ba.unsa.etf.si.bbqms.repository.TenantLogoRepository;
import ba.unsa.etf.si.bbqms.repository.TenantRepository;
import ba.unsa.etf.si.bbqms.tenant_service.implementation.TenantServiceImpl;
import ba.unsa.etf.si.bbqms.ws.models.TenantDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TenantServiceTest {
    @Mock
    private TenantRepository tenantRepository;
    @Mock
    private TenantLogoRepository tenantLogoRepository;

    @InjectMocks
    private TenantServiceImpl tenantService;

    @Test
    public void testAddTenant_ShouldReturnTenant() {
        TenantDto request = new TenantDto("T001","Example Tenant", "Example Address", "Arial", "Welcome to our bank!", "sampleBase64Logo");

        final TenantLogo expectedLogo = new TenantLogo("sampleBase64Logo");
        when(tenantLogoRepository.save(any())).thenReturn(expectedLogo);

        final Tenant expectedTenant = new Tenant("T001","Example Tenant", "Example Address", "Arial", "Welcome to our bank!");
        when(tenantRepository.save(any())).thenReturn(expectedTenant);

        final Tenant returnedTenant = tenantService.addTenant(request);

        assertEquals(expectedTenant,returnedTenant);

    }

}
