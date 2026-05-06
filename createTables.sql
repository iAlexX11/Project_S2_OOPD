DROP TABLE IF EXISTS Crypto_History CASCADE;
DROP TABLE IF EXISTS Portfolio CASCADE;
DROP TABLE IF EXISTS Cryptocurrency CASCADE;
DROP TABLE IF EXISTS Users CASCADE;

CREATE TABLE Users (
    user_id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username        VARCHAR(100) NOT NULL,
    email           VARCHAR(250) NOT NULL UNIQUE,
    password        VARCHAR(512) NOT NULL,
    balance           DECIMAL(15, 2) DEFAULT 0.00
);

CREATE TABLE Cryptocurrency (
    name            VARCHAR(100)   UNIQUE PRIMARY KEY,
    current_price   DECIMAL(18, 8) NOT NULL,
    original_price  DECIMAL(18, 8) NOT NULL
);

CREATE TABLE Portfolio (
    user_id         BIGINT NOT NULL,
    crypto_id       VARCHAR(100) NOT NULL,
    units           DECIMAL(18, 8) NOT NULL,
    buy_price       DECIMAL(18, 8) NOT NULL,
    time_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, crypto_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (crypto_id) REFERENCES Cryptocurrency(name) ON DELETE CASCADE
);

CREATE TABLE Crypto_History (
    crypto_id       VARCHAR(100) NOT NULL,
    event_id        SERIAL,
    time_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    price           DECIMAL(18, 8) NOT NULL,
    PRIMARY KEY (crypto_id, event_id),
    FOREIGN KEY (crypto_id) REFERENCES Cryptocurrency(name) ON DELETE CASCADE
);

CREATE TRIGGER update_price_history
AFTER UPDATE OF current_price ON Cryptocurrency
FOR EACH ROW
WHEN (OLD.current_price IS DISTINCT FROM NEW.current_price)
EXECUTE FUNCTION record_price_history();

CREATE OR REPLACE FUNCTION record_price_history()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO Crypto_History (crypto_id, price, time_stamp)
    VALUES (NEW.name, NEW.current_price, CURRENT_TIMESTAMP);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
