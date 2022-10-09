package br.com.adagio.adagioagendadigital.models.dto.task;

import java.time.LocalDateTime;
import java.util.List;

public class TaskDtoRead {


    public TaskDtoRead(int id, String description, LocalDateTime initialMoment,
                         LocalDateTime limitMoment,
                         boolean isFinished){
        this.id = id;
        setAttributesExceptId(description,initialMoment,limitMoment,isFinished);
    }

    public TaskDtoRead(String description, LocalDateTime initialMoment,
                         LocalDateTime limitMoment,
                         boolean isFinished){
        setAttributesExceptId(description,initialMoment,limitMoment,isFinished);
    }

    private void setAttributesExceptId(String description, LocalDateTime initialMoment,
                                       LocalDateTime limitMoment,
                                       boolean isFinished){
        this.description=description;
        this.initialMoment=initialMoment;
        this.limitMoment=limitMoment;
        this.isFinished=isFinished;
    }


    private int id;

    private String description;

    private LocalDateTime initialMoment;

    private LocalDateTime limitMoment;

    private boolean isFinished;

    private List<Integer> tags;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setInitialMoment( LocalDateTime initialMoment) {
        this.initialMoment = initialMoment;
    }

    public void setLimitMoment(LocalDateTime limitMoment) {
        this.limitMoment = limitMoment;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public void setTags(List<Integer> tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getInitialMoment() {
        return initialMoment;
    }

    public LocalDateTime getLimitMoment() {
        return limitMoment;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public List<Integer> getTags() {
        return tags;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

}
