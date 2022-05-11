package accountManagement.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDtl extends User{
	private int usercode;
	private String address;
	private String phone;
	private int gender;
	private String favorite_news;
	private LocalDateTime create_date;
	private LocalDateTime update_date;
}
