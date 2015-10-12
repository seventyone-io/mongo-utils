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

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;

import io.seventyone.mongoutils.annotations.MongoIndex;

public class MongoServiceImplementation implements AutoCloseable, MongoService {

    private final MongoDatabase db;
    private final MongoClient   mongoClient;

    public MongoServiceImplementation(String host, int port, String dbName, String user, String password) {

        ServerAddress serverAddress = new ServerAddress(host, port);
        if (StringUtils.isBlank(user)) {
            this.mongoClient = new MongoClient(serverAddress);
        }
        else {
            MongoCredential credential = MongoCredential.createCredential(user, dbName, password.toCharArray());
            this.mongoClient = new MongoClient(serverAddress, Collections.singletonList(credential));
        }

        this.db = this.mongoClient.getDatabase(dbName);
    }

    public MongoServiceImplementation(String dbName, String user, String password) {
        this(MongoService.DEFAULT_HOST, MongoService.DEFAULT_PORT, dbName, user, password);
    }

    public MongoServiceImplementation(String dbName) {
        this(MongoService.DEFAULT_HOST, MongoService.DEFAULT_PORT, dbName, null, null);
    }

    @Override
    public MongoCollection<Document> getCollection(String collectionName) {

        if (StringUtils.isBlank(collectionName)) {
            return null;
        }

        return this.db.getCollection(collectionName);
    }

    @Override
    public MongoCollection<Document> getCollection(Class<?> entityClass) {
        io.seventyone.mongoutils.annotations.MongoCollection annotation;
        annotation = entityClass.getAnnotation(io.seventyone.mongoutils.annotations.MongoCollection.class);

        if (annotation == null) {
            String message =
                String.format("Annotation '@MongoCollection' not present on class '%s'", entityClass.getSimpleName());
            throw new UnsupportedOperationException(message);
        }
        String collectionName = annotation.value();
        if (StringUtils.isBlank(collectionName)) {
            collectionName = entityClass.getSimpleName();
        }
        return this.getCollection(annotation.value());
    }

    @Override
    public void setupCollection(Class<?> entityClass) {

        if (entityClass == null) {
            return;
        }

        this.setupIndexes(entityClass);
    }

    @Override
    public void setupCollection(Class<?> entityClass, String collectionName) {

        if (entityClass == null || StringUtils.isBlank(collectionName)) {
            return;
        }

        this.setupIndexes(entityClass, collectionName);
    }

    @Override
    public void setupIndexes(Class<?> entityClass) {

        if (entityClass == null) {
            return;
        }

        internalSetupIndexes(entityClass, this.getCollection(entityClass));
    }

    @Override
    public void setupIndexes(Class<?> entityClass, String collectionName) {

        if (entityClass == null || StringUtils.isBlank(collectionName)) {
            return;
        }

        MongoCollection<Document> collection = this.getCollection(collectionName);
        internalSetupIndexes(entityClass, collection);
    }

    private void internalSetupIndexes(Class<?> entityClass, MongoCollection<Document> collection) {

        if (entityClass == null || collection == null) {
            return;
        }

        MongoIndex[] indexes = entityClass.getAnnotationsByType(MongoIndex.class);
        if (indexes != null && indexes.length > 0) {
            for (MongoIndex index : indexes) {
                Document indexDocument = new Document();
                indexDocument.put(index.key(), index.direction());

                IndexOptions options = new IndexOptions();
                options.unique(index.unique());
                options.background(index.background());
                collection.createIndex(indexDocument, options);
            }
        }
    }

    @Override
    public void autoSetup(String packageName) {

        List<ClassLoader> classLoadersList = Lists.newArrayList();
        ClassLoader contextClassLoader = ClasspathHelper.contextClassLoader();
        classLoadersList.add(contextClassLoader);

        ClassLoader staticClassLoader = ClasspathHelper.staticClassLoader();
        if (staticClassLoader != contextClassLoader) {
            classLoadersList.add(staticClassLoader);
        }

        // formatter: off
        ConfigurationBuilder configuration = new ConfigurationBuilder()
            .setScanners(new SubTypesScanner(false), new TypeAnnotationsScanner())
            .addClassLoaders(classLoadersList)
            .setUrls(ClasspathHelper.forPackage(packageName));
        // formatter: on
        Reflections reflections = new Reflections(configuration);

        Set<Class<?>> collections =
            reflections.getTypesAnnotatedWith(io.seventyone.mongoutils.annotations.MongoCollection.class);
        collections.forEach(c -> {
            io.seventyone.mongoutils.annotations.MongoCollection annotation;
            annotation = c.getAnnotation(io.seventyone.mongoutils.annotations.MongoCollection.class);
            if (annotation.noAutoSetup() == false) {
                this.setupCollection(c, annotation.value());
            }
        });
    }

    @Override
    public void close() throws Exception {
        this.mongoClient.close();
    }

}
