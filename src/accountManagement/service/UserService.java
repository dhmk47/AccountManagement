package accountManagement.service;

import accountManagement.dto.UserDtl;
import accountManagement.dto.UserMst;

public interface UserService {
	public void signup();
	public void signin();
	public void modifyInfo(UserMst userMst, UserDtl userDtl);
	public int dropOut(UserMst userMst);
	public void allUserInfo();
	public void loginUserService(UserMst userMset, UserDtl userDtl);
	public void showMyInfo(UserMst userMst, UserDtl userDtl);
}