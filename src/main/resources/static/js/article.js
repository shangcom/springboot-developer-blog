// 삭제 버튼 이벤트 리스너
const deleteButton = document.getElementById('delete-btn');

// 삭제 버튼이 존재할 경우
if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        // 숨겨진 input 요소에서 article-id를 가져옴
        let id = document.getElementById('article-id').value;

        // DELETE 요청을 서버로 전송
        fetch(`/api/articles/${id}`, {
            method: 'DELETE'
        })
            .then(() => {
                // 요청 성공 시 알림을 표시하고 목록 페이지로 이동
                alert('삭제가 완료되었습니다.');
                location.replace('/articles'); // /articles로 리다이렉트
            });
    });
}

// 수정 버튼 이벤트 리스너
const modifyButton = document.getElementById('modify-btn');

// 수정 버튼이 존재할 경우
if (modifyButton) {
    modifyButton.addEventListener('click', event => {
        console.log("Modify button clicked"); // 클릭 이벤트 발생 확인

        // URL 쿼리 파라미터에서 id 값을 추출
        let params = new URLSearchParams(location.search);
        let id = params.get('id'); // 현재 URL에서 id 값을 가져옴
        console.log("Article ID:", id); // URL에서 id 추출 확인

        // PUT 요청으로 수정된 데이터를 서버로 전송
        fetch(`/api/articles/${id}`, {
            method: 'PUT',
            headers: {
                "Content-Type": "application/json", // JSON 데이터 형식 지정
            },
            body: JSON.stringify({
                // 수정된 제목과 내용을 JSON으로 전달
                title: document.getElementById('title').value,
                content: document.getElementById('content').value
            })
        })
            .then(() => {
                // 요청 성공 시 알림을 표시하고 수정된 글 페이지로 이동
                alert('수정이 완료되었습니다.');
                location.replace(`/articles/${id}`); // /articles/{id}로 리다이렉트
            });
    });
}

// 생성 버튼 이벤트 리스너
const createButton = document.getElementById('create-btn');

// 생성 버튼이 존재할 경우
if (createButton) {
    createButton.addEventListener('click', event => {
        // POST 요청으로 새 데이터를 서버로 전송
        fetch('/api/articles', {
            method: 'POST',
            headers: {
                "Content-Type": "application/json", // JSON 데이터 형식 지정
            },
            body: JSON.stringify({
                // 입력된 제목과 내용을 JSON으로 전달
                title: document.getElementById('title').value,
                content: document.getElementById('content').value
            })
        })
            .then(() => {
                // 요청 성공 시 알림을 표시하고 목록 페이지로 이동
                alert('등록 완료되었습니다.');
                location.replace('/articles'); // /articles로 리다이렉트
            });
    });
}
