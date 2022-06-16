# SpringbootShoppingMall / Chapter01

	- 스프링부트의 특징
		○ 내장 서버를 이용해 별도의 설정 없이 독립 실행이 가능한 스프링 애플리케이션
		○ 톰캣, 제티 또는 언더토우와 같은 웹 애플리케이션서버(WAS) 자체 내장
		○ 빌드 구성을 단순화하기 위한 'Spring Boot Starter' 의존성 제공
		○ XML 설정 없이 단순 자바 수준의 설정 방식 제공
		○ JAR을 이용해 자바 옵션만으로 배포 가능
		○ 애플리케이션의 모니터링과 관리를 위한 스프링 액추에이터 제공


		○ 스프링 액추에이터의 특징
			§ 애플리케이션에서 제공하는 여러가지 정보를 손쉽게 모니터링 할 수 있도록 도와주는 라이브러리.
	
	
	- 개발환경 
		○ 운영체제 : windows 10
		○ IDE : IntelliJ
		○ JDK : 11
		○ Springboot version : 2.5.2
		○ Database : MySQL
		○ Build Tool : maven

		○ 운영체제 : Mac OS (M1)
		○ IDE : IntelliJ
		○ JDK : zulu-15
		○ Springboot version : 2.7.0
		○ Database : MySQL
		○ Build Tool : maven
	
	
	
		○ JDK 설치
			§ 오라클 홈페이지에서 사용할 JDK 다운로드를 한다.
			
			§ 윈도우의 경우 환경변수 설정을 진행한다.
				□ 제어판 - 시스템 및 보안 - 시스템 - (우측) 고급 시스템 설정 - 고급 - (하단) 환경 변수 - 시스템 변수 새로만들기 - JDK를 설치한 경로를 변수 값에 대입 및 변수 이름 'JAVA_HOME' 으로 변경 - 시스템 변수 목록 내 'Path' 더블 클릭 - (우상단) 새로만들기 클릭 후 '%JAVA_HOME%\bin' 입력 후 확인
				
			§ 자바 설치가 정상적으로 되었는지 확인하기
				□ CMD 에서 'java -version' 입력, 설치가 정상적으로 되었다면 현재 자바 버전에 대한 메시지가 출력된다.
		
	
		○ 인텔리제이 설치
			§ 필요에 따라 커뮤니티 버전 다운로드, 혹은 유료버전을 사용 (유료 버전은 추가적인 기능이 더 많다)
	
	
	
		○ 프로젝트 생성
			§ https://start.spring.io/  에서 프로젝트를 생성할 수 있다.
			§ ADD DEPENDENCIES 에서 프로젝트에 대한 의존성들을 추가할 수 있다.
	
	
			§ 프로젝트 폴더 구조에 대한 설명
				□ src/main/java : 자바 소스코드 작성
				□ src/main/resoures : HTML, CSS, JS, 이미지 파일 등의 정적 리소스를 저장
				□ src/main/resources/templates : HTML 파일을 작성하고, Controller Class 에서 반환한 뷰와 동일한 이름의 HTML 파일을 작성하는 곳
				□ src/test/java : 테스트코드 작성
		
		
		○ 의존성
			§ pom.xml 파일에서 parent 속서 내에 있는 dependencies 속성으로 묶여있는 것들이 현재 사용하기 위해 선언해둔 의존성 속성들이다.
			§ 의존성을 하기 위한 다른 방법으로 인텔리제이 우측 'Maven' 을 클릭하여 'Dependencies' 에서 현재 주입된 의존성들을 확인할 수 있다.
	
	
	
		○ application.properties
			§ 실행 시 사용하는 여러가지 설정값들을 정의하는 파일.
			§ src/main/resource 폴더에 자동으로 생성된다. (자동 생성이 되지 않는다면 직접 생성해줘도 무관하다.)
			§ port, debug level 등을 설정할때 쓰며 간혼 application-dev.properties, application-prod.properties 파일로 분할하여 사용하기도 한다.
			§ application.properties에서 설정한 값을 자바코드에서 사용해야할때 @Value 어노테이션을 이용해 읽어올 수 있다.
	
			§ 직접 해보기
			#애플리케이션의실행할포트를설정한다.80포트는url뒤에포트번호를생략할수있다.
			server.port=80
			
			#애플리케이션이름을설정한다.
			application.name=spring-demo
				
				
			§ 애플리케이션 설정 파일을 만드는 또 다른 방법으로 'application.yml' 파일을 사용할 수 있다.
		
		
	- Hello World 출력하기
		○ RestController
			§ Restful Web API를 좀 더 쉽게 만들기 위해 도입된 기능.
			§ Controller 와 ResponseBody 를 합쳐논 어노테이션
			§ 이 어노테이션으로 별도의 HTML 파일이 없어도 HTTP 응답 본문의 객체로 변환해 클라이언트에게 전송한다.

		○ GetMapping
			§ URL 매핑을 한다.
	
	
	
	- Lombok 라이브러리
		○ 플러그인 Lombok을 설치해준다.
		○ 설정 - 빌드 - 컴파일러 - Annotation Processors 에서 Enable annotation processing 을 체크해준다. (롬북을 사용하는 모든 프로젝트에서 체크를 해줘야한다고 한다.)
		○ pom.xml 에 dependencies 속성으로 롬복을 추가해준다.
		<!--Lombok사용선언-->
		<dependency>
		<groupId>org.projectlombok</groupId>
		<artifactId>lombok</artifactId>
		</dependency>
			
		○ 추가 후 재실행 시켜주면 Lombok이 Maven - Dependencies 에 추가가 되었을 것이다.
		○ Lombok 어노테이션
			§ 어노테이션	설명
			@Getter/Setter	코드를 컴파일 할 때 속성들에 대한 Getter/Setter 자동 생성
			@ToString	toString() 메소드 생성
			@ToString(exclude={"변수명"})	원하지 않는 속성을 제외한 toString 메소드 생성
			@NonNull	해당 변수가 null체크, NullPointException 예외 발생
			@EqualsAndHashCode	equals() 와 hashCode() 메소드 생성
			@Builder	빌더 패턴을 이용한 객체 생성
			@NoArgsConstructor	파라미터가 없는 기본 생성자 생성
			@AllArgsConstructor	모든 속성에 대한 생성자 생성
			@RequiredArgsConstructor	초기화되지 않은 Final, @NonNull 어노테이션이 붙은 필드에 대한 생성자 생성
			@Log	log 변수 자동 생성
			@Value	불변 클래스 생성
			@Data	@ToString, @EqualsAndHashCode, @Getter, @Setter, @RequiredArgsConstructor를 합치 어노테이션

			§ 컴파일러가 실행된 후 컴파일 된 파일(target - classes)을 확인해보면 getter, setter, toString 메소드가 생성된걸 볼 수 있다.
			
			
			
	- MySQL 설치
		○ MySQL Community Server 을 다운로드 받는다.
			§ MAC 에서 사용을 위해 sequal pro 프로그램을 사용한다.
			§ shop 데이터베이스를 생성하고 utf8 을 사용하기 위해 아래와 같이 명령어를 실행한다.
				□ create database shop default character set utf8 collate utf8_general_ci;
		
			
![image](https://user-images.githubusercontent.com/77163842/174022805-2070c1fb-ddb7-4e84-a033-38684d5a5785.png)
