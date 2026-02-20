INSERT INTO tenants (id, name) VALUES (1, 'Apothecary of Alice');
INSERT INTO tenants (id, name) VALUES (2, 'Bakery of Bob');

INSERT INTO documents (id, name, contents, tenant) VALUES (11, 'Private Document', 'My password is ...', 1);
INSERT INTO documents (id, name, contents, tenant) VALUES (12, 'Confidential Document', 'My credit card number is ...', 2);
INSERT INTO documents (id, name, contents, tenant) VALUES (13, 'Public Document', 'Hello World!', null);

INSERT INTO notes (id, contents, document) VALUES (101, 'You should not!', 11);
INSERT INTO notes (id, contents, document) VALUES (102, 'Blimey!', 11);
INSERT INTO notes (id, contents, document) VALUES (103, 'Delete it!', 12);
INSERT INTO notes (id, contents, document) VALUES (104, 'Hello!', 13);

INSERT INTO tags (id, name, tenant) VALUES (1001, 'Sales', 1);
INSERT INTO tags (id, name, tenant) VALUES (1002, 'IT', 1);
INSERT INTO tags (id, name, tenant) VALUES (1003, 'Legal', 1);
INSERT INTO tags (id, name, tenant) VALUES (1004, 'TLP Red', 2);
INSERT INTO tags (id, name, tenant) VALUES (1005, 'TLP Amber', 2);
INSERT INTO tags (id, name, tenant) VALUES (1006, 'TLP Green', 2);
INSERT INTO tags (id, name, tenant) VALUES (1007, 'TLP Clear', 2);
INSERT INTO tags (id, name, tenant) VALUES (1008, 'Draft', null);

INSERT INTO tags_to_documents (id, tag, document) VALUES (10001, 1001, 11);
INSERT INTO tags_to_documents (id, tag, document) VALUES (10002, 1002, 11);
INSERT INTO tags_to_documents (id, tag, document) VALUES (10003, 1008, 11);
INSERT INTO tags_to_documents (id, tag, document) VALUES (10004, 1004, 12);
INSERT INTO tags_to_documents (id, tag, document) VALUES (10005, 1004, 13);
INSERT INTO tags_to_documents (id, tag, document) VALUES (10006, 1008, 13);
