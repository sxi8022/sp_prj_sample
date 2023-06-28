# sp_prj_sample
스프링프로젝트샘플

1. 구현 기능
   - 로그인
   - 회원가입
   - 게시글 등록
   - 게시글 수정
   - 게시글 목록 조회
   - 게시글 상세 조회
   - 댓글 등록
   - 댓글 수정
   - 댓글 삭제
   - 필터로그인 인증 
2. 개선할 점
   - PostMan에서 아예 토큰을 받지 않고 게시글을 등록하거나 수정 하는 등의 인증받지않은 일을 할 때 필터에서 
     Exception 발생은 되지만 PostMan의 ResponseBody에 ResponseStatus 와 에러메시지를 표시하고 싶었다. 그러나 기술적으로 한계가 있었다.
   - 게시글과 댓글 컨트롤러를 보면 게시글에서는 HttpServletRequest을 파라미터로 받지 않지만 댓글 컨트롤러에서는 HttpServletRequest를 파라미터로 받아와서
     토큰에 숨진 유저정보를 활용하는데 사용한다. 시간이 된다면 게시글 테이블도 user 테이블과 조인해서 user테이블 키를 컬럼으로 활용하도록 통일성을 갖춰도 될 것 같다. 
     (번거로우니 개념만 이해하고 넘어갈 생각)
   
   