DROP TABLE IF EXISTS Bots CASCADE;
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
    symbol            VARCHAR(100)   UNIQUE PRIMARY KEY,
    name              VARCHAR(100) NOT NULL,
    current_price   DECIMAL(18, 8) NOT NULL,
    original_price  DECIMAL(18, 8) NOT NULL,
    volatility      DECIMAL(5, 2) NOT NULL
);

-- Bots are a specialisation of Users, one per cryptocurrency
CREATE TABLE Bots (
     bot_id          BIGINT PRIMARY KEY,                -- same PK as Users.user_id
     crypto_id       VARCHAR(100) NOT NULL UNIQUE,      -- one bot per crypto
     volatility      DECIMAL(5, 2) NOT NULL,            -- cached here for the scheduler
     FOREIGN KEY (bot_id)    REFERENCES Users(user_id)  ON DELETE CASCADE,
     FOREIGN KEY (crypto_id) REFERENCES Cryptocurrency(symbol) ON DELETE CASCADE
);

CREATE TABLE Portfolio (
    user_id         BIGINT NOT NULL,
    crypto_id       VARCHAR(100) NOT NULL,
    units           DECIMAL(18, 8) NOT NULL,
    buy_price       DECIMAL(18, 8) NOT NULL,
    time_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, crypto_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (crypto_id) REFERENCES Cryptocurrency(symbol) ON DELETE CASCADE
);

CREATE TABLE Crypto_History (
    crypto_id       VARCHAR(100) NOT NULL,
    event_id        SERIAL,
    time_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    price           DECIMAL(18, 8) NOT NULL,
    PRIMARY KEY (crypto_id, event_id),
    FOREIGN KEY (crypto_id) REFERENCES Cryptocurrency(symbol) ON DELETE CASCADE
);


CREATE OR REPLACE FUNCTION record_price_history()
    RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO Crypto_History (crypto_id, price, time_stamp)
    VALUES (NEW.symbol, NEW.current_price, CURRENT_TIMESTAMP);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_price_history
AFTER UPDATE OF current_price ON Cryptocurrency
FOR EACH ROW
WHEN (OLD.current_price IS DISTINCT FROM NEW.current_price)
EXECUTE FUNCTION record_price_history();

-- used when first insert is done
CREATE TRIGGER insert_price_history
    AFTER INSERT ON Cryptocurrency
    FOR EACH ROW
EXECUTE FUNCTION record_price_history();


CREATE OR REPLACE FUNCTION update_crypto_price_after_buy()
    RETURNS TRIGGER AS $$
BEGIN
    -- Increase price by 1% after a buy
    UPDATE Cryptocurrency
    SET current_price = current_price * 1.01
    WHERE symbol = NEW.crypto_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER update_crypto_price_after_buy
    AFTER INSERT ON Portfolio
    FOR EACH ROW
    EXECUTE FUNCTION update_crypto_price_after_buy();

CREATE TRIGGER update_crypto_price_after_buy_update
    AFTER UPDATE OF units ON Portfolio
    FOR EACH ROW
    WHEN (OLD.units < NEW.units) -- Only trigger on buy (units increase)
    EXECUTE FUNCTION update_crypto_price_after_buy();


CREATE OR REPLACE FUNCTION update_crypto_price_after_sell()
    RETURNS TRIGGER AS $$
BEGIN
    -- Decrease price by 1% after a sell
    UPDATE Cryptocurrency
    SET current_price = current_price * 0.99 -- Decrease price by 1% after a sell
    WHERE symbol = NEW.crypto_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_crypto_price_after_sell
    AFTER UPDATE OF units ON Portfolio
    FOR EACH ROW
    WHEN (OLD.units > NEW.units) -- Only trigger on sell (units decrease)
    EXECUTE FUNCTION update_crypto_price_after_sell();




