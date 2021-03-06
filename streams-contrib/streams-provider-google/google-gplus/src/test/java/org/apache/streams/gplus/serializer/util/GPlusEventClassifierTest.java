/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.streams.gplus.serializer.util;

import org.apache.streams.jackson.StreamsJacksonMapper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.Person;
import org.junit.Assert;
import org.junit.Test;

/**
 * GPlusEventClassifierTest tests GPlusEventClassifier.
 */
public class GPlusEventClassifierTest {

  private static StreamsJacksonMapper mapper = StreamsJacksonMapper.getInstance();

  @Test
  public void classifyActivityTest() {
    try {
      Activity activity = new Activity();
      activity.setKind("plus#activity");
      Class retClass = GPlusEventClassifier.detectClass(mapper.writeValueAsString(activity));
      Assert.assertEquals(retClass, Activity.class);
    } catch (Exception ex) {
      //
    }
  }

  @Test
  public void classifyPersonTest() {
    try {
      Person person = new Person();
      person.setKind("plus#person");
      Class retClass = GPlusEventClassifier.detectClass(mapper.writeValueAsString(person));
      Assert.assertEquals(retClass, Person.class);
    } catch (Exception ex) {
      //
    }
  }

  @Test
  public void classifyObjectNodeTest() {
    try {
      Person person = new Person();
      person.setKind("fake");
      Class retClass = GPlusEventClassifier.detectClass(mapper.writeValueAsString(person));
      Assert.assertEquals(retClass, ObjectNode.class);
    } catch (Exception ex) {
      //
    }
  }
}
