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
import io.netty.handler.codec.ByteToMessageDecoder;
import org.onosproject.bgpio.protocol.BgpMessage;
import org.onlab.util.HexDump;
import org.onosproject.bgpio.protocol.BgpFactories;
import org.onosproject.bgpio.protocol.BgpMessageReader;
import org.onosproject.bgpio.types.BgpHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decode an bgp message from a Channel, for use in a netty pipeline.
 */
public class BgpMessageDecoder extends ByteToMessageDecoder {

    protected static final Logger log = LoggerFactory.getLogger(BgpMessageDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        log.debug("MESSAGE IS RECEIVED.");
        if (!ctx.channel().isActive()) {
            log.info("Channel is not connected.");
            return;
        }

        HexDump.dump(buf);

        BgpMessageReader<BgpMessage> reader = BgpFactories.getGenericReader();

        try {
            while (buf.readableBytes() > 0) {
                buf.markReaderIndex();
                BgpHeader bgpHeader = new BgpHeader();
                BgpMessage message = reader.readFrom(buf, bgpHeader);
                out.add(message);
            }
        } catch (Exception e) {
            log.debug("Bgp protocol message decode error");
            buf.resetReaderIndex();
            buf.discardReadBytes();
        }
    }
}