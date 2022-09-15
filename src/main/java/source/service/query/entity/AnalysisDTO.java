package source.service.query.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnalysisDTO {
    public AnalysisDTO() {

    }

    public AnalysisDTO(String name, Action action, Object data) {
        this.name = name;
        this.action = action;
        this.data = data;
    }

    public enum Action {
        CREATED
    }

    @JsonProperty("name")
    private String name;
    @JsonProperty("action")
    private Action action;
    @JsonProperty("data")
    private Object data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
