
# iamport-cordova

아임포트 코르도바 플러그인입니다. 아이오닉 환경에서 아임포트 연동을 위한 가이드는 [여기](manuals/IONIC.md)를 참고해주세요.

## 목차
- [버전정보](manuals/VERSION.md)
- [지원정보](manuals/SUPPORT.md)
- 설치하기
- IOS 설정하기
- [예제](example/README.md)
- 콜백 함수 설정하기

## 버전정보
최신버전은 [v0.9.7](https://github.com/iamport/iamport-cordova/tree/master)입니다. 버전 히스토리는 [버전정보](manuals/VERSION.md)를 참고하세요.

## 지원정보
아임포트 코르도바 플러그인은 안드로이드와 IOS에서 결제 및 휴대폰 본인인증 기능을 제공합니다. 결제시 지원하는 PG사와 결제수단에 대한 자세한 정보는 [지원정보](manuals/SUPPORT.md)를 참고하세요. 

## 설치하기
아래 명령어를 통해 아임포트 코르도바 플러그인을 귀하의 코르도바 프로젝트에 추가할 수 있습니다.

```
$ cordova plugin add iamport-cordova
```

## IOS 설정하기
IOS에서는 **외부 앱 이동 후 복귀를 위해 커스텀 앱 URL Scheme값을 반드시 지정해야** 합니다. 자세한 내용은 [App Scheme 등록](https://github.com/iamport/iamport-react-native/blob/HEAD/manuals/SETTING.md#1-app-scheme-%EB%93%B1%EB%A1%9D)을 참고하세요.

설정된 결과는 아래와 같습니다.

```html
// [프로젝트이름]/platforms/ios/[프로젝트이름]/프로젝트이름-info.plist 파일
...
<key>CFBundleURLTypes</key>
<array>
  <dict>
    <key>CFBundleURLName</key>
    <string/>
    <key>CFBundleURLSchemes</key>
    <array>
      <!-- 예) 커스텀 앱 URL Scheme을 example로 설정 -->
      <string>example</string>
    </array>
  </dict>
</array>
...
```

## 예제
아임포트 코르도바 플러그인을 사용해 아래와 같이 일반/정기결제 및 휴대폰 본인인증 기능을 구현할 수 있습니다. 필요한 파라미터는 [예제](example/README.md)를 참고하세요.

#### 일반/정기결제 예제
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
    app_scheme: 'example',                        // 앱 URL 스킴
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


#### 휴대폰 본인인증 예제
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
    is_iframe: false,                             // 본인인증 창 렌더링 방식
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

## 콜백 함수 설정하기
콜백 함수는 필수입력 필드로, 결제/본인인증 완료 후 라우트 이동을 위해 아래와 같이 로직을 작성할 수 있습니다. 콜백 함수에 대한 자세한 설명은 [콜백 설정하기](manuals/CALLBACK.md)를 참고하세요.

```javascript
function callback(response) {
  /**
   * 결과 페이지로 이동
   * 결과는 JSON을 string화 해서 쿼리 형태로 넘김
  */
  window.location.href = 'result.html?' + JSON.stringify(response);
}
```