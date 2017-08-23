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

package org.onosproject.driver.sdwn.handshaker;

import com.google.common.collect.ImmutableList;
import org.onlab.packet.MacAddress;
import org.onosproject.net.Device;
import org.onosproject.net.PortNumber;
import org.onosproject.net.device.AccessPointDescription;
import org.onosproject.net.device.DefaultAccessPointDescription;
import org.onosproject.net.device.PortDescription;
import org.onosproject.openflow.controller.Dpid;
import org.onosproject.openflow.controller.OpenFlowWirelessSwitch;
import org.onosproject.openflow.controller.driver.AbstractOpenFlowWirelessSwitch;
import org.onosproject.openflow.controller.driver.SwitchDriverSubHandshakeAlreadyStarted;
import org.onosproject.openflow.controller.driver.SwitchDriverSubHandshakeCompleted;
import org.onosproject.openflow.controller.driver.SwitchDriverSubHandshakeNotStarted;
import org.projectfloodlight.openflow.protocol.OFSdwnEntity;
import org.projectfloodlight.openflow.protocol.OFSdwnEntityAccesspoint;
import org.projectfloodlight.openflow.protocol.OFSdwnEntityNic;
import org.projectfloodlight.openflow.protocol.OFSdwnEntityRelatedSwitch;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFPortDesc;
import org.projectfloodlight.openflow.protocol.OFSdwnPortDescReply;
import org.projectfloodlight.openflow.protocol.OFSdwnPortDescRequest;
import org.projectfloodlight.openflow.protocol.OFStatsReply;
import org.projectfloodlight.openflow.protocol.OFStatsReplyFlags;
import org.projectfloodlight.openflow.protocol.OFStatsType;
import org.projectfloodlight.openflow.types.DatapathId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static org.projectfloodlight.openflow.protocol.OFSdwnAccesspointConfig.AP_ENABLED;

/**
 * Driver for SDWN OpenFlow wireless switches. Instances of this class are
 * responsible for the wireless switch-specific sub-handshake when the switch
 * first connects to the controller.
 * During this sub-handshake, OpenFlow Experimenter Port Description messages
 * are sent by the switch to inform the controller about
 * - Wireless Network Interface Cards
 * - Wireless Access Points running on the NICs and
 * - Wireless Networks (SSIDs) announced by those access points.
 * The information comes in the form of SDWN entities which are stored by the
 * driver and can be retrieved using {@code sdwnEntities()}.
 */
