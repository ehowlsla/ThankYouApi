# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table content (
  id                        bigint auto_increment not null,
  title                     nvarchar(255),
  contents                  text,
  create_date               datetime,
  view_count                integer,
  rec_count                 integer,
  reply_count               integer,
  image_url                 nvarchar(100),
  status                    integer,
  is_open_community         integer,
  is_open_facebook          integer,
  user_id                   bigint,
  constraint pk_content primary key (id))
;

create table reply (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  content_id                bigint,
  contents                  text,
  create_at                 datetime,
  status                    integer,
  constraint pk_reply primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
  nickname                  nvarchar(255),
  udid                      nvarchar(100),
  memo                      text,
  status                    integer,
  gender                    integer,
  created_at                datetime,
  image_url                 nvarchar(255),
  app_version               nvarchar(20),
  os_version                nvarchar(20),
  constraint pk_user primary key (id))
;

alter table content add constraint fk_content_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_content_user_1 on content (user_id);
alter table reply add constraint fk_reply_user_2 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_reply_user_2 on reply (user_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table content;

drop table reply;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

