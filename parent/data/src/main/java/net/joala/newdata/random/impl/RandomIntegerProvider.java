/*
 * Copyright 2012 CoreMedia AG
 *
 * This file is part of Joala.
 *
 * Joala is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Joala is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.joala.newdata.random.impl;

import net.joala.newdata.random.AbstractRandomNumberProvider;
import net.joala.newdata.random.AbstractRandomNumberProviderBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Provider;
import java.util.Random;

/**
 * @since 10/23/12
 */
public class RandomIntegerProvider extends AbstractRandomNumberProvider<Integer> {
  public RandomIntegerProvider() {
    this(null, null, null);
  }

  public RandomIntegerProvider(@Nullable final Provider<Random> randomProvider) {
    this(null, null, randomProvider);
  }

  public RandomIntegerProvider(final Integer minValue, final Integer maxValue) {
    this(minValue, maxValue, null);
  }

  public RandomIntegerProvider(@Nullable final Integer minValue, @Nullable final Integer maxValue,
                               @Nullable final Provider<Random> randomProvider) {
    super(minValue, maxValue, randomProvider);
  }

  @Nonnull
  @Override
  protected Integer getMaxDefault() {
    return Integer.MAX_VALUE;
  }

  @Nonnull
  @Override
  protected Integer getMinDefault() {
    return Integer.MIN_VALUE;
  }

  @Nonnull
  @Override
  public Builder min(@Nonnull final Integer minValue) {
    return new Builder(getRandomProvider()).min(minValue);
  }

  @Nonnull
  @Override
  public Builder max(@Nonnull final Integer maxValue) {
    return new Builder(getRandomProvider()).max(maxValue);
  }

  @Override
  @Nonnull
  protected Integer get(@Nonnull final Integer minValue, @Nonnull final Integer maxValue, final double percentage) {
    return (int) (percentage * maxValue + (1d - percentage) * minValue);
  }

  public static final class Builder extends AbstractRandomNumberProviderBuilder<Integer> {
    private Builder(final Provider<Random> randomProvider) {
      super(randomProvider);
    }

    @Override
    protected boolean isValidRange(@Nonnull final Integer lowerBound, @Nonnull final Integer upperBound) {
      return Integer.compare(lowerBound, upperBound) <= 0;
    }

    @Override
    protected Provider<Integer> newProvider(final Integer minValue, final Integer maxValue, final Provider<Random> randomProvider) {
      return new RandomIntegerProvider(minValue, maxValue, randomProvider);
    }

    @Nonnull
    @Override
    public Builder max(@Nonnull final Integer maxValue) {
      super.max(maxValue);
      return this;
    }

    @Nonnull
    @Override
    public Builder min(@Nonnull final Integer minValue) {
      super.min(minValue);
      return this;
    }
  }
}
