package lt.vtmc.back_end.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReturnProject {
	
	private Long id;

    private String name;

    private String description;

    private String status;

    private Integer tasksAmount;

    private Integer tasksLeft;

	public ReturnProject(Long id, String name, String description, String status, Integer tasksAmount,
			Integer tasksLeft) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.status = status;
		this.tasksAmount = tasksAmount;
		this.tasksLeft = tasksLeft;
	}

	public ReturnProject() {
	}
    
    

}
