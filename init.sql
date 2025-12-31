create table local_flow.workflow
(
    id          uuid                                not null
        constraint workflow_pk
            primary key,
    name        varchar(50)                         not null,
    description varchar(300),
    config      jsonb,
    is_active   boolean   default true              not null,
    created_at  timestamp default current_timestamp not null,
    updated_at  timestamp default current_timestamp not null
);