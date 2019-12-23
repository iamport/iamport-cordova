var exec = require('cordova/exec');
var REDIRECT_URL = 'http://localhost/iamport';

var payment = function(userCode, data, callback) {
  var type = getType(data);
  var params = {
    userCode: userCode,
    data: Object.assign({}, data, { m_redirect_url: REDIRECT_URL }),
    triggerCallback: triggerCallback.toString(),
    redirectUrl: REDIRECT_URL
  };

  var successCallback = function(url) {
    if (url.indexOf(REDIRECT_URL) == 0) {
      var query = url.substring(REDIRECT_URL.length + 1); // [REDIRECT_URL]? 이후로 자름
      var parsedQuery = parseQuery(query);

      callback(parsedQuery);
    }
  }

  var failureCallback = function(message) {
    var response = {
      imp_success: false,
      imp_uid: null,
      merchant_uid: data.merchant_uid,
      error_code: 'IAMPORT_CORDOVA',
      error_msg: message,
    };

    callback(response);
  }

  exec(
    successCallback,
    failureCallback,
    'IamportCordova',
    'startActivity',
    [type, params]
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
    query.push(key + '=' + response[key]);
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

module.exports = payment;