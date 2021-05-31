package lt.vtmc.back_end.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
public class ReturnTask {

    private Long id;

    private String name;

    private String userStory;

    private String priority;

    private String status;

    private String creationDate;

    private String updateDate;

    public ReturnTask(Long id, String name, String userStory, String priority,
                      String status, String creationDate, String updateDate) {
        this.id = id;
        this.name = name;
        this.userStory = userStory;
        this.priority = priority;
        this.status = status;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
    }

    public ReturnTask() {
    }
}
