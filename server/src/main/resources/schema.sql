CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS items (
    id SERIAL PRIMARY KEY,
    user_id BIGINT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    available BOOLEAN NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS bookings (
    id SERIAL PRIMARY KEY,
    item_id BIGINT,
    user_id BIGINT,
    start_of TIMESTAMP NOT NULL,
    end_of TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'WAITING',
    FOREIGN KEY (item_id) REFERENCES items(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS comments (
    id SERIAL PRIMARY KEY,
    text VARCHAR(255) NOT NULL,
    author_id BIGINT,
    item_id BIGINT,
    created TIMESTAMP NOT NULL,
    FOREIGN KEY (author_id) REFERENCES users(id),
    FOREIGN KEY (item_id) REFERENCES items(id)
);

CREATE TABLE IF NOT EXISTS item_requests (
    id SERIAL PRIMARY KEY,
    description VARCHAR(255),
    user_id BIGINT,
    created TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);