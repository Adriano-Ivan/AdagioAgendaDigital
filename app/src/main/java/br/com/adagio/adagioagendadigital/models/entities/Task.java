package br.com.adagio.adagioagendadigital.models.entities;

import java.time.LocalDateTime;
import java.util.List;

public class Task {

    public int id;

    public String description;

    public LocalDateTime initialMoment;

    public LocalDateTime limitMoment;

    public boolean isFinished;

    public List<Integer> tags;
}
