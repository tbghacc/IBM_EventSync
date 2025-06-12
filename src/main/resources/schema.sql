create table if not exists events (
    id UUID,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(200),
    PRIMARY KEY(id)
);

create table if not exists event_feedback (
    id UUID,
    event UUID NOT NULL REFERENCES events (id),
    submitter VARCHAR(100) NOT NULL,
    feedback VARCHAR(200) NOT NULL,
    positive DOUBLE,
    neutral DOUBLE,
    negative DOUBLE,
    submitted TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id)
);

