# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table content (
  id                        bigint auto_increment not null,
  contents                  text,
  create_date               datetime,
  like_count                integer,
  reply_count               integer,
  image_url1                nvarchar(100),
  image_url2                nvarchar(100),
  image_url3                nvarchar(100),
  image_url4                nvarchar(100),
  status                    integer,
  open_level                integer,
  ban_count                 integer,
  user_id                   bigint,
  constraint pk_content primary key (id))
;

create table content_like (
  id                        bigint auto_increment not null,
  create_date               datetime,
  content_id                bigint,
  user_id                   bigint,
  constraint pk_content_like primary key (id))
;

create table notice (
  id                        bigint auto_increment not null,
  image_url                 nvarchar(255),
  message                   text,
  redirect_id               bigint,
  user_id                   bigint,
  status                    integer,
  create_date               datetime,
  constraint pk_notice primary key (id))
;

create table reply (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  content_id                bigint,
  contents                  text,
  create_date               datetime,
  status                    integer,
  like_count                integer,
  constraint pk_reply primary key (id))
;

create table reply_like (
  id                        bigint auto_increment not null,
  create_date               datetime,
  reply_id                  bigint,
  user_id                   bigint,
  constraint pk_reply_like primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
  email                     nvarchar(255),
  password                  nvarchar(255),
  nickname                  nvarchar(255),
  udid                      nvarchar(100),
  memo                      text,
  status                    integer,
  gender                    integer,
  birth                     nvarchar(20),
  create_date               datetime,
  city                      nvarchar(100),
  job                       nvarchar(100),
  image_url1                nvarchar(255),
  image_url2                nvarchar(255),
  image_url3                nvarchar(255),
  image_url4                nvarchar(255),
  app_version               nvarchar(20),
  os_version                nvarchar(20),
  token_key                 nvarchar(20),
  phone                     nvarchar(40),
  device_id                 nvarchar(40),
  constraint uq_user_phone unique (phone),
  constraint uq_user_device_id unique (device_id),
  constraint pk_user primary key (id))
;

alter table content add constraint fk_content_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_content_user_1 on content (user_id);
alter table content_like add constraint fk_content_like_user_2 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_content_like_user_2 on content_like (user_id);
alter table reply add constraint fk_reply_user_3 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_reply_user_3 on reply (user_id);
alter table reply_like add constraint fk_reply_like_user_4 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_reply_like_user_4 on reply_like (user_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table content;

drop table content_like;

drop table notice;

drop table reply;

drop table reply_like;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

