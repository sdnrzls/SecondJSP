package com.board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.member.MemberDAOImpl;

public class BoardDAO {

	//DB 세팅
		private static BoardDAO instance = new BoardDAO();
		
		public static BoardDAO  getInstance() {
			return instance;
		}
		
    private Connection getConnection() throws Exception{
    	Context initCtx = new InitialContext();
		Context envCtx =(Context) initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource) envCtx.lookup("jdbc/member");
		return ds.getConnection();
    }
    
    //추가
    public void boardInsert(BoardVO board) {
    	Connection con = null;
    	PreparedStatement ps = null;
    	
    	try {
    		//writer,email,content,pssswd,num,subject
    		String sql = "insert into board(num, writer, subject, email, content, passwd,ip) values(board_seq.nextval,?,?,?,?,?,?)";
			con=getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, board.getWriter());
			ps.setString(2, board.getSubject());
			ps.setString(3, board.getEmail());
			ps.setString(4, board.getContent());
			ps.setString(5, board.getPasswd());
			ps.setString(6, board.getIp());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			closeConnection(con, ps);
		}
    }
    
    //전체보기-검색 아님
    public ArrayList<BoardVO>boardList(){
    	Connection con =null;
    	Statement st = null;
    	ResultSet rs =null;
    	ArrayList<BoardVO>arr=new ArrayList<>();
    	try {
    		String sql="select * from board";
			con=getConnection();
			st=con.createStatement();
			rs=st.executeQuery(sql);
			while(rs.next()) {
				BoardVO board = new BoardVO();
				board.setNum(rs.getInt("num"));
				board.setEmail(rs.getString("email"));
				board.setIp(rs.getString("ip"));
				board.setReadcount(rs.getInt("readcount"));
				board.setSubject(rs.getString("subject"));
				board.setWriter(rs.getString("writer"));
				board.setReg_date(rs.getString("reg_date"));
				arr.add(board);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return arr;	
    }
    
  //전체보기-검색포함
    public ArrayList<BoardVO>boardList(String field,String word){
    	Connection con =null;
    	Statement st = null;
    	ResultSet rs =null;
    	ArrayList<BoardVO>arr=new ArrayList<>();
    	try {
    		String sql="select * from board where "+field+" like '%"+word+"%'";
			con=getConnection();
			st=con.createStatement();
			rs=st.executeQuery(sql);
			while(rs.next()) {
				BoardVO board = new BoardVO();
				board.setNum(rs.getInt("num"));
				board.setEmail(rs.getString("email"));
				board.setIp(rs.getString("ip"));
				board.setReadcount(rs.getInt("readcount"));
				board.setSubject(rs.getString("subject"));
				board.setWriter(rs.getString("writer"));
				board.setReg_date(rs.getString("reg_date"));
				arr.add(board);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return arr;	
    }
 
    //상세보기
    public BoardVO boardView(int num) {
    	Connection con =null;
    	Statement st = null;
    	ResultSet rs =null;
    	BoardVO board =null;
    	
    	try {
			con = getConnection();
			String sql = "select * from board where num="+num;
			st = con.createStatement();
			rs = st.executeQuery(sql);

			if(rs.next()) {
				board =new BoardVO();
				board.setNum(rs.getInt("num"));
				board.setContent(rs.getString("content"));
				board.setEmail(rs.getString("email"));
				board.setSubject(rs.getString("subject"));
				board.setIp(rs.getString("ip"));
				board.setPasswd(rs.getString("passwd"));
				board.setRe_level(rs.getInt("re_level"));
				board.setRe_step(rs.getInt("re_step"));
				board.setRef(rs.getInt("ref"));
				board.setReadcount(rs.getInt("readcount"));
				board.setReg_date(rs.getString("reg_date"));
				board.setWriter(rs.getString("writer"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			closeConnection(con, st, rs);
		} return board;
    }
    
    //수정
    public int boardUpdate(BoardVO board) {
    	Connection con=null;
    	PreparedStatement ps= null;
    	ResultSet rs =null;
    	int flag=0;
    	String sql;
    	try {
    		con = getConnection();
    		sql ="select passwd from board where num="+board.getNum();
    		ps= con.prepareStatement(sql); //passwd추출
    		rs=ps.executeQuery();
    		if(rs.next()) {
    			if(rs.getString("passwd").equals(board.getPasswd())) {// 비밀번호가 같다면
    				sql = "update board set subject=?, email=?, content=?, reg_date=sysdate where num=?";
    				ps = con.prepareStatement(sql);
    				ps.setString(1, board.getSubject());
    				ps.setString(2, board.getEmail());
    				ps.setString(3, board.getContent());
    				ps.setInt(4, board.getNum());
    				flag=ps.executeUpdate();
    			}
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			closeConnection(con, ps);
		}
    	return flag;
    }
    
    //삭제
    public int boardDelete(int num) {
    	Connection con = null;
    	Statement st =null;
    	
    	try {
			con = getConnection();
			String sql =  "delete from board where num='"+num+"'";
			st = con.createStatement();
			st.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			closeConnection(con, st, null);
			
		}
    	int flag=0;
    	return flag;
    }
    
    //개수 - 검색아님
    public int boardCount() {
	Connection con = null;
	Statement st= null;
	ResultSet rs =null;
	int count= 0;
	try {
		con=getConnection();
		String sql = "select COUNT(*) from board";
		st=con.createStatement();
		rs =st.executeQuery(sql);
		if(rs.next()) {
			count=rs.getInt(1);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}finally {
		closeConnection(con, st, rs);
	}
	return count;
}
    
    //개수 - 검색포함
    public int boardCount(String field,String word) {
	Connection con = null;
	Statement st= null;
	ResultSet rs =null;
	int count= 0;
	try {
		con=getConnection();
		String sql = "select COUNT(*) from board where "+field+" like '%"+word+"%'";
		st=con.createStatement();
		rs =st.executeQuery(sql);
		if(rs.next()) {
			count=rs.getInt(1);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}finally {
		closeConnection(con, st, rs);
	}
	return count;
}
    
    
  //닫기
  		private void closeConnection(Connection con, Statement st , ResultSet rs) {
  			try {
  				if(rs!=null) rs.close();
  				if(st!=null)  	st.close();
  				if(con!=null ) con.close();
  			} catch (SQLException e) {
  				e.printStackTrace();
  			}
  		}
  		private void closeConnection(Connection con, PreparedStatement ps) {
  			try {
  				if(ps!=null) ps.close();
  				if(con!=null ) con.close();
  			} catch (SQLException e) {
  				e.printStackTrace();
  			}
  		}
  		
  		
  		
}
