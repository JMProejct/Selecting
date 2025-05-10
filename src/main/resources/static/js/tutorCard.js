// 페이지 로딩 시 선생님 데이터 가져오기
document.addEventListener("DOMContentLoaded", function() {
    loadTeachers();

    // 카테고리 필터 버튼 클릭
    document.querySelectorAll('button[data-category]').forEach(button => {
        button.addEventListener('click', function() {
            const category = this.getAttribute('data-category');
            loadTeachers(category);
        });
    });

    // 검색 버튼 클릭
    document.getElementById('searchButton').addEventListener('click', function() {
        const keyword = document.getElementById('searchInput').value.trim();
        if (keyword) {
            loadTeachers(null, keyword);
        } else {
            alert("검색어를 입력해주세요.");
        }
    });
});

function loadTeachers(subCategoryName = null, keyword = null) {
    let url = '/api/teachers';
    if (subCategoryName) {
        url += `?subCategoryName=${encodeURIComponent(subCategoryName)}`;
    }

    fetch(url)
        .then(response => response.json())
        .then(data => {
            const teacherCards = document.getElementById('teacherCards');
            const recommendTitle = document.getElementById('recommendTitle2');

            teacherCards.innerHTML = ''; // 초기화

            if (subCategoryName == null && keyword == null) {
                recommendTitle.classList.remove('hidden'); // 추천 과외 제목 보이기
            } else {
                recommendTitle.classList.add('hidden'); // 추천 과외 제목 숨기기
            }

            if (data.content.length === 0) {
                teacherCards.innerHTML = '<p class="text-gray-500 col-span-full text-center">검색 결과가 없습니다.</p>';
                return;
            }

            data.content.forEach(teacher => {
                const card = `
                    <div class="max-w-[280px] w-full bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition aspect-[3/4]">
                        <img src="${teacher.profileImage}" alt="${teacher.name}" class="w-full h-40 object-cover">
                        <div class="p-4">
                            <h3 class="text-lg font-bold text-gray-800 mb-1">${teacher.name} 선생님</h3>
                            <p class="text-sm text-gray-500 mb-2">${teacher.categories.join(', ')}</p>
                            <p class="text-sm text-gray-700 mb-4 line-clamp-3">${teacher.intro}</p>
                            <div class="text-xs text-gray-400">경력: ${teacher.careerYears}년</div>
                        </div>
                    </div>
                `;
                teacherCards.innerHTML += card;
            });
        })
        .catch(error => {
            console.error('선생님 카드 로딩 실패', error);
        });
}