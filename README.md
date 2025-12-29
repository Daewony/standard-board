# 📌 Standard Board Service (기본에 충실한 게시판)

## 1. 프로젝트 개요
- **목적:** Java/Spring Boot의 핵심 기술을 활용하여, **유지보수성과 확장성**을 고려한 백엔드 시스템 구축
- **핵심 가치:**
  - **안정성 (Stability):** 예외 처리 및 데이터 무결성 보장
  - **가독성 (Readability):** 일본 실무 표준에 맞춘 패키지 구조 및 코드 컨벤션 준수
  - **객체지향 (OOP):** JPA를 활용한 도메인 주도 설계

## 2. 기술 스택 (Tech Stack)
- **Language:** Java 17
- **Framework:** Spring Boot 3.5.9
- **Database:** H2 (Develop) / MySQL (Production - 예정)
- **ORM:** Spring Data JPA
- **Template Engine:** Thymeleaf
- **Tool:** IntelliJ IDEA, Gradle, Git

---

## 3. 기능 명세 (Functional Specifications)

### 👤 회원 (Member)
1. **회원가입:** 이메일(ID), 비밀번호, 닉네임 입력 (유효성 검사 포함)
2. **로그인/로그아웃:** Session 기반의 인증 시스템
3. **회원 정보 수정:** 본인만 수정 가능

### 📝 게시글 (Post)
1. **게시글 작성:** 로그인한 회원만 작성 가능
2. **게시글 조회:** 페이징(Paging) 처리된 목록 조회 및 상세 조회
3. **게시글 수정/삭제:** 작성자 본인만 가능
    - **특이사항:** 데이터 보존을 위해 **'논리적 삭제 (Logical Deletion)'** 적용

---

## 4. DB 설계 (ERD)

### 🏗 설계 의도 (Design Concept)
- **BaseEntity 활용:** 생성일시(`created_at`), 수정일시(`updated_at`)를 공통으로 관리하여 중복 코드 제거
- **논리적 삭제 (Soft Delete):** `status` 컬럼을 두어 실수로 삭제된 데이터를 복구할 수 있도록 안전 장치 마련

### 📊 테이블 구조

#### [Member Table] - 사용자
| Column Name | Type | Key | Description |
|---|---|---|---|
| member_id | Long | PK | 회원 고유 ID (Auto Increment) |
| email | Varchar | UK | 로그인 ID (이메일 형식) |
| password | Varchar | | 암호화된 비밀번호 |
| nickname | Varchar | | 사용자 닉네임 |
| role | Varchar | | 권한 (USER, ADMIN) |
| created_at | DateTime | | 가입 일시 |
| updated_at | DateTime | | 정보 수정 일시 |

#### [Post Table] - 게시글
| Column Name | Type | Key | Description |
|---|---|---|---|
| post_id | Long | PK | 게시글 고유 ID |
| title | Varchar | | 게시글 제목 |
| content | Text | | 게시글 본문 (대용량) |
| view_count | Long | | 조회수 |
| status | Varchar | | 상태 (ACTIVE, DELETED) - **논리적 삭제용** |
| member_id | Long | FK | 작성자 ID (N:1 관계) |
| created_at | DateTime | | 작성 일시 |
| updated_at | DateTime | | 수정 일시 |