CREATE TABLE elaboration.projects
(
	projects_id integer DEFAULT nextval('elaboration.projects_projects_id_seq'::regclass) PRIMARY KEY NOT NULL,
	projects_description varchar(255),
	projects_name varchar(255),
	projects_team_id integer,
	projects_website varchar(255)
);
