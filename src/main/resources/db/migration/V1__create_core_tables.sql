CREATE TABLE users (
                       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       name VARCHAR(150) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP NOT NULL,
                       CONSTRAINT uk_users_email UNIQUE (email)
);

CREATE TABLE events (
                        id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        name VARCHAR(150) NOT NULL,
                        description VARCHAR(1000),
                        event_date TIMESTAMP NOT NULL,
                        venue VARCHAR(200),
                        type VARCHAR(30) NOT NULL,
                        created_at TIMESTAMP NOT NULL,
                        updated_at TIMESTAMP NOT NULL
);

CREATE TABLE tickets (
                         id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                         event_id BIGINT NOT NULL,
                         type VARCHAR(100) NOT NULL,
                         price NUMERIC(10,2) NOT NULL,
                         total_quantity INTEGER NOT NULL,
                         available_quantity INTEGER NOT NULL,
                         created_at TIMESTAMP NOT NULL,
                         updated_at TIMESTAMP NOT NULL,
                         CONSTRAINT fk_tickets_event FOREIGN KEY (event_id) REFERENCES events (id),
                         CONSTRAINT ck_tickets_available_quantity CHECK (available_quantity >= 0)
);

CREATE TABLE reservations (
                              id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                              user_id BIGINT NOT NULL,
                              ticket_id BIGINT NOT NULL,
                              status VARCHAR(20) NOT NULL,
                              created_at TIMESTAMP NOT NULL,
                              updated_at TIMESTAMP NOT NULL,
                              CONSTRAINT fk_reservations_user FOREIGN KEY (user_id) REFERENCES users (id),
                              CONSTRAINT fk_reservations_ticket FOREIGN KEY (ticket_id) REFERENCES tickets (id)
);