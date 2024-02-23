create table addresses
(
    id             int auto_increment
        primary key,
    address_id     varchar(36)  not null,
    street_address varchar(255) not null,
    city           varchar(255) not null,
    country        varchar(255) not null,
    postal_code    varchar(10)  not null,
    constraint address_id
        unique (address_id)
);

create table quotes
(
    id                          int auto_increment
        primary key,
    quote_id                    varchar(36)  not null,
    pickup_street_address       varchar(255) null,
    pickup_city                 varchar(255) null,
    pickup_country              varchar(255) null,
    pickup_postal_code          varchar(10)  null,
    pickup_number_of_rooms      int          null,
    pickup_elevator             tinyint(1)   null,
    pickup_building_type        varchar(255) null,
    destination_street_address  varchar(255) null,
    destination_city            varchar(255) null,
    destination_country         varchar(255) null,
    destination_postal_code     varchar(10)  null,
    destination_number_of_rooms int          null,
    destination_elevator        tinyint(1)   null,
    destination_building_type   varchar(255) null,
    first_name                  varchar(255) null,
    last_name                   varchar(255) null,
    email_address               varchar(255) null,
    phone_number                varchar(15)  null,
    contact_method              varchar(30)  null,
    expected_moving_date        date         null,
    initiation_date             datetime     null,
    comment                     text         null,
    quote_status                varchar(10)  null,
    shipment_name               varchar(255) null,
    approximate_weight          double       null,
    approximate_shipment_value  double       null,
    constraint quote_id
        unique (quote_id)
);

create table trucks
(
    id       int auto_increment
        primary key,
    vin      varchar(17) not null,
    capacity double      null,
    constraint vin
        unique (vin)
);

create table users
(
    id                  int auto_increment
        primary key,
    user_id             varchar(36)  not null,
    first_name          varchar(255) not null,
    last_name           varchar(255) null,
    profile_picture_url varchar(255) null,
    email               varchar(255) null,
    phone_number        varchar(15)  null,
    constraint email
        unique (email),
    constraint user_id
        unique (user_id)
);

create table shipments
(
    id                         int auto_increment primary key,
    shipment_id                varchar(36)  null,
    status                     varchar(255) null,
    expected_moving_date       date         null,
    actual_moving_date         date         null,
    name                       varchar(255) null,
    approximate_weight         double       null,
    weight                     double       null,
    pickup_address_id          varchar(36)  null,
    destination_address_id     varchar(36)  null,
    vin                        varchar(17)  null,
    user_id                    varchar(36)  null,
    email                      varchar(255) null,
    phone_number               varchar(15)  null,
    first_name                 varchar(255) null,
    last_name                  varchar(255) null,
    approximate_shipment_value double       null,
    driver_id                  varchar(36)  null,
    constraint shipment_id unique (shipment_id),
    constraint shipments_customers_user_id_fk foreign key (driver_id) references users (user_id),
    constraint shipments_ibfk_2 foreign key (pickup_address_id) references addresses (address_id),
    constraint shipments_ibfk_3 foreign key (destination_address_id) references addresses (address_id),
    constraint shipments_ibfk_4 foreign key (vin) references trucks (vin),
    constraint shipments_ibfk_5 foreign key (user_id) references users (user_id)
);

create table inventories
(
    id                 int auto_increment
        primary key,
    name               varchar(255)  null,
    inventory_id       varchar(36)   not null,
    description        varchar(2000) null,
    shipment_id        varchar(36)   null,
    inventory_status   varchar(255)  null,
    approximate_weight double        null,
    constraint inventory_id
        unique (inventory_id),
    constraint inventories_ibfk_1
        foreign key (shipment_id) references shipments (shipment_id)
);

create index shipment_id
    on inventories (shipment_id);

create table items
(
    id                    int auto_increment
        primary key,
    item_id               varchar(36)          not null,
    name                  varchar(255)         not null,
    type                  enum ('ITEM', 'BOX') not null,
    price                 decimal(38, 2)       null,
    description           varchar(255)         null,
    weight                double               null,
    handling_instructions varchar(255)         null,
    inventory_id          varchar(36)          null,
    constraint item_id
        unique (item_id),
    constraint items_ibfk_1
        foreign key (inventory_id) references inventories (inventory_id) ON DELETE CASCADE
);

create index inventory_id
    on items (inventory_id);

create table observers
(
    id            int auto_increment
        primary key,
    observer_id   varchar(36)  not null,
    name          varchar(255) null,
    permission    tinyint      null,
    observer_code varchar(6)   not null,
    shipment_id   varchar(36)  null,
    constraint observer_code
        unique (observer_code),
    constraint observer_id
        unique (observer_id),
    constraint observers_ibfk_1
        foreign key (shipment_id) references shipments (shipment_id)
);

create index shipment_id
    on observers (shipment_id);

create index destination_address_id
    on shipments (destination_address_id);

create index pickup_address_id
    on shipments (pickup_address_id);

create index user_id
    on shipments (user_id);

create index vin
    on shipments (vin);
