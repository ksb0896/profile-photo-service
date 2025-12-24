
CREATE TABLE IF NOT EXISTS profile_photos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL,
    photo_data LONGBLOB,
    content_type VARCHAR(50),
    CONSTRAINT fk_user
        FOREIGN KEY(user_id)
        REFERENCES user_profiles(id)
        ON DELETE CASCADE
);