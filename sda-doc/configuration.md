# SDA 서버 설정

SDA 서버 설정방법을 설명합니다.

설정은 properties 파일로 작성되며 sda-common 폴더의 resources 폴더에 존재합니다. 

```
system.properties
```
<br>

설정항목별 의미는 아래와 같습니다.

- jena운영<br>
 com.pineone.icbms.sda.knowledgebase.host=XXX.XXX.XXX.XXX
 com.pineone.icbms.sda.knowledgebase.public.host=XXX.XXX.XXX.XXX

- fuseki운영<br>
 com.pineone.icbms.sda.knowledgebase.sparql.endpoint=http://XXX.XXX.XXX.XXX:포트/icbms 
 com.pineone.icbms.sda.knowledgebase.host.port=포트 
 com.pineone.icbms.sda.knowledgebase.uri=http://www.iotoasis.org 

- SI운영<br>
 com.pineone.icbms.sda.mongodb.server=XXX.XXX.XXX.XXX 
 com.pineone.icbms.sda.mongodb.port=포트
 com.pineone.icbms.sda.mongodb.db=db명 

- data수집 1회 처리개수<br>
 com.pineone.icbms.sda.mongodb.read_limit=20000

-  DB정보운영<br>
 com.pineone.icbms.sda.ss.db.server=XXX.XXX.XXX.XXX 
 com.pineone.icbms.sda.ss.db.port=포트 
 com.pineone.icbms.sda.ss.db.name.timetable=db명 
 com.pineone.icbms.sda.ss.db.name.device=db명 
 com.pineone.icbms.sda.ss.db.user=사용자계정 
 com.pineone.icbms.sda.ss.db.pass=패스워드 

- fuseki의 s-post위치<br>
 com.pineone.icbms.sda.triple.regist.bin=/fuseki설치위치/bin/s-post 

- triple파일 저장위치<br>
 com.pineone.icbms.sda.triple.save_path=/triple저장폴더/triples
  
- SDA운영서버의 콜백 URI<br>
 com.pineone.icbms.sda.si.notification_uri=http://XXX.XXX.XXX.XXX:포트/sda/subscribe/callback 


- SI운영(description설정용)<br>
 com.pineone.icbms.sda.si.subscription_uri=http://XXX.XXX.XXX.XXX:포트번호/경로/
  
- SO운영(callback용)<br>
 com.pineone.icbms.sda.so.callback_result_uri=http://XXX.XXX.XXX.XXX:포트번호/경로/ 

- riot모드<br>
 --skip : riot적용하지 않음<br>
--check : 전체 파일을 체크하여 오류가 나는 부분 추출 —> 개발중에 활용<br>
--validate : 파일을 스캔하다가 문법의 오류가 있으면 바로 파싱을 멈추고 오류 발생시 종료<br>
 com.pineone.icbms.sda.riot.bin=/jena설치위치/bin/riot 
 com.pineone.icbms.sda.riot.mode=--skip 
 com.pineone.icbms.sda.riot.result.save_path=/riot결과저장폴더/riot-result 

- kafka 쓰레드 개수<br>
 com.pineone.icbms.sda.kafka.thread.count=3

- lastestedContentInstance를 구하는 기준시간을 구할때 사용되는 마이너스 할 시간을 ms단위로 지정한다.<br>
 (현재시간 - 10초) 부터 유효하게 하려면 1000*10값을 지정한다.<br>
com.pineone.icbms.sda.init.adjust.ms=10000
 
--통계 쿼리를 수행할 db접속정보<br>
 com.pineone.icbms.sda.stat.db.server=XXX.XXX.XXX.XXX 
 com.pineone.icbms.sda.stat.db.port=포트번호 
 com.pineone.icbms.sda.stat.db.name=db명
 com.pineone.icbms.sda.stat.db.user=사용자계정 
 com.pineone.icbms.sda.stat.db.pass=패스워드 
 
 --mongodb정보(통계용)<br>
com.pineone.icbms.sda.mongo.db.server=XXX.XXX.XXX.XXX
com.pineone.icbms.sda.mongo.db.port=포트번호
com.pineone.icbms.sda.mongo.db.name=db명
com.pineone.icbms.sda.mongo.db.collection.name=collection명