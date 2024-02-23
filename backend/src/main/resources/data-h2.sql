-- quotes
INSERT INTO quotes (quote_id, pickup_street_address, pickup_city, pickup_country, pickup_postal_code, pickup_number_of_rooms, pickup_elevator, pickup_building_type, destination_street_address, destination_city, destination_country, destination_postal_code, destination_number_of_rooms, destination_elevator, destination_building_type, first_name, last_name, email_address, phone_number, contact_method, expected_moving_date, initiation_date, comment, quote_status, shipment_name,approximate_weight,approximate_shipment_value) VALUES
    ('a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6', '123 Main St', 'Cityville', 'CA', 'M1A 1A1', 3, TRUE, 'Apartment', '456 Oak St', 'Townsville', 'USA', 'M5V 2H1', 4, FALSE, 'House', 'John', 'Doe', 'emilyd@example.com' , '123-555-1234', 'EMAIL', '2023-12-15', '2023-12-13 08:30:00', 'Moving details for John Doe', 'PENDING', 'ShipmentXYZ',0.0,0.0);
INSERT INTO quotes (quote_id, pickup_street_address, pickup_city, pickup_country, pickup_postal_code, pickup_number_of_rooms, pickup_elevator, pickup_building_type, destination_street_address, destination_city, destination_country, destination_postal_code, destination_number_of_rooms, destination_elevator, destination_building_type, first_name, last_name, email_address, phone_number, contact_method, expected_moving_date, initiation_date, comment, quote_status, shipment_name,approximate_weight,approximate_shipment_value) VALUES
    ('a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p7', '789 Elm St', 'Villagetown', 'USA', 'R2D 2C3', 2, TRUE, 'Condo', '101 Pine St', 'Hometown', 'CA', 'K8X 4Z5', 3, TRUE, 'Apartment', 'Jane', 'Smith', 'jane.smith@example.com', '987-555-6789', 'PHONE_NUMBER', '2023-12-20', '2023-12-18 09:45:00', 'Moving details for Jane Smith', 'PENDING', 'ShipmentABC',0.0,0.0);
INSERT INTO quotes (quote_id, pickup_street_address, pickup_city, pickup_country, pickup_postal_code, pickup_number_of_rooms, pickup_elevator, pickup_building_type, destination_street_address, destination_city, destination_country, destination_postal_code, destination_number_of_rooms, destination_elevator, destination_building_type, first_name, last_name, email_address, phone_number, contact_method, expected_moving_date, initiation_date, comment, quote_status, shipment_name,approximate_weight,approximate_shipment_value) VALUES
    ('a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p8', '222 Maple St', 'Maple City', 'USA', 'P3R 5T6', 1, FALSE, 'House', '333 Birch St', 'Groveville', 'CA', 'L7L 2K9', 2, TRUE, 'Condo', 'Michael', 'Johnson', 'dylan.brassard@outlook.com', '555-123-4567', 'PHONE_NUMBER', '2023-12-25', '2023-12-22 11:00:00', 'Moving details for Michael Johnson', 'ACCEPTED', 'ShipmentMNO',0.0,0.0);
INSERT INTO quotes (quote_id, pickup_street_address, pickup_city, pickup_country, pickup_postal_code, pickup_number_of_rooms, pickup_elevator, pickup_building_type, destination_street_address, destination_city, destination_country, destination_postal_code, destination_number_of_rooms, destination_elevator, destination_building_type, first_name, last_name, email_address, phone_number, contact_method, expected_moving_date, initiation_date, comment, quote_status, shipment_name,approximate_weight,approximate_shipment_value) VALUES
    ('a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p9', '444 Oak Ave', 'Forestville', 'CA', 'V8V 1W1', 2, TRUE, 'Apartment', '555 Pine Ave', 'Treeville', 'USA', 'M4S 3Z2', 3, FALSE, 'House', 'Emily', 'Davis', 'chahbouney2000@outlook.fr', '789-555-0123', 'EMAIL', '2023-12-30', '2023-12-28 13:15:00', 'Moving details for Emily Davis', 'ACCEPTED', 'ShipmentPQR',0.0,0.0);
INSERT INTO quotes (quote_id, pickup_street_address, pickup_city, pickup_country, pickup_postal_code, pickup_number_of_rooms, pickup_elevator, pickup_building_type, destination_street_address, destination_city, destination_country, destination_postal_code, destination_number_of_rooms, destination_elevator, destination_building_type, first_name, last_name, email_address, phone_number, contact_method, expected_moving_date, initiation_date, comment, quote_status, shipment_name,approximate_weight,approximate_shipment_value) VALUES
    ('a1sc3d4-e5f6-g7h8-i9j0-oil2m3n4o5p9', '4 Oak Ave', 'Fotville', 'CA', 'V3V 1W1', 2, TRUE, 'House', '555 Pine Ave', 'NwHoke', 'USA', 'M4S 7H6', 3, FALSE, 'House', 'Emily', 'Davis', 'nicholasmartoccia04@icloud.com', '789-555-3123', 'EMAIL', '2023-12-30', '2013-12-28 13:15:00', 'Moving details for Emily Davis', 'ACCEPTED', 'ShipmentPQR',0.0,0.0);

