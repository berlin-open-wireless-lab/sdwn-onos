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

package org.onosproject.provider.of.device.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.onosproject.net.DeviceId;
import org.onosproject.net.config.basics.BasicElementConfig;
import org.onosproject.openflow.controller.Dpid;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WirelessSwitchConfig extends BasicElementConfig<DeviceId> {

    public static final String CONFIG_KEY = "wireless_switch";
    public static final String RELATED_SWITCHES = "related_switches";

    public WirelessSwitchConfig(DeviceId id) {
        ObjectMapper mapper = new ObjectMapper();
        init(id, CONFIG_KEY, mapper.createObjectNode(), mapper, null);
    }

    @Override
    public boolean isValid() {
        return super.isValid();
    }

    protected List<Dpid> get(String key, List<Dpid> l) {
        List<String> values = object.findValuesAsText(key);

        if (values.isEmpty()) {
            return l;
        } else {
            return object.findValuesAsText(key).stream()
                    .map(Dpid::new)
                    .collect(Collectors.toList());
        }
    }

    public List<Dpid> relatedSwitches() {
        List<Dpid> out = new ArrayList<>();
        ArrayNode node = (ArrayNode) object.get(RELATED_SWITCHES);
        node.forEach(entry -> out.add(Dpid.dpid(URI.create(entry.textValue()))));
        return out;
    }

    public WirelessSwitchConfig relatedSwitches(List<Dpid> list) {
        if (list == null) {
            object.remove(RELATED_SWITCHES);
        } else {
            ArrayNode array = mapper.createArrayNode();
            list.stream().map(Dpid::toString).forEach(array::add);
            object.set(RELATED_SWITCHES, array);
        }
        return this;
    }
}
