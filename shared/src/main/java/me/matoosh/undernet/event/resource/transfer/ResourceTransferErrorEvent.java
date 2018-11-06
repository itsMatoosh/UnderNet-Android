package me.matoosh.undernet.event.resource.transfer;

import me.matoosh.undernet.p2p.router.data.resource.FileResource;
import me.matoosh.undernet.p2p.router.data.resource.transfer.ResourceTransferHandler;

/**
 * Called when an error occurs with file resource transfer.
 * Created by Mateusz Rębacz on 14.10.2017.
 */

public class ResourceTransferErrorEvent extends ResourceTransferEvent {
    /**
     * The exception.
     */
    public Exception exception;

    public ResourceTransferErrorEvent(ResourceTransferHandler transferHandler, Exception exception) {
        super(transferHandler);
        this.exception = exception;
    }

    @Override
    public void onCalled() {
        logger.error("Error occurred transferring resource: " + transferHandler.getResource(), exception);

        //Deleting the remainders of the transferred file.
        if(this.transferHandler.getResource() instanceof FileResource) {

        }
    }
}
