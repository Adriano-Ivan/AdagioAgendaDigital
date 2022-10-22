package br.com.adagio.adagioagendadigital.models.dto.task;

import android.util.Log;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class TaskDtoRead implements Serializable {


    public TaskDtoRead(int id, String description, LocalDateTime initialMoment,
                         LocalDateTime limitMoment,
                         boolean isFinished,int priority_id){
        this.id = id;
        setAttributesExceptId(description,initialMoment,limitMoment,isFinished,priority_id);
    }

    public TaskDtoRead(String description, LocalDateTime initialMoment,
                         LocalDateTime limitMoment,
                         boolean isFinished){
        setAttributesExceptId(description,initialMoment,limitMoment,isFinished,null);
    }

    private void setAttributesExceptId(String description, LocalDateTime initialMoment,
                                       LocalDateTime limitMoment,
                                       boolean isFinished,Integer priority_id){
        this.description=description;
        this.initialMoment=initialMoment;
        this.limitMoment=limitMoment;
        this.isFinished=isFinished;

        Log.i("priority é isso", priority_id+"");
        if(priority_id !=null){
            this.priority_id = priority_id;
        }
    }


    private int id;

    private String description;

    private int priority_id;

    private LocalDateTime initialMoment;

    private LocalDateTime limitMoment;

    private boolean isFinished;

    private List<Integer> tags;

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
