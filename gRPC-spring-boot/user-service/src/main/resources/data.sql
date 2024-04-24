DROP TABLE IF EXISTS "userz";
CREATE TABLE "userz" AS SELECT * FROM CSVREAD('classpath:user.csv');