package accountManagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.util.ArrayList;

import accountManagement.db.DBConnectionMgr;
import accountManagement.dto.User;
import accountManagement.dto.UserDtl;
import accountManagement.dto.UserMst;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserDao {
	private final DBConnectionMgr pool;
	
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
		
	}
	
	public void dropOutUser() {
		
	}
}