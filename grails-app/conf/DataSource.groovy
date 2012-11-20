dataSource {
    pooled = true
    driverClassName = "org.postgresql.Driver"
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
	dialect = "org.hibernatespatial.postgis.PostgisDialect"
}
// environment specific settings
environments {
    development {
        dataSource {
            //dbCreate = "update" // one of 'create', 'create-drop','update'
			url = "jdbc:postgresql://localhost:5432/spatialsearch_dev"
			username = "postgres"
			password = "postgres"
        }
    }
    test {
        dataSource {
            dbCreate = "create-drop"
            url = "jdbc:hsqldb:mem:testDb"
        }
    }
    production {
        dataSource {
            jndiName = "java:comp/env/jdbc/spatialsearch"
        }
    }
}
