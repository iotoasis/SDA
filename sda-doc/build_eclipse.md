## SDA Framework Server Build

다운받은 SDA Framework 서버 소스를 Eclipse를 이용해서 Build하는 방법을 설명합니다.

Build를 위한 요구조건은 아래와 같습니다.

## Requirements
* JDK 7+
* Windows / Linux
* Eclipse
* Maven

Build 실행 순서는 아래와 같습니다.

> 1. 이클립스로 소스 import
> 2. Maven을 이용해서 소스 컴파일
> 3. 설정 파일 추가 및 수정
> 4. sda-web모듈 실행

<br>
#### 1. 이클립스로 소스 import
- 메뉴에서 File/Import 메뉴를 선택하여 Import 창을 열어  Maven Project를 선택한 후 소스가 저장된 폴더를 선택하여 import합니다.

<br>
#### 2. 설정 파일 추가 및 수정
- Release 페이지에서 다운받은 설정파일(system.properties)을 수정 합니다.
- DB설정 등 로컬환경에 맞게 설정파일을 수정합니다. 설정파일에 대한 자세한 내용은 [SDA 서버설정](./configuration.md)페이지를 참고하세요.

<br>
#### 3. Maven을 이용한 Build
- Alt+Shift+X,M 로 Maven Build 실행.
- Package를 입력하고 build 수행

<br>
#### 4. SDA 웹모듈 실행
- target 폴더(sda-web/target/sda-web-2.0.war)를 tomcat의 webapps에 sda.war로 이름을 변경하여 복사하고 tomcat를 기동해준다.