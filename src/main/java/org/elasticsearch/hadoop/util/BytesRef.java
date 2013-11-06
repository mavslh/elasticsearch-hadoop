/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.elasticsearch.hadoop.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper around multiple byte arrays and {@link BytesArray}. Used to pass bytes around by referencing them w/o having to make a copy.
 */
public class BytesRef {

    List<Object> list = null;
    private int size = 0;

    public void add(BytesArray bytes) {
        if (list == null) {
            list = new ArrayList<Object>();
        }
        list.add(bytes);
        size += bytes.size();
    }

    public void add(byte[] bytes) {
        if (list == null) {
            list = new ArrayList<Object>();
        }
        list.add(bytes);
        size += bytes.length;
    }

    public int size() {
        return size;
    }

    public void write(BytesArray to) {
        if (list == null) {
            return;
        }
        for (Object ref : list) {
            if (ref instanceof BytesArray) {
                BytesArray ba = (BytesArray) ref;
                System.arraycopy(ba.bytes(), 0, to.bytes(), to.size(), ba.size());
                to.increment(ba.size());
            }
            else {
                byte[] bytes = (byte[]) ref;
                System.arraycopy(bytes, 0, to.bytes(), to.size(), bytes.length);
                to.increment(bytes.length);
            }
        }
    }

    public void clear() {
        if (list != null) {
            list.clear();
        }
    }
}