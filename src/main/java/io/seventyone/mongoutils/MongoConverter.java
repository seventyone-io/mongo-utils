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

import org.bson.Document;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mongodb.client.MongoIterable;


/**
 * Converts objects to MongoDB-compatible documents and vice versa.
 */
public interface MongoConverter {

    /**
     * Provide the converter with an existing {@link com.fasterxml.jackson.databind.ObjectMapper}.
     * <p>
     * This will reset the currently used objectMapper.
     * <p>
     * If you don't provide an objectMapper a senseful default will created:
     * <li>SerializationFeature.WRITE_DATES_AS_TIMESTAMPS = false
     * <li>SerializationFeature.FAIL_ON_EMPTY_BEANS = false
     * <li>DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES = false
     */
    void provideObjectMapper(ObjectMapper objectMapper);

    /**
     * Configures the currently set objectMapper.
     * <p>
     * Changes the state of a serialization feature.
     */
    void configure(SerializationFeature feature, boolean state);

    /**
     * Configures the currently set objectMapper.
     * <p>
     * Changes the state of a deserialization feature.
     */
    void configure(DeserializationFeature feature, boolean state);

    /**
     * Configures the currently set objectMapper.
     * <p>
     * Changes the state of a mapper feature.
     */
    void configure(MapperFeature feature, boolean state);

    /**
     * Converts a {@link org.bson.Document} to an entity.
     */
    <T> T entityFrom(Document document, Class<T> entityClass);

    /**
     * Converts the content of an {@link java.lang.Iterable<Document>} to a List of entities.
     */
    <T> List<T> entitiesFrom(Iterable<Document> iterable, Class<T> entityClass);

    /**
     * Converts the first document of an {@link java.lang.MongoIterable<Document>} to an entity.
     */
    <T> T firstEntityFrom(MongoIterable<Document> iterable, Class<T> entityClass);

    /**
     * Converts an entity to a {@link org.bson.Document}.
     */
    Document documentFrom(Object object);

    /**
     * Converts a List of entities to a List of {@link org.bson.Document}.
     */
    List<Document> documentsForm(List<Object> objects);

}