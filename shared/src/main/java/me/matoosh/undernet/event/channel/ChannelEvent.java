package me.matoosh.undernet.event.channel;

import io.netty.channel.Channel;
import me.matoosh.undernet.event.Event;
import me.matoosh.undernet.p2p.node.Node;
import me.matoosh.undernet.p2p.router.client.ClientNetworkMessageHandler;
import me.matoosh.undernet.p2p.router.server.ServerNetworkMessageHandler;

/**
 * An event concerning a connection.
 * Created by Mateusz Rębacz on 27.06.2017.
 */

public abstract class ChannelEvent extends Event {
    /**
     * The connection.
     */
    public Channel channel;

    /**
     * Whether the channel is on the server or on the client.
     */
    public boolean isServer;

    /**
     * The server node object of the channel.
     * Available only on the client.
     */
    public Node remoteNode;

    /**
     * Creates a new channel event, given the channel.
     * @param c
     */
    public ChannelEvent(Channel c, boolean isServer) {
        this.channel = c;
        this.isServer = isServer;

        if(isServer) {
            this.remoteNode = c.attr(ServerNetworkMessageHandler.ATTRIBUTE_KEY_CLIENT_NODE).get();
        } else {
            this.remoteNode = c.attr(ClientNetworkMessageHandler.ATTRIBUTE_KEY_SERVER_NODE).get();
        }
    }
}
