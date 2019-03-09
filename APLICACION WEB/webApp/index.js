var reader = new FileReader();
UR='http://sistemanipon.ddns.net'
function Sesion() {
  
  var user=document.getElementById('user').value
  var pass=document.getElementById('pass').value
  var url=UR+'/validate/'+user+'/'+pass;
  var xhr = new XMLHttpRequest();
  xhr.open("GET", url, false);
  xhr.send()
  var json = JSON.parse(xhr.responseText);
  if (json['data'].toString()=="true"){
    $( "#bad" ).load( "webApp.html #projects li" );
  }
}

 function includeHTML_callBack(result){
        var my_var = result;
    }

    function includeHTML(link, callBack) {
            var xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                callBack(this.responseText);
            }
          }      
          xhttp.open("GET", link, true);
          xhttp.send();
          return;
    }

