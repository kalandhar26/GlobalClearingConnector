package com.jpm.gcc.e2e.embedded;

import java.io.IOException;


public interface EmbeddedServer {

    String getServerName();

    void startup() throws ServerException, IOException;

    void shutdown() throws ServerException;
}
