package br.com.adagio.adagioagendadigital.models.entities;

import java.time.LocalDateTime;

public class Notification {
    private int id;

    private int task_id;

    private LocalDateTime emitted_at;

    private String message;

    private String priority_name;
}
