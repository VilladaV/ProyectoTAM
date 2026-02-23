package org.breaze.data.repositories;


import org.breaze.common.exceptions.DuplicatePatientException;
import org.breaze.data.entities.Patient;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVPatientRepository implements IPatientRepository {
    private final String filePath;

    public CSVPatientRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void save(Patient p) throws DuplicatePatientException, IOException {
        if (findById(p.getId()) != null) {
            throw new DuplicatePatientException(p.getId());
        }
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)))) {
            // id,nombre,apellido,edad,email,genero,ciudad,pais
            out.println(String.format("%s,%s,%s,%d,%s,%s,%s,%s",
                    p.getId(), p.getName(), p.getLastName(), p.getAge(),
                    p.getEmail(), p.getGender(), p.getCity(), p.getCountry()));
        }
    }

    @Override
    public Patient findById(String id) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) return null;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(id)) {
                    return new Patient(data[0], data[1], data[2], Integer.parseInt(data[3]), data[4], data[5], data[6], data[7]);
                }
            }
        }
        return null;
    }

    @Override
    public List<Patient> findAll() throws IOException {
        List<Patient> patients = new ArrayList<>();
        //
        return patients;
    }
}
