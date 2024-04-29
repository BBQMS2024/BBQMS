package ba.unsa.etf.si.bbqms.export.implementation;

import ba.unsa.etf.si.bbqms.admin_service.api.BranchService;
import ba.unsa.etf.si.bbqms.domain.TellerStation;
import ba.unsa.etf.si.bbqms.domain.Ticket;
import ba.unsa.etf.si.bbqms.export.api.Exporter;
import ba.unsa.etf.si.bbqms.utils.DateUtils;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.util.Set;

@Service
public class TicketToPdfExporter implements Exporter<Ticket> {
    private final SpringTemplateEngine templateEngine;
    private final BranchService branchService;

    public TicketToPdfExporter(final SpringTemplateEngine templateEngine, final BranchService branchService) {
        this.templateEngine = templateEngine;
        this.branchService = branchService;
    }

    @Override
    public Resource exportPdf(final Ticket entity) {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        final Context context = new Context();
        context.setVariable("ticketTitle", "Ticket " + entity.getNumber());
        context.setVariable("ticketNumber", entity.getNumber());
        context.setVariable("ticketCreatedAt", DateUtils.getHourMinute(entity.getCreatedAt()));

        final Set<TellerStation> availableStations = extractPossibleStations(entity);
        context.setVariable("anyAvailableStations", !availableStations.isEmpty());
        final String availableStationsText = availableStations.stream()
                .reduce("", (rest, nextStation) -> {
                    if (rest.isEmpty()) {
                        return nextStation.getName();
                    } else {
                        return rest + ", " + nextStation.getName();
                    }
                }, String::concat);
        context.setVariable("ticketPossibleStations", availableStationsText);


        final String printHtml = this.templateEngine.process("ticket-template", context);
        HtmlConverter.convertToPdf(printHtml, outputStream);
        return new ByteArrayResource(outputStream.toByteArray());
    }

    private Set<TellerStation> extractPossibleStations(final Ticket ticket) {
        return this.branchService.getStationsWithService(ticket.getBranch(), ticket.getService());
    }
}
