package br.com.adagio.adagioagendadigital.models.entities;

import java.time.LocalDateTime;

public class Notification {
    private int id;

    private int task_id;

    private LocalDateTime emitted_at;

    private String message;

    private String priority_name;

    public Notification(int task_id,LocalDateTime emitted_at,String message,
                        String priority_name){
        setAttributes(task_id, emitted_at, message, priority_name);

    }

    public Notification(int id, int task_id,LocalDateTime emitted_at,String message,
                        String priority_name){
        this.id = id;
        setAttributes(task_id, emitted_at, message, priority_name);
    }

    private void setAttributes(int task_id, LocalDateTime emitted_at, String message, String priority_name) {
        this.task_id = task_id;
        this.emitted_at = emitted_at;
        this.message = message;
        this.priority_name = priority_name;
    }

    public int getId() {
        return id;
    }

    public int getTask_id() {
        return task_id;
    }

    public LocalDateTime getEmitted_at() {
        return emitted_at;
    }

    public String getMessage() {
        return message;
    }

    public String getPriority_name() {
        return priority_name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public void setEmitted_at(LocalDateTime emitted_at) {
        this.emitted_at = emitted_at;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPriority_name(String priority_name) {
        this.priority_name = priority_name;
    }
}
