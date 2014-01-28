
/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */

databaseChangeLog = {

    changeSet(author: "tfotak", id: "1318893850589-1", failOnError: true) {
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

    changeSet(author: "tfotak", id: "1318893850589-2", failOnError: true) {
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

    changeSet(author: "tfotak", id: "1318893850589-3", failOnError: true) {
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

    changeSet(author: "tfotak", id: "1318893850589-4", failOnError: true) {
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

    changeSet(author: "tfotak", id: "1318893850589-6", failOnError: true) {
        createSequence(sequenceName: "hibernate_sequence")
    }

    changeSet(author: "tfotak", id: "1319582037000-2", failOnError: true) {
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

    changeSet(author: "tfotak", id: "1320790055000-1", failOnError: true) {
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:soop_sst', 'au.org.emii.search.index.FeatureTypeRequest', 'id,geometry'")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:soop_asf_recent', 'au.org.emii.search.index.FeatureTypeRequest', 'id,geometry'")
    }

    changeSet(author: "tfotak", id: "1321934760000-1", failOnError: true) {
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:soop_tmv_recent', 'au.org.emii.search.index.FeatureTypeRequest', 'id,geometry'")
    }

    changeSet(author: "tfotak", id: "1322547045000-1", failOnError: true) {
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:faimms_sensors', 'au.org.emii.search.index.NullFeatureTypeRequest', null")
    }

    changeSet(author: "tfotak", id: "1322802892000-1", failOnError: true) {
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'aims:AIMS_TRIP_', 'au.org.emii.search.index.AimsFeatureTypeRequest', 'track_id,shape'")
    }

    changeSet(author: "tfotak", id: "1323838488000-1", failOnError: true) {
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:soop_ba_mv', 'au.org.emii.search.index.FeatureTypeRequest', 'id,geometry'")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:asfs', 'au.org.emii.search.index.FeatureTypeRequest', 'pkid,geom'")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:faimms_vw', 'au.org.emii.search.index.IdAsAttributeFeatureTypeRequest', 'geom'")
    }

    changeSet(author: "tfotak", id: "1324506034000-1", failOnError: true) {
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:deepwater_arrays', 'au.org.emii.search.index.FeatureTypeRequest', 'pkid,geom'")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:zooview', 'au.org.emii.search.index.IdAsAttributeFeatureTypeRequest', 'position'")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:phytoview', 'au.org.emii.search.index.IdAsAttributeFeatureTypeRequest', 'position'")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:srs_altimetry_deployments', 'au.org.emii.search.index.IdAsAttributeFeatureTypeRequest', 'geom'")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:pciview', 'au.org.emii.search.index.IdAsAttributeFeatureTypeRequest', 'position'")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:BioOptical_deployments', 'au.org.emii.search.index.IdAsAttributeFeatureTypeRequest', 'geom'")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'aims:WeatherStation', 'au.org.emii.search.index.FeatureTypeRequest', 'OBJECTID,SHAPE'")
    }

    changeSet(author: "tfotak", id: "1326755933000-1", failOnError: true) {
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'bioreg:CAAB', 'au.org.emii.search.index.CmarBioregFeatureTypeRequest', 'SPCODE,GEOMETRY'")
    }

    changeSet(author: "tfotak", id: "1326930435000-1", failOnError: true) {
        dropColumn(columnName: "documents", tableName: "index_run")
        dropColumn(columnName: "failures", tableName: "index_run")
    }

    changeSet(author: "tfotak", id: "1326930435000-2", failOnError: true) {
        sql("update feature_type_request_class set class_name = 'au.org.emii.search.index.FeatureTypeRequest' where class_name = 'au.org.emii.search.index.AimsFeatureTypeRequest'")
        sql("update feature_type_request_class set class_name = 'au.org.emii.search.index.FeatureTypeRequest' where class_name = 'au.org.emii.search.index.CmarBioregFeatureTypeRequest'")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'aims:MantaTowReefs', 'au.org.emii.search.index.FeatureTypeRequest', 'fullreef_id,shape'")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'ea:GBR_AIMS_WQ-summary', 'au.org.emii.search.index.Gml2FeatureTypeRequest', 'Id,the_geom'")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'ea:Secchi-Secchi-m', 'au.org.emii.search.index.NullFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'ea:Chlorophyll-Chlorophyll-micro_grams_per_litre', 'au.org.emii.search.index.NullFeatureTypeRequest', null")
    }

    changeSet(author: "tfotak", id: "1326930435000-3", failOnError: true) {
        addColumn(tableName: "feature_type_request_class") {
            column(name: "feature_members_element_name", type: "varchar(255)")
        }
        sql("update feature_type_request_class set feature_members_element_name = 'featureMember' where feature_type_name = 'bioreg:CAAB'")
    }

    changeSet(author: "tfotak", id: "1327357239000-3", failOnError: true) {
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'ran:xbt', 'au.org.emii.search.index.NullFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'ran:secchi', 'au.org.emii.search.index.DiskCachingFeatureTypeRequest', '@id,SHAPE'")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'ran:biolum', 'au.org.emii.search.index.DiskCachingFeatureTypeRequest', '@id,SHAPE'")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'ran:seabed', 'au.org.emii.search.index.DiskCachingFeatureTypeRequest', '@id,SHAPE'")
        sql("update feature_type_request_class set class_name = 'au.org.emii.search.index.FeatureTypeRequest', constructor_args = '@id,' || constructor_args where class_name = 'au.org.emii.search.index.IdAsAttributeFeatureTypeRequest'")

    }

    changeSet(author: "tfotak", id: "1333080327000-1", failOnError: true) {
        sql("insert into feature_type_request_class (id, version, class_name, feature_type_name, constructor_args, feature_members_element_name) select nextval('hibernate_sequence'), 0, class_name, 'imos'||substring(feature_type_name from position(':' in feature_type_name)), constructor_args, feature_members_element_name from feature_type_request_class where feature_type_name like 'topp:%'")
    }

    changeSet(author: "craigj", id: "1333080327000-2", failOnError: true) {
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'imos:argo_aggregation', 'au.org.emii.search.index.FeatureTypeRequest', 'uuid,geometry'")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'imos:ANMN_SOOC', 'au.org.emii.search.index.FeatureTypeRequest', 'METADATA,the_geom'")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'topp:anmn_nrs_realtime', 'au.org.emii.search.index.FeatureTypeRequest', 'metadata_uuid,geom'")
    }

    changeSet(author: "craigj", id: "1333080327000-3", failOnError: true) {
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'imos:anmn_nrs_realtime', 'au.org.emii.search.index.FeatureTypeRequest', 'metadata_uuid,geom'")
    }

    changeSet(author: "craigj", id: "1333080327000-4", failOnError: true) {
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, '2001_ABOM-L3P_GHRSST-SSTsubskin-AVHRR_MOSAIC_01km-AO_DAAC-v01-fv01_0', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, '2002_ABOM-L3P_GHRSST-SSTsubskin-AVHRR_MOSAIC_01km-AO_DAAC-v01-fv01_0', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, '2003_ABOM-L3P_GHRSST-SSTsubskin-AVHRR_MOSAIC_01km-AO_DAAC-v01-fv01_0', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, '2004_ABOM-L3P_GHRSST-SSTsubskin-AVHRR_MOSAIC_01km-AO_DAAC-v01-fv01_0', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, '2005_ABOM-L3P_GHRSST-SSTsubskin-AVHRR_MOSAIC_01km-AO_DAAC-v01-fv01_0', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, '2007_ABOM-L3P_GHRSST-SSTsubskin-AVHRR_MOSAIC_01km-AO_DAAC-v01-fv01_0', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, '2008_ABOM-L3P_GHRSST-SSTsubskin-AVHRR_MOSAIC_01km-AO_DAAC-v01-fv01_0', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, '2008-L4_RAMSSA', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, '2009_ABOM-L3P_GHRSST-SSTsubskin-AVHRR_MOSAIC_01km-AO_DAAC-v01-fv01_0', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, '2009-L3C_GHRSST-SSTskin-AVHRR19_D-1d_day', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, '2009-L4_RAMSSA', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, '2010_ABOM-L3P_GHRSST-SSTsubskin-AVHRR_MOSAIC_01km-AO_DAAC-v01-fv010', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, '2010-L3C_GHRSST-SSTskin-AVHRR19_D-1d_day', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, '2010-L4_ODYSSEA_GBR', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, '2010-L4_RAMSSA', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, '2011_ABOM-L3P_GHRSST-SSTsubskin-AVHRR_MOSAIC_01km-AO_DAAC-v01-fv010', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, '2011-L3C_GHRSST-SSTskin-AVHRR19_D-1d_day', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, '2012_ABOM-L3P_GHRSST-SSTsubskin-AVHRR_MOSAIC_01km-AO_DAAC-v01-fv010', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, '2012-L3C_GHRSST-SSTskin-AVHRR19_D-1d', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'ACORN_', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'CARS_', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'CARS/', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'CARS_', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'GHRSST-SSTsubskin', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'INFORMD_', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'L3C_SSTskin_1d_', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'L3P_SSTsubskin_', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'L4_RAMSSA_', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'ocean_east_aus_temp', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'RIBBON/', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'satellite_data_sst', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'setas_', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'shed.', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
        sql("insert into feature_type_request_class (id, version, feature_type_name, class_name, constructor_args) select nextval('hibernate_sequence'), 0, 'storm_', 'au.org.emii.search.index.NcwmsFeatureTypeRequest', null")
    }

    changeSet(author: "tfotak", id: "1338789394000-1", failOnError: true) {
        addColumn(tableName: "feature_type_request_class") {
            column(name: "output_format", type: "varchar(10)")
        }
    }

    changeSet(author: "tfotak", id: "1338789394000-2", failOnError: true) {
        update(tableName: "feature_type_request_class") {
            column(name: "output_format", value: "gml2")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            where("class_name = 'au.org.emii.search.index.Gml2FeatureTypeRequest'")
        }
    }

    changeSet(author: "tfotak", id: "1338864976000-1", failOnError: true) {
        update(tableName: "feature_type_request_class") {
            column(name: "output_format", value: "gml2")
            where("feature_type_name like '%:soop_asf'")
        }
    }

    changeSet(author: "tfotak", id: "1338864976000-2", failOnError: true) {
        update(tableName: "feature_type_request_class") {
            column(name: "output_format", value: "gml2")
            where("feature_type_name like '%:soop_sst'")
        }
    }

    changeSet(author: "tfotak", id: "1338864976000-3", failOnError: true) {
        update(tableName: "feature_type_request_class") {
            column(name: "output_format", value: "gml2")
            where("feature_type_name like '%:ctd_profile_vw'")
        }
    }

    changeSet(author: "tfotak", id: "1338864976000-4", failOnError: true) {
        update(tableName: "feature_type_request_class") {
            column(name: "output_format", value: "gml2")
            where("feature_type_name like '%:soop_co2'")
        }
    }

    changeSet(author: "tfotak", id: "1339040995000-1", failOnError: true) {
        update(tableName: "feature_type_request_class") {
            column(name: "output_format", value: "gml2")
            where("feature_type_name like '%:soop_asf_recent'")
        }
    }

    // WA IVEC records
    changeSet(author: "tfotak", id: "1340761212000-1", failOnError: true) {

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "waodn:Dept_of_Transport-Port_Areas")
            column(name: "class_name", value: "au.org.emii.search.index.FeatureTypeRequest")
            column(name: "constructor_args", value: "@id,the_geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "waodn:Dept_of_Transport-Navigable_Water_Regulation_Areas")
            column(name: "class_name", value: "au.org.emii.search.index.FeatureTypeRequest")
            column(name: "constructor_args", value: "@id,the_geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "waodn:Dept_of_Transport-Declared_Mooring_Control_Areas")
            column(name: "class_name", value: "au.org.emii.search.index.FeatureTypeRequest")
            column(name: "constructor_args", value: "@id,the_geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "waodn:Dept_of_Transport-Navigation_Aids")
            column(name: "class_name", value: "au.org.emii.search.index.FeatureTypeRequest")
            column(name: "constructor_args", value: "@id,the_geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "waodn:Areas_under_Marine_and_Harbours_Act_1981")
            column(name: "class_name", value: "au.org.emii.search.index.NullFeatureTypeRequest")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "waodn:Dept_of_Transport-Mooring_Control_Areas")
            column(name: "class_name", value: "au.org.emii.search.index.FeatureTypeRequest")
            column(name: "constructor_args", value: "@id,the_geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "waodn:Dept_of_Transport-Hydrographic_chart_locations")
            column(name: "class_name", value: "au.org.emii.search.index.FeatureTypeRequest")
            column(name: "constructor_args", value: "@id,the_geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "waodn:SRFME_algal_biomass")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@id,the_geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "waodn:Cape_N_to_L_Beaches")
            column(name: "class_name", value: "au.org.emii.search.index.FeatureTypeRequest")
            column(name: "constructor_args", value: "@id,the_geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "topp:argo_aggregation")
            column(name: "class_name", value: "au.org.emii.search.index.FeatureTypeRequest")
            column(name: "constructor_args", value: "uuid,geometry")
        }
    }

    changeSet(author: "tfotak", id: "1341883299000-1", failOnError: true) {
        addColumn(tableName: "geonetwork_metadata") {
            column(name: "change_date", type: "timestamp")
            column(name: "last_indexed", type: "timestamp")
        }
    }

    changeSet(author: "tfotak", id: "1341883299000-2", failOnError: true) {
        dropColumn(columnName: "index_run_id", tableName: "geonetwork_metadata")
        dropTable(tableName: "index_run")
    }

    changeSet(author: "tfotak", id: "1341883299000-3", failOnError: true) {
        update(tableName: "feature_type_request_class") {
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            where("feature_type_name like '%:soop%'")
        }
    }

    changeSet(author: "tfotak", id: "1341883299000-4", failOnError: true) {
        update(tableName: "geonetwork_metadata") {
            column(name: "change_date", valueComputed: "to_date('01 Jan 1970', 'DD Mon YYYY')")
        }
    }

    changeSet(author: "tfotak", id: "1341883299000-5", failOnError: true) {
        addNotNullConstraint(tableName: "geonetwork_metadata", columnName: "change_date")
    }

    changeSet(author: "tfotak", id: "1341974714000-1", failOnError: true) {
        update(tableName: "feature_type_request_class") {
            column(name: "output_format", value: "gml2")
            where("feature_type_name like 'waodn:%'")
        }
    }

    changeSet(author: "tfotak", id: "1341974714000-2", failOnError: true) {
        sql("update feature_type_request_class set constructor_args = replace(constructor_args, '@id', '@fid') where feature_type_name like 'waodn:%'")
    }

    changeSet(author: "tfotak", id: "1341974714000-3", failOnError: true) {
        sql("update feature_type_request_class set constructor_args = replace(constructor_args, '@id', '@gml:id') where constructor_args like '@id%' and class_name = 'au.org.emii.search.index.DiskCachingFeatureTypeRequest'")
    }

    changeSet(author: "tfotak", id: "1342073293000-1", failOnError: true) {
        sql("SELECT AddGeometryColumn('geonetwork_metadata', 'geo_box', 4326, 'GEOMETRY', 2)")
    }

    changeSet(author: "tfotak", id: "1342575535000-1", failOnError: true) {
        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "waodn:RI_CoastalGeomorphology")
            column(name: "class_name", value: "au.org.emii.search.index.FeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,the_geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "waodn:RI_Beaches")
            column(name: "class_name", value: "au.org.emii.search.index.FeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,the_geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "waodn:L_to_K_Beaches")
            column(name: "class_name", value: "au.org.emii.search.index.FeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,the_geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "waodn:L_to_K_CoastalGeomorphology")
            column(name: "class_name", value: "au.org.emii.search.index.FeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,the_geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "waodn:L_to_K_AerialPhotographs")
            column(name: "class_name", value: "au.org.emii.search.index.FeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,the_geom")
        }
    }

    changeSet(author: "dnahodil", id: "1381297645000-1", failOnError: true) {
        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:argo_profile_general")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,position")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:aatams_sattag_nrt_trajectories")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "SRS_SST_14subskin/sst_dtime")
            column(name: "class_name", value: "au.org.emii.search.index.NcwmsFeatureTypeRequest")
            column(name: "constructor_args", value: null)
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "SRS_SST_14subskin/sea_surface_temperature")
            column(name: "class_name", value: "au.org.emii.search.index.NcwmsFeatureTypeRequest")
            column(name: "constructor_args", value: null)
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:csiro_harvest_zoop")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,position")
        }
    }

    changeSet(author: "tfotak", id: "1385350630000-1", failOnError: true) {
        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:csiro_harvest_pci")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,position")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:xbt_realtime_recent")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "uuid,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:xbt_realtime")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:csiro_harvest_phyto")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,position")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "ACORN_ROT_QC/sea_water_velocity")
            column(name: "class_name", value: "au.org.emii.search.index.NcwmsFeatureTypeRequest")
            column(name: "constructor_args", value: null)
        }
    }

    changeSet(author: "dnahodil", id: "1390448364000-1", failOnError: true) {

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:argo_profile_layer_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,position")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:anmn_burst_avg_timeseries_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:t_id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:abos_sofs_surfaceprop_rt_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:file_id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:aatams_sattag_nrt_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:profile_id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:abos_sofs_surfaceprop_dm_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:file_id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:aatams_biologging_shearwater_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:ref,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:anmn_velocity_timeseries_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:timeseries_id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:anmn_ts_timeseries_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:timeseries_id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:aatams_biologging_penguin_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:pttid,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:installation_summary")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:installation_id,location")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:faimms_timeseries_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:channel_id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:soop_ba_trajectory_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:file_id,geom")
        }
    }
}
