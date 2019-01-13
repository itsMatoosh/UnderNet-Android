package me.matoosh.undernet.standalone.uix;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import me.matoosh.undernet.UnderNet;
import me.matoosh.undernet.event.Event;
import me.matoosh.undernet.event.EventHandler;
import me.matoosh.undernet.event.EventManager;
import me.matoosh.undernet.event.channel.message.tunnel.MessageTunnelClosedEvent;
import me.matoosh.undernet.event.channel.message.tunnel.MessageTunnelEstablishedEvent;
import me.matoosh.undernet.event.router.RouterStatusEvent;
import me.matoosh.undernet.p2p.router.InterfaceStatus;
import me.matoosh.undernet.p2p.router.data.message.tunnel.MessageTunnel;
import me.matoosh.undernet.p2p.router.data.message.tunnel.MessageTunnelState;

import javax.swing.*;
import java.awt.*;

/**
 * Represents a message tunnel panel.
 */
public class TunnelPanel extends EventHandler {
    private JPanel panel;
    private JList tunnelList;


    public TunnelPanel() {
        $$$setupUI$$$();
        registerListeners();
    }

    /**
     * Registers event listeners.
     */
    private void registerListeners() {
        EventManager.registerHandler(this, MessageTunnelEstablishedEvent.class);
        EventManager.registerHandler(this, MessageTunnelClosedEvent.class);
        EventManager.registerHandler(this, RouterStatusEvent.class);
    }

    private void createUIComponents() {
        tunnelList = new JList(new MessageTunnel[]{});
        tunnelList.setCellRenderer(new TunnelListCellRenderer());
    }

    @Override
    public void onEventCalled(Event e) {
        refreshTunnelList();
    }

    private void refreshTunnelList() {
        if (UnderNet.router.status == InterfaceStatus.STARTED) {
            this.tunnelList.setListData(UnderNet.router.messageTunnelManager.messageTunnels.toArray());
        } else {
            this.tunnelList.setListData(new MessageTunnel[0]);
        }
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        panel = new JPanel();
        panel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel.setEnabled(true);
        panel.setForeground(new Color(-1));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane1.setViewportView(tunnelList);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }

}
/**
 * Renders elements within the nodes list.
 */
class TunnelListCellRenderer extends DefaultListCellRenderer
{
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof MessageTunnel) {
            MessageTunnel tunnel = (MessageTunnel) value;
            setText(tunnel.toString());
            switch(tunnel.getTunnelState()) {
                case ESTABLISHED:
                    setBackground(Color.GREEN);
                    break;
                case HOSTED:
                    setBackground(Color.GRAY);
                    break;
                case ESTABLISHING:
                    setBackground(Color.ORANGE);
                    break;
                case NOT_ESTABLISHED:
                    setBackground(Color.RED);
                    break;
            }

            if (isSelected) {
                setBackground(getBackground().darker());
            }
        } else {
            setText("UNKNOWN");
            setBackground(Color.GRAY);
        }
        return c;
    }
}
