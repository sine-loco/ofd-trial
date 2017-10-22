create table ofd.account (
  acc_id IDENTITY,
  login VARCHAR ( 64 ) UNIQUE,
  password VARCHAR ( 64 ) NOT NULL,
  balance DECIMAL ( 10, 2 ) DEFAULT 0.00 NOT NULL,
  PRIMARY KEY ( acc_id )
);