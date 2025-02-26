function submit_go(){
   let input_id = document.querySelector("input[name='id']");
   let input_pwd = document.querySelector("input[name='pwd']");

   if(!input_id.value){
     alert("아이디는 필수입니다.");
     input_id.focus();
     return;
   }
   if(!input_pwd.value){
     alert("패스워드는 필수입니다.");
     input_pwd.focus();
     return;
   }
   
   document.forms.login.submit();
 }