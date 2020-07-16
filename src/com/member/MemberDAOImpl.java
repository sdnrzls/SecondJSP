package com.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;




public class MemberDAOImpl implements MemberDAO{
	private static MemberDAOImpl instance = new  MemberDAOImpl();
	public static  MemberDAOImpl getInstance() {
		return instance;
	}
	private Connection getConnection() throws Exception{
	Context initCtx = new InitialContext();
	Context envCtx = (Context)initCtx.lookup("java:comp/env");
	DataSource ds = (DataSource)envCtx.lookup("jdbc/member");
	return ds.getConnection();
}

	@Override
	public void memberInsert(MemberVO vo) {
		Connection con = null;
		PreparedStatement ps =null;
		
		try {
			con = getConnection();
			String sql= "insert into member values(?,?,?,?,?,?)";
			ps= con.prepareStatement(sql);
			ps.setString(1,vo.getUserid());		
			ps.setString(2,vo.getName());	
			ps.setString(3,vo.getPwd()); 
			ps.setString(4,vo.getMail());
			ps.setString(5,vo.getPhone());
			ps.setInt(6,vo.getAdmin());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			closeConnection(con, ps);
		}
	}

	@Override
	public ArrayList<MemberVO> memberList() {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList<MemberVO> arr = new ArrayList<>();
		try {
			con= getConnection();
			String sql = "select * from member";
			st= con.createStatement();
			rs= st.executeQuery(sql);
			while(rs.next()) {
				MemberVO vo= new MemberVO();
				vo.setAdmin(rs.getInt("admin"));
				vo.setMail(rs.getString("mail"));
				vo.setName(rs.getString("name"));
				vo.setPhone(rs.getString("phone"));
				vo.setPwd(rs.getString("pwd"));
				vo.setUserid(rs.getString("userid"));
				arr.add(vo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			closeConnection(con, st, rs);
		}
		return arr;
	}

	@Override
	public void memberUpdate(MemberVO vo) {
		
	}

	@Override
	public MemberVO memberView(String userid) {
		return null;
	}

	@Override
	public void memberDel(String userid) {
		
	}

	@Override
	public String idCheck(String userid) {
		Connection con= null;
		Statement st = null;
		ResultSet rs = null;
		String flag = "yes";//사용가능
		try {
			con=getConnection();
			String sql = "select * from member where userid='"+userid+"'";
		//	System.out.println(sql);
			st = con.createStatement();
			rs = st.executeQuery(sql);
			if(rs.next()) {
				flag="no";//사용불가능
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	//닫기
		private void closeConnection(Connection con, 
				     PreparedStatement ps) {
			try {
				if(ps!=null)  	ps.close();
				if(con!=null ) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		private void closeConnection(Connection con, 
				Statement st , ResultSet  rs) {
			try {
				if(rs!=null) rs.close();
				if(st!=null)  	st.close();
				if(con!=null ) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
}
