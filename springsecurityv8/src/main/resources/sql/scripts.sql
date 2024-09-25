INSERT IGNORE INTO users VALUES ('user', '{noop}Fener.1907', '1');
INSERT IGNORE INTO authorities VALUES ('user', 'read');

INSERT IGNORE INTO users VALUES ('admin', '{bcrypt}$2a$12$K.cakmcZhuDst70Sp54AZutxCXDRRNk1M90ERxieVTT5mPtEKSSke', '1');
INSERT IGNORE INTO authorities VALUES ('admin', 'admin');

CREATE TABLE customer (
                          id int NOT NULL AUTO_INCREMENT,
                          email varchar(45) NOT NULL,
                          pwd varchar(200) NOT NULL,
                          role varchar(45) NOT NULL,
                          PRIMARY KEY(id)
)

INSERT INTO customer (email, pwd, role) VALUES ('user@example.com', '{noop}Fener.1907', 'read');
INSERT INTO customer (email, pwd, role) VALUES ('admin@example.com', '{bcrypt}$2a$12$K.cakmcZhuDst70Sp54AZutxCXDRRNk1M90ERxieVTT5mPtEKSSke', 'admin');
