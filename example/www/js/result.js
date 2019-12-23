document.addEventListener('DOMContentLoaded', function() { 
  var url = window.location.href;
  var query = url.split('?')[1];
  var decodedQuery = decodeURIComponent(query);
  var parsedQuery = JSON.parse(decodedQuery);

  if (parsedQuery['imp_success'] == 'true') {
    document.getElementById('success-container').style.display = 'flex'; 
  } else {
    document.getElementById('failure-container').style.display = 'flex';

    document.getElementById('tr-error-code').style.display = 'table-row';
    document.getElementById('tr-error-msg').style.display = 'table-row';
    document.getElementById('error-code').innerText = parsedQuery['error_code'];
    document.getElementById('error-msg').innerText = parsedQuery['error_msg'];
  }

   document.getElementById('imp-uid').innerText = parsedQuery['imp_uid'];
   document.getElementById('merchant-uid').innerText = parsedQuery['merchant_uid'];
});