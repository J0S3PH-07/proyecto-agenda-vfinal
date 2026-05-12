CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(150) UNIQUE,
    password VARCHAR(255),
    role VARCHAR(20), -- ADMIN, PROFESSOR, PUBLIC
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE rooms (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE,
    floor VARCHAR(20),
    capacity INTEGER,
    type VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE reservation_requests (
    id BIGSERIAL PRIMARY KEY,
    room_id BIGINT REFERENCES rooms(id),
    user_id BIGINT REFERENCES users(id),
    reservation_date DATE,
    start_time TIME,
    end_time TIME,
    title VARCHAR(255),
    description TEXT,
    requester_name VARCHAR(100),
    requester_email VARCHAR(150),
    status VARCHAR(20), -- PENDING APPROVED REJECTED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reviewed_by BIGINT,
    reviewed_at TIMESTAMP
);

CREATE TABLE reservations (
    id BIGSERIAL PRIMARY KEY,
    room_id BIGINT REFERENCES rooms(id),
    request_id BIGINT REFERENCES reservation_requests(id),
    reservation_date DATE,
    start_time TIME,
    end_time TIME,
    title VARCHAR(255),
    description TEXT,
    requester_name VARCHAR(100),
    requester_email VARCHAR(150),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    approved_by BIGINT,
    approved_at TIMESTAMP
);

CREATE TABLE reservation_history (
    id BIGSERIAL PRIMARY KEY,
    reservation_id BIGINT,
    action VARCHAR(20), -- CREATED UPDATED CANCELLED
    changed_by_user_id BIGINT,
    room_id BIGINT,
    reservation_date DATE,
    start_time TIME,
    end_time TIME,
    title VARCHAR(255),
    description TEXT,
    requester_name VARCHAR(100),
    requester_email VARCHAR(150),
    status VARCHAR(20),
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
