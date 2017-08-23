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

package org.onosproject.openflow.controller.impl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.projectfloodlight.openflow.protocol.OFMessage;

import java.util.List;

/**
 * Encode an OpenFlow message or a list of OpenFlow messages for output into a
 * ByteBuf, for use in a netty pipeline.
 */
public class OFMessageEncoder extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (msg instanceof List) {
            @SuppressWarnings("unchecked")
            List<OFMessage> msgList = (List<OFMessage>) msg;

            for (OFMessage ofm : msgList) {
                ofm.writeTo(out);
            }
        } else if (msg instanceof OFMessage) {
            ((OFMessage) msg).writeTo(out);
        } else {
            throw new IllegalArgumentException("{} is neither an OFMessage nor a list");
        }
    }


//    protected Object encode(ChannelHandlerContext ctx, Channel channel,
//                            Object msg) throws Exception {
//        if (!(msg instanceof List)) {
//            return msg;
//        }
//
//        @SuppressWarnings("unchecked")
//        List<OFMessage> msglist = (List<OFMessage>) msg;
//        /* XXX S can't get length of OFMessage in loxigen's openflowj??
//        int size = 0;
//        for (OFMessage ofm : msglist) {
//            size += ofm.getLengthU();
//        }*/
//
////        ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
//        // TODO: initial buffer size?
//        ByteBuf buf = channel.alloc().buffer();
//        for (OFMessage ofm : msglist) {
//            if (ofm != null) {
//                ofm.writeTo(buf);
//            }
//        }
//        return buf;
//    }
}
