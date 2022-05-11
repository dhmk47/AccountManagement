package accountManagement.service;

import java.util.ArrayList;
import java.util.Scanner;

import accountManagement.dao.UserDao;
import accountManagement.dto.User;
import accountManagement.dto.UserDtl;
import accountManagement.dto.UserMst;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
	private final UserDao userDao;
	private final Scanner sc;

	@Override
	public void signup() {
		System.out.println("[회원가입]");
		System.out.print("이메일: ");
		String email = sc.nextLine();
		System.out.print("이름: ");
		String name = sc.nextLine();
		System.out.print("아이디: ");
		String username = sc.nextLine();
		System.out.print("비밀번호: ");
		String password = sc.nextLine();
		userDao.signupUser(email, name, username, password);
	}

	@Override
	public void signin() {
		System.out.print("아이디: ");
		String username = sc.nextLine();
		System.out.print("비밀번호: ");
		String password = sc.nextLine();
		ArrayList<User> userList = userDao.signinUser(username, password);
		loginUserService((UserMst)userList.get(0), (UserDtl)userList.get(1));
	}
	
	@Override
	public void loginUserService(UserMst userMst, UserDtl userDtl) {
		while(true) {
			System.out.println("[" + userMst.getName() +"님의 회원 정보]");
			System.out.println("1. 회원정보 수정\n2. 로그아웃\n3. 회원탈퇴");
			int choice = sc.nextInt();
			sc.nextLine();
			if(choice == 1) {
				modifyInfo(userMst, userDtl);
			}else if(choice == 2) {
				break;
			}else if(choice == 3) {
				dropOut();
			}else {
				System.out.println("다시 선택해주세요.");
			}
		}
	}

	@Override
	public void modifyInfo(UserMst userMst, UserDtl userDtl) {
		System.out.println("수정 가능한 정보");
		System.out.println("email, name, password, address, phone, gender, favorite_news");
		System.out.print("몇 개를 수정할지 입력하세요: ");
		System.out.print("수정할 정보를 입력하세요:(공백을 구분) ");
		String[] newInfo = sc.nextLine().split(" ");
		
		
		userDao.modifyUserInfo(userMst, userDtl, newInfo);
	}

	@Override
	public void dropOut() {
		
	}

	@Override
	public void allUserInfo() {
		
	}

	
}