INSERT INTO elaboration.projects(projects_name, projects_description, projects_website, projects_team_id)
	VALUES ('Vostra', 'Wearable smart bracelet', 'http://matthewpoletin.ru/vostra', 1);

INSERT INTO elaboration.versions(versions_major, versions_project_id)
	VALUES (1,
			(SELECT projects_id FROM elaboration.projects WHERE projects_name = 'Vostra')
	);