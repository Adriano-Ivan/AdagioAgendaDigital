package br.com.adagio.adagioagendadigital.models.dto.task;

import java.util.List;

public class TaskDtoCreate {

    public TaskDtoCreate(String description, String initialMoment,
                         String limitMoment,int priority_id,
                         int isFinished){
       setAttributesExceptId(description,initialMoment,limitMoment,isFinished,priority_id);
    }

    private void setAttributesExceptId(String description, String initialMoment,
                                  String limitMoment, int priority_id,
                                  int isFinished){
        this.description=description;
        this.initialMoment=initialMoment;
        this.limitMoment=limitMoment;
        this.isFinished=isFinished;
        this.priority_id = priority_id;
    }

    private String description;

    private String initialMoment;

    private String limitMoment;

    private int isFinished;

    private List<Integer> tags;

    private int priority_id;

    public void setPriority_id(int priority_id) {
        this.priority_id = priority_id;
    }

    public int getPriority_id() {
        return priority_id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setInitialMoment(String initialMoment) {
        this.initialMoment = initialMoment;
    }

    public void setLimitMoment(String limitMoment) {
        this.limitMoment = limitMoment;
    }

    public void setFinished(int finished) {
        isFinished = finished;
    }

    public void setTags(List<Integer> tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public String getInitialMoment() {
        return initialMoment;
    }

    public String getLimitMoment() {
        return limitMoment;
    }

    public void setIsFinished(int isFinished) {
        this.isFinished = isFinished;
    }

    public int isFinished() {
        return isFinished;
    }

    public List<Integer> getTags() {
        return tags;
    }

}