public class OpenFlowWirelessSwitch13 extends AbstractOpenFlowWirelessSwitch
        implements OpenFlowWirelessSwitch {

    private final AtomicBoolean handshakeComplete = new AtomicBoolean(false);
    private List<OFSdwnEntity> sdwnEntities = new ArrayList<>();

    @Override
    public List<OFSdwnEntity> sdwnEntities() {
        return ImmutableList.copyOf(this.sdwnEntities);
    }

    @Override
    public List<OFPortDesc> getPorts() {
        List<OFPortDesc> portReplies = new ArrayList<>();
//        this.sdwnEntities.stream()
//                .filter(entity -> entity.getType() == ETH_TYPE)
//                .map(OFSdwnEntityEthport.class::cast)
//                .forEach(eth -> portReplies.add(eth.getPortDesc()));
        return portReplies;
    }

    @Override
    public Boolean supportNxRole() {
        return false;
    }

    // Called as part of the OFChannelHandler's handshake with the switch after
    // the appropriate driver has been loaded.
    @Override
    public void startDriverHandshake() {
        log.info("Starting driver handshake for switch {}", getStringId());
        if (startDriverHandshakeCalled) {
            throw new SwitchDriverSubHandshakeAlreadyStarted();
        }
        startDriverHandshakeCalled = true;

        log.debug("sendHandshakeOFSdwnPortDescRequest for switch {}",
                  getStringId());

        try {
            this.sendHandshakeOFSdwnPortDescRequest();
        } catch (IOException e) {
            log.error("Failed to send handshake message " +
                              "OFExperimenterPortDescRequest for switch {}", e);
        }
    }

    @Override
    public void processDriverHandshakeMessage(OFMessage m) {
        if (!startDriverHandshakeCalled) {
            throw new SwitchDriverSubHandshakeNotStarted();
        }
        if (handshakeComplete.get()) {
            throw new SwitchDriverSubHandshakeCompleted(m);
        }

        log.info("processDriverHandshakeMessage for switch {}", getStringId());

        switch (m.getType()) {
            case STATS_REPLY: // multipart message is reported as STATS
                processOFMultipartReply((OFStatsReply) m);
                break;
            default:
                log.warn("Received message {} during driver sub-handshake " +
                         "from switch {}: Ignoring message", m, getStringId());
        }
    }

    private void processOFMultipartReply(OFStatsReply stats) {
        OFSdwnPortDescReply reply;

        if (stats.getStatsType().equals(OFStatsType.EXPERIMENTER)) {
            try {
                reply = (OFSdwnPortDescReply) stats;
                sdwnEntities.addAll(reply.getEntities());

                if (!reply.getFlags().contains(OFStatsReplyFlags.REPLY_MORE)) {
                    handshakeComplete.set(true);
                }
            } catch (ClassCastException e) {
                log.error("Unexpected Experimenter Multipart message type {} ",
                          stats.getClass().getName());
            }
        }
    }

    public boolean isDriverHandshakeComplete() {
        return handshakeComplete.get();
    }

    private void sendHandshakeOFSdwnPortDescRequest() throws IOException {
        OFSdwnPortDescRequest req = factory()
                .buildSdwnPortDescRequest()
                .setXid(getNextTransactionId())
                .build();

        log.info("Sending SDWN port description request: {}",
                  req.toString());

        this.sendHandshakeMessage(req);
    }

    @Override
    public Device.Type deviceType() {
        return Device.Type.WIRELESS_SWITCH;
    }

    @Override
    public List<PortDescription> handleExpMultipartReply(OFMessage msg) {
        if (msg instanceof OFSdwnPortDescReply) {
            OFSdwnPortDescReply reply = (OFSdwnPortDescReply) msg;
            return extractAPDescriptions(reply.getEntities());
        }

        return Collections.emptyList();
    }

    private List<PortDescription> extractAPDescriptions(List<OFSdwnEntity> entities) {
        return getAPs().stream().map(this::apDescFromEntity)
                .collect(Collectors.toList());
    }

    private AccessPointDescription apDescFromEntity(OFSdwnEntityAccesspoint entity) {
        boolean enabled = (entity.getConfig() & AP_ENABLED.ordinal()) == AP_ENABLED.ordinal();
        MacAddress bssid = MacAddress.valueOf(entity.getBssid().getBytes());
        StringBuilder sb = new StringBuilder();

        for (byte b : entity.getSsid()) {
            sb.append(b);
        }

        return new DefaultAccessPointDescription(
                PortNumber.portNumber(entity.getIfNo().getPortNumber()),
                enabled,
                entity.getCurrSpeed(),
                bssid,
                MacAddress.valueOf(entity.getPhyMac().getBytes()),
                sb.toString());
    }

    @Override
    public List<OFSdwnEntityAccesspoint> getAPs() {
        return this.sdwnEntities.stream()
                .filter(entity -> entity instanceof OFSdwnEntityAccesspoint)
                .map(OFSdwnEntityAccesspoint.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    public Dpid relatedOfSwitch() {
        return this.sdwnEntities.stream()
                .filter(OFSdwnEntityRelatedSwitch.class::isInstance)
                .map(OFSdwnEntityRelatedSwitch.class::cast)
                .map(OFSdwnEntityRelatedSwitch::getDatapathId)
                .map(DatapathId::getLong)
                .map(Dpid::uri)
                .map(Dpid::dpid)
                .findFirst().orElse(null);
    }

    @Override
    public List<OFSdwnEntityNic> nics() {
        return this.sdwnEntities().stream()
                .filter(OFSdwnEntityNic.class::isInstance)
                .map(OFSdwnEntityNic.class::cast)
                .collect(Collectors.toList());
    }
}
