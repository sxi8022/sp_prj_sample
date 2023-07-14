# sp_prj_sample
스프링프로젝트샘플

1. 주요 구현 기능
   - 회원가입
   - 게시글 등록 (등록 시 카테고리 자동 등록)
   - 게시글 수정
   - 게시글 목록 조회(대댓글 조회 포함)
   - 카테고리별 게시글 목록 조
   - 게시글 상세 조회
   - 댓글 등록
   - 댓글 수정
   - 댓글 삭제
   - 로그인/로그아웃 인증(refresh 토큰 활용)
   - 토큰 재발급(redis활용)
   - aop를 활용한 예외처
   - 회원탈퇴 (연관된 게시글 데이터 삭제)
   - swagger 라이브러리 추가 및 적용
2. 궁금한 점 
   - redis 기능을 활용해서 로그아웃 한상태에서 게시글 등록을 하면 로그인 해야한다고 메시지를 뿌렸는데 authorizationFilter에서 조건을 걸었는데
    로그인/로그아웃 시도를 하지 않은상태에서 게시글을 작성하려고할때 예외처리는 어느부분에서 처리해주어야 하는것이 맞는가  
   - 토큰 재발급은 포스트맨에서 호출 시 서비스에서 작동하도로 처리하였는데 프론트하고 연동이 된다면 어떤상황에서 토큰 재발급을 해주는가 ?
   - 카테고리와 게시글 기능을 구현하였는데 게시글은 여러 카테고리를 가질 수 있고 카테고리는 여러 게시글을 가질 수있다.
     중간 테이블을 만들어서 구현했는데, 해당 소스에서 카테고리만 삭제 했을때는 어떻게 대처할 수 있을까?
   
   
