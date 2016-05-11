CREATE SCHEMA somedb;

CREATE TABLE somedb.OrderLog (
       orderid BIGINT PRIMARY KEY,
       executionDate TIMESTAMP,
       orderType VARCHAR(30) NOT NULL,
       quantity INT,
       executionPrice DECIMAL(5, 2),
       symbol VARCHAR(10),
       userId INT,
       complete_batch_date DATE
);