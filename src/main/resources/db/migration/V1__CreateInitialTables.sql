CREATE TABLE account
(
    email               VARCHAR(100)                    NOT NULL,
    password            VARCHAR(80)                     NOT NULL,
    PRIMARY KEY (email)
);

CREATE TABLE journalist
(
    id              BIGINT      AUTO_INCREMENT  NOT NULL,
    first_name      VARCHAR(50)                 NOT NULL,
    last_name       VARCHAR(50)                 NOT NULL,
    birthday        DATE                        NOT NULL,
    account_email   VARCHAR(100)                NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (account_email) REFERENCES account (email)
);

CREATE TABLE story
(
    id              BIGINT      AUTO_INCREMENT  NOT NULL,
    title           VARCHAR(50)                 NOT NULL,
    description     VARCHAR(50),
    journalist_id   BIGINT                      NOT NULL,
    publish_date    DATE                        NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (journalist_id) REFERENCES journalist (id)
);

CREATE TABLE file
(
    id              BIGINT  AUTO_INCREMENT  NOT NULL,
    name            VARCHAR(50)             NOT NULL,
    data            LONGBLOB                NOT NULL,
    filetype        VARCHAR(50)             NOT NULL,
    created_date    DATE                    NOT NULL,
    story_id        BIGINT                  NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (story_id) REFERENCES story (id)
);