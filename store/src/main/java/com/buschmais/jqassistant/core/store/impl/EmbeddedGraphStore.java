package com.buschmais.jqassistant.core.store.impl;

import java.util.Properties;

import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.core.store.api.StoreConfiguration;
import com.buschmais.jqassistant.core.store.spi.StorePluginRepository;
import com.buschmais.jqassistant.neo4j.embedded.EmbeddedNeo4jConfiguration;
import com.buschmais.jqassistant.neo4j.embedded.EmbeddedNeo4jServer;
import com.buschmais.jqassistant.neo4j.embedded.EmbeddedNeo4jServerFactory;
import com.buschmais.jqassistant.neo4j.embedded.neo4jv4.Neo4jV4ServerFactory;
import com.buschmais.xo.api.XOManagerFactory;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.buschmais.xo.neo4j.embedded.api.EmbeddedNeo4jXOProvider;
import com.buschmais.xo.neo4j.embedded.impl.datastore.EmbeddedNeo4jDatastore;

import org.neo4j.dbms.api.DatabaseManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link Store} implementation using an embedded Neo4j instance.
 */
public class EmbeddedGraphStore extends AbstractGraphStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmbeddedGraphStore.class);

    private static final int AUTOCOMMIT_THRESHOLD = 32678;

    private EmbeddedNeo4jServerFactory serverFactory;

    private EmbeddedNeo4jConfiguration embeddedNeo4jConfiguration;

    /**
     * Constructor.
     *
     * @param configuration
     *            The configuration.
     */
    public EmbeddedGraphStore(StoreConfiguration configuration, StorePluginRepository storePluginRepository) {
        super(configuration, storePluginRepository);
        this.serverFactory = new Neo4jV4ServerFactory();
    }

    @Override
    protected XOUnit configure(XOUnit.XOUnitBuilder builder, StoreConfiguration storeConfiguration) {
        this.embeddedNeo4jConfiguration = storeConfiguration.getEmbedded();
        // Determine store specific default properties
        Properties properties = serverFactory.getProperties(this.embeddedNeo4jConfiguration);
        // Add/overwrite with user properties
        properties.putAll(storeConfiguration.getProperties());
        builder.properties(properties);
        builder.provider(EmbeddedNeo4jXOProvider.class);
        return builder.build();
    }

    @Override
    protected void initialize(XOManagerFactory xoManagerFactory) {
        EmbeddedNeo4jServer server = serverFactory.getServer();
        EmbeddedNeo4jDatastore embeddedNeo4jDatastore = xoManagerFactory.getDatastore(EmbeddedNeo4jDatastore.class);
        DatabaseManagementService managementService = embeddedNeo4jDatastore.getManagementService();
        LOGGER.info("Initializing embedded Neo4j server " + server.getVersion());
        server.initialize(managementService, embeddedNeo4jConfiguration, storePluginRepository.getProcedureTypes(), storePluginRepository.getFunctionTypes());
    }

    @Override
    protected int getAutocommitThreshold() {
        return AUTOCOMMIT_THRESHOLD;
    }

}
