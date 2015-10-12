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

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.bson.Document;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Lists;
import com.mongodb.client.MongoIterable;

public class MongoConverterImplementation implements MongoConverter {

    private ObjectMapper objectMapper;

    @Override
    public void provideObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void configure(SerializationFeature feature, boolean state) {
        getObjectMapper().configure(feature, state);
    }

    @Override
    public void configure(DeserializationFeature feature, boolean state) {
        getObjectMapper().configure(feature, state);
    }

    @Override
    public void configure(MapperFeature feature, boolean state) {
        getObjectMapper().configure(feature, state);
    }

    @Override
    public <T> T entityFrom(Document document, Class<T> entityClass) {

        if (document == null || entityClass == null) {
            return null;
        }

        T entity = null;
        try {
            entity = getObjectMapper().convertValue(document, entityClass);
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return entity;
    }

    @Override
    public <T> List<T> entitiesFrom(Iterable<Document> iterable, Class<T> entityClass) {

        if (iterable == null || entityClass == null) {
            return null;
        }

        List<T> entities = Lists.newArrayList();

        Consumer<Document> consumer = (Document document) -> {
            T entity = this.entityFrom(document, entityClass);
            if (entity != null) {
                entities.add(entity);
            }
        };

        iterable.forEach(consumer);

        return entities;
    }

    @Override
    public <T> T firstEntityFrom(MongoIterable<Document> iterable, Class<T> entityClass) {

        if (iterable == null || entityClass == null) {
            return null;
        }

        Document document = iterable.first();

        return entityFrom(document, entityClass);
    }

    @Override
    public Document documentFrom(Object object) {

        if (object == null) {
            return null;
        }

        Document document = null;
        try {
            document = getObjectMapper().convertValue(object, Document.class);
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return document;
    }

    @Override
    public List<Document> documentsForm(List<Object> objects) {

        if (objects == null) {
            return null;
        }

        // formatter: off
        return
            objects
                .stream()
                .map(this::documentFrom)
                .collect(Collectors.toList());
        // formatter: on
    }

    private ObjectMapper getObjectMapper() {
        if (this.objectMapper == null) {
            this.objectMapper = new ObjectMapper();
            this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }

        return this.objectMapper;
    }

}
