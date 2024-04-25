DROP TABLE IF EXISTS userr;
CREATE TABLE userr AS SELECT * FROM CSVREAD('classpath:user.csv');

