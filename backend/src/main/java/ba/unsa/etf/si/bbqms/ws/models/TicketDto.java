package ba.unsa.etf.si.bbqms.ws.models;

import ba.unsa.etf.si.bbqms.domain.Ticket;

import java.time.Instant;

public record TicketDto(long id, long number, Instant createdAt, ServiceDto service, BranchDto branch) {
    public static TicketDto fromEntity(final Ticket ticket) {
        return new TicketDto(
                ticket.getId(),
                ticket.getNumber(),
                ticket.getCreatedAt(),
                ServiceDto.fromEntity(ticket.getService()),
                BranchDto.fromEntity(ticket.getBranch())
        );
    }
}
