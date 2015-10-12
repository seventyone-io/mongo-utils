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
package io.seventyone.mongoutils;

import org.bson.Document;

import com.mongodb.client.MongoCollection;


/**
 * Convience wrapper for setting up / accessing a database.
 */
public interface MongoService {

    static final String DEFAULT_HOST = "localhost";
    static final int    DEFAULT_PORT = 27017;

    /**
     * Get collection by name.
     *
     * @param collectionName the collection name
     * @return the collection
     */
    MongoCollection<Document> getCollection(String collectionName);

    /**
     * Get collection by {@link io.seventyone.mongoutils.annotation.MongoCollection} annotation.
     *
     * @param entityClass the entity class
     * @return the collection
     */
    MongoCollection<Document> getCollection(Class<?> entityClass);

    /**
     * Setup a collection according to its {@link io.seventyone.mongoutils.annotation.MongoCollection} and
     * {@link io.seventyone.mongoutils.annotation.MongoIndex} annotations.
     *
     * @param entityClass the new up collection
     */
    void setupCollection(Class<?> entityClass);

    /**
     * Setup a collection by name and its {@link io.seventyone.mongoutils.annotation.MongoIndex} annotations.
     *
     * @param entityClass the entity class
     * @param collectionName the collection name
     */
    void setupCollection(Class<?> entityClass, String collectionName);

    /**
     * Setup the indexes of a collection by {@link io.seventyone.mongoutils.annotation.MongoCollection} annotation and
     * its {@link io.seventyone.mongoutils.annotation.MongoIndex} annotations.
     *
     * @param entityClass the new up indexes
     */
    void setupIndexes(Class<?> entityClass);

    /**
     * Setup the indexes of a collection by name and its {@link io.seventyone.mongoutils.annotation.MongoIndex}
     * annotations.
     *
     * @param entityClass the entity class
     * @param collectionName the collection name
     */
    void setupIndexes(Class<?> entityClass, String collectionName);

    /**
     * Scan provided package for {@link io.seventyone.mongoutils.annotation.MongoCollection} annotations and setup the
     * collections accordingly.
     *
     * @param packageName the package name
     */
    void autoSetup(String packageName);

}