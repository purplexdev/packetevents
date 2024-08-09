/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerSoundEffect extends PacketWrapper<WrapperPlayServerSoundEffect> {
    private String soundName;
    private Vector3i effectPosition;
    private float volume;
    private short pitch;

    public WrapperPlayServerSoundEffect(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerSoundEffect(String soundName, Vector3i effectPosition, float volume, byte pitch) {
        super(PacketType.Play.Server.SOUND_EFFECT);
        this.soundName = soundName;
        this.effectPosition = effectPosition;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public void read() {
        soundName = readString();
        effectPosition = new Vector3i(readInt() / 8, readInt() / 8, readInt() / 8);
        volume = readFloat();
        pitch = readUnsignedByte();
    }

    @Override
    public void write() {
        writeString(soundName);
        writeInt(effectPosition.getX() * 8);
        writeInt(effectPosition.getY() * 8);
        writeInt(effectPosition.getZ() * 8);
        writeFloat(volume);
        writeByte((byte) pitch);
    }

    @Override
    public void copy(WrapperPlayServerSoundEffect wrapper) {
        this.soundName = wrapper.soundName;
        this.effectPosition = wrapper.effectPosition;
        this.volume = wrapper.volume;
        this.pitch = wrapper.pitch;
    }

    public String getSoundName() {
        return soundName;
    }

    public void setSoundName(String soundName) {
        this.soundName = soundName;
    }

    public short getPitch() {
        return pitch;
    }

    public void setPitch(short pitch) {
        this.pitch = pitch;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public Vector3i getEffectPosition() {
        return effectPosition;
    }

    public void setEffectPosition(Vector3i effectPosition) {
        this.effectPosition = effectPosition;
    }
}