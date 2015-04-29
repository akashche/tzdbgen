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

/**
 * Copyright (C) 2012-2013 The named-regexp Authors
 *
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
 */
package build.tools.tzdb.support.com.google.code.regexp;

import java.io.Serializable;

/**
 * Contains the position and group index of capture groups
 * from a named pattern
 */
public class GroupInfo implements Serializable {

    /**
     * Determines if a de-serialized file is compatible with this class.
     *
     * Maintainers must change this value if and only if the new version
     * of this class is not compatible with old versions. See Sun docs
     * for <a href=http://java.sun.com/products/jdk/1.1/docs/guide
     * /serialization/spec/version.doc.html> details. </a>
     *
     * Not necessary to include in first version of the class, but
     * included here as a reminder of its importance.
     */
    private static final long serialVersionUID = 1L;

    private int pos;
    private int groupIndex;

    /**
     * Constructs a {@code GroupInfo} with a group index and string
     * position
     *
     * @param groupIndex the group index
     * @param pos the position
     */
    public GroupInfo(int groupIndex, int pos) {
        this.groupIndex = groupIndex;
        this.pos = pos;
    }

    /**
     * Gets the string position of the group within a named pattern
     *
     * @return the position
     */
    public int pos() {
        return pos;
    }

    /**
     * Gets the group index of the named capture group
     *
     * @return the group index
     */
    public int groupIndex() {
        return groupIndex;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof GroupInfo)) {
            return false;
        }
        GroupInfo other = (GroupInfo)obj;
        return (pos == other.pos) && (groupIndex == other.groupIndex);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return pos ^ groupIndex;
    }
}
