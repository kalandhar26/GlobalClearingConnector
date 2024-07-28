package com.jpm.gcc.e2e.embedded.hsqldb;

import com.jpm.gcc.e2e.embedded.EmbeddedServer;
import com.jpm.gcc.e2e.embedded.ServerException;
import com.jpm.gcc.e2e.util.ResourceUtil;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import org.hsqldb.Server;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class HsqlDbServer implements EmbeddedServer {

    private Server server;

    private String jdbcUrlTemplate = "jdbc:hsqldb:hsql://localhost:%d/testdb;sql.syntax_ora=true;user=sa";
    @Override
    public String getServerName() {
        return "Embedded HSQL DB Server";
    }

    @Override
    public void startup() throws ServerException, IOException {
        final int hsqlPort = 3340;
        final String jdbcUrl = String.format(jdbcUrlTemplate, hsqlPort);
        System.setProperty("embedded.database.url",jdbcUrl);
        server = new Server();
        server.setDatabaseName(0,"testdb");
        server.setDatabasePath(0,"mem:testdb");
        server.setPort(hsqlPort);
        server.setSilent(true);
        server.setTrace(false);
        server.start();

        initialDatabase(jdbcUrl);
        initialProperties(jdbcUrl);
        Runtime.getRuntime().addShutdownHook(new Thread(()-> shutdown()));
    }

    @Override
    public void shutdown(){
        log.info("Stopping {}...", getServerName());
        server.stop();
        server.shutdown();
        log.info("Stopped {} ...",getServerName());
    }

    private void initialDatabase(String jdbcUrl) throws ServerException {
        try{
            Class.forName("org.hsqldb.jdbcDriver");
        }catch(ClassNotFoundException e){
            log.error("Error in finding the org.hsqldb.jdbcDriver class");
        }

        try(Connection conn = DriverManager.getConnection(jdbcUrl)){
            Statement statement = conn.createStatement();
            String initSqlDir = ResourceUtil.replacePlaceHolders("${project.dir.root}/E2E/src/test/resources/database/hsqldb", System.getProperties());
            List<String> sqlFiles = Files.walk(Paths.get(initSqlDir)).filter(Files::isRegularFile).map(Path::toString).sorted().collect(Collectors.toList());
            for(String sqlFile : sqlFiles){
                log.info("Loading HsqlDB initial SQL {}", sqlFile);
                statement.execute(ResourceUtil.loadTextResource(sqlFile));
            }

            String e2eTestFolder = new ClassPathResource("db/../").getFile().getAbsolutePath().replaceAll("\\\\","/");
            String appFileFolder = e2eTestFolder.replaceAll("E2E/target/test-classes","/ORCHESTRATION/target/classes");

            this.loadLiquibaseChangelog(conn, new File(appFileFolder+"/db/changelogs/gcc-irctt-dml-e2e.xml"));
        } catch (Exception e) {
            this.shutdown();
            throw new ServerException("Initial Exception: ",e);
        }
    }
    private void loadLiquibaseChangelog(Connection connection, File chnageLogXml) throws IOException, LiquibaseException {
        ResourceAccessor resourceAccessor = new FileSystemResourceAccessor();
        JdbcConnection jdbcConnection = new JdbcConnection(connection);
        Liquibase liquibase = new Liquibase(chnageLogXml.getAbsolutePath(), resourceAccessor, jdbcConnection);
        liquibase.update("");
    }

    private void initialProperties(String jdbcUrl){
        System.setProperty("local.db.jdbc.url", jdbcUrl);
        System.setProperty("local.db.jdbc.drv","org.hsqldb.jdbc.JDBCDriver");
        System.setProperty("local.db.jdbc.usr","sa");
        System.setProperty("local.db.jdbc.pwd","");
        System.setProperty("local.db.jdbc.scm","PUBLIC");
        log.info("Embedded HsqlDB is running at {}",jdbcUrl);
    }
}
