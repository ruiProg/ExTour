# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table category (
  id                            varchar(4) not null,
  title                         varchar(255) not null,
  is_protected                  tinyint(1) default 0,
  constraint pk_category primary key (id)
);

create table council (
  id                            varchar(10) not null,
  name                          varchar(255) not null,
  district_id                   varchar(10),
  constraint pk_council primary key (id)
);

create table district (
  id                            varchar(10) not null,
  name                          varchar(255) not null,
  constraint pk_district primary key (id)
);

create table estate (
  id                            varchar(10) not null,
  title                         varchar(255) not null,
  category_id                   varchar(4),
  parish_id                     varchar(10),
  details                       TEXT,
  image_url                     varchar(255),
  link_url                      varchar(255),
  coords                        varchar(40),
  constraint pk_estate primary key (id)
);

create table parish (
  id                            varchar(10) not null,
  name                          varchar(255) not null,
  council_id                    varchar(10),
  constraint pk_parish primary key (id)
);

alter table council add constraint fk_council_district_id foreign key (district_id) references district (id) on delete restrict on update restrict;
create index ix_council_district_id on council (district_id);

alter table estate add constraint fk_estate_category_id foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_estate_category_id on estate (category_id);

alter table estate add constraint fk_estate_parish_id foreign key (parish_id) references parish (id) on delete restrict on update restrict;
create index ix_estate_parish_id on estate (parish_id);

alter table parish add constraint fk_parish_council_id foreign key (council_id) references council (id) on delete restrict on update restrict;
create index ix_parish_council_id on parish (council_id);


# --- !Downs

alter table council drop foreign key fk_council_district_id;
drop index ix_council_district_id on council;

alter table estate drop foreign key fk_estate_category_id;
drop index ix_estate_category_id on estate;

alter table estate drop foreign key fk_estate_parish_id;
drop index ix_estate_parish_id on estate;

alter table parish drop foreign key fk_parish_council_id;
drop index ix_parish_council_id on parish;

drop table if exists category;

drop table if exists council;

drop table if exists district;

drop table if exists estate;

drop table if exists parish;

