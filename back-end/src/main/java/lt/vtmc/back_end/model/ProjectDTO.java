package lt.vtmc.back_end.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProjectDTO {

    @NotNull
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String description;

	public ProjectDTO(@NotNull @Size(max = 255) String name, @Size(max = 255) String description) {
		super();
		this.name = name;
		this.description = description;
	}

	public ProjectDTO() {
		super();
	}

}
