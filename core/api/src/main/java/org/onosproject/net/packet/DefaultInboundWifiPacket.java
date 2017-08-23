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

import org.onlab.packet.Ethernet;
import org.onlab.packet.IEEE80211;
import org.onosproject.net.ConnectPoint;

import java.util.Objects;
import java.util.Optional;

import java.nio.ByteBuffer;

import static com.google.common.base.MoreObjects.toStringHelper;

public final class DefaultInboundWifiPacket implements InboundWifiPacket {

    private final ConnectPoint receivedFrom;
    private final IEEE80211 parsed;
    private final ByteBuffer unparsed;
    private final Optional<Long> cookie;

    public DefaultInboundWifiPacket(ConnectPoint from, IEEE80211 parsed,
                                    ByteBuffer unparsed) {
        this(from, parsed, unparsed, Optional.empty());
    }

    public DefaultInboundWifiPacket(ConnectPoint from, IEEE80211 parsed,
                                    ByteBuffer unparsed, Optional<Long> cookie) {
        this.receivedFrom = from;
        this.parsed = parsed;
        this.unparsed = unparsed;
        this.cookie = cookie;
    }

    @Override
    public ConnectPoint receivedFrom() {
        return receivedFrom;
    }

    @Override
    public Ethernet parsed() {
        return parsed;
    }

    @Override
    public ByteBuffer unparsed() {
        return unparsed;
    }

    @Override
    public Optional<Long> cookie() {
        return cookie;
    }

    @Override
    public int hashCode() {
        return Objects.hash(receivedFrom, parsed, unparsed);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof InboundWifiPacket) {
            final DefaultInboundWifiPacket other = (DefaultInboundWifiPacket) obj;
            return Objects.equals(this.receivedFrom, other.receivedFrom) &&
                    Objects.equals(this.parsed, other.parsed) &&
                    Objects.equals(this.unparsed, other.unparsed);
        }
        return false;
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("receivedFrom", receivedFrom)
                .add("parsed", parsed)
                .toString();
    }
}
