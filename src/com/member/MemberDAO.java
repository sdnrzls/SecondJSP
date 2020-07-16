package com.member;

import java.util.ArrayList;

public interface MemberDAO {
	//�߰�
	public void memberInsert(MemberVO vo);
	//��ü����
	public ArrayList<MemberVO>memberList();
	//����
	public void memberUpdate(MemberVO vo);
	//�󼼺���
	public MemberVO memberView(String userid);
	//����
	public void memberDel(String userid);
	//���̵�üũ(�ߺ�üũ)
	public String idCheck(String userid);
}
