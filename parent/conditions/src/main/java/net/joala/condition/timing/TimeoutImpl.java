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

package net.joala.condition.timing;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Implementation of {@link Timeout}.
 * </p>
 *
 * @since 8/22/12
 * @deprecated since 0.5.0; use {@link net.joala.time.TimeoutImpl} instead
 */
@Deprecated
public class TimeoutImpl implements Timeout {
  private final net.joala.time.Timeout wrapped;

  public TimeoutImpl(@Nonnegative final long amount, @Nonnull final TimeUnit unit) {
    this(new net.joala.time.TimeoutImpl(amount, unit));
  }

  public TimeoutImpl(@Nonnull final net.joala.time.Timeout wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  @Nonnegative
  @SuppressWarnings("PMD.ShortMethodName")
  public long in(@Nonnull final TimeUnit targetUnit) {
    return wrapped.in(targetUnit);
  }

  @Override
  @Nonnegative
  @SuppressWarnings("PMD.ShortMethodName")
  public long in(@Nonnull final TimeUnit targetUnit, @Nonnegative final double factor) {
    return wrapped.in(targetUnit, factor);
  }

  public net.joala.time.Timeout getWrapped() {
    return wrapped;
  }
}