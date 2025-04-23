document.addEventListener("DOMContentLoaded", function () {
    const usernameInput = document.getElementById("username");
    const usernameError = document.getElementById("usernameError");


    let debounceTimeout;

    usernameInput.addEventListener("input", () => {
        clearTimeout(debounceTimeout); // 너무 자주 호출 방지 (디바운스)

        const username = usernameInput.value.trim();

        if (username.length < 6) {
            usernameError.textContent = "6자 이상 입력해주세요.";
            return;
        }

        debounceTimeout = setTimeout(() => {
            fetch(`/api/check-username?username=${encodeURIComponent(username)}`)
                .then(response => {
                    if (response.ok) {
                        usernameError.textContent = ""; // 사용 가능
                    } else {
                        return response.json();
                    }
                })
                .then(error => {
                    if (error?.errorCode === "409") {
                        usernameError.textContent = "🚫 이미 사용 중인 아이디입니다.";
                    }
                })
                .catch(() => {
                    usernameError.textContent = "⚠️ 오류가 발생했습니다.";
                });
        }, 300); // 300ms 뒤에 fetch
    });
});

document.addEventListener("DOMContentLoaded", () => {
    const emailInput = document.getElementById("email");
    const emailError = document.getElementById("emailError");
    const passwordInput = document.getElementById("password");
    const passwordBar = document.getElementById("passwordStrengthBar");
    const passwordLabel = document.getElementById("passwordStrengthLabel");

    let debounce;

    // ✅ 이메일 중복 체크
    emailInput.addEventListener("input", () => {
        clearTimeout(debounce);
        const email = emailInput.value.trim();

        if (!email.includes("@")) {
            emailError.textContent = "유효한 이메일을 입력해주세요.";
            return;
        }

        debounce = setTimeout(() => {
            fetch(`/api/check-email?email=${encodeURIComponent(email)}`)
                .then(res => res.ok ? null : res.json())
                .then(data => {
                    if (data?.errorCode === "409") {
                        emailError.textContent = "🚫 이미 사용 중인 이메일입니다.";
                    } else {
                        emailError.textContent = "";
                    }
                })
                .catch(() => emailError.textContent = "⚠️ 서버 오류 발생");
        }, 300);
    });

    // ✅ 비밀번호 강도 체크
    passwordInput.addEventListener("input", () => {
        const pwd = passwordInput.value;
        let strength = 0;

        if (pwd.length >= 6) strength += 1;
        if (/[a-z]/.test(pwd) && /[A-Z]/.test(pwd)) strength += 1;
        if (/\d/.test(pwd)) strength += 1;
        if (/[@$!%*?&#^]/.test(pwd)) strength += 1;

        const levels = ["없음", "약함", "보통", "강함", "매우 강함"];
        const colors = ["w-0", "w-1/4", "w-2/4", "w-3/4", "w-full"];
        const colorClass = ["bg-red-400", "bg-orange-400", "bg-yellow-400", "bg-blue-400", "bg-green-500"];

        passwordBar.className = `h-2 rounded transition-all duration-300 ${colorClass[strength]} ${colors[strength]}`;
        passwordLabel.textContent = `🔋 비밀번호 강도: ${levels[strength]}`;
    });
});

