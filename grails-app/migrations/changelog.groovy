import liquibase.change.core.DropNotNullConstraintChange;

databaseChangeLog = {

	changeSet(author: "tfotak", id: "1318893850589-1") {
		createTable(tableName: "feature_type") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "feature_typePK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "feature_type_name", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "geonetwork_uuid", type: "varchar(255)") {
				constraints(nullable: "false")
			}
			
			column(name: "feature_type_id", type: "varchar(255)") {
				constraints(nullable: "false")
			}
		}
		
		sql("SELECT AddGeometryColumn('feature_type', 'geometry', 4326, 'GEOMETRY', 2)")
		sql("create index idx_ft_geometry on feature_type using gist (geometry)")
		// The below might need to be run separately, it can't be run as part of a transaction apparently
		//sql("vacuum analyze feature_type")
		
		createIndex(tableName: "feature_type", indexName: "idx_ft_feature_type") {
			column(name: "feature_type_name")
			column(name: "geonetwork_uuid")
			column(name: "feature_type_id")
		}
	}

	changeSet(author: "tfotak", id: "1318893850589-2") {
		createTable(tableName: "feature_type_request_class") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "feature_type_PK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "class_name", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "feature_type_name", type: "varchar(255)") {
				constraints(nullable: "false")
			}
			
			column(name: "constructor_args", type: "varchar(255)")
		}
	}

	changeSet(author: "tfotak", id: "1318893850589-3") {
		createTable(tableName: "index_run") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "index_runPK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "documents", type: "int4")
			
			column(name: "failures", type: "int4")

			column(name: "run_date", type: "timestamp") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "tfotak", id: "1318893850589-4") {
		createTable(tableName: "geonetwork_metadata") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "geonetwork_metPK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "added", type: "timestamp")

			column(name: "feature_type_name", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "geonetwork_uuid", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "geoserver_end_point", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "index_run_id", type: "int8")
		}
		
		createIndex(tableName: "geonetwork_metadata", indexName: "idx_qd_feature_type") {
			column(name: "feature_type_name")
			column(name: "geonetwork_uuid")
		}
		
		createIndex(tableName: "geonetwork_metadata", indexName: "idx_qd_index_run_id") {
			column(name: "index_run_id")
		}
		
		addForeignKeyConstraint(baseColumnNames: "index_run_id", baseTableName: "geonetwork_metadata", constraintName: "FKE5C5EA6741AAA315", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "index_run", referencesUniqueColumn: "false")
	}

	changeSet(author: "tfotak", id: "1318893850589-6") {
		createSequence(sequenceName: "hibernate_sequence")
	}
	
	changeSet(author: "tfotak", id: "1319582037000-2") {
		sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:anfog_glider', 'au.org.emii.search.index.FeatureTypeRequest', 'uuid,thepoint_lonlat'")
		sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:soop_asf', 'au.org.emii.search.index.DiskCachingFeatureTypeRequest', 'id,geometry'")
		sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:soop_co2', 'au.org.emii.search.index.FeatureTypeRequest', 'id,geometry'")
		sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:soop_tmv', 'au.org.emii.search.index.FeatureTypeRequest', 'id,geometry'")
		sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:soop_sst_without_1min_vw', 'au.org.emii.search.index.FeatureTypeRequest', 'id,geometry'")
		sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:soop_sst_recent_1min_avg_vw', 'au.org.emii.search.index.FeatureTypeRequest', 'id,geometry'")
		sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:anmn_acoustics', 'au.org.emii.search.index.FeatureTypeRequest', 'code,geom'")
		sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:anmn_nrs', 'au.org.emii.search.index.FeatureTypeRequest', 'code,geom'")
		sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:anmn_regions', 'au.org.emii.search.index.FeatureTypeRequest', 'ANMNCODE,geom'")
		sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:argo_float', 'au.org.emii.search.index.FeatureTypeRequest', 'uuid,last_location'")
		sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:soop_xbt', 'au.org.emii.search.index.DiskCachingFeatureTypeRequest', 'id,geometry'")
		sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:ctd_profile_vw', 'au.org.emii.search.index.DiskCachingFeatureTypeRequest', 'profile_id,geom'")
		sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:radar_stations', 'au.org.emii.search.index.FeatureTypeRequest', 'platform_code,geom'")
		sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:satellite', 'au.org.emii.search.index.FeatureTypeRequest', 'station,the_geom'")
		sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:sots', 'au.org.emii.search.index.FeatureTypeRequest', 'pkid,geom'")
		sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:auv', 'au.org.emii.search.index.IdAsAttributeFeatureTypeRequest', 'geom'")
	}
	
	changeSet(author: "tfotak", id: "1320790055000-1") {
		sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:soop_sst', 'au.org.emii.search.index.FeatureTypeRequest', 'id,geometry'")
		sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:soop_asf_recent', 'au.org.emii.search.index.FeatureTypeRequest', 'id,geometry'")
	}
	
	changeSet(author: "tfotak", id: "1321934760000-1") {
		sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:soop_tmv_recent', 'au.org.emii.search.index.FeatureTypeRequest', 'id,geometry'")
	}
	
	changeSet(author: "tfotak", id: "1322547045000-1") {
		sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:faimms_sensors', 'au.org.emii.search.index.NullFeatureTypeRequest', null")
	}
	
	changeSet(author: "tfotak", id: "1322802892000-1") {
		sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'aims:AIMS_TRIP_', 'au.org.emii.search.index.AimsFeatureTypeRequest', 'track_id,shape'")
	}
	
	changeSet(author: "tfotak", id: "1323838488000-1") {
		sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:soop_ba_mv', 'au.org.emii.search.index.FeatureTypeRequest', 'id,geometry'")
		sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:asfs', 'au.org.emii.search.index.FeatureTypeRequest', 'pkid,geom'")
		sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:faimms_vw', 'au.org.emii.search.index.IdAsAttributeFeatureTypeRequest', 'geom'")
	}
}
