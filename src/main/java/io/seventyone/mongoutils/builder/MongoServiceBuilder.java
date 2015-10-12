/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Copyright (C) 2015 SEVENTYONE.io - Benjamin Weidig <github+mongo-utils@seventyone.io>
 */
/**
 *
 */
package io.seventyone.mongoutils.builder;

import org.apache.commons.lang3.StringUtils;

import io.seventyone.mongoutils.MongoService;
import io.seventyone.mongoutils.MongoServiceImplementation;

/**
 * Convenience builder for {@link io.seventyone.mongoutils.MongoService} interface.
 */
public class MongoServiceBuilder {

    private String host = MongoService.DEFAULT_HOST;
    private int    port = MongoService.DEFAULT_PORT;
    private String dbName;
    private String user;
    private String password;

    /**
     * Returns a new MongoServiceBuilder.
     */
    public static MongoServiceBuilder start() {
        return new MongoServiceBuilder();
    }

    /**
     * Creates a new MongoService instance based on the previous configuration.
     */
    public MongoService build() {
        return new MongoServiceImplementation(this.host, this.port, this.dbName, this.user, this.password);
    }

    /**
     * Sets the host of the MongoDB server.
     * <p>
     * Default: localhost
     */
    public MongoServiceBuilder host(String host) {

        if (StringUtils.isBlank(host)) {
            throw new IllegalArgumentException("Host can't be blank");
        }

        this.host = host;

        return this;
    }

    /**
     * Sets the port of the MongoDB server.
     * <p>
     * Default: 27017
     */
    public MongoServiceBuilder port(int port) {

        if (port < 1 || port > 65536) {
            String msg = String.format("Port can't be '%d', must be in the range 1-65536", port);
            throw new IllegalArgumentException(msg);
        }

        this.port = port;

        return this;
    }

    /**
     * Sets host and port of the MongoDB server.
     * <p>
     * Default: localhost / 27017
     */
    public MongoServiceBuilder host(String host, int port) {

        host(host);
        port(port);

        return this;
    }

    /**
     * Sets the database name (required).
     */
    public MongoServiceBuilder database(String dbName) {

        if (StringUtils.isBlank(dbName)) {
            throw new IllegalArgumentException("Database can't be blank");
        }

        this.dbName = dbName;

        return this;
    }

    /**
     * Sets the credentials for the database (optional).
     */
    public MongoServiceBuilder credential(String user, String password) {

        this.user = user;
        this.password = password;

        return this;
    }

}
