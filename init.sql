CREATE SCHEMA IF NOT EXISTS local_flow;

CREATE TABLE local_flow.workflow
(
    id          uuid                                not null
        constraint workflow_pk
            primary key,
    name        varchar(50)                         not null,
    description varchar(300),
    is_active   boolean   default true              not null,
    created_at  timestamp default current_timestamp not null,
    updated_at  timestamp default current_timestamp not null
);

CREATE TABLE local_flow.workflow_node
(
    id           UUID                                NOT NULL PRIMARY KEY,
    workflow_id  UUID                                NOT NULL,
    node_key     VARCHAR(50)                         NOT NULL,
    name         VARCHAR(50)                         NOT NULL,
    version      VARCHAR(30),
    position_x   INTEGER                             NOT NULL,
    position_y   INTEGER                             NOT NULL,
    parameters   JSONB,
    credentials  JSONB,
    is_active    BOOLEAN     DEFAULT TRUE            NOT NULL,
    created_at   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NOT NULL,

    CONSTRAINT fk_workflow_node_workflow
        FOREIGN KEY (workflow_id)
            REFERENCES local_flow.workflow (id)
            ON DELETE CASCADE
);
CREATE INDEX idx_workflow_node_workflow_id
    ON local_flow.workflow_node (workflow_id);


CREATE TABLE local_flow.workflow_execution
(
    id          bigserial
        constraint workflow_execution_pk
            primary key,
    workflow_id uuid                                not null,
    status      varchar(50)                         not null,
    trigger_context jsonb,
    started_at  timestamp default CURRENT_TIMESTAMP not null,
    finished_at timestamp
);

CREATE TABLE local_flow.workflow_execution_node
(
    id                    bigserial
        constraint workflow_execution_node_pk
            primary key,
    workflow_execution_id bigint      not null,
    node_id               uuid        not null,
    status                varchar(50) not null,
    started_at            timestamp   not null,
    finished_at           timestamp,
    input_data            jsonb,
    output_data           jsonb,
    error_message         text
);

CREATE TABLE local_flow.workflow_connection
(
    id           uuid                                not null
        constraint workflow_connection_pk
            primary key,
    workflow_id  uuid                                not null,
    from_node_id uuid                                not null,
    to_node_id   uuid                                not null,
    created_at   timestamp default CURRENT_TIMESTAMP not null,
    updated_at   timestamp default CURRENT_TIMESTAMP not null
);

CREATE TABLE local_flow.credential
(
    id             uuid                                not null
        constraint credential_pk
            primary key,
    name           varchar(30)                         not null,
    type           varchar(20)                         not null,
    encrypted_data jsonb                               not null,
    created_at     timestamp default CURRENT_TIMESTAMP not null
);
