package com.uci.dao.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractReactiveCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.DropKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;

import java.util.Collections;
import java.util.List;

/**
 * @author chakshu
 */
@Configuration
@EnableReactiveCassandraRepositories(basePackages = "com.uci.dao")
public class CassandraConfig extends AbstractReactiveCassandraConfiguration {

    @Value("${cassandra.contactpoints}")
    private String contactPoints;

    @Value("${cassandra.keyspace}")
    private String keyspace;

    @Value("${cassandra.port}")
    private int port;

    @Override
    protected String getKeyspaceName() {
        return keyspace;
    }

    @Override
    protected String getContactPoints() {
        return contactPoints;
    }

    @Override
    protected int getPort() {
        return port;
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.RECREATE;
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[]{"com.uci.dao"};
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        final CreateKeyspaceSpecification specification =
                CreateKeyspaceSpecification.createKeyspace(keyspace)
                        .ifNotExists()
                        .with(KeyspaceOption.DURABLE_WRITES, true)
                        .withSimpleReplication();
        return Collections.singletonList(specification);
    }

    @Override
    protected List<DropKeyspaceSpecification> getKeyspaceDrops() {
        return Collections.singletonList(DropKeyspaceSpecification.dropKeyspace(keyspace));
    }

    @Override
    protected List<String> getStartupScripts() {


        return Collections.singletonList("CREATE TABLE IF NOT EXISTS " +
                keyspace + ".XMessage(id bigint," +
                "userId text, " +
                "fromId text, " +
                "channel text, " +
                "provider text, " +
                "timestamp timestamp, " +
                "messageState text, " +
                "xMessage text, " +
                "app text, " +
                "auxData text, " +
                "messageId text, " +
                "replyId text, " +
                "causeId text, " +
                "PRIMARY KEY (id) " +
                ") WITH default_time_to_live = 600;");
    }
}
