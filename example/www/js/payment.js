var form;

document.addEventListener('DOMContentLoaded', function() { 
  form = document.forms['iamport-payment'];
  form['merchant_uid'].value = 'mid_' + new Date().getTime();
});
document.getElementById('iamport-payment').addEventListener('submit', function(e) {
  e.preventDefault();

  var pg = form['pg'].value;
  var pay_method = form['pay_method'].value;
  var name = form['name'].value;
  var merchant_uid = form['merchant_uid'].value;
  var amount = form['amount'].value;
  var buyer_name = form['buyer_name'].value;
  var buyer_tel = form['buyer_tel'].value;
  var buyer_email = form['buyer_email'].value;

  var titleOptions = {
    text: '포트원 코르도바 테스트',
    textAlignment: 'center',
    leftButtonType: 'close',
    rightButtonType: 'hide',
  };
  var userCode = getUserCode(pg);
  var data = {
    pg: pg,
    pay_method: pay_method,
    name: name,
    merchant_uid: merchant_uid,
    amount: amount,
    buyer_name: buyer_name,
    buyer_tel: buyer_tel,
    buyer_email: buyer_email,
    app_scheme: 'example',
  };

  var params = {
    titleOptions: titleOptions,
    userCode: userCode,
    data: data,
    callback: callback,
  };
  cordova.plugins.IamportCordova.payment(params);
});

function callback(response) {
  /**
   * 결과 페이지로 이동
   * 결제 결과는 JSON을 string화 해서 쿼리 형태로 넘김
  */
  window.location.href = 'result.html?' + JSON.stringify(response);
}

function getUserCode(pg) {
  switch(pg) {
    case 'kakao':
      return 'imp10391932';
    case 'paypal':
      return 'imp09350031';
    case 'mobilians':
      return 'imp60029475';
    case 'naverco':
    case 'naverpay':
      return 'imp41073887';
    default:
      return 'imp19424728';
  }
}
