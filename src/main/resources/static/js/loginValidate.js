function loginValidate(event) {
    let usernameOrEmail = document.getElementById("usernameOrEmail").value;
    let password = document.getElementById("password").value;

    if(usernameOrEmail==null||password==null){
        alert("값을 입력해주세요")
    }
    else if(usernameOrEmail.length<6||usernameOrEmail.length>12){
        alert("아이디를 6~12글자로 입력해주세요");
        event.preventDefault();
    }
    else if(password.length<6||password.length>12){
        alert("비밀번호를 6~12글자로 입력해주세요");
        event.preventDefault();
    }
}