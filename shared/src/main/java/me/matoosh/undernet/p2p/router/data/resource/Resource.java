package me.matoosh.undernet.p2p.router.data.resource;

import me.matoosh.undernet.p2p.node.Node;
import me.matoosh.undernet.p2p.router.Router;
import me.matoosh.undernet.p2p.router.data.NetworkID;
import me.matoosh.undernet.p2p.router.data.message.NetworkMessage;
import me.matoosh.undernet.p2p.router.data.message.ResourceInfoMessage;
import me.matoosh.undernet.p2p.router.data.resource.transfer.ResourceTransferHandler;
import me.matoosh.undernet.p2p.router.data.resource.transfer.ResourceTransferType;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Represents a stored resource.
 * Created by Mateusz Rębacz on 25.09.2017.
 */

public abstract class Resource implements Serializable {
    /**
     * The network id of this resource.
     */
    private NetworkID networkID;

    /**
     * The owner of the resource.
     */
    private NetworkID owner;

    /**
     * The attributes of the resource.
     */
    public HashMap<Integer, String> attributes = new HashMap<Integer, String>();

    /**
     * Calculates the network id of the resource based on its contents.
     */
    public abstract void calcNetworkId();
    /**
     * Sets the network id.
     * @param id
     */
    public void setNetworkID(NetworkID id) {
        if(id.isValid()) {
            this.networkID = id;
        } else {
            ResourceManager.logger.info("Can't set resource id to: {}, invalid ID", id);
        }
    }
    /**
     * Sets the owner.
     * @param owner
     */
    public void setOwner(NetworkID owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "networkID=" + networkID +
                '}';
    }

    /**
     * Checks whether the resource is present in the self node.
     * @return
     */
    public abstract boolean isLocal();

    /**
     * Gets the type of the resource.
     * @return
     */
    abstract ResourceType getResourceType();

    /**
     * Gets the attributes of the resource.
     * @return
     */
    abstract void updateAttributes();

    /**
     * Gets the class of the resource transfer type.
     */
    public abstract ResourceTransferHandler getTransferHandler(ResourceTransferType resourceTransferType, NetworkMessage.MessageDirection messageDirection, NetworkID recipient, Router router);

    /**
     * Gets the network id.
     * @return
     */
    public NetworkID getNetworkID() {
        return this.networkID;
    }

    /**
     * Gets the owner.
     */
    public NetworkID getOwner() {
        return this.owner;
    }

    /**
     * Gets the resource info.
     * @return
     */
    public ResourceInfo getInfo() {
        return new ResourceInfo(this);
    }

    /**
     * Sends resource info the destination.
     * @param destination
     */
    public void sendInfo(NetworkID destination, NetworkMessage.MessageDirection messageDirection, byte transferId) {
        if(messageDirection == NetworkMessage.MessageDirection.TO_DESTINATION) {
            destination.sendMessage(new ResourceInfoMessage(getInfo(), transferId));
        } else {
            destination.sendResponse(new ResourceInfoMessage(getInfo(), transferId));
        }
    }

    /**
     * Listens for the finishing of a resource action.
     */
    public interface IResourceActionListener {
        /**
         * Called when the action is finished.
         * @param other the node associated with the action.
         */
        public void onFinished(Node other);
    }
}
