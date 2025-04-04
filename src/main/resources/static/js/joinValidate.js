function joinValidate(event){
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;
    let email = document.getElementById("email").value;
    let name = document.getElementById("name").value;

    if(username==null||password==null||email==null||name==null){
        alert("값을 입력해주세요")
    }
        
    else if(username.length<6||username.length>12){
        alert("아이디를 6~12글자로 입력해주세요");
        event.preventDefault();
    }
    else if(password.length<6||password.length>12){
        alert("비밀번호를 6~12글자로 입력해주세요");
        event.preventDefault();
    }
    else if(!email.includes("@")){
        alert("이메일을 올바른 형식으로 작성해주세요")
        event.preventDefault();
    }
}