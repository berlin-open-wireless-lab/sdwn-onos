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
import org.onosproject.net.AbstractDescription;
import org.onosproject.net.Port;
import org.onosproject.net.PortNumber;
import org.onosproject.net.SparseAnnotations;

public class DefaultAccessPointDescription extends AbstractDescription
    implements AccessPointDescription {

    private final long speed;
    private final boolean enabled;
    private final PortNumber portNumber;
    private final MacAddress bssid;
    private final MacAddress phyMac;
    private final String ssid;

    public DefaultAccessPointDescription(PortNumber portNumber, boolean enabled,
            long speed, MacAddress bssid, MacAddress phyMac, String ssid,
                                         SparseAnnotations... annotations) {
        super(annotations);
        this.portNumber = portNumber;
        this.enabled = enabled;
        this.speed = speed;
        this.bssid = bssid;
        this.phyMac = phyMac;
        this.ssid = ssid;
    }

    @Override
    public long portSpeed() {
        return this.speed;
    }

    @Override
    public PortNumber portNumber() {
        return this.portNumber;
    }

    @Override
    public Port.Type type() {
        return Port.Type.IEEE802_11_AP;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public MacAddress bssid() {
        return this.bssid;
    }

    @Override
    public MacAddress phyMac() {
        return this.phyMac;
    }

    @Override
    public String ssid() {
        return this.ssid;
    }
}
