
/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
package au.org.emii.search.index

import org.springframework.jdbc.core.BatchPreparedStatementSetter
import java.sql.PreparedStatement
import org.slf4j.LoggerFactory

class FeatureTypeIndexJdbcBatchStatementSetter implements BatchPreparedStatementSetter {

    static final log = LoggerFactory.getLogger(FeatureTypeIndexJdbcBatchStatementSetter.class)

    def features
    def updating

    FeatureTypeIndexJdbcBatchStatementSetter(features, updating) {
        this.features = features
        this.updating = updating
    }

    @Override
    void setValues(PreparedStatement ps, int i) {
        def feature = features[i]
        log.debug(feature.toString())

        int j = 1
        ps.setString(j++, feature.featureTypeId)
        ps.setString(j++, feature.featureTypeName)
        ps.setString(j++, feature.geonetworkUuid)
        ps.setString(j++, feature.gml ?: feature.geometry.toText())
        if (updating) {
            ps.setLong(j++, feature.id)
        }
    }

    @Override
    int getBatchSize() {
        return features.size()
    }
}
