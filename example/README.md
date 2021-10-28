# 예제 프로젝트

아임포트 코르도바 플러그인 예제 안내입니다.

iamport-cordova 플러그인을 clone받아 폴더 위치를 `example` 프로젝트로 이동합니다.

```
$ git clone https://github.com/iamport/iamport-cordova.git
$ cd ./iamport-cordova/example
```

모바일 플랫폼을 추가합니다.
(cordova platform android/ios 및 iamport-cordova 플러그인 설치)

```
$ npm run install-iamport
(재설치시 문제가 생길경우 먼저 $ npm run uninstall-iamport 실행)
```

각 환경에 맞게 앱을 빌드합니다.

### Android
- [안드로이드 스튜디오를 설치](https://developer.android.com/studio)합니다.
- 안드로이드 스튜디오에서 프로젝트(`iamport-cordova/example/platforms/android`)를 더블클릭해 오픈합니다.
- 빌드 타깃을 선택하고 앱을 빌드합니다.

### iOS
- [Xcode를 설치](https://developer.apple.com/xcode/)합니다.
- Xcode에서 프로젝트(`iamport-cordova/example/platforms/ios/IamportCordovaExample.xcworkspace`)를 오픈합니다.
- [App Scheme 등록](https://github.com/iamport/iamport-react-native/blob/HEAD/manuals/SETTING.md#1-app-scheme-%EB%93%B1%EB%A1%9D)을 참고하시어 앱스킴을 "example" 으로 등록합니다.
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
  var titleOptions = {
    text: '아임포트 코르도바 테스트',                   // 타이틀
    textColor: '#ffffff',                         // 타이틀 색
    textSize: '20',                               // 타이틀 크기
    textAlignment: 'left',                        // 타이틀 정렬 유형
    backgroundColor: '#344e81',                   // 타이틀 배경색
    show: true,                                   // 타이틀 유무
    leftButtonType: 'back',                       // 왼쪽 버튼 유형
    leftButtonColor: '#ffffff',                   // 왼쪽 버튼 색
    rightButtonType: 'close',                     // 오른쪽 버튼 유형
    rightButtonColor: '#ffffff',                  // 오른쪽 버튼 색
  };
  var userCode = 'iamport';                       // 가맹점 식별코드
  var data = {
    pg: 'html5_inicis',                           // PG사
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
    titleOptions: titleOptions,                   // 타이틀 옵션
    userCode: userCode,                           // 가맹점 식별코드
    data: data,                                   // 결제 데이터
    callback: callback,                           // 콜백 함수
  }
  cordova.plugins.IamportCordova.payment(params);
});
```

| Prop               | Type     |  Description                                               | Required | Default          |
| ------------------ | -------- | ---------------------------------------------------------- | -------- | ---------------- |
| titleOptions       | object   | 타이틀 옵션                                                   | false    |                  |
| - text             | string   | 타이틀                                                       | false    | 아임포트 코르도바 예제 |
| - textColor        | string   | 타이틀 색                                                    | false     | #ffffff          |
| - textSize         | string   | 타이틀 크기                                                   | false     | 20               |
| - textAlignment    | string   | 타이틀 정렬 유형(`left`, `center`, `right`)                    | false     | left             |  
| - backgroundColor  | string   | 타이틀 배경색                                                  | false    | #344e81           |
| - show             | bool     | 타이틀 유무                                                   | false    | true               |
| - leftButtonType   | string   | 왼쪽 버튼 유형(`back`, `hide`, `close`)                        | false    | back              |
| - leftButtonColor  | string   | 왼쪽 버튼 색                                                  | false    | textColor         |
| - rightButtonType  | string   | 오른쪽 버튼 유형(`hide`, `close`)                              | false    | close             |
| - rightButtonColor | string   | 오른쪽 버튼 색                                                 | false    | textColor         |
| userCode           | string   | 가맹점 식별코드                                                 | true     |                   |
| data               | object   | 결제에 필요한 정보 [자세히 보기](https://docs.iamport.kr/tech/imp) | true     |                   |
| callback           | function | 결제 후 실행 될 함수 [자세히보기](#callback)                       | true     |                   |


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
  var titleOptions = {
    text: '아임포트 코르도바 테스트',                   // 타이틀
    textColor: '#ffffff',                         // 타이틀 색
    textSize: '20',                               // 타이틀 크기
    textAlignment: 'left',                        // 타이틀 정렬 유형
    backgroundColor: '#344e81',                   // 타이틀 배경색
    show: true,                                   // 타이틀 유무
    leftButtonType: 'back',                       // 왼쪽 버튼 유형
    leftButtonColor: '#ffffff',                   // 왼쪽 버튼 색
    rightButtonType: 'close',                     // 오른쪽 버튼 유형
    rightButtonColor: '#ffffff',                  // 오른쪽 버튼 색
  };
  var userCode = 'imp10391932';                   // 가맹점 식별코드
  var data = {
    carrier: 'KTF',                               // 통신사
    merchant_uid: 'mid_' + new Date().getTime(),  // 주문번호
    company: 'SIOT',                              // 회사명
    name: '홍길동',                                 // 이름
    phone: '01012341234',                         // 전화번호
    is_iframe: false,                             // 본인인증창 렌더링 방식
  };
  var params = {
    titleOptions: titleOptions,                   // 타이틀 옵션
    userCode: userCode,                           // 가맹점 식별코드
    data: data,                                   // 본인인증 데이터
    callback: callback,                           // 콜백 함수
  };

  cordova.plugins.IamportCordova.certification(params);
});
```

| Prop          | Type          |  Description                       | Required   |
| ------------- | ------------- | ---------------------------------- | ---------- |
| titleOptions  | object        | 타이틀 옵션                           | false      |
| userCode      | string        | 가맹점 식별코드                        | true       |
| data          | object        | 본인인증에 필요한 정보 [자세히 보기](https://https://docs.iamport.kr/tech/mobile-authentication#call-authentication)      | true       |
| - merchant_uid| string        | 가맹점 주문번호                        | false      |
| - company     | string        | 회사명 또는 URL                       | false      |
| - carrier     | string        | 통신사                               | false      |
| - name        | string        | 본인인증 할 이름                        | false      |
| - phone       | number        | 본인인증 할 전화번호                     | false      |
| - min_age     | number        | 본인인증 허용 최소 연령                  | false      |
| - is_iframe   | boolean       | 본인인증 창 렌더링 방식                  | false      |
| callback      | function      | 본인인증 후 실행 될 함수                 | true       |

#### is_iframe 파라메터
본래 다날이 제공하는 휴대폰 본인인증 화면의 오른쪽 상단에는 X 버튼이 없었습니다. 따라서 사용자가 본인인증 도중에 취소를 하려면 명시적으로 하단의 취소 버튼을 눌렀어야 했습니다. 이에 대해 아임포트는 **Iframe 방식에 한해** 자체적으로 오른쪽 상단에 X 버튼을 렌더링하고 있습니다. 리디렉션 방식으로 본인인증 창을 띄우면, 말 그대로 PG사의 URL로 리디렉션되기 때문에 기술적으로 불가능하기 때문입니다.

iamport-cordova 플러그인은 본인인증 창을 리디렉션 방식으로만 제공하는 대신 상단에 타이틀을 설정하고 뒤로가기/닫기 버튼을 렌더링 할 수 있도록 `titleOptions` 파라메터를 제공하고 있습니다. 하지만 이 `titleOptions` 파라메터를 사용하지 않는 사용자가 Iframe 방식으로 본인인증 창을 띄워 X 버튼을 렌더링 할 수 있도록 v0.9.8부터 `is_iframe` 파라메터를 제공하고 있으니 이용에 참고 부탁드립니다.

| 값 | 본인인증 창 렌더링 방식 | 본인인증 창 내부 오른쪽 상단 X 버튼 렌더링 여부 |
| - | - | - |
| `true` | Iframe 방식 | O |
| `false(기본값)` | 리디렉션 방식 | X |
