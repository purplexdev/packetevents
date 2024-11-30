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
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerEntityVelocity extends PacketWrapper<WrapperPlayServerEntityVelocity> {
    private int entityID;
    private int rawX;
    private int rawY;
    private int rawZ;
    private Vector3d cachedVelocity;

    public WrapperPlayServerEntityVelocity(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerEntityVelocity(int entityID, Vector3d velocity) {
        super(PacketType.Play.Server.ENTITY_VELOCITY);
        this.entityID = entityID;
        this.cachedVelocity = velocity;
        this.updateRawValues();
    }

    @Override
    public void read() {
        if (serverVersion == ServerVersion.V_1_7_10) {
            entityID = readInt();
        } else {
            entityID = readVarInt();
        }

        this.rawX = readShort();
        this.rawY = readShort();
        this.rawZ = readShort();

        double velX = (double) this.rawX / 8000.0;
        double velY = (double) this.rawY / 8000.0;
        double velZ = (double) this.rawZ / 8000.0;
        this.cachedVelocity = new Vector3d(velX, velY, velZ);
    }

    @Override
    public void write() {
        if (serverVersion == ServerVersion.V_1_7_10) {
            writeInt(entityID);
        } else {
            writeVarInt(entityID);
        }

        writeShort(rawX);
        writeShort(rawY);
        writeShort(rawZ);
    }

    @Override
    public void copy(WrapperPlayServerEntityVelocity wrapper) {
        entityID = wrapper.entityID;
        rawX = wrapper.rawX;
        rawY = wrapper.rawY;
        rawZ = wrapper.rawZ;
    }

    private void updateRawValues() {
        this.rawX = (int) (cachedVelocity.x * 8000.0);
        this.rawY = (int) (cachedVelocity.y * 8000.0);
        this.rawZ = (int) (cachedVelocity.z * 8000.0);
    }

    public int getEntityId() {
        return entityID;
    }

    public void setEntityId(int entityID) {
        this.entityID = entityID;
    }

    public Vector3d getVelocity() {
        return cachedVelocity;
    }

    public void setVelocity(Vector3d velocity) {
        this.cachedVelocity = velocity;
        this.updateRawValues();
    }
}
