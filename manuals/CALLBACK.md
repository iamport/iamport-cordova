
# 콜백 함수 설정하기
아임포트 코르도바 모듈 콜백 함수 설정을 위한 안내입니다.

콜백 함수는 필수입력 필드로, 결제/본인인증 완료 후 페이지 이동을 위해 아래와 같이 로직을 작성해야합니다.

```javascript
function callback(response) {
  /**
   * 결과 페이지로 이동
   * 결과는 JSON을 string화 해서 쿼리 형태로 넘김
  */
  window.location.href = 'result.html?' + JSON.stringify(response);
}
```

### 결과에 따라 로직 작성하기
콜백 함수의 첫번째 인자(response)는 결제/본인인증 결과를 담고 있는 오브젝트로 아래와 같이 구성되어 있습니다. 자세한 내용은 아임포트 공식 문서 [IMP.request_pay - param, rsp 객체 - Callback 함수의 rsp 객제](https://docs.iamport.kr/tech/imp#callback)를 참고해주세요.

| key           |  Description               | 
| ------------- | -------------------------- | 
| imp_success   | 결제 / 본인인증 프로세스 완료 여부 |
| imp_uid       | 아임포트 번호                  |
| merchant_uid  | 주문번호                      |
| error_msg     | 실패한 경우, 에러 메시지         |

# imp-success 파라미터
`imp-success` 파라미터는 결제 / 본인인증 프로세스 완료 여부를 의미하며 아래와 같이 구분됩니다.

| Value  |  Description                                       | 
| ------ | -------------------------------------------------- | 
| true   | 결제 완료(결제 상태: `paid`), 가상계좌 발급 완료(결제 상태: `ready`), 본인인증 완료 |
| false  | 결제 실패(결제 상태: `failed`), 본인인증 실패 |

여기서 말하는 `결제 / 본인인증 실패`란, 아래의 모든 경우를 의미합니다.

- PG 모듈 설정이 올바르지 않아, 결제 / 본인인증 창 자체가 렌더링 되지 않음
- 사용자가 임의로 X 버튼이나 취소 버튼을 눌러 결제 / 본인인증 프로세스가 종료 됨
- 카드 정보 불일치, 한도 초과, 잔액 부족 등의 사유로 결제 프로세스가 중단 됨

아임포트는 결제 금액 위/변조 공격에 대비하기 위해, 결제 프로세스 완료 후 결제 금액 유효성(실제로 승인 된 금액과 결제를 의도한 금액이 일치하는지 여부) 검사를 하도록 권장합니다. 자세한 내용은 아임포트 공식 문서 [일반결제 연동하기 - STEP5. 서버에서 거래 검증 및 데이터 동기화](https://docs.iamport.kr/implementation/payment#server-side-logic)를 참고해주세요.
