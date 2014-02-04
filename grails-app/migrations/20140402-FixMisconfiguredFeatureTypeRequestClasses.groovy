/*
 * Copyright 2014 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */

databaseChangeLog = {
    
    changeSet(author: "dnahodil", id: "1391053813000-1", failOnError: true) {

        update(tableName: "feature_type_request_class") {
            column(name: "constructor_args", valueComputed: "replace(constructor_args, '@gml:', '')")
            where("""feature_type_name in (
                'imos:anmn_burst_avg_timeseries_map',
                'imos:abos_sofs_surfaceprop_rt_map',
                'imos:aatams_sattag_nrt_map',
                'imos:abos_sofs_surfaceprop_dm_map',
                'imos:aatams_biologging_shearwater_map',
                'imos:anmn_velocity_timeseries_map',
                'imos:anmn_ts_timeseries_map',
                'imos:aatams_biologging_penguin_map',
                'imos:installation_summary',
                'imos:faimms_timeseries_map',
                'imos:soop_ba_trajectory_map')"""
            )
        }
    }

    changeSet(author: "dnahodil", id: "1391053813000-2", failOnError: true) {

        update(tableName: "feature_type_request_class") {
            column(name: "class_name", value: "au.org.emii.search.index.DiskCachingFeatureTypeRequest")
            where("class_name LIKE 'au.org.emii.search.index.FeatureTypeRequest'")
        }
    }
}