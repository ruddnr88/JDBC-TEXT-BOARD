package com.sbs.example.demo.service;

import java.util.List;
import java.util.Map;

import com.sbs.example.demo.dao.ArticleDao;
import com.sbs.example.demo.dto.Article;
import com.sbs.example.demo.dto.ArticleReply;
import com.sbs.example.demo.dto.Board;
import com.sbs.example.demo.dto.Member;
import com.sbs.example.demo.factory.Factory;

public class ArticleService {
	private ArticleDao articleDao;
	private MemberService memberService;

	public ArticleService() {
		articleDao = Factory.getArticleDao();
		memberService = Factory.getMemberService();
	}

	public List<Article> getArticlesByBoardCode(String code) {
		return articleDao.getArticlesByBoardCode(code);
	}

	public List<Board> getBoards() {
		return articleDao.getBoards();
	}

	public int makeBoard(String name, String code) {
		Board oldBoard = articleDao.getBoardByCode(code);

		if (oldBoard != null) {
			return -1;
		}

		Board board = new Board(name, code);
		return articleDao.saveBoard(board);
	}

	public Board getBoard(int id) {
		return articleDao.getBoard(id);
	}

	public int write(int boardId, int memberId, String title, String body) {
		Article article = new Article(boardId, memberId, title, body);
		return articleDao.save(article);
	}

	public List<Article> getArticles() {
		return articleDao.getArticles();
	}

	public void makeBoardIfNotExists(String name, String code) {
		Board board = articleDao.getBoardByCode(code);

		if (board == null) {
			makeBoard(name, code);
		}
	}
	
	public Member getMemberName(int id) {
		return memberService.getMember(id);
	}
	

	public Board getBoardByCode(String boardCode) {
		return articleDao.getBoardByCode(boardCode);
	}

	public int isExistArticle(int number) {
		return articleDao.isExistArticle(number);
	}

	public int modifyArticle(int number, String title, String body) {
		return articleDao.modifyArticle(number, title, body);
	}

	public int deleteArticle(int number) {
		return articleDao.deleteArticle(number);
	}

	public Article getArticlesById(int number) {
		return articleDao.getArticlesById(number);
	}
	
	public int replyArticle(int memberId, int articleId,int number,String body) {
		ArticleReply articleReply = new ArticleReply();
		return articleDao.replyArticle(articleReply);
	}

	public List<ArticleReply> getArticleRepliesByArticleId(int id) {
		return articleDao.getArticleRepliesByArticleId(id);
	}

	

}