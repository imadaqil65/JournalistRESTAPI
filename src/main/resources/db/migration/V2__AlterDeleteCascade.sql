ALTER TABLE journalist
    DROP FOREIGN KEY journalist_ibfk_1,
    ADD CONSTRAINT fk_journalist_account
    FOREIGN KEY (account_email) REFERENCES account (email) ON DELETE CASCADE;

ALTER TABLE story
    DROP FOREIGN KEY story_ibfk_1,
    ADD CONSTRAINT fk_story_journalist
    FOREIGN KEY (journalist_id) REFERENCES journalist (id) ON DELETE CASCADE;

ALTER TABLE file
    DROP FOREIGN KEY file_ibfk_1,
    ADD CONSTRAINT fk_file_story
    FOREIGN KEY (story_id) REFERENCES story (id) ON DELETE CASCADE;

ALTER TABLE story
    ADD COLUMN published BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE story
    MODIFY COLUMN publish_date DATE NULL;