package io.github.retrooper.packetevents;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.handshaking.client.WrapperHandshakingClientHandshake;
import io.github.retrooper.packetevents.factory.fabric.FabricPacketEventsBuilder;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("modid");

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        //TODO Mod idea, every 30 seconds or so, on a new port, establish a new connection to the server, get a server list response and
        //that is your ping
        PacketEvents.setAPI(FabricPacketEventsBuilder.build("modid"));
        PacketEvents.getAPI().getSettings().debug(true).bStats(true);
        PacketEvents.getAPI().load();
        PacketEvents.getAPI().getEventManager().registerListener(new PacketListenerAbstract() {
            @Override
            public void onPacketReceive(PacketReceiveEvent event) {
                if (event.getPacketType() == PacketType.Handshaking.Client.HANDSHAKE) {
                    WrapperHandshakingClientHandshake handshake = new WrapperHandshakingClientHandshake(event);
                    ClientVersion version = handshake.getClientVersion();
                    String address = handshake.getServerAddress();
                    int port = handshake.getServerPort();
                    System.out.println("Sent handshaking packet: " + version + " " + address + ":" + port);
                } else if (event.getConnectionState() == ConnectionState.PLAY) {
                    ClientPlayerEntity player = (ClientPlayerEntity) event.getPlayer();
                    MinecraftClient.getInstance().currentScreen.sendMessage("Packet sent: " + event.getPacketType());
                }
            }

            @Override
            public void onPacketSend(PacketSendEvent event) {
                super.onPacketSend(event);
            }
        });
        PacketEvents.getAPI().init();
    }


}