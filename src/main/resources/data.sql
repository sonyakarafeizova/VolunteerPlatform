
INSERT INTO roles (role) VALUES ('USER'), ('MODERATOR'), ('ADMIN');

INSERT INTO users (username, password, full_name, age, email, level)
VALUES ('admin', '$2a$10$3YuSM/3wsUmgfm.5yfyMg.t5tYk56N9800M8rImv7EUeCWRCxivAS', 'Admin User', 30, 'admin@example.com', 'ADVANCED');

SELECT id FROM users WHERE username = 'admin';
SELECT id FROM roles WHERE role = 'ADMIN';

INSERT INTO users_roles (user_id, role_id) VALUES (4, 12);

INSERT INTO users (username, password, full_name, age, email, level)
VALUES ('moderator', '$2a$10$ErILPqDdiy3hpeVszM5RuuXQGioRj2q3PqTq48kjoAA2V510fNG1O', 'Moderator User', 25, 'moderator@example.com', 'JUNIOR');

