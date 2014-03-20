/*
 * Copyright 2014 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */

databaseChangeLog = {

    changeSet(author: "tfotak", id: "1395026936000-1", failOnError: true) {

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:soop_trv_trajectory_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:anfog_rt_trajectory_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:srs_oc_soop_rad_trajectory_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:anfog_dm_trajectory_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:abos_ts_timeseries_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:soop_xbt_dm_profile_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:auv_trajectory_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:abos_sofs_surfaceflux_rt_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:soop_tmv_nrt_trajectory_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:soop_xbt_nrt_profiles_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:soop_tmv_trajectory_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:anmn_nrs_rt_bio_timeseries_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:soop_co2_trajectory_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:anmn_nrs_rt_meteo_timeseries_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:soop_sst_dm_trajectory_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:anmn_nrs_yon_dar_timeseries_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:csiro_harvest_nrs_zooplankton_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,position")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:anmn_am_nrt_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:srs_altimetry_timeseries_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:soop_sst_nrt_trajectory_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:soop_asf_mft_trajectory_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:anmn_nrs_rt_wave_timeseries_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:csiro_harvest_nrs_biomass_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,position")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "imos:abos_sofs_surfaceflux_dm_map")
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            column(name: "constructor_args", value: "@gml:id,geom")
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "GSLA_")
            column(name: "class_name", value: "au.org.emii.search.index.NcwmsFeatureTypeRequest")
            column(name: "constructor_args", value: null)
        }

        insert(tableName: "feature_type_request_class") {
            column(name: "id", valueComputed: "nextval('hibernate_sequence')")
            column(name: "version", valueNumeric: "0")
            column(name: "feature_type_name", value: "SRS_SST_L3P_14subskin/sea_surface_temperature")
            column(name: "class_name", value: "au.org.emii.search.index.NcwmsFeatureTypeRequest")
            column(name: "constructor_args", value: null)
        }
    }
}