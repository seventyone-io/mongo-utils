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
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation that can defines an enitity is needing/having a MongoDB-collection.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MongoCollection {

    /**
     * The collection name.
     * <p>
     * If no value is provided the simple class name of the annotated type will be used.
     */
    String value() default "";

    /**
     * Prevents the marked typed to be setup during {@link io.seventyone.mongoutils.MongoService#autoSetup(String)}.
     */
    boolean noAutoSetup() default false;

}
