package lt.vtmc.back_end.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDTO {

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    private String userStory;
    
    private String priority;


	public TaskDTO(String name, String userStory, String priority) {
		this.name = name;
		this.userStory = userStory;
		this.priority = priority;
	}

	public TaskDTO() {
	}
    
}
