package br.com.adagio.adagioagendadigital.models.dto.task;

import android.util.Log;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskDtoRead implements Serializable {


    public TaskDtoRead(int id, String description, LocalDateTime initialMoment,
                       LocalDateTime limitMoment,
                       boolean isFinished, int priority_id, ArrayList<Integer> tagIds,String priorityName){
        this.id = id;
        setAttributesExceptId(description,initialMoment,limitMoment,isFinished,priority_id,
                tagIds,priorityName);
    }

    public TaskDtoRead(String description, LocalDateTime initialMoment,
                         LocalDateTime limitMoment,
                         boolean isFinished,ArrayList<Integer> tagIds,String priorityName){
        setAttributesExceptId(description,initialMoment,limitMoment,isFinished,null,tagIds,priorityName);
    }

    private void setAttributesExceptId(String description, LocalDateTime initialMoment,
                                       LocalDateTime limitMoment,
                                       boolean isFinished,Integer priority_id,
                                       ArrayList<Integer> tagIds,String priorityName){
        this.description=description;
        this.initialMoment=initialMoment;
        this.limitMoment=limitMoment;
        this.isFinished=isFinished;
        this.tags = new ArrayList<>(tagIds);
        this.priorityName = priorityName;

        if(priority_id !=null){
            this.priority_id = priority_id;
        }
    }


    private int id;

    private String description;

    private String priorityName;

    private int priority_id;

    private LocalDateTime initialMoment;

    private LocalDateTime limitMoment;

    private boolean isFinished;

    private ArrayList<Integer> tags;

    public int getPriority_id() {
        return priority_id;
    }

    public void setPriority_id(int priority_id) {
        this.priority_id = priority_id;
    }

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

    public void setTags(ArrayList<Integer> tags) {
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

    public ArrayList<Integer> getTags() {
        return tags;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    @Override
    public String toString() {
        return "TaskDtoRead{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", priorityName='" + priorityName + '\'' +
                ", priority_id=" + priority_id +
                ", initialMoment=" + initialMoment +
                ", limitMoment=" + limitMoment +
                ", isFinished=" + isFinished +
                ", tags=" + tags +
                '}';
    }
}
