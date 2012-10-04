/*
 * Copyright 2012 CoreMedia AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.joala.expression;

import net.joala.data.DataProvider;
import net.joala.data.random.DefaultRandomStringProvider;
import net.joala.expression.AbstractExpression;
import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * <p>
 * Tests {@link net.joala.expression.AbstractExpression}.
 * </p>
 *
 * @since 8/26/12
 */
public class AbstractExpressionTest {
  private static final DataProvider<String> TEXT_PROVIDER = new DefaultRandomStringProvider().prefix("text").fixate();
  private static final DataProvider<String> DESCRIPTION_PROVIDER = new DefaultRandomStringProvider().prefix("description").fixate();

  @Test
  public void empty_simple_description_should_not_cause_update_of_description() throws Exception {
    final SelfDescribing expression = new AbstractExpression<Object>() {
      @Override
      public Object get() {
        return null;
      }
    };
    final String text = TEXT_PROVIDER.get();
    final Description description = new StringDescription().appendText(text);
    expression.describeTo(description);
    assertEquals("Description should not have been updated.", text, description.toString());
  }

  @Test
  public void non_empty_simple_description_should_be_added_to_description() throws Exception {
    final String simpleDescription = DESCRIPTION_PROVIDER.get();
    final SelfDescribing expression = new AbstractExpression<Object>(simpleDescription) {
      @Override
      public Object get() {
        return null;
      }
    };
    final String text = TEXT_PROVIDER.get();
    final Description description = new StringDescription().appendText(text);
    expression.describeTo(description);
    assertThat("Original description should still be contained.", description.toString(), containsString(text));
    assertThat("Description should be enriched by expression's description.", description.toString(), containsString(simpleDescription));
  }

  @Test
  public void expression_description_should_be_contained_in_toString() throws Exception {
    final String simpleDescription = DESCRIPTION_PROVIDER.get();
    final SelfDescribing expression = new AbstractExpression<Object>(simpleDescription) {
      @Override
      public Object get() {
        return null;
      }
    };
    assertThat("toString should contain description.", expression.toString(), containsString(simpleDescription));
  }
}