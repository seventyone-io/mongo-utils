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
package io.seventyone.mongoutils.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation that can defines an enitity to have an MongoDB-index.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(MongoIndexes.class)
public @interface MongoIndex {

    /**
     * Helper interface for easier usage.
     */
    interface MongoIndexDirection {

        int ASC  = 1;
        int DESC = -1;
    }

    /**
     * Field name of the index key.
     * <p>
     * This needs to be the MongDB-document field name, not the Java field name!
     */
    String key();

    /**
     * Direction of the index.
     * <p>
     * Default: {@link io.seventyone.mongoutils.annotations.MongoIndex.MongoIndexDirection.ASC}
     */
    int direction() default MongoIndexDirection.ASC;

    /**
     * Builds the index in the background, defaults to false.
     * <p>
     * Default: false
     */
    boolean background() default false;

    /**
     * Creates an unique index.
     * <p>
     * Default: false
     */
    boolean unique() default false;

}