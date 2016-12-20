# Quick Start

Oasis SDA FrameWork를 처음 접하는 분들이 소스를 다운받고 쉽게 시험할 수 있도록 안내합니다.

SDA Server 시험은 아래의 순서로 진행할 수 있습니다.

> 1. MariaDB, JDK 다운로드 및 설치
> 2. Oasis SDA 소스 다운로드
> 3. MariaDB db및 테이블 생성
> 4. SDA 빌드
> 5. SDA 설정 및 웹모듈 실행

## Requirements
* JDK 7+ 
* MariaDB 10.1.X+
* Maven 3.3.X+
* Tomcat 7.0.X+
* Windows / Linux  

## 따라하기

#### (1) MariaDB 다운로드및 설치, JDK 다운로드 및 설치
 - [MariaDB 설치안내](https://mariadb.com/products/get-started)
 - [JDK 설치안내](http://docs.oracle.com/javase/7/docs/webnotes/install/)

#### (2) Oasis SDA 소스 다운로드
 - [릴리즈 페이지](https://github.com/iotoasis/SDA/releases)에서 SDA 소스 및 설치관련 파일을 다운받는다.

#### (3) MariaDB 기본 셋팅
 - [다운로드](../sda-doc/mariadb_script.txt)에서 다운받은 mariadb 스크립트파일(mariadb_script.txt)를 실행시켜준다.

#### (4) SDA 빌드
 - [릴리즈 페이지](https://github.com/iotoasis/SDA/releases)에서 다운받은 SDA 소스를 이클립스에서 불러와서 Build한다.(Maven이용함)
 - SO 소스를 빌드하는 방법은 [소스 Build방법](./build_eclipse.md)페이지를 참고한다.

#### (5) SDA 설정 및 실행
 - 다운 받은 소스의 sda-common/resources/system.properties 파일을 오픈하여 SDA 설정을 수정한다.
 - SDA 설정방법은 [SDA 서버 설정방법](./configuration.md)페이지를 참고한다.

<br>
<br>
