package lt.vtmc.back_end.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDTO {

    @NotNull
    @Size(max = 255)
    private String mail;

    @NotNull
    @Size(max = 255)
    private String password;

	public UserDTO(@NotNull @Size(max = 255) String mail, @NotNull @Size(max = 255) String password) {
		super();
		this.mail = mail;
		this.password = password;
	}

	public UserDTO() {
		super();
	}
    
    
}
