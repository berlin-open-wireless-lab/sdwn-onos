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

package org.onosproject.openflow.controller;

import org.onosproject.net.device.PortDescription;
import org.projectfloodlight.openflow.protocol.OFSdwnEntity;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFSdwnEntityAccesspoint;
import org.projectfloodlight.openflow.protocol.OFSdwnEntityNic;

import java.util.List;

public interface OpenFlowWirelessSwitch extends OpenFlowSwitch {

    List<OFSdwnEntity> sdwnEntities();

    /**
     * Return SDWN entities describing all the access points on the switch.
     *
     * @return list of Acess Point entities.
     */
    List<OFSdwnEntityAccesspoint> getAPs();

    /**
     * Return updated PortDescriptions from experimenter message received from
     * device.
     *
     * @param msg the OpenFlow message.
     * @return List of updated PortDescriptions.
     */
    List<PortDescription> handleExpMultipartReply(OFMessage msg);

    /**
     * Read related OpenFlow switch Datapath ID.
     *
     * @return Dpid of related OF switch
     */
    Dpid relatedOfSwitch();

    /**
     * Read wireless network interface cards.
     *
     * @return List of entities of this switch's wireless NICs
     */
    List<OFSdwnEntityNic> nics();
}
