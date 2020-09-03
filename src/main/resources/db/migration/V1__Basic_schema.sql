/*
 * Copyright 2002-2020 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

create schema if not exists catalogue;

create table if not exists catalogue.t_product
(
    id      uuid primary key,
    c_title varchar(255)
);

create schema if not exists warehouse;

create table if not exists warehouse.t_stored_product
(
    id         uuid primary key,
    c_version  int  not null,
    id_product uuid not null references catalogue.t_product (id),
    c_amount   int  not null default 0 check ( c_amount > -1 )
);

create index if not exists idx_stored_product_id_product on warehouse.t_stored_product (id_product);

create table if not exists warehouse.t_domain_event
(
    id bigserial primary key,
    c_timestamp timestamp not null,
    c_payload jsonb
);
