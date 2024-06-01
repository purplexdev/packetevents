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
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerEffect extends PacketWrapper<WrapperPlayServerEffect> {
    private int event;
    private Vector3i blockPosition;
    private int data;
    private boolean disableRelativeVolume;

    public WrapperPlayServerEffect(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerEffect(int event, Vector3i blockPosition, int data, boolean disableRelativeVolume) {
        super(PacketType.Play.Server.EFFECT);
        this.event = event;
        this.blockPosition = blockPosition;
        this.data = data;
        this.disableRelativeVolume = disableRelativeVolume;
    }

    @Override
    public void read() {
        event = readInt();
        if (serverVersion == ServerVersion.V_1_7_10) {
            int x = readInt();
            int y = readInt();
            int z = readInt();
            blockPosition = new Vector3i(x, y, z);
        } else {
            blockPosition = readBlockPosition();
        }
        data = readInt();
        disableRelativeVolume = readBoolean();
    }

    @Override
    public void write() {
        writeInt(event);
        if (serverVersion == ServerVersion.V_1_7_10) {
            writeInt(blockPosition.x);
            writeInt(blockPosition.y);
            writeInt(blockPosition.z);
        } else {
            writeBlockPosition(blockPosition);
        }
        writeInt(data);
        writeBoolean(disableRelativeVolume);
    }

    @Override
    public void copy(WrapperPlayServerEffect wrapper) {
        event = wrapper.event;
        blockPosition = wrapper.blockPosition;
        data = wrapper.data;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public Vector3i getBlockPosition() {
        return blockPosition;
    }

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public boolean isDisableRelativeVolume() {
        return disableRelativeVolume;
    }

    public void setDisableRelativeVolume(boolean disableRelativeVolume) {
        this.disableRelativeVolume = disableRelativeVolume;
    }
}
