package accountManagement.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserMst extends User{
	private int usercode;
	private String email;
	private String name;
	private String username;
	private String password;
	private LocalDateTime create_date;
	private LocalDateTime update_date;
	
	public void attack() {
		System.out.println("test");
		toString();
	}
	
}