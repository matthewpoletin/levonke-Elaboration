INSERT INTO elaboration.projects(projects_name, projects_description, projects_website, projects_team_id)
	VALUES ('Vostra', 'Wearable smart bracelet', 'http://matthewpoletin.ru/vostra', 1);
INSERT INTO elaboration.projects(projects_name, projects_description, projects_website, projects_team_id)
	VALUES ('Podzol', 'DIY game consolw', 'http://matthewpoletin.ru/podzol', 1);

INSERT INTO elaboration.versions(versions_major, versions_project_id)
	VALUES (1,
			(SELECT projects_id FROM elaboration.projects WHERE projects_name = 'Vostra')
	);

INSERT INTO elaboration.components(components_uuid)
	VALUES ('48cc19ca-ca21-11e7-abc4-cec278b6b50a');
INSERT INTO elaboration.components(components_uuid)
	VALUES ('689e0b5a-ca21-11e7-abc4-cec278b6b50a');

INSERT INTO elaboration.versions_components(versions_versions_id, componetns_components_id)
	VALUES (1, 1);
INSERT INTO elaboration.versions_components(versions_versions_id, componetns_components_id)
	VALUES (1, 2);