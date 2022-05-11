package accountManagement.main;

import java.util.Scanner;

import accountManagement.dao.UserDao;
import accountManagement.db.DBConnectionMgr;
import accountManagement.service.UserService;
import accountManagement.service.UserServiceImpl;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
//		UserDao userDao = new UserDao(DBConnectionMgr.getInstance());
		UserService userService = new UserServiceImpl(new UserDao(DBConnectionMgr.getInstance()), sc);
		
		
		while(true) {
			System.out.println("[유저 회원가입 프로그램]");
			System.out.println("1. 회원가입\n2. 로그인\n3. 유저정보 전체보기\nq. 프로그램 종료");
			String choice = sc.nextLine();
			if(choice.equals("1")) {
				userService.signup();
			}else if(choice.equals("2")) {
				userService.signin();
			}else if(choice.equals("3")) {
				userService.allUserInfo();
			}else if(choice.equals("q")) {
				for(int i = 1; i < 11; i++) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("프로그램 종료중...(" + i + "/10)");
				}
				break;
			}
		}
		System.out.println("프로그램이 종료되었습니다.");
	}
}