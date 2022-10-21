package br.com.adagio.adagioagendadigital.models.entities;

import java.time.LocalDateTime;

public class Tag {

    private int id;

    private String name;

    public Tag(int id , String name){
        this.id = id;
        this.name = name;
    }

    public Tag(String name){
        this.name =name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
