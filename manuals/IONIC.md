# 아이오닉 환경에서 아임포트 연동하기

아이오닉 환경에서 아임포트 코르도바 플러그인을 사용해 일반/정기결제 및 본인인증을 연동하는 방법을 안내합니다.

## 설치하기
아래 명령어를 통해 아임포트 코르도바 플러그인을 귀하의 아이오닉 프로젝트에 추가할 수 있습니다.

### 1. ionic-native 설치
```
$ npm install @ionic-native/core --save // @ionic-native/core v5.22.0 이상
```

### 2. ionic-native/iamport-cordova 설치
```
$ npm install @ionic-native/iamport-cordova --save
```

### 3. 아임포트 코르도바 플러그인 설치
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
아임포트 코르도바 플러그인을 사용해 아래와 같이 일반/정기결제 및 본인인증 기능을 구현할 수 있습니다. 필요한 파라미터는 [예제](example/README.md)를 참고하세요.

### 일반/정기결제 예제
```html
<!-- payment.page.html -->
<ion-content [fullscreen]="true">
  ...
  <!-- 1. 결제 버튼 생성 -->
  <button (click)="onClickPayment()">결제하기</button>
</ion-content>
```

```javascript
// payment.page.ts
import { Component } from '@angular/core';
// 2. 아임포트 코르도바 플러그인 임포트
import { IamportCordova } from '@ionic-native/iamport-cordova';

@Component({
  selector: 'app-payment',
  templateUrl: 'payment.page.html',
  styleUrls: ['payment.page.scss'],
})
export class PaymentPage {

  constructor() {}

  // 3. 결제버튼에 대해 클릭 이벤트 핸들러 정의
  onClickPayment() {
    var userCode = 'iamport';                       // 가맹점 식별코드
    var data = {
      pg: 'html5_inicis',                           // PG사
      pay_method: 'card',                           // 결제수단
      name: '아임포트 코르도바 테스트',                   // 주문명
      merchant_uid: 'mid_' + new Date().getTime(),  // 주문번호
      amount: '1000',                               // 결제금액
      buyer_name: '홍길동',                           // 구매자 이름
      buyer_tel: '01012341234',                     // 구매자 연락처
      buyer_email: 'example@example.com',           // 구매자 이메일
      app_scheme: 'example',                        // 앱 URL 스킴
      ...
    };
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

    // 4. 아임포트 코르도바 파라미터 정의
    var params = {
      userCode: userCode,                           // 4-1. 가맹점 식별코드 정의
      data: data,                                   // 4-2. 결제 데이터 정의
      titleOptions: titleOptions,                   // 4-3. 결제창 헤더 옵션 정의
      callback: function(response) {                // 4-3. 콜백 함수 정의
        alert(JSON.stringify(response));
      },
    };
    // 5. 결제창 호출
    IamportCordova.payment(params);
  }
}
```


### 본인인증 예제
```html
<!-- certification.page.html -->
<ion-content [fullscreen]="true">
  ...
  <!-- 1. 본인인증 버튼 생성 -->
  <button (click)="onClickCertification()">본인인증 하기</button>
</ion-content>
```

```javascript
// certification.page.ts
import { Component } from '@angular/core';
// 2. 아임포트 코르도바 플러그인 임포트
import { IamportCordova } from '@ionic-native/iamport-cordova';

@Component({
  selector: 'app-certification',
  templateUrl: 'certification.page.html',
  styleUrls: ['certification.page.scss'],
})
export class CertificationPage {

  constructor() {}

  // 3. 본인인증 버튼에 대해 클릭 이벤트 핸들러 정의
  onClickCertification() {
    var userCode = 'iamport';                       // 가맹점 식별코드
    var data = {
      carrier: 'KTF',                               // 통신사
      merchant_uid: 'mid_' + new Date().getTime(),  // 주문번호
      company: 'SIOT',                              // 회사명
      name: '홍길동',                                 // 이름
      phone: '01012341234',                         // 전화번호
    };
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

    // 4. 아임포트 코르도바 파라미터 정의
    var params = {
      userCode: userCode,                           // 4-1. 가맹점 식별코드 정의
      data: data,                                   // 4-2. 결제 데이터 정의
      titleOptions: titleOptions,                   // 4-3. 본인인증 창 헤더 옵션 정의
      callback: function(response) {                // 4-3. 콜백 함수 정의
        alert(JSON.stringify(response));
      },
    };
    // 5. 본인인증 창 호출
    IamportCordova.certification(params);
  }
}
```