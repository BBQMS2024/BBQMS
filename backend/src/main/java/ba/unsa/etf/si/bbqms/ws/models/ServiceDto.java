package ba.unsa.etf.si.bbqms.ws.models;

import ba.unsa.etf.si.bbqms.domain.Tenant;

public record ServiceDto (long id, String name, Tenant tenant){
}
