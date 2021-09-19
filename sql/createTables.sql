/**
  SQL code to create expenses table.
  saved here for future need
 */
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username varchar(30) NOT NULL,
                       password varchar(300) NOT NULL
);

/**
  SQL code to create expenses table.
  saved here for future need
 */
CREATE TABLE expenses (
                          expense_id SERIAL PRIMARY KEY,
                          user_id int4 NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                          title varchar(200) NOT NULL,
                          cost numeric(7, 3) NOT NULL,
                          date varchar(10) NOT NULL
);