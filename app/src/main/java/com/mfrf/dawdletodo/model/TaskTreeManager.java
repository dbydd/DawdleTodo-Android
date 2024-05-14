package com.mfrf.dawdletodo.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mfrf.dawdletodo.exceptions.AddTaskError;
import com.mfrf.dawdletodo.model.task_container.AbstractTaskContainer;
import com.mfrf.dawdletodo.model.task_container.DailyTaskContainer;

public class TaskTreeManager {
    private final AbstractTaskContainer root;
    private final String configID;

    public TaskTreeManager(AbstractTaskContainer root, String configID) {
        this.root = root;
        this.configID = configID;
    }

    public TaskTreeManager(String configID) {
        this.configID = configID;
        this.root = new DailyTaskContainer("root");
    }

    public TaskTreeManager addTo(String parent_id, AbstractTaskContainer container) throws AddTaskError {
        AbstractTaskContainer abstractTaskContainer = root.find(parent_id);
        if (abstractTaskContainer == null) {
            throw new AddTaskError.GroupNotFoundError(parent_id);
        }
        abstractTaskContainer.add(container);
        return this;
    }


    public String toJSON() throws JsonProcessingException {
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonNode = writer.writeValueAsString(this);
        return jsonNode;
    }

    public static TaskTreeManager fromJSON(String json) throws JsonProcessingException {
        ObjectReader reader = new ObjectMapper().reader();
        return reader.readValue(json);
    }
}
