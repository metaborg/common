/*
 * Copyright (C) 2008 The Guava Authors
 * Modifications copyright (C) 2019 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package mb.common.util;

import static mb.common.util.Preconditions.checkArgument;

// Selectively copied from https://github.com/google/guava/blob/master/guava/src/com/google/common/primitives/Floats.java.
public class FloatUtil {
    /**
     * Returns the least value present in {@code array}, using the same rules of comparison as {@link Math#min(float,
     * float)}.
     *
     * @param array a <i>nonempty</i> array of {@code float} values
     * @return the value present in {@code array} that is less than or equal to every other value in the array
     * @throws IllegalArgumentException if {@code array} is empty
     */
    public static float min(float... array) {
        checkArgument(array.length > 0);
        float min = array[0];
        for(int i = 1; i < array.length; i++) {
            min = Math.min(min, array[i]);
        }
        return min;
    }

    /**
     * Returns the greatest value present in {@code array}, using the same rules of comparison as {@link Math#max(float,
     * float)}.
     *
     * @param array a <i>nonempty</i> array of {@code float} values
     * @return the value present in {@code array} that is greater than or equal to every other value in the array
     * @throws IllegalArgumentException if {@code array} is empty
     */
    public static float max(float... array) {
        checkArgument(array.length > 0);
        float max = array[0];
        for(int i = 1; i < array.length; i++) {
            max = Math.max(max, array[i]);
        }
        return max;
    }
}
