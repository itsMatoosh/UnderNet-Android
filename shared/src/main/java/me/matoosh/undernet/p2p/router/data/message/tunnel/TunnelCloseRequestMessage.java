package me.matoosh.undernet.p2p.router.data.message.tunnel;

import me.matoosh.undernet.p2p.router.data.message.MsgBase;
import me.matoosh.undernet.p2p.router.data.message.MsgType;

public class TunnelCloseRequestMessage extends MsgBase {
    @Override
    public MsgType getType() {
        return MsgType.TUNNEL_CLOSE_REQUEST;
    }
}
