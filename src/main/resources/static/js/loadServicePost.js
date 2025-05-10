document.addEventListener("DOMContentLoaded", function () {
    loadServicePosts(); // 기본 무작위 30개

    document.getElementById("searchButton").addEventListener("click", () => {
        const keyword = document.getElementById("searchInput").value.trim();
        if (keyword) {
            loadServicePosts(keyword);
        } else {
            alert("검색어를 입력해주세요.");
        }
    });
});

function loadServicePosts(keyword = null) {
    let url = '/api/posts/search';
    if (keyword) {
        url += `?q=${encodeURIComponent(keyword)}`;
    }

    fetch(url)
        .then(res => res.json())
        .then(data => {
            const postCards = document.getElementById("postCards");
            const recommendTitle = document.getElementById("recommendTitle");

            postCards.innerHTML = ""; // 초기화

            if (!keyword) {
                recommendTitle.classList.remove("hidden");
            } else {
                recommendTitle.classList.add("hidden");
            }

            if (data.content.length === 0) {
                postCards.innerHTML = '<p class="text-gray-500 col-span-full text-center">검색 결과가 없습니다.</p>';
                return;
            }

            data.content.forEach(post => {
                const card = `
                    <div class="max-w-[280px] w-full bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition aspect-[3/4]">
                        <div class="p-4">
                            <h3 class="text-lg font-bold text-gray-800 mb-1">${post.title}</h3>
                            <p class="text-sm text-gray-500 mb-1">${post.subcategoryName} · ${post.location}</p>
                            <p class="text-sm text-gray-600 mb-2">${post.teacherName} 선생님</p>
                            <p class="text-xs text-gray-400 mb-1">경력 ${post.careerYears}년 / 학력 ${post.education}</p>
                            <p class="text-sm font-semibold text-blue-600">💰 ${post.price.toLocaleString()}원</p>
                        </div>
                    </div>
                `;
                postCards.innerHTML += card;
            });
        })
        .catch(err => {
            console.error("게시글 로딩 실패", err);
        });
}
