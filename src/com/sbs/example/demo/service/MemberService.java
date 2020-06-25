package com.sbs.example.demo.service;

import com.sbs.example.demo.dao.MemberDao;
import com.sbs.example.demo.dto.Member;
import com.sbs.example.demo.factory.Factory;

public class MemberService {
	private MemberDao memberDao;

	public MemberService() {
		memberDao = Factory.getMemberDao();
	}

	public Member getMemberByLoginIdAndLoginPw(String loginId, String loginPw) {
		return memberDao.getMemberByLoginIdAndLoginPw(loginId, loginPw);
	}
	
	public boolean isUsedLoginId(String loginId) {
		Member member = memberDao.getMemberByLoginId(loginId);

		if (member == null) {
			return false;
		}
		return true;
	}

	public int join(String name, String loginId, String loginPw) {
		Member oldMember = memberDao.getMemberByLoginId(loginId);

		if (oldMember != null) {
			return -1;
		}

		Member member = new Member(loginId, loginPw, name);
		return memberDao.save(member);
	}

	public Member getMember(int id) {
		return memberDao.getMember(id);
	}

	public void makeAdminUserIfNotExists() {
		Member member = memberDao.getMemberByLoginId("admin");
		
		if (member == null) {
			join("관리자", "admin", "admin");
		}
	}
}