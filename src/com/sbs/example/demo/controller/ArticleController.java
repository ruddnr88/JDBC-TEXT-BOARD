package com.sbs.example.demo.controller;

import java.util.List;
import java.util.Map;

import com.sbs.example.demo.dto.Article;
import com.sbs.example.demo.dto.ArticleReply;
import com.sbs.example.demo.dto.Board;
import com.sbs.example.demo.dto.Member;
import com.sbs.example.demo.factory.Factory;
import com.sbs.example.demo.service.ArticleService;
import com.sbs.example.demo.service.MemberService;

public class ArticleController extends Controller {
	private ArticleService articleService;
	private MemberService memberService;

	public ArticleController() {
		articleService = Factory.getArticleService();
	}

	public void doAction(Request reqeust) {
		if (reqeust.getActionName().equals("list")) {
			actionList(reqeust);
		} else if (reqeust.getActionName().equals("write")) {
			if (Factory.getSession().isLogined() == false) {
				System.out.println("현재 로그인 상태가 아닙니다.");
			} else {
				actionWrite(reqeust);
			}

		} else if (reqeust.getActionName().equals("modify")) {
			actionModify(reqeust);
		} else if (reqeust.getActionName().equals("delete")) {
			actionDelete(reqeust);
		} else if (reqeust.getActionName().equals("detail")) {
			actionDetail(reqeust);
		} else if (reqeust.getActionName().equals("changeBoard")) {
			actionChangeBoard(reqeust);
		} else if (reqeust.getActionName().equals("currentBoard")) {
			actionCurrentBoard(reqeust);
		}
	}

	private void actionDetail(Request reqeust) {
		System.out.println("== 게시물 상세보기 ==");
		int number = -1;
		try {
			number = Integer.parseInt(reqeust.getArg1());

		} catch (Exception e) {
			System.out.println("게시물 번호를 숫자로 입력해주세요.");
		}

		Article article = articleService.getArticlesById(number);

		if (article == null) {
			System.out.println("해당 게시물은 존재하지 않습니다.");
			return;
		}

		String writerName = getMember(article.getMemberId()).getName();

		List<ArticleReply> articleReplies = articleService.getArticleRepliesByArticleId(article.getId());
		int repliesCount = articleReplies.size();

//		int WriteId = article.getMemberId();
//		Member member = memberService.getMember(WriteId);
//		String articleN = member.getName();
		System.out.printf("번호 : %d\n", article.getId());
		System.out.printf("제목 : %s\n", article.getTitle());
		System.out.printf("날짜 : %s\n", article.getRegDate());
		System.out.printf("작성자 : %s\n", writerName);
		System.out.printf("내용 : %s\n", article.getBody());
		System.out.printf("댓글개수 : %d\n", repliesCount);
		
		for ( ArticleReply articleReply : articleReplies ) {
			String replyWriterName = getMember(article.getMemberId()).getName();
			
			System.out.printf("%d번 댓글 : %s by %s\n", articleReply.getId(), articleReply.getBody(), replyWriterName);
		}
	

	}

	//댓글 오류있음!! 값이 없때..ㅠㅠ 
	private void actionReply(Request reqeust) {

		System.out.printf("내용 : ");
		String replyBody = Factory.getScanner().nextLine();
		

		int memberId = Factory.getSession().getLoginedMember().getId();

		int newId = articleService.replyArticle(memberId,replyBody);

		System.out.printf("%d번 댓글이 생성되었습니다.\n", newId);

	}

	private Member getMember(int memberId) {
		return articleService.getMemberName(memberId);
	}

	private void actionDelete(Request reqeust) {
		System.out.println("== 게시물 삭제 ==");
		int number = -1;
		try {
			number = Integer.parseInt(reqeust.getArg1());
		} catch (Exception e) {
		}

		int result = articleService.isExistArticle(number);

		if (result != -1) {
			articleService.deleteArticle(number);
			System.out.printf("%s번 게시물이 삭제되었습니다.\n", number);
		} else if (result == -1) {
			System.out.println("삭제할 게시물이 존재하지 않습니다.");
		}
	}

	private void actionModify(Request reqeust) {
		int number = -1;

		try {
			number = Integer.parseInt(reqeust.getArg1());
		} catch (Exception e) {
		}

		int result = articleService.isExistArticle(number);

		if (result > 0) {
			System.out.println("== 게시글 수정 ==");
			System.out.printf("제목 : ");
			String title = Factory.getScanner().nextLine();
			System.out.printf("내용 : ");
			String body = Factory.getScanner().nextLine();
			articleService.modifyArticle(number, title, body);
			System.out.println(number + "번 글이 수정되었습니다.");
		} else if (result == -1) {
			System.out.println("해당 번호의 게시글이 존재하지 않습니다.");
		}
		return;
	}

	private void actionCurrentBoard(Request reqeust) {
		Board board = Factory.getSession().getCurrentBoard();
		System.out.printf("현재 게시판 : %s\n", board.getName());
	}

	private void actionChangeBoard(Request reqeust) {
		String boardCode = reqeust.getArg1();

		Board board = articleService.getBoardByCode(boardCode);

		if (board == null) {
			System.out.println("해당 게시판이 존재하지 않습니다.");
		} else {
			System.out.printf("%s 게시판으로 변경되었습니다.\n", board.getName());
			Factory.getSession().setCurrentBoard(board);
		}
	}

	private void actionList(Request reqeust) {
		Board currentBoard = Factory.getSession().getCurrentBoard();
		List<Article> articles = articleService.getArticlesByBoardCode(currentBoard.getCode());
	

		System.out.printf("== %s 게시물 리스트 시작 ==\n", currentBoard.getName());
		System.out.printf("%-2s|%-25s|%-28s|%-10s\n", "번호", "날짜", "제목","작성자");
		for (Article article : articles) {
			System.out.printf("%-4d|%-27s|%-30s|%-8s\n", article.getId(), article.getRegDate(), article.getTitle(),getMember(article.getMemberId()).getName());
		}
		System.out.printf("== %s 게시물 리스트 끝 ==\n", currentBoard.getName());
	}

	private void actionWrite(Request reqeust) {
		System.out.printf("제목 : ");
		String title = Factory.getScanner().nextLine();
		System.out.printf("내용 : ");
		String body = Factory.getScanner().nextLine();

		// 현재 게시판 id 가져오기
		int boardId = Factory.getSession().getCurrentBoard().getId();

		// 현재 로그인한 회원의 id 가져오기
		int memberId = Factory.getSession().getLoginedMember().getId();
		int newId = articleService.write(boardId, memberId, title, body);

		System.out.printf("%d번 글이 생성되었습니다.\n", newId);
	}
}