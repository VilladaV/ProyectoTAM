package org.breaze.data.entities;

public class DNASample {
    private String patientId;
    private String date;
    private String sequence;

    public DNASample(String patientId, String date, String sequence) {
        this.patientId = patientId;
        this.date = date;
        this.sequence = sequence;
    }

    //
    public String getPatientId() { return patientId; }
    public String getDate() { return date; }
    public String getSequence() { return sequence; }
}