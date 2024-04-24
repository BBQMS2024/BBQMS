package ba.unsa.etf.si.bbqms.export.api;

import org.springframework.core.io.Resource;

public interface Exporter<T> {
    Resource exportPdf(T entity);
}
