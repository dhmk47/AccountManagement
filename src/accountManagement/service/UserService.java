package accountManagement.service;

import accountManagement.dto.UserDtl;
import accountManagement.dto.UserMst;

public interface UserService {
	public void signup();
	public void signin();
	public void modifyInfo(UserMst userMst, UserDtl userDtl);
	public void dropOut();
	public void allUserInfo();
	public void loginUserService(UserMst userMset, UserDtl userDtl);
}