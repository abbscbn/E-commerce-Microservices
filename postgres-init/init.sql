CREATE DATABASE identitydb;
CREATE DATABASE productdb;
CREATE DATABASE orderdb;

-- Schema yaratmak istersen (opsiyonel)
\c identitydb
CREATE SCHEMA IF NOT EXISTS identitydb;

\c productdb
CREATE SCHEMA IF NOT EXISTS productdb;

\c orderdb
CREATE SCHEMA IF NOT EXISTS orderdb;
