package org.breaze.data.repositories;


import org.breaze.data.entities.Virus;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FastaDiseaseRepository implements IDiseaseRepository {
    private final String folderPath;

    public FastaDiseaseRepository(String folderPath) {
        this.folderPath = folderPath;
    }

    @Override
    public List<Virus> findAllVirus() throws IOException {
        List<Virus> virusList = new ArrayList<>();
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".fasta"));

        if (files != null) {
            for (File file : files) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String header = br.readLine(); // >nombre|nivel
                    String sequence = br.readLine(); // ATCG...

                    if (header != null && sequence != null && header.startsWith(">")) {
                        String cleanHeader = header.substring(1);
                        String[] parts = cleanHeader.split("\\|");
                        virusList.add(new Virus(parts[0], parts[1], sequence));
                    }
                }
            }
        }
        return virusList;
    }

    @Override
    public void saveVirus(Virus v) throws IOException {
        File file = new File(folderPath, v.getName() + ".fasta");
        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            out.println(">" + v.getName() + "|" + v.getInfectiousnessLevel());
            out.println(v.getSequence());
        }
    }
}
