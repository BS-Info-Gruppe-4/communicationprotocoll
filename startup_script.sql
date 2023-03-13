create table if not exists gm3.Ablesung
(
    id            varchar(255) not null,
    zaehlernummer varchar(255) not null,
    datum         date         not null,
    kunde         varchar(255) null,
    kommentar     varchar(255) null,
    neuEingebaut  tinyint(1)   not null,
    zaehlerstand  int          not null
);

alter table gm3.Ablesung
    add primary key (id);

create table if not exists gm3.Kunde
(
    id      varchar(255) not null,
    name    varchar(255) not null,
    vorname varchar(255) null
);

alter table gm3.Kunde
    add primary key (id);

alter table gm3.Ablesung
    add constraint Ablesung_Kunde_id_fk
        foreign key (kunde) references gm3.Kunde (id)
        on delete set null;


