/*
 * Copyright 2015 Red Hat, Inc.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; see the file COPYING.  If not see
 * <http://www.gnu.org/licenses/>.
 */

package build.tools.tzdb.support.com.redhat.openjdk.support7;

/**
 * Partial copy of Long from jdk7u
 */
public class LongUtils {

    /**
     * Compares two {@code long} values numerically.
     * The value returned is identical to what would be returned by:
     * <pre>
     *    Long.valueOf(x).compareTo(Long.valueOf(y))
     * </pre>
     *
     * @param  x the first {@code long} to compare
     * @param  y the second {@code long} to compare
     * @return the value {@code 0} if {@code x == y};
     *         a value less than {@code 0} if {@code x < y}; and
     *         a value greater than {@code 0} if {@code x > y}
     */
    public static int compare(long x, long y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }
}
