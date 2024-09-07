package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.potion.PotionType;
import com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerEntityEffect extends PacketWrapper<WrapperPlayServerEntityEffect> {
    private int entityID;
    private PotionType potionType;
    private int effectAmplifier;
    private int effectDurationTicks;
    private boolean hideParticles;

    public WrapperPlayServerEntityEffect(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerEntityEffect(int entityID, PotionType potionType, int amplifier, int duration, boolean hideParticles) {
        super(PacketType.Play.Server.ENTITY_EFFECT);
        this.entityID = entityID;
        this.potionType = potionType;
        this.effectAmplifier = amplifier;
        this.effectDurationTicks = duration;
        this.hideParticles = hideParticles;
    }

    @Override
    public void read() {
        entityID = readVarInt();
        potionType = PotionTypes.getById(readByte(), ServerVersion.V_1_8_8);
        effectAmplifier = readByte();
        effectDurationTicks = readVarInt() * 20;
        hideParticles = readBoolean();
    }

    @Override
    public void write() {
        writeVarInt(entityID);
        writeByte(potionType.getId(ClientVersion.V_1_8));
        writeByte(effectAmplifier);
        writeVarInt(effectDurationTicks / 20);
        writeBoolean(hideParticles);
    }

    @Override
    public void copy(WrapperPlayServerEntityEffect wrapper) {
        this.entityID = wrapper.entityID;
        this.potionType = wrapper.potionType;
        this.effectAmplifier = wrapper.effectAmplifier;
        this.effectDurationTicks = wrapper.effectDurationTicks;
        this.hideParticles = wrapper.hideParticles;
    }

    public int getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public PotionType getPotionType() {
        return potionType;
    }

    public void setPotionType(PotionType potionType) {
        this.potionType = potionType;
    }

    public int getEffectAmplifier() {
        return effectAmplifier;
    }

    public void setEffectAmplifier(int effectAmplifier) {
        this.effectAmplifier = effectAmplifier;
    }

    public int getEffectDurationTicks() {
        return effectDurationTicks;
    }

    public void setEffectDurationTicks(int effectDurationTicks) {
        this.effectDurationTicks = effectDurationTicks;
    }

    public boolean isHideParticles() {
        return hideParticles;
    }

    public void setHideParticles(boolean hideParticles) {
        this.hideParticles = hideParticles;
    }
}
