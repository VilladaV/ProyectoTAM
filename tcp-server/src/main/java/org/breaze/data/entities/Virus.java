package org.breaze.data.entities;

public class Virus {
    private String name;
    private String infectiousnessLevel; // Poco Infeccioso, Normal, Alto
    private String sequence; // Cadena ATCG

    public Virus(String name, String infectiousnessLevel, String sequence) {
        this.name = name;
        this.infectiousnessLevel = infectiousnessLevel;
        this.sequence = sequence;
    }

    public boolean isHighlyInfectious() {
        return "Altamente Infeccioso".equalsIgnoreCase(infectiousnessLevel);
    }

    //
    public String getName() { return name; }
    public String getInfectiousnessLevel() { return infectiousnessLevel; }
    public String getSequence() { return sequence; }
}