-- addresses
INSERT INTO addresses (address_id, street_address, city, country, postal_code) VALUES
    ('a1sc3d4-e5f6-g7h8-i9j0-oil2m3n4o5p1', '4 Oak Ave', 'Fotville', 'CA', 'V3V 1W1');
INSERT INTO addresses (address_id, street_address, city, country, postal_code) VALUES
    ('a1sc3d4-e5f6-g7h8-i9j0-oil2m3n4o5p2', '555 Pine Ave', 'NwHoke', 'USA', 'M4S 7H6');
-- test addresses
INSERT INTO addresses (address_id, street_address, city, country, postal_code) VALUES
    ('a1sc3d4-e5f6-g7h8-i9j0-oil2m3n4o512', '4 Oak Ave', 'Fotville', 'CA', 'V3V 1W1');
INSERT INTO addresses (address_id, street_address, city, country, postal_code) VALUES
    ('a1sc3d4-e5f6-g7h8-i9j0-oil2m3n4o5pl', '4 Oak Ave', 'Fotville', 'CA', 'V3V 1W1');
INSERT INTO addresses (address_id, street_address, city, country, postal_code) VALUES
    ('a1sc3d4-e5f6-g7h8-i9j0-oil2m3n4o123', '123 Maple St', 'Springfield', 'USA', '12345');
INSERT INTO addresses (address_id, street_address, city, country, postal_code) VALUES
    ('a1sc3d4-e5f6-g7h8-i9j0-oil2m3n4o456', '456 Elm St', 'Riverside', 'USA', '67890');
INSERT INTO addresses (address_id, street_address, city, country, postal_code) VALUES
    ('a1sc3d4-e5f6-g7h8-i9j0-oil2m3n4o789', '789 Oak St', 'Hilltown', 'USA', '54321');
INSERT INTO addresses (address_id, street_address, city, country, postal_code) VALUES
    ('a1sc3d4-e5f6-g7h8-i9j0-oil2m3n4o987', '987 Pine St', 'Lakeview', 'USA', '13579');


-- shipments
INSERT INTO shipments (shipment_id, status, expected_moving_date, actual_moving_date, name, approximate_weight, weight, pickup_address_id, destination_address_id, vin, user_id, email, phone_number,approximate_shipment_value, driver_id) VALUES
    ('c0a80121-7f5e-4d77-a5a4-5d41b04f5a57', 'QUOTED', '2023-12-30', '2024-01-02', 'ShipmentPQR', 1500.0, 1575.0, 'a1sc3d4-e5f6-g7h8-i9j0-oil2m3n4o5p1', 'a1sc3d4-e5f6-g7h8-i9j0-oil2m3n4o5p2', 'f4b29e3c-a526-487d-84ec-7c16791b26b6', NULL, 'john@icloud.com', '789-555-3123',250.0, 'auth0|65a586c28dea5e6136569cf0');
-- Test Shipment
INSERT INTO shipments (shipment_id, status, expected_moving_date, actual_moving_date, name, approximate_weight, weight, pickup_address_id, destination_address_id, vin, user_id, email, phone_number,approximate_shipment_value, driver_id) VALUES
    ('c0a80121-7f5e-4d77-a5a4-5d41b04f5a23', 'QUOTED', '2023-12-30', '2024-01-02', 'ShipmentPQR', 1500.0, 1575.0, 'a1sc3d4-e5f6-g7h8-i9j0-oil2m3n4o512', 'a1sc3d4-e5f6-g7h8-i9j0-oil2m3n4o5pl', NULL, NULL, 'john@example.com', '789-555-3123',250.0, 'auth0|65a586c28dea5e6136569cf0');
INSERT INTO shipments (shipment_id, status, expected_moving_date, actual_moving_date, name, approximate_weight, weight, pickup_address_id, destination_address_id, vin, user_id, email, phone_number, approximate_shipment_value, driver_id) VALUES
    ('c0a80121-7f5e-4d77-a5a4-5d41b04f5a24', 'QUOTED', '2023-12-31', '2024-01-03', 'ShipmentXYZ', 1750.0, 1837.5, 'a1sc3d4-e5f6-g7h8-i9j0-oil2m3n4o123', 'a1sc3d4-e5f6-g7h8-i9j0-oil2m3n4o456', NULL, NULL, 'john@example.com', '789-555-3123', 300.0, NULL);
INSERT INTO shipments (shipment_id, status, expected_moving_date, actual_moving_date, name, approximate_weight, weight, pickup_address_id, destination_address_id, vin, user_id, email, phone_number, approximate_shipment_value, driver_id) VALUES
    ('c0a80121-7f5e-4d77-a5a4-5d41b04f5a25', 'QUOTED', '2024-01-01', '2024-01-04', 'ShipmentABC', 2000.0, 2100.0, 'a1sc3d4-e5f6-g7h8-i9j0-oil2m3n4o789', 'a1sc3d4-e5f6-g7h8-i9j0-oil2m3n4o987', NULL, NULL, 'john@example.com', '789-555-3123', 350.0, NULL);


-- inventories
INSERT INTO inventories (name, inventory_id, description, shipment_id, inventory_status, approximate_weight) VALUES
    ('Kitchen', '550e8400-e29b-41d4-a716-446655440000', 'This is the inventory for the kitchen', 'c0a80121-7f5e-4d77-a5a4-5d41b04f5a57', 'CREATED', 150);
INSERT INTO inventories (name, inventory_id, description, shipment_id, inventory_status, approximate_weight) VALUES
    ('Bathroom', '8a4b1e77-2fb3-4d3c-9e6c-af9d8b1f361a', 'This is the inventory for the bathroom', 'c0a80121-7f5e-4d77-a5a4-5d41b04f5a57', 'PACKED', 180);
-- inventory test
INSERT INTO inventories (name, inventory_id, description, shipment_id, inventory_status, approximate_weight) VALUES
    ('Bathroom', '8a4b1e77-2fb3-4d3c-9e6c-af9d8b1f3613', 'This is the inventory for the bathroom', 'c0a80121-7f5e-4d77-a5a4-5d41b04f5a23', 'CREATED', 180);

-- items
INSERT INTO items (item_id, name, type, price, description, weight, handling_instructions, inventory_id) VALUES
    ('a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6', 'Widget A', 'ITEM', 19.99, 'A high-quality widget', 0.5, 'Handle with care', '8a4b1e77-2fb3-4d3c-9e6c-af9d8b1f3613');
INSERT INTO items (item_id, name, type, price, description, weight, handling_instructions, inventory_id) VALUES
    ('q7r8s9t0-u1v2-w3x4-y5z6-a7b8c9d0e1f2', 'Box B', 'BOX', 39.99, 'Sturdy box for shipping', 2.0, 'Fragile items inside', '8a4b1e77-2fb3-4d3c-9e6c-af9d8b1f3613');

-- Observers
INSERT INTO observers (observer_id, name, permission, observer_code, shipment_id)
VALUES
    ('456e7891-e89b-12d3-a456-426614174801', 'Dummy Observer', 2, 'MBS123', 'c0a80121-7f5e-4d77-a5a4-5d41b04f5a23');

INSERT INTO trucks (id, vin, capacity) VALUES (1, '1G1YY22G945102710', 25000);
INSERT INTO trucks (id, vin, capacity) VALUES (2, '3C4PDCGB4ET312960', 27000);
INSERT INTO trucks (id, vin, capacity) VALUES (3, '1FDKF37G2TEB29842', 30000);
INSERT INTO trucks (id, vin, capacity) VALUES (4, '1FMJU2A53CEF92575', 26000);
INSERT INTO trucks (id, vin, capacity) VALUES (5, '4A3AB36F29E026270', 32000);

INSERT INTO users (id, user_id, first_name, last_name, profile_picture_url, email, phone_number) VALUES (1, 'auth0|6564e04317b4bdb50115f582', 'admin', null, 'https://s.gravatar.com/avatar/64e1b8d34f425d19e1ee2ea7236d3028?s=480&r=pg&d=https%3A%2F%2Fcdn.auth0.com%2Favatars%2Fad.png', 'admin@admin.com', null);
INSERT INTO users (id, user_id, first_name, last_name, profile_picture_url, email, phone_number) VALUES (2, 'auth0|65969386148831e2e8dbbe2d', 'john', 'Smith', 'https://s.gravatar.com/avatar/d4c74594d841139328695756648b6bd6?s=480&r=pg&d=https%3A%2F%2Fcdn.auth0.com%2Favatars%2Fjo.png', 'john@example.com', '');
INSERT INTO users (id, user_id, first_name, last_name, profile_picture_url, email, phone_number) VALUES (3, 'auth0|657e975fb037b915578c26d3', 'shipmentestimator', null, 'https://s.gravatar.com/avatar/c322c17c0519f554628ef42e77b0dcbb?s=480&r=pg&d=https%3A%2F%2Fcdn.auth0.com%2Favatars%2Fsh.png', 'shipmentestimator@movingexpress.com', null);
INSERT INTO users (id, user_id, first_name, last_name, profile_picture_url, email, phone_number) VALUES (4, 'auth0|65aeb02e654aed8d6be9e80d', 'chahbouney2000', null, 'https://s.gravatar.com/avatar/3e80e7cbf86658e49655348bffa82e63?s=480&r=pg&d=https%3A%2F%2Fcdn.auth0.com%2Favatars%2Fch.png', 'chahbouney2000@outlook.fr', null);
INSERT INTO users (id, user_id, first_name, last_name, profile_picture_url, email, phone_number) VALUES (5, 'auth0|657d692eb037b915578b9e42', 'liucaleb8', null, 'https://s.gravatar.com/avatar/b6cadd1c85cd34af2e6f743c9635e2a5?s=480&r=pg&d=https%3A%2F%2Fcdn.auth0.com%2Favatars%2Fli.png', 'liucaleb8@gmail.com', null);
INSERT INTO users (id, user_id, first_name, last_name, profile_picture_url, email, phone_number) VALUES (6, 'auth0|65b005926f1a1391e9ddcaf9', 'karinaevang', null, 'https://s.gravatar.com/avatar/fb9ac2d658d4110de4ee500b63395623?s=480&r=pg&d=https%3A%2F%2Fcdn.auth0.com%2Favatars%2Fka.png', 'karinaevang@hotmail.com', null);
INSERT INTO users (id, user_id, first_name, last_name, profile_picture_url, email, phone_number) VALUES (7, 'auth0|657e971dd4eada6b02d1925d', 'shipmentreviewer', null, 'https://s.gravatar.com/avatar/0c812b3ea7fed653375085f712054fe2?s=480&r=pg&d=https%3A%2F%2Fcdn.auth0.com%2Favatars%2Fsh.png', 'shipmentreviewer@movingexpress.com', null);
INSERT INTO users (id, user_id, first_name, last_name, profile_picture_url, email, phone_number) VALUES (8, 'auth0|657fec4b9a0fa387ad49ea55', 'movingestimator', null, 'https://s.gravatar.com/avatar/cbda9a9ddb5d9cf470e36884d052220b?s=480&r=pg&d=https%3A%2F%2Fcdn.auth0.com%2Favatars%2Fmo.png', 'movingestimator@movingexpress.com', null);
INSERT INTO users (id, user_id, first_name, last_name, profile_picture_url, email, phone_number) VALUES (9, 'auth0|657d1cb17588006882ac0d4a', 'richard2004danon', null, 'https://s.gravatar.com/avatar/85966e1819d32288e953390db3f1d370?s=480&r=pg&d=https%3A%2F%2Fcdn.auth0.com%2Favatars%2Fri.png', 'richard2004danon@gmail.com', null);
INSERT INTO users (id, user_id, first_name, last_name, profile_picture_url, email, phone_number) VALUES (10, 'auth0|656ec0a619c6480cbd876ff6', 'test', 'hello', 'https://s.gravatar.com/avatar/93942e96f5acd83e2e047ad8fe03114d?s=480&r=pg&d=https%3A%2F%2Fcdn.auth0.com%2Favatars%2Fte.png', 'test@email.com', '5142961854');
INSERT INTO users (id, user_id, first_name, last_name, profile_picture_url, email, phone_number) VALUES (11, 'auth0|65bfffe70fa0032f10456386', 'corey.woodcox', null, 'https://s.gravatar.com/avatar/113240551234de8328aab7056eb6ce4b?s=480&r=pg&d=https%3A%2F%2Fcdn.auth0.com%2Favatars%2Fco.png', 'corey.woodcox@gmail.com', null);
INSERT INTO users (id, user_id, first_name, last_name, profile_picture_url, email, phone_number) VALUES (12, 'auth0|65c253cce9045b6b62e94ff3', 'nicomartoccia', null, 'https://s.gravatar.com/avatar/1a9221ea0ea5e684b8495aae8f79bd57?s=480&r=pg&d=https%3A%2F%2Fcdn.auth0.com%2Favatars%2Fni.png', 'nicomartoccia@gmail.com', null);
INSERT INTO users (id, user_id, first_name, last_name, profile_picture_url, email, phone_number) VALUES (13, 'auth0|65a586c28dea5e6136569cf0', 'truckdriver', null, 'https://s.gravatar.com/avatar/a3cca9423a230ac57e669e166e32eb01?s=480&r=pg&d=https%3A%2F%2Fcdn.auth0.com%2Favatars%2Ftr.png', 'truckdriver@movingexpress.com', null);

