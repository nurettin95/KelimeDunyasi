package com.nurettingorsoy.kelimedunyasi;

public class Model {
    private String kelime;
    private String anlami;
    private int id;

    public Model() {

    }
    public Model(String kelime, String anlami, int id) {
        this.kelime = kelime;
        this.anlami = anlami;
        this.id = id;
    }

    public String getKelime() {
        return kelime;
    }

    public void setKelime(String kelime) {
        this.kelime = kelime;
    }

    public String getAnlami() {
        return anlami;
    }

    public void setAnlami(String anlami) {
        this.anlami = anlami;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
