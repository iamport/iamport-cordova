var form;

document.addEventListener('DOMContentLoaded', function() { 
  form = document.forms['iamport-certification'];
  form['merchant_uid'].value = 'mid_' + new Date().getTime();
});
document.getElementById('iamport-certification').addEventListener('submit', function(e) {
  e.preventDefault();

  var carrier = form['carrier'].value;
  var merchant_uid = form['merchant_uid'].value;
  var company = form['company'].value;
  var name = form['name'].value;
  var phone = form['phone'].value;
  var min_age = form['min_age'].value;
  var is_iframe = form['is_iframe'].value;

  var titleOptions = {
    text: '아임포트 코르도바 테스트',
    textColor: '#344e81',
    backgroundColor: '#ffffff',
    leftButtonType: 'hide',
    rightButtonType: 'close',
  };
  var userCode = 'imp10391932';
  var data = {
    carrier,
    merchant_uid,
    company,
    name,
    phone,
    min_age,
    is_iframe: is_iframe === 'true',
  };
  var params = {
    titleOptions: titleOptions,
    userCode: userCode,
    data: data,
    callback: callback,
  };

  cordova.plugins.IamportCordova.certification(params);
});

function callback(response) {
  /**
   * 결과 페이지로 이동
   * 본인인증 결과는 JSON을 string화 해서 쿼리 형태로 넘김
  */
  window.location.href = 'result.html?' + JSON.stringify(response);
}
