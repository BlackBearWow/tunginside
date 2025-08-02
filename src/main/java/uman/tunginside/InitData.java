package uman.tunginside;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import uman.tunginside.domain.category.CategoryRegisterForm;
import uman.tunginside.domain.comment.CommentWriteForm;
import uman.tunginside.domain.member.MemberSignupForm;
import uman.tunginside.domain.post.PostWriteForm;
import uman.tunginside.service.CategoryService;
import uman.tunginside.service.CommentService;
import uman.tunginside.service.MemberService;
import uman.tunginside.service.PostService;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class InitData {

    private final MemberService memberService;
    private final CategoryService categoryService;
    private final PostService postService;
    private final CommentService commentService;

    @PostConstruct
    public void init() {
        Long member_id = memberService.signup(new MemberSignupForm("admin", "admin1", "관리자"));
        Long member_id2 = memberService.signup(new MemberSignupForm("guest", "guest1", "떠돌이"));

        Long categoryIdLol = categoryService.registerCategory(new CategoryRegisterForm("리그오브레전드", "lol"), member_id);
        Long categoryIdMp = categoryService.registerCategory(new CategoryRegisterForm("메이플스토리", "mp"), member_id);
        Long categoryIdDf = categoryService.registerCategory(new CategoryRegisterForm("던전앤파이터", "df"), member_id);
        Long categoryIdSc = categoryService.registerCategory(new CategoryRegisterForm("스타크래프트", "sc"), member_id);
        Long categoryIdSoop = categoryService.registerCategory(new CategoryRegisterForm("숲(soop)", "soop"), member_id);
        Long categoryIdChzzk = categoryService.registerCategory(new CategoryRegisterForm("치지직", "chzzk"), member_id);

        Long postId = postService.writePost(new PostWriteForm("lol", "여러분 제가 오늘 어이없는 일을 겪었는데요", "여러분 제가 오늘 어이없는 일을 겪었는데요...\n" +
                "원래 탕후루란게 제철이고 수요많은 과일들로 만드는거 아닌가요...?\n" +
                "오늘 탕후루 가게에 갔는데\n" +
                "글쎄 대상혁 탕후루가 없다는거에요...\n" +
                "대상혁만큼 제철인게 어디 있다고...\n" +
                "심지어 계절도 안타서 항상 제철일텐데...\n" +
                "속상한 마음에 댓글에라도 남겨봐요...\n", ""), member_id, "127.0.0.1");

        postId = postService.writePost(new PostWriteForm("lol", "어제 페이커 카페에 갔습니다", "어제 페이커 카페에 갔습니다\n" +
                "페이커 카페가 열린 건 아니고요\n" +
                "그냥 카페에서 페이커 생각을 했습니다\n" +
                "사실 카페에 간 건 아니고요\n" +
                "그냥 집에서 커피를 마셨습니다.\n" +
                "사실 커피도 안 마셨습니다\n" +
                "그냥 페이커 상태입니다", ""), member_id, "127.0.0.1");
        Long commentId = commentService.writeComment(postId, new CommentWriteForm("", "ㄷㄷ", null), member_id2, "");

        postId = postService.writePost(new PostWriteForm("mp", "다 해줬잖아 (신창섭)", "리부트 정상화 해줬잖아\n" +
                "본서버 완화도 해줬잖아\n" +
                "컨텐츠 출시도 해줬잖아\n" +
                "씨발 다! 그냥 다 해줬잖아\n" +
                "리부트 정상화 해줬잖아\n" +
                "본서버 완화도 해줬잖아\n" +
                "컨텐츠 출시도 해줬잖아\n" +
                "씨발 다! 그냥 다 해줬잖아\n" +
                "메벤남 이 씹새끼들\n" +
                "돈 열심히 쓰는줄 알았더니\n" +
                "대부분 전투력 5000만 플마\n" +
                "이런놈들 믿고 해달란거 다 해준\n" +
                "내가 병신이지 병신이야\n" +
                "아니지 나는 정상화의 신\n" +
                "아직 남아있는 최후의 수단이 있지\n" +
                "리부트 최종뎀 정상화\n" +
                "공격력 마력 체력 마나까지 정상화\n" +
                "(이제 돈 열심히 써줄거지?)", ""), member_id, "127.0.0.1");
        commentId = commentService.writeComment(postId, new CommentWriteForm("", "신창섭", null), member_id2, "");

        postId = postService.writePost(new PostWriteForm("mp", "팡이요", "메이플에 돈 얼마나 씀?", "1234"), null, "192.168.0.102");
        commentId = commentService.writeComment(postId, new CommentWriteForm("1234", "40억 넘는거로 아는데", null), null, "192.168.0.103");
        commentId = commentService.writeComment(postId, new CommentWriteForm("1234", "그형님은 결혼 언제하누", commentId), null, "192.168.0.104");

        postId = postService.writePost(new PostWriteForm("df", "던파 뉴비 직업 추천좀요", "제곧내", "1234"), null, "192.168.0.102");
        commentId = commentService.writeComment(postId, new CommentWriteForm("1234", "그건 너가 알아서 알아보시고~", null), null, "192.168.0.104");
        commentId = commentService.writeComment(postId, new CommentWriteForm("1234", "좀 알려줘라", commentId), null, "192.168.0.102");

        postId = postService.writePost(new PostWriteForm("sc", "님들 공격키 새끼손가락으로 누름 4번째 손가락으로 누름?", "나는 새끼손가락", "1234"), null, "192.168.0.104");
        commentId = commentService.writeComment(postId, new CommentWriteForm("1234", "나도 새끼손가락으로 누른다", null), null, "192.168.0.110");
        commentId = commentService.writeComment(postId, new CommentWriteForm("1234", "아니 어떻게 새끼손가락으로 누르냐? 당연히 4번째 손가락이지", commentId), null, "192.168.0.111");
        commentId = commentService.writeComment(postId, new CommentWriteForm("1234", "롤도 qwer 새끼손가락부터 시작하는게 정석인데 스타도 당연히 새끼손가락이지\n" +
                "이놈 키설정 못하던 시대에 적응해버린거같은데?", commentId), null, "192.168.0.112");

        postId = postService.writePost(new PostWriteForm("sc", "테사기 진짜", "테혼 진짜 테사기 못이겨먹겠네\n" +
                "테란으로 종변한다 더러워서", "1234"), null, "192.168.0.105");
        postId = postService.writePost(new PostWriteForm("soop", "ㅊㄱㅇ", "철구업", "1234"), null, "192.168.0.106");

        postId = postService.writePost(new PostWriteForm("soop", "ㅂㅈㅇ", "봉준업", "1234"), null, "192.168.0.106");

        postId = postService.writePost(new PostWriteForm("chzzk", "유니유니가", "텐미리임?", "1234"), null, "192.168.0.107");
        commentId = commentService.writeComment(postId, new CommentWriteForm("1234", "이걸 이제앎?", null), null, "192.168.0.113");

        postId = postService.writePost(new PostWriteForm("chzzk", "따효니 활협전 하는거", "한글 정발 나옴?\n" +
                "왜 요즘 스트리머들 활협전하는거 다시 보이냐", "1234"), null, "192.168.0.108");
    }
}