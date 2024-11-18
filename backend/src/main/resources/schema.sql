CREATE TABLE users (
    user_id VARCHAR(50) PRIMARY KEY,       -- 사용자 ID (Primary Key)
    user_password VARCHAR(255) NOT NULL,   -- 사용자 비밀번호
    user_name VARCHAR(100) NOT NULL,       -- 사용자 이름
    email_domain VARCHAR(100),             -- 이메일 도메인 (선택 사항)
    join_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- 가입 날짜 (현재 시간 자동 입력)
);


CREATE TABLE board (
    post_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    view INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);