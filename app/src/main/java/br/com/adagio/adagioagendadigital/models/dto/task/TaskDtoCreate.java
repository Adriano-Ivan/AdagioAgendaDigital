package br.com.adagio.adagioagendadigital.models.dto.task;

import java.util.List;

public class TaskDtoCreate {

//    public TaskDtoCreate(int id, String description, String initialMoment,
//                         String limitMoment,
//                         int isFinished){
//        this.id = id;
//        setAttributesExceptId(description,initialMoment,limitMoment,isFinished);
//    }

    public TaskDtoCreate(String description, String initialMoment,
                         String limitMoment,
                         int isFinished){
       setAttributesExceptId(description,initialMoment,limitMoment,isFinished);
    }

    private void setAttributesExceptId(String description, String initialMoment,
                                  String limitMoment,
                                  int isFinished){
        this.description=description;
        this.initialMoment=initialMoment;
        this.limitMoment=limitMoment;
        this.isFinished=isFinished;
    }


//    private int id;

    private String description;

    private String initialMoment;

    private String limitMoment;

    private int isFinished;

    private List<Integer> tags;

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

    public int isFinished() {
        return isFinished;
    }

    public List<Integer> getTags() {
        return tags;
    }

//    public void setId(int id){
//        this.id = id;
//    }
//
//    public int getId(){
//        return id;
//    }

}
