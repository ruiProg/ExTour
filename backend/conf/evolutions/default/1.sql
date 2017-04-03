# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

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

create table parish (
  id                            varchar(10) not null,
  name                          varchar(255) not null,
  council_id                    varchar(10),
  constraint pk_parish primary key (id)
);

alter table council add constraint fk_council_district_id foreign key (district_id) references district (id) on delete restrict on update restrict;
create index ix_council_district_id on council (district_id);

alter table parish add constraint fk_parish_council_id foreign key (council_id) references council (id) on delete restrict on update restrict;
create index ix_parish_council_id on parish (council_id);


# --- !Downs

alter table council drop foreign key fk_council_district_id;
drop index ix_council_district_id on council;

alter table parish drop foreign key fk_parish_council_id;
drop index ix_parish_council_id on parish;

drop table if exists council;

drop table if exists district;

drop table if exists parish;

