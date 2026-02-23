package org.breaze.business;

import org.breaze.data.entities.DNASample;
import java.io.IOException;

public interface GenomicService {
    void analyzeDNA(DNASample sample) throws IOException;
    void checkMutations(DNASample sample) throws IOException;
    String getHighRiskReport() throws IOException;
}
