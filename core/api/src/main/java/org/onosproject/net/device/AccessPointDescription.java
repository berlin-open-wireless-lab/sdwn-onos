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

package org.onosproject.net.device;

import org.onlab.packet.MacAddress;

/**
 * SDWN access point description.
 * Access Points are to SDWN wireless networks what switch ports are to wired
 * networks.
 */
public interface AccessPointDescription extends PortDescription {

    /**
     * Get the access point's BSSID.
     */
    MacAddress bssid();

    /**
     * Get the MAC address of the phy this AP is running on.
     */
    MacAddress phyMac();

    /**
     * Get the SSIDs the access point is running.
     *
     * @return List of SSIDs represented as String.
     */
    String ssid();
}
