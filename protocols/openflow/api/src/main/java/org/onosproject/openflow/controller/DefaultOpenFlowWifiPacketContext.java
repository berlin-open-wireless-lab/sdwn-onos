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

package org.onosproject.openflow.controller;

import org.onlab.packet.Ethernet;
import org.onlab.packet.IEEE80211;
import org.projectfloodlight.openflow.protocol.OFSdwnPacketIn;
import org.projectfloodlight.openflow.types.OFPort;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.onosproject.security.AppGuard.checkPermission;
import static org.onosproject.security.AppPermission.Type.PACKET_READ;
import static org.onosproject.security.AppPermission.Type.PACKET_WRITE;

public final class DefaultOpenFlowWifiPacketContext extends DefaultOpenFlowPacketContext {

    private final AtomicBoolean free = new AtomicBoolean(true);
    private final AtomicBoolean isBuilt = new AtomicBoolean(false);
    private final OpenFlowWirelessSwitch sw;
    private final OFSdwnPacketIn pktin;
// TODO    private OFSdwnPacketOut pktout;

    private DefaultOpenFlowWifiPacketContext(OpenFlowWirelessSwitch sw,
                                             OFSdwnPacketIn pkt) {
        super(sw, null);
        this.sw = sw;
        this.pktin = pkt;
    }

    public static DefaultOpenFlowWifiPacketContext wifiPacketContextFromPacketIn(
            OpenFlowWirelessSwitch sw, OFSdwnPacketIn pkt) {
        return new DefaultOpenFlowWifiPacketContext(sw, pkt);
    }


    @Override
    public void build(Ethernet frame, OFPort outPort) {
        if (isBuilt.getAndSet(true) || !(frame instanceof IEEE80211)) {
            return;
        }
        // TODO: packet out
    }

    @Override
    public boolean block() {
        checkPermission(PACKET_WRITE);
        return free.getAndSet(false);
    }

    @Override
    public boolean isHandled() {
        checkPermission(PACKET_READ);
        return !free.get();
    }

    @Override
    public void send() {

    }

    @Override
    public void build(OFPort outPort) {
        // TODO: serialization
    }

    @Override
    public IEEE80211 parsed() {
        checkPermission(PACKET_READ);
        // TODO: deserialization
        return null;
    }

    @Override
    public byte[] unparsed() {
        checkPermission(PACKET_READ);
        return pktin.getFrame().clone();
    }

    @Override
    public Dpid dpid() {
        return new Dpid(sw.getId());
    }

    @Override
    public Integer inPort() {
        checkPermission(PACKET_READ);
        return pktin.getIfNo().getPortNumber();
    }

    @Override
    public boolean isBuffered() {
        return false;
    }

    @Override
    public Optional<Long> cookie() {
        checkPermission(PACKET_READ);
        return Optional.empty();
    }
}
