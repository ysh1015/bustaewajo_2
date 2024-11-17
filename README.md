# 융합프로젝트 - 버스태워줘 앱

#### 첫화면 구성 : 네이버 지도나 구글 지도처럼 띄우기

![완성된 앱 구조](https://github.com/ysh1015/Transport_Project/blob/FrontAnd_MAP/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7_10-10-2024_16321_cdn.discordapp.com.jpeg)

---

## 필요한 정보

### 1. 지도 API

네이버 지도 API나 티맵 API를 우선적으로 넣어보고, 안되면 구글 지도API을 사용해서 구현할 것

[네이버 지도 API 신청](https://www.ncloud.com/product/applicationService/maps)

##### 2. 문화유산 API

경기도 오픈 API를 사용해서 구현할 것

[문화유산 경기도 오픈 API](https://www.data.go.kr/data/15094713/fileData.do)

##### 3. 날씨 API

아직찾아봐야 됨. 하지만 오픈 데이터 API 사이트에 있을 것으로 예상됨.

---



## 필요한 xml

1. __지도 API를 보여주는 메인 xml__

2. __버스 정류장 클릭시 그것에 대한 정보와 문화유산을 보여주는 xml__

3. __날씨 아이콘 클릭시 날씨에 대한 자세한 정보를 보여주는 (날씨 API연결) xml__ 

---

# 프로젝트 하면서 공부한 것
## 1. Retrofit
  Retrofit : HTTP를 안드로이드 스튜디오와 연결시켜주는 기술

  ### Retrofit 관련 오류
    1. BASE_URL 와 엔드포인트 경계
    2. DTO 파일의 생성 : Gson의 jsonObject로 해결함
  __주의 : 안드로이드의 JSONObject와 Gson의 jsonObject는 서로 다른 것임__

#### XML 연결 관련 오류
    1. SimpleXML 버전 오류 : 옛날 버전에서나 쓰이던거라 현재 코알라 버전에서는 사용 불가
    2. JXDBXML 사용 : 이클립스나 인텔리제이같은 Java 표준 라이브러리에서 사용하는 모듈이라 안드로이드 스튜디오에서는 사용안됨
    3. TikXML 사용 : 현재 XML 형식의 API을 연결하게 되면 사용하는 모듈. 의존성 추가에 확실하게 다 해줘야 작동함(어노테이션, 컨버터, 코어,프로세서)
----> XML 자체에서 API에 없는 태그가 DTO 파일에 추가되있지 않다는 이상한 오류가 떠서 방법을 바꿈

__결론 : 교통정보 API(XML 형식)는 안드로이드 스튜디오가 아닌 Spring-boots에서 처리하여 안드로이드 스튜디오에서 호출 시 불러오게 할거임.__

#### 2024.11.08 알아낸 사실
    지금까지 사용한 교통정보 API 자체가 Postman이나 공공데이터포털에서 테스트할때는 최고 헤더가 response로 잘 불러오는데 
    Springboots나 안드로이드 스튜디오에서 불러올시 최고헤더가 OpenAPI_ServiceResponse로 엉뚱하게 변경되어 불러오게 되 오류가 생기는 것이었다.
    결론 : 교통정보 API를 변경해야 함






## 2. 네이버 지도 SDK 연결

### MPAS

[네이버 지도 API 개발자 가이드](https://navermaps.github.io/android-map-sdk/guide-ko/4-2.html)

가이드와 블로그를 이용하여 구현함

    오류 : 디버깅 과정 빌드 오류는 생기지 않았음. 하지만, 디버그 실행 시 실제 위치와 대략 1km정도의 차이가 나는 위치에 표시됨
    해결방법 : Databinding을 사용하여 GPS 불러오니까 해결됨. 원래는 기본으로 불러왔었음.


