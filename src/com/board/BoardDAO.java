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

import com.sun.org.apache.bcel.internal.generic.CPInstruction;


public class BoardDAO {

	// 디비셋팅
	private static BoardDAO instance = new BoardDAO();

	public static BoardDAO getInstance() {
		return instance;
	}

	private Connection getConnection() throws Exception {
		Context initCtx = new InitialContext();
		Context envCtx = (Context) initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource) envCtx.lookup("jdbc/member");
		return ds.getConnection();
	}
	
	// 추가
	 public void boardInsert(BoardVO board) {//새글 답글 구분
		 Connection con = null;
		 PreparedStatement ps = null;
		 ResultSet rs =null;
		 
		 int num = board.getNum();
		 int ref = board.getRef();
		 int re_step = board.getRe_step();
		 int re_level = board.getRe_level();
		 
		 int number = 0;
		 String sql;
		 try {
			con =getConnection();
			ps = con.prepareStatement("select max(num) from board");
			rs = ps.executeQuery();
			if(rs.next()) { //기존데이터가 있을 때 ref를 최대값 +1로 결정
				number=rs.getInt(1)+1;
			}else { //기존데이터가 없을때 ref를 1로 결정
				number=1;
			}
			if(num!=0) {//답글
				sql="update board set re_step = re_step+1 where ref=? and re_step>?";
				ps=con.prepareStatement(sql);
				ps.setInt(1, ref);
				ps.setInt(2, re_step);
				ps.executeUpdate();
				re_step = re_step+1;
				re_level = re_level+1;
			}else {//새글
				ref=number;
				re_step = 0;
				re_level =0;
			}
			//num, writer, subject, email, content, ip,  readcount, ref, re_step, re_level, passwd, reg_date
			sql = "insert into "
					+ " board(num,writer,subject,email,content,ip,readcount,ref,re_step,re_level,passwd)"
					+ "  values(board_seq.nextval, ?,?,?,?,?,0,?,?,?,?)";
			ps = con.prepareStatement(sql);
			ps.setString(1, board.getWriter());
			ps.setString(2, board.getSubject());
			ps.setString(3, board.getEmail());
			ps.setString(4, board.getContent());
			ps.setString(5, board.getIp());
			ps.setInt(6, ref);
			ps.setInt(7, re_step);
			ps.setInt(8, re_level);
			ps.setString(9, board.getPasswd());
			ps.executeUpdate();
		} catch (Exception e) {
				e.printStackTrace();
		}finally {
			closeConnection(con, ps);
		}
		 
	 }
	 
	 
	 //상세보기
	  public BoardVO boardView(int num) {
		  Connection con = null;
		  Statement st = null;
		  ResultSet rs = null;
		  BoardVO board = null;
		  
		  try {
			con = getConnection();
			st = con.createStatement();
			st.executeUpdate("update board set readcount=readcount+1 where num="+num);
			String sql = "select * from board where num="+num;
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
		}
		  return board;
		  
	  }

	// 전체보기-  검색 아님
	public  ArrayList<BoardVO> boardList(int startRow, int endRow){
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		ArrayList<BoardVO> arr = new ArrayList<>();
		try {
			con = getConnection();
			String sql = "select * from ("
								+      "select rownum rn, aa.* from( "
								+           "select * from board order by ref desc,re_step asc) aa "
					            + ")  where rn <=? and rn >= ?";
			ps = con.prepareStatement(sql);
			ps.setInt(1, endRow);
			ps.setInt(2, startRow);
			rs = ps.executeQuery();
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
		} finally {
			closeConnection(con, ps, rs);
		}
		
		 return arr;
	 }
	
	// 전체보기 -검색포함
		public  ArrayList<BoardVO> boardList(String field, String word,int startRow, int endRow){
			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			
			ArrayList<BoardVO> arr = new ArrayList<>();
			try {
				con = getConnection();
				String sql = "select * from ("
						+      "select rownum rn, aa.* from( "
						+           "select * from board  where "
						+            field +"  like '%"+word+"%'  order by ref desc,re_step asc) aa "
			            + ")  where rn <=? and rn >= ?";
						
				ps = con.prepareStatement(sql);
				ps.setInt(1, endRow);
				ps.setInt(2, startRow);
				rs = ps.executeQuery();
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
			} finally {
				closeConnection(con, ps, rs);
			}
			
			 return arr;
		 }


	// 수정
	public int boardUpdate(BoardVO board) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int flag =0;
		String sql="";
		
		try {
			con = getConnection();
			sql = "select passwd from board where num = "+board.getNum();
			ps = con.prepareStatement(sql);  //passwd 추출
			rs = ps.executeQuery();
			if(rs.next()) {
				if(rs.getString("passwd").equals(board.getPasswd())) { //비번 일치
					 sql = "update board set email = ?, subject=?, "
								+ " content=?, reg_date=sysdate where num=? ";
						ps = con.prepareStatement(sql);
						ps.setString(1, board.getEmail());
						ps.setString(2, board.getSubject());
						ps.setString(3, board.getContent());
						ps.setInt(4, board.getNum());
						flag = ps.executeUpdate();  //업데이트된 수를   flag에 담는다.
				}
			}
		} catch (Exception e) {
				e.printStackTrace();
		}finally {
			closeConnection(con, ps);
		}
		return flag;
	}

	// 삭제
	public int boardDelete(int num) {
		int flag = 0;
		Connection con = null;
		Statement st = null;
		try {
			con =getConnection();
			String sql="delete from board where num="+num;
			st = con.createStatement();
			flag=st.executeUpdate(sql);
		} catch (Exception e) {
				e.printStackTrace();
		}finally {
			closeConnection(con, st, null);
		}
		return flag;
	}

	// 개수 -검색없음
	public int boardCount() {
		int count=0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
	
		try {
			con = getConnection();
			String sql = "select count(*) from board";
			st = con.createStatement();
			rs = st.executeQuery(sql);
			if(rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
				e.printStackTrace();
		} finally {
			closeConnection(con, st, rs);
		}
		
		
		return count;
	}
	
	// 개수 -검색 포함
		public int boardCount(String field, String word) {
			int count=0;
			
			Connection con = null;
			Statement st = null;
			ResultSet rs = null;
		
			try {
				con = getConnection();
				String sql = "select count(*) from board where "
				                      + field +" like '%"+word+"%'";
				st = con.createStatement();
				rs = st.executeQuery(sql);
				if(rs.next()) {
					count = rs.getInt(1);
				}
			} catch (Exception e) {
					e.printStackTrace();
			} finally {
				closeConnection(con, st, rs);
			}
			return count;
		}
		
		//comment
		public void commentInsert(CommentVO cvo) {
			Connection con = null;
			PreparedStatement ps = null;
			try {
				con= getConnection();
				String sql = "insert into commentboard(cnum,userid,regdate,msg,bnum) "
						+ "values(comment_seq.nextval,?,sysdate,?,?)";
				ps = con.prepareStatement(sql);
				ps.setString(1,cvo.getUserid());
				ps.setString(2, cvo.getMsg());
				ps.setInt(3, cvo.getBnum());
				ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				closeConnection(con, ps);
			}
		}

		//commentList
		public ArrayList<CommentVO> commentList(int num) {
			Connection con= null;
			Statement st =null;
			ResultSet rs = null;
			ArrayList<CommentVO>arr=new ArrayList<>();
			
			try {
				con = getConnection();
				String sql = "select * from commentboard where bnum = "+num+" order by cnum desc";
				st = con.createStatement();
				rs=st.executeQuery(sql);
				while(rs.next()) {
					CommentVO cb = new CommentVO();
					cb.setCnum(rs.getInt("cnum"));
					cb.setBnum(rs.getInt("bnum"));
					cb.setMsg(rs.getString("msg"));
					cb.setRegdate(rs.getString("regdate"));
					cb.setUserid(rs.getString("userid"));
					arr.add(cb);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				closeConnection(con, st, rs);
			}
		
			
			return arr;
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
