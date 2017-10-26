CREATE TABLE elaboration.versions
(
	versions_id integer DEFAULT nextval('elaboration.versions_versions_id_seq'::regclass) PRIMARY KEY NOT NULL,
	versions_major integer,
	versions_project_id integer,
	CONSTRAINT fk2vy8boaujsi50li5at1d6v34l FOREIGN KEY (versions_project_id) REFERENCES projects (projects_id)
);
