/*
 * Copyright 2015-present Open Networking Laboratory
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
package org.onosproject.bgp.controller.impl;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.onosproject.bgpio.protocol.BgpMessage;
import org.onlab.util.HexDump;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.buffer.Unpooled.buffer;

/**
 * Encode an bgp message for output into a ChannelBuffer, for use in a
 * netty pipeline.
 */
public class BgpMessageEncoder extends MessageToMessageEncoder<BgpMessage> {
    protected static final Logger log = LoggerFactory.getLogger(BgpMessageEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, BgpMessage msg, List<Object> out) throws Exception {
        log.debug("BGPMessageEncoder::encode");
        if (!(msg instanceof List)) {
            log.debug("Invalid msg.");
        }

        @SuppressWarnings("unchecked")
        List<BgpMessage> msglist = (List<BgpMessage>) msg;

        ByteBuf buf = buffer();

        log.debug("SENDING MESSAGE");
        for (BgpMessage pm : msglist) {
            pm.writeTo(buf);
        }

        HexDump.dump(buf);
        out.add(buf);
    }
}