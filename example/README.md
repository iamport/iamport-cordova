# 예제 프로젝트

아임포트 코르도바 플러그인 예제 안내입니다.

iamport-cordova 플러그인을 clone받아 폴더 위치를 `example` 프로젝트로 이동합니다.

```
$ git clone https://github.com/iamport/iamport-cordova.git
$ cd ./iamport-cordova/example
```

안드로이드 플랫폼을 추가합니다.

```
$ yarn install-android
```

각 환경에 맞게 앱을 빌드합니다.

### Android
- [안드로이드 스튜디오를 설치](https://developer.android.com/studio)합니다.
- 안드로이드 스튜디오에서 프로젝트(`iamport-cordova/example/platforms/android`)를 더블클릭해 오픈합니다.
- 빌드 타깃을 선택하고 앱을 빌드합니다.


### 일반/정기결제 코드 예시
```html
<html>
  ...
  <body>
    <button id="iamport-payment">결제하기</button>

    <script type="text/javascript" src="cordova.js"></script>
    <script type="text/javascript" src="js/payment.js"></script>
  </body>
</html>
```

```javascript
// js/payment.js
document.getElementById('iamport-payment').addEventListener('click', function() {
  var title = {
    name: '아임포트 코르도바 테스트',                   // 안드로이드 액션바 타이틀
    color: '#344e81'                              // 안드로이드 액션바 배경색
  };
  var userCode = 'iamport';                       // 가맹점 식별코드
  var data = {
    pg: 'html5_inicis'                            // PG사
    pay_method: 'card',                           // 결제수단
    name: '아임포트 코르도바 테스트',                   // 주문명
    merchant_uid: 'mid_' + new Date().getTime(),  // 주문번호
    amount: '39000',                              // 결제금액
    buyer_name: '홍길동',                           // 구매자 이름
    buyer_tel: '01012341234',                     // 구매자 연락처
    buyer_email: 'example@example.com',           // 구매자 이메일
    app_scheme: 'example',                        // [필수입력] 앱 URL 스킴
  };

  var params = {
    title: title,                                 // 안드로이드 액티비티 타이틀
    userCode: userCode,                           // 가맹점 식별코드
    data: data,                                   // 결제 데이터
    callback: callback,                           // 콜백 함수
  }
  cordova.plugins.IamportCordova.payment(params);
});
```

| Prop      | Type          |  Description                                                | Required   |
| --------- | ------------- | ----------------------------------------------------------- | ---------- |
| title     | object        | 안드로이드 액티비티 액션바 옵션                                      | false      |
| - name    | string        | 안드로이드 액티비티 액션바 타이틀                                     | false      |
| - color   | string        | 안드로이드 액티비티 액션바 배경색                                     | false      |
| userCode  | string        | 가맹점 식별코드                                                 | true       |
| data      | object        | 결제에 필요한 정보 [자세히 보기](https://docs.iamport.kr/tech/imp) | true       |
| callback  | function      | 결제 후 실행 될 함수 [자세히보기](#callback)                       | true       |


### 휴대폰 본인인증 코드 예시
```html
<html>
  ...
  <body>
    <button id="iamport-certification">본인인증 하기</button>

    <script type="text/javascript" src="cordova.js"></script>
    <script type="text/javascript" src="js/certification.js"></script>
  </body>
</html>
```

```javascript
document.getElementById('iamport-certification').addEventListener('click', function() {
  var title = {
    name: '아임포트 코르도바 테스트',                   // 안드로이드 액션바 타이틀
    color: '#344e81'                              // 안드로이드 액션바 배경색
  };
  var userCode = 'imp10391932';                   // 가맹점 식별코드
  var data = {
    carrier: 'KTF',                               // 통신사
    merchant_uid: 'mid_' + new Date().getTime(),  // 주문번호
    company: 'SIOT',                              // 회사명
    name: '홍길동',                                 // 이름
    phone: '01012341234',                         // 전화번호
  };
  var params = {
    title: title,                                 // 안들외드 액티비티 타이틀
    userCode: userCode,                           // 가맹점 식별코드
    data: data,                                   // 본인인증 데이터
    callback: callback,                           // 콜백 함수
  };

  cordova.plugins.IamportCordova.certification(params);
});
```

| Prop          | Type          |  Description                       | Required   |
| ------------- | ------------- | ---------------------------------- | ---------- |
| title         | string        | 안드로이드 액티비티 타이틀                | false      |
| userCode      | string        | 가맹점 식별코드                        | true       |
| data          | object        | 본인인증에 필요한 정보 [자세히 보기](https://https://docs.iamport.kr/tech/mobile-authentication#call-authentication)      | true       |
| - merchant_uid| string        | 가맹점 주문번호                        | false      |
| - company     | string        | 회사명 또는 URL                       | false      |
| - carrier     | string        | 통신사                               | false      |
| - name        | string        | 본인인증 할 이름                        | false      |
| - phone       | number        | 본인인증 할 전화번호                     | false      |
| - min_age     | number        | 본인인증 허용 최소 연령                  | false      |
| callback      | function      | 본인인증 후 실행 될 함수                 | true       |
