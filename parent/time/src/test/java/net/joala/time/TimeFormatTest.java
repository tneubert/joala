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
 * along with Joala.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.joala.time;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static net.joala.junit.ParameterizedParametersBuilders.defaultParametersBuilder;
import static org.junit.Assert.assertThat;

/**
 * <p>
 * Tests {@link TimeFormat}.
 * </p>
 *
 * @since 9/19/12
 */
@SuppressWarnings("ProhibitedExceptionDeclared")
@RunWith(Parameterized.class)
public class TimeFormatTest {
  private final String message;
  private final long amount;
  private final TimeUnit timeUnit;
  private final Pattern expectedTimePattern;

  public TimeFormatTest(final long amount, final TimeUnit timeUnit, final String expectedTimePattern) {
    this.timeUnit = timeUnit;
    this.expectedTimePattern = Pattern.compile(expectedTimePattern);
    this.message = String.format("Format amount %d of %s.", amount, timeUnit);
    this.amount = amount;
  }

  @Test
  public void time_should_be_formatted_as_expected() throws Exception {
    final String description = TimeFormat.format(amount, timeUnit);
    assertThat(message, description, new MatchesPattern(expectedTimePattern));
  }

  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    return defaultParametersBuilder(TimeFormatTest.class)
            .add(TimeUnit.SECONDS.toMillis(TimeFormat.TIMEUNIT_LIMIT) - 1, TimeUnit.MILLISECONDS, "^1999\\sms$")
            .add(TimeUnit.SECONDS.toMillis(TimeFormat.TIMEUNIT_LIMIT), TimeUnit.MILLISECONDS, "^2\\ss$")
            .add(TimeUnit.MINUTES.toMillis(TimeFormat.TIMEUNIT_LIMIT) - 1, TimeUnit.MILLISECONDS, "^119\\ss$")
            .add(TimeUnit.MINUTES.toMillis(TimeFormat.TIMEUNIT_LIMIT), TimeUnit.MILLISECONDS, "^2\\smin$")
            .add(TimeUnit.HOURS.toMillis(TimeFormat.TIMEUNIT_LIMIT) - 1, TimeUnit.MILLISECONDS, "^119\\smin$")
            .add(TimeUnit.HOURS.toMillis(TimeFormat.TIMEUNIT_LIMIT), TimeUnit.MILLISECONDS, "^2\\sh$")
            .add(TimeUnit.DAYS.toMillis(TimeFormat.TIMEUNIT_LIMIT) - 1, TimeUnit.MILLISECONDS, "^47\\sh$")
            .add(TimeUnit.DAYS.toMillis(TimeFormat.TIMEUNIT_LIMIT), TimeUnit.MILLISECONDS, "^2\\sd$")
            .add(Long.MAX_VALUE, TimeUnit.MILLISECONDS, "^\\d+\\sd$")
            .add(0L, TimeUnit.NANOSECONDS, "^0\\sns$")
            .add(0L, TimeUnit.MICROSECONDS, "^0\\sµs$")
            .add(0L, TimeUnit.MILLISECONDS, "^0\\sms$")
            .add(0L, TimeUnit.SECONDS, "^0\\ss$")
            .add(0L, TimeUnit.MINUTES, "^0\\smin$")
            .add(0L, TimeUnit.HOURS, "^0\\sh$")
            .add(0L, TimeUnit.DAYS, "^0\\sd$")
            .build();
  }

  // Duplicate in order to remove dependency cycles between modules.
  private static final class MatchesPattern extends TypeSafeMatcher<CharSequence> {
    private final Pattern pattern;

    private MatchesPattern(final Pattern pattern) {
      this.pattern = pattern;
    }

    @Override
    public void describeTo(final Description description) {
      description.appendText("a sequence of characters matching pattern ");
      description.appendValue(pattern);
    }

    @Override
    protected boolean matchesSafely(final CharSequence item) {
      return pattern.matcher(item).matches();
    }
  }
}
