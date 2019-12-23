var exec = require('cordova/exec');
var REDIRECT_URL = 'http://localhost/iamport';

var iamport = function(type, params) {
  var title = params.title;
  var userCode = params.userCode;
  var data = params.data;
  var callback = params.callback

  /* 타이틀 설정 */
  var titleData = {};
  if (title) {
    titleData.name = title.name ? title.name : '아임포트 코르도바 예제',
    titleData.color = title.color ? title.color : '#344e81';
  }

  /* 결제/본인인증 데이터 설정 */
  var iamportData = {
    userCode: userCode,
    data: Object.assign({}, data, { m_redirect_url: REDIRECT_URL }),
    triggerCallback: triggerCallback.toString(),
    redirectUrl: REDIRECT_URL,
  };

  var successCallback = function(url) {
    if (url.indexOf(REDIRECT_URL) == 0) {
      var query = url.substring(REDIRECT_URL.length + 1); // [REDIRECT_URL]? 이후로 자름
      var parsedQuery = parseQuery(query);

      var imp_success = Object.keys(parsedQuery).indexOf('imp_success') == -1 ? parsedQuery.success : parsedQuery.imp_success;
      var response = {
        imp_success: imp_success,
        imp_uid: parsedQuery.imp_uid,
        merchant_uid: parsedQuery.merchant_uid,
        error_code: parsedQuery.error_code,
        error_msg: parsedQuery.error_msg,
      };
      callback(response);
    }
  };

  var failureCallback = function(message) {
    var response = {
      imp_success: 'false',
      imp_uid: null,
      merchant_uid: data.merchant_uid,
      error_code: 'IAMPORT_CORDOVA',
      error_msg: message,
    };

    callback(response);
  };

  exec(
    successCallback,
    failureCallback,
    'IamportCordova',
    'startActivity',
    [type, titleData, iamportData]
  );
} 

var getType = function(data) {
  var pg = data.pg;
  var method = data.pay_method;

  if (method == 'trans') {
    if (pg.includes('html5_inicis')) {
      return 'inicis';
    }
    if (pg.includes('nice')) {
      return 'nice';
    }
  }
  return 'payment';
}

var triggerCallback = function(response) {
  var query = [];
  Object.keys(response).forEach(function(key) {
    query.push(key + '=' + decodeURIComponent(response[key]));
  });
  location.href = 'http://localhost/iamport?' + query.join('&');
};

var parseQuery = function(query) {
  var obj = {};
  var arr = decodeURIComponent(query).split('&');
  for (var i = 0; i < arr.length; i++) {
    var pair = arr[i].split('=');
    obj[decodeURIComponent(pair[0])] = decodeURIComponent(pair[1]);
  }

  return obj;
}

exports.payment = function(params) {
  var data = params.data;
  var type = getType(data);
  iamport(type, params);
};
exports.certification = function(params) {
  iamport('certification', params);
}