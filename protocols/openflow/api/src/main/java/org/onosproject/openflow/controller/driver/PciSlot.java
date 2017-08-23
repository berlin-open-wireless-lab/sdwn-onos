/*
 * Copyright 2016-present Open Networking Laboratory
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

package org.onosproject.openflow.controller.driver;

import java.util.Arrays;

public final class PciSlot {

    private byte[] bytes = new byte[4];

    private PciSlot(long raw) {
        // PCI domain
        bytes[0] = (byte) (raw >> 24);
        bytes[1] = (byte) (raw >> 16);
        // PCI bus
        bytes[2] = (byte) (raw >> 8);
        // device number
        bytes[3] = (byte) raw;
    }

    public static PciSlot fromLong(long l) {
        return new PciSlot(l);
    }

    public long toLong() {
         return bytes[0] << 24 | bytes[1] << 16 | bytes[2] << 8 | bytes[3];
    }

    @Override
    public String toString() {
        return String.format("%04x:%02x:%02x", (bytes[0] << 8) | bytes[1], bytes[2], bytes[3]);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof PciSlot) &&
                Arrays.equals(((PciSlot) other).bytes, this.bytes);
    }
}
