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

import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Maps;

import io.seventyone.mongoutils.MongoConverter;
import io.seventyone.mongoutils.MongoConverterImplementation;

/**
 * Convenience builder for {@link io.seventyone.mongoutils.MongoConverter} interface.
 */
public class MongoConverterBuilder {

    private final Map<SerializationFeature, Boolean>   serializationFeatures   = Maps.newHashMap();
    private final Map<DeserializationFeature, Boolean> deserializationFeatures = Maps.newHashMap();
    private final Map<MapperFeature, Boolean>          mapperFeatures          = Maps.newHashMap();
    private ObjectMapper                               objectMapper;

    /**
     * Returns a new MongoConverterBuilder.
     */
    public static MongoConverterBuilder start() {
        return new MongoConverterBuilder();
    }

    /**
     * Creates a MongoConverter instance based on the previous configuration.
     */
    public MongoConverter build() {
        MongoConverter converter = new MongoConverterImplementation();

        if (this.objectMapper != null) {
            converter.provideObjectMapper(this.objectMapper);
        }

        this.serializationFeatures.forEach((feature, state) -> converter.configure(feature, state));
        this.deserializationFeatures.forEach((feature, state) -> converter.configure(feature, state));
        this.mapperFeatures.forEach((feature, state) -> converter.configure(feature, state));

        return converter;
    }

    /**
     * Configure the state of a {@link com.fasterxml.jackson.databind.SerializationFeature}.
     */
    public MongoConverterBuilder configure(SerializationFeature feature, boolean state) {

        this.serializationFeatures.put(feature, state);

        return this;
    }

    /**
     * Configure the state of a {@link com.fasterxml.jackson.databind.DeserializationFeature}.
     */
    public MongoConverterBuilder configure(DeserializationFeature feature, boolean state) {

        this.deserializationFeatures.put(feature, state);

        return this;
    }

    /**
     * Configure the state of a {@link com.fasterxml.jackson.databind.MapperFeature}.
     */
    public MongoConverterBuilder configure(MapperFeature feature, boolean state) {

        this.mapperFeatures.put(feature, state);

        return this;
    }


    /**
     * Provide an {@link com.fasterxml.jackson.databind.ObjectMapper}.
     */
    public MongoConverterBuilder objectMapper(ObjectMapper objectMapper) {

        this.objectMapper = objectMapper;

        return this;
    }

}
