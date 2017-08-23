/*
 * Copyright 2017-present Open Networking Laboratory
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

package org.onosproject.net.packet;

import com.google.common.base.MoreObjects;
import org.onosproject.net.DeviceId;
import org.onosproject.net.flow.TrafficTreatment;

import java.nio.ByteBuffer;
import java.util.Objects;

public final class DefaultOutboundWifiPacket implements OutboundWifiPacket {

    private final DeviceId sendThrough;
    private final TrafficTreatment treatment;
    private final ByteBuffer data;

    public DefaultOutboundWifiPacket(DeviceId sendThrough, TrafficTreatment t,
                                     ByteBuffer buf) {
        this.sendThrough = sendThrough;
        this.treatment = t;
        this.data = buf;
    }

    @Override
    public DeviceId sendThrough() {
        return sendThrough;
    }

    @Override
    public TrafficTreatment treatment() {
        return treatment;
    }

    @Override
    public ByteBuffer data() {
        return data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sendThrough, treatment, data);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof OutboundPacket) {
            final DefaultOutboundWifiPacket other = (DefaultOutboundWifiPacket) obj;
            return Objects.equals(this.sendThrough, other.sendThrough) &&
                    Objects.equals(this.treatment, other.treatment) &&
                    Objects.equals(this.data, other.data);
        }
        return false;
    }
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("sendThrough", sendThrough)
                .add("treatment", treatment)
                .toString();
    }
}
