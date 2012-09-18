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

package net.joala.base;

import net.joala.data.DataProvider;
import net.joala.data.random.DefaultRandomStringProvider;
import net.joala.data.random.RandomLongProvider;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.lang.String.format;
import static net.joala.matcher.exception.CausedBy.causedBy;
import static net.joala.matcher.exception.MessageContains.messageContains;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

/**
 * <p>
 * Base test for classes implementing {@link WaitFailStrategy}.
 * </p>
 *
 * @since 8/26/12
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class WaitFailStrategyTest<S extends WaitFailStrategy, T extends Throwable> {
  private static final DataProvider<String> FAIL_MESSAGE_PROVIDER = new DefaultRandomStringProvider().prefix("failMessage").fixate();
  private static final DataProvider<String> EXCEPTION_MESSAGE_PROVIDER = new DefaultRandomStringProvider().prefix("exceptionMessage").fixate();
  private static final DataProvider<Long> CONSUMED_MILLIS_PROVIDER = new RandomLongProvider().min(0l).max(1000l).fixate();

  @Mock
  private Object failedFunction;
  @Mock
  private Object failedInput;
  @Mock
  private Object failedLastValue;
  @Mock
  private Throwable lastFailure;

  @Test
  public void failed_match_method_should_throw_exception() throws Exception {
    final S strategy = getFailStrategy();
    boolean success = false;
    try {
      strategy.fail(FAIL_MESSAGE_PROVIDER.get(), failedFunction, failedInput, failedLastValue, nullValue(), CONSUMED_MILLIS_PROVIDER.get());
      success = true;
    } catch (RuntimeException ignored) {
      // fine, that's what we expected
    } catch (Error ignored) {
      // fine that's what we expected
    } catch (Throwable t) {
      fail(format("Unexpected exception of type %s thrown.", t.getClass()));
    }
    assertFalse("An exception should have been thrown.", success);
  }

  @Test
  public void failed_with_exceptions_method_should_throw_exception() throws Exception {
    final S strategy = getFailStrategy();
    boolean success = false;
    try {
      strategy.fail(FAIL_MESSAGE_PROVIDER.get(), failedFunction, failedInput, lastFailure, CONSUMED_MILLIS_PROVIDER.get());
      success = true;
    } catch (RuntimeException ignored) {
      // fine, that's what we expected
    } catch (Error ignored) {
      // fine that's what we expected
    } catch (Throwable t) {
      fail(format("Unexpected exception of type %s thrown.", t.getClass()));
    }
    assertFalse("An exception should have been thrown.", success);
  }

  @Test
  public void message_on_failed_match_should_be_contained_in_exception() throws Throwable {
    final S strategy = getFailStrategy();
    final String message = FAIL_MESSAGE_PROVIDER.get();
    boolean success = false;
    try {
      strategy.fail(message, failedFunction, failedInput, failedLastValue, nullValue(), CONSUMED_MILLIS_PROVIDER.get());
      success = true;
    } catch (Throwable t) {
      if (!getRaisedExceptionType().isAssignableFrom(t.getClass())) {
        throw t;
      }
      assertThat("Message should be contained in exception message.", t.getMessage(), containsString(message));
    }
    assertFalse("An exception should have been thrown.", success);
  }

  @Test
  public void message_when_failed_with_exception_should_be_contained_in_exception() throws Throwable {
    final S strategy = getFailStrategy();
    boolean success = false;
    final String message = FAIL_MESSAGE_PROVIDER.get();
    try {
      strategy.fail(message, failedFunction, failedInput, lastFailure, CONSUMED_MILLIS_PROVIDER.get());
      success = true;
    } catch (Throwable t) {
      if (!getRaisedExceptionType().isAssignableFrom(t.getClass())) {
        throw t;
      }
      assertThat("Message should be contained in exception message.", t.getMessage(), containsString(message));
    }
    assertFalse("An exception should have been thrown.", success);
  }

  @Test
  public void cause_when_failed_with_exception_should_be_contained_in_exception() throws Throwable {
    final S strategy = getFailStrategy();
    final String evaluationExceptionMessage = EXCEPTION_MESSAGE_PROVIDER.get();
    when(lastFailure.getMessage()).thenReturn(evaluationExceptionMessage);
    boolean success = false;
    try {
      strategy.fail(FAIL_MESSAGE_PROVIDER.get(), failedFunction, failedInput, lastFailure, CONSUMED_MILLIS_PROVIDER.get());
      success = true;
    } catch (Throwable t) {
      if (!getRaisedExceptionType().isAssignableFrom(t.getClass())) {
        throw t;
      }
      assertThat("Cause should be mentioned in failure.", t,
              anyOf(
                      causedBy(lastFailure),
                      messageContains(lastFailure.getMessage(), true)
              )
      );
    }
    assertFalse("An exception should have been thrown.", success);
  }

  @Test
  public void should_throw_expected_exception_type_on_failure_because_of_exception() throws Exception {
    final S strategy = getFailStrategy();
    try {
      strategy.fail(FAIL_MESSAGE_PROVIDER.get(), failedFunction, failedInput, lastFailure, CONSUMED_MILLIS_PROVIDER.get());
      fail("Exception should have been thrown.");
    } catch (Throwable t) {
      assertThat("Raised exception should be of expected type.", t, Matchers.instanceOf(getRaisedExceptionType()));
    }
  }

  @Test
  public void should_throw_expected_exception_type_on_failure_because_mismatch() throws Exception {
    final S strategy = getFailStrategy();
    try {
      strategy.fail(FAIL_MESSAGE_PROVIDER.get(), failedFunction, failedInput, failedLastValue, nullValue(), CONSUMED_MILLIS_PROVIDER.get());
      fail("Exception should have been thrown.");
    } catch (Throwable t) {
      assertThat("Raised exception should be of expected type.", t, Matchers.instanceOf(getRaisedExceptionType()));
    }
  }


  protected abstract Class<T> getRaisedExceptionType();

  protected abstract S getFailStrategy();
}
