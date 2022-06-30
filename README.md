# 스프링부트 쇼핑몰 프로젝트

### 출처 (서적)
- 스프링부트 쇼핑몰 프로젝트 with JPA
  - 저자 : 변구훈
  - 출판사 : 로드북
  - 발행년도 : 2021

### 개발환경 (하드웨어)
- Pc
  - MacBook Air m1 (ram 8gb)

### 개발환경 (OS)
- MacOS Monterey (12.4)

### 개발환경 (소프트웨어)
- IDE : IntelliJ
- SDK : zulu-15
- Project : Maven
- Language : Java
- Java Version : 11
- SpringBoot : 2.7.0


### 특이사항
- __branches를 이용한 Chapter 관리__
  - Chapter 별 README에 학습 당시 필기자료 업로드  

<br>

- __application.properties 의 경우 DB 설정 비밀번호로 인해 ignore 설정__
  - 대신 Chapter06 부터 application-noti.properties 파일 생성 후 비밀번호를 제외한 모든 설정 값 작성  

<br>

- __스프링부트 쇼핑몰 프로젝트는 Windows를 기준으로 학습이 진행됨.__
  - MacOS로 진도를 따라가기 위해 일부 코드 MacOS 설정에 맞춰 작성  
    - ex) application.properties 내 이미지 업로드 경로 등

<br>

- __일부 deprecated된 메소드의 경우 수정된 메소드로 작업 진행__
  - ItemRepositoryCustomImpl 내 fetchResults() 메소드를 feth() 메소드로 변경
    - 변경에 따른 일부 코드 수정

<br>

- __HTML에서 금액 관련된 부분에 1000단위 ',' 생성__
