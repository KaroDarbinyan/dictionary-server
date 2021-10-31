drop table if exists "user";
create table "user"
(
    id                 bigserial not null,
    name               varchar   not null,
    surname            varchar   not null,
    username           varchar   not null,
    email              varchar   not null,
    password           varchar   not null,
    created_by         varchar   not null,
    created_date       timestamp not null default current_timestamp,
    last_modified_by   varchar   not null,
    last_modified_date timestamp not null default current_timestamp,
    primary key (id),
    unique (username),
    unique (email)
);

drop table if exists authority;
create table authority
(
    id   bigserial not null,
    name varchar   not null,
    primary key (id),
    unique (name)
);

drop table if exists user_authority;
create table user_authority
(
    user_id      bigint not null,
    authority_Id bigint not null,
    foreign key (user_id) references "user" (id) on update cascade on delete cascade,
    foreign key (authority_Id) references authority (id) on update cascade on delete cascade
);

drop table if exists refresh_token;
create table refresh_token
(
    id          bigserial not null,
    expire_date timestamp not null,
    token       varchar   not null,
    user_Id     bigint    not null,
    primary key (id),
    foreign key (user_id) references "user" (id) on update cascade on delete cascade
);

drop table if exists word_group;
create table word_group
(
    id                 bigserial not null,
    name               varchar   not null,
    created_by         varchar   not null,
    created_date       timestamp not null default current_timestamp,
    last_modified_by   varchar   not null,
    last_modified_date timestamp not null default current_timestamp,
    primary key (id),
    unique (name)
);

drop table if exists word;
create table word
(
    id                 bigserial not null,
    armenian           varchar   not null,
    english            varchar   not null,
    repeat             boolean   not null default true,
    word_group_id      bigint    not null,
    created_by         varchar   not null,
    created_date       timestamp not null default current_timestamp,
    last_modified_by   varchar   not null,
    last_modified_date timestamp not null default current_timestamp,
    foreign key (word_group_id) references word_group (id) on update cascade on delete cascade
);

