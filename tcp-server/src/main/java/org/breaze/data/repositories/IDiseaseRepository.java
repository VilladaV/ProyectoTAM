package org.breaze.data.repositories;

import org.breaze.data.entities.Virus;
import java.io.IOException;
import java.util.List;

public interface IDiseaseRepository {
    /**
     * Registra un nuevo virus en el sistema
     */
    void saveVirus(Virus v) throws IOException;

    /**
     * Recupera todos los virus disponibles en el catálogo
     */
    List<Virus> findAllVirus() throws IOException;
}
