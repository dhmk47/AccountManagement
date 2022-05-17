package accountManagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import accountManagement.db.DBConnectionMgr;
import accountManagement.dto.User;
import accountManagement.dto.UserDtl;
import accountManagement.dto.UserMst;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserDao {
	private final DBConnectionMgr pool;
	private final Scanner sc;
	
	public void signupUser(String email, String name, String username, String password) {
		
		Connection con = null;
		String sql = null;
		PreparedStatement pstmt = null;
		int result = 0;
		try {
			con = pool.getConnection();
			sql = "INSERT INTO\r\n"
					+ "	user_mst\r\n"
					+ "VALUES(\r\n"
					+ "	0,\r\n"
					+ "	?,\r\n"
					+ "	?,\r\n"
					+ "	?,\r\n"
					+ "	?,\r\n"
					+ "	NOW(),\r\n"
					+ "	NOW()\r\n"
					+ ")";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, email);
			pstmt.setString(2, name);
			pstmt.setString(3, username);
			pstmt.setString(4, password);
			result = pstmt.executeUpdate();
			
			if(result != 0)
				System.out.println("회원가입이 완료되었습니다.");
			else
				System.out.println("회원가입 오류!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		
	}
	
	public ArrayList<User> signinUser(String username, String password) {
		Connection con = null;
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<User> userList = new ArrayList<User>();
		try {
			con = pool.getConnection();
			sql = "SELECT\r\n"
					+ "	*\r\n"
					+ "FROM\r\n"
					+ "	user_mst um\r\n"
					+ "	LEFT OUTER JOIN user_dtl ud ON(ud.usercode = um.usercode)\r\n"
					+ "WHERE\r\n"
					+ "	um.username = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();
			
			rs.next();
			
			try {
				rs.getInt(1);
				User userMst = UserMst.builder()
						.usercode(rs.getInt(1))
						.email(rs.getString(2))
						.name(rs.getString(3))
						.username(rs.getString(4))
						.password(rs.getString(5))
						.create_date(rs.getTimestamp(6).toLocalDateTime())
						.update_date(rs.getTimestamp(7).toLocalDateTime())
						.build();
				
				if(((UserMst)userMst).getPassword().equals(password)) {
					User userDtl = UserDtl.builder()
							.usercode(rs.getInt(8))
							.address(rs.getString(9))
							.phone(rs.getString(10))
							.gender(rs.getInt(11))
							.favorite_news(rs.getString(12))
							.create_date(rs.getTimestamp(13).toLocalDateTime())
							.update_date(rs.getTimestamp(14).toLocalDateTime())
							.build();
					
					System.out.println("로그인 성공!");
					userList.add(userMst);
					userList.add(userDtl);
					return userList;
					
				}else {
					System.out.println("비밀번호가 틀렸습니다.");
				}
			}catch (SQLDataException e) {
				System.out.println("해당 아이디가 존재하지 않습니다.");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return null;
	}
	
	public void modifyUserInfo(UserMst userMst, UserDtl userDtl, String[] newInfo) {
		ArrayList<String> mst = new ArrayList<String>();
		ArrayList<String> dtl = new ArrayList<String>();
		String sql ="";
		for(int i = 0; i < newInfo.length; i++) {
			if(newInfo[i].equals("email") || newInfo[i].equals("name") || newInfo[i].equals("password")) {
				mst.add(newInfo[i]);
			}else {
				dtl.add(newInfo[i]);
			}
		}
			
			Connection con = null;
			PreparedStatement pstmt = null;
			int result = 0;
			String um = "um.";
			String ud = "ud.";
			// toString으로 문자열로 바뀌니까 오류?
			try {
				con = pool.getConnection();
				sql += "UPDATE\r\n"
						+ "	user_mst um,\r\n"
						+ " user_dtl ud\r\n"
						+ "SET\r\n";
				for(int i = 0; i < mst.size(); i++) {
					sql += " " + um + mst.get(i) +" = ?";
					
					if(i != mst.size() - 1 || dtl.size() != 0) {
						sql += ", \r\n";
					}
				}
				for(int i = 0; i < dtl.size(); i++) {
					sql += " " + ud + dtl.get(i) + " = ?";
					
					if(i != dtl.size() - 1) {
						sql += ", \r\n";
					}
				}
				
				
				sql += "\r\nWHERE\r\n"
					+ "	um.usercode = ? AND ud.usercode = ?";
				pstmt = con.prepareStatement(sql);
				
				int index = 1;
				for(int i = 0; i < mst.size(); i++) {
					System.out.print(mst.get(i) + ": ");
					pstmt.setString(i + 1, sc.nextLine());
					index++;
				}
				
				for(int i = 0; i < dtl.size(); i++) {
					System.out.print(dtl.get(i) + ": ");
					if(dtl.get(i).equals("gender")) {
						pstmt.setInt(i + index, sc.nextInt());
						sc.nextLine();
					}else {
						pstmt.setString(i + index, sc.nextLine());
					}
				}
				pstmt.setInt(mst.size() + dtl.size() + 1, userMst.getUsercode());
				pstmt.setInt(mst.size() + dtl.size() + 2, userMst.getUsercode());
				
				result = pstmt.executeUpdate();
				
				if(result != 0) {
					System.out.println("정보가 수정되었습니다.");
				}else {
					System.out.println("정보를 수정하는데 오류가 났습니다.");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				pool.freeConnection(con, pstmt);
			}
	}
	
	public int dropOutUser(UserMst userMst) {
		Connection con = null;
		String sql = null;
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			con = pool.getConnection();
			sql = "delete from user_mst where usercode = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, userMst.getUsercode());
			
			result = pstmt.executeUpdate();
			
			if(result != 0) {
				System.out.println("회원탈퇴 완료!");
			}else {
				System.out.println("회원탈퇴 오류!");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		
		return result;
	}
	
	public ArrayList<ArrayList<User>> showAllUserInfo() {
		Connection con = null;
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<ArrayList<User>> userList = new ArrayList<>();
		
		try {
			con = pool.getConnection();
			sql = "SELECT\r\n"
					+ "	*\r\n"
					+ "FROM\r\n"
					+ "	user_mst um\r\n"
					+ "	LEFT OUTER JOIN user_dtl ud ON(ud.usercode = um.usercode)";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			try {
				while(rs.next()) {
					rs.getInt(1);
					int i = 0;
					userList.add(new ArrayList<User>());
					User userMst = UserMst.builder()
							.usercode(rs.getInt(1))
							.email(rs.getString(2))
							.name(rs.getString(3))
							.username(rs.getString(4))
							.password(rs.getString(5))
							.create_date(rs.getTimestamp(6).toLocalDateTime())
							.update_date(rs.getTimestamp(7).toLocalDateTime())
							.build();
					
					User userDtl = UserDtl.builder()
							.usercode(rs.getInt(8))
							.address(rs.getString(9))
							.phone(rs.getString(10))
							.gender(rs.getInt(11))
							.favorite_news(rs.getString(12))
							.create_date(rs.getTimestamp(13).toLocalDateTime())
							.update_date(rs.getTimestamp(14).toLocalDateTime())
							.build();
					
					userList.get(i).add(userMst);
					userList.get(i).add(userDtl);
					i++;
				}
				
				
				
			} catch (SQLDataException e) {
				System.out.println("아직 가입된 회원이 없습니다.");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return userList;
	}
	
	public HashMap<String, User> showMyInfo(UserMst userMst, UserDtl userDtl){
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		HashMap<String, User> userMap = new HashMap<>();
		
		try {
			con = pool.getConnection();
			sql = "select * from user_mst um left outer join user_dtl ud on(ud.usercode = um.usercode) where um.usercode = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, userMst.getUsercode());
			rs = pstmt.executeQuery();
			
			rs.next();
			
			try {
				
				rs.getInt(1);
				User myUserMst = UserMst.builder()
						.usercode(rs.getInt(1))
						.email(rs.getString(2))
						.name(rs.getString(3))
						.username(rs.getString(4))
						.password(rs.getString(5))
						.create_date(rs.getTimestamp(6).toLocalDateTime())
						.update_date(rs.getTimestamp(7).toLocalDateTime())
						.build();
				
				User myUserDtl = UserDtl.builder()
						.usercode(rs.getInt(8))
						.address(rs.getString(9))
						.phone(rs.getString(10))
						.gender(rs.getInt(11))
						.favorite_news(rs.getString(12))
						.create_date(rs.getTimestamp(13).toLocalDateTime())
						.update_date(rs.getTimestamp(14).toLocalDateTime())
						.build();
				
				
//				System.out.println(myUserMst.getClass());
//				User class1 = myUserMst.getClass().newInstance();
//				((UserMst)class1).attack();
				
				userMap.put("um", myUserMst);
				userMap.put("ud", myUserDtl);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		
		return userMap;
	}
}