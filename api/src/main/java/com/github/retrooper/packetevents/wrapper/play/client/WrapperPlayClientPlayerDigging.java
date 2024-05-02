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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientPlayerDigging extends PacketWrapper<WrapperPlayClientPlayerDigging> {
    private DiggingAction action;
    private Vector3i blockPosition;
    private BlockFace blockFace;
    private int sequence;

    public WrapperPlayClientPlayerDigging(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientPlayerDigging(DiggingAction action, Vector3i blockPosition, BlockFace blockFace, int sequence) {
        super(PacketType.Play.Client.PLAYER_DIGGING);
        this.action = action;
        this.blockPosition = blockPosition;
        this.blockFace = blockFace;
        this.sequence = sequence;
    }

    @Override
    public void read() {
        action = DiggingAction.getById(readVarInt());

        blockPosition = readBlockPosition();
        short face = readUnsignedByte();
        // Vanilla 1.8 doesn't seem to care about BlockFace of OTHER in this packet... just leave it as 1.9+
        blockFace = BlockFace.getBlockFaceByValue(face);

        sequence = readVarInt();
    }

    @Override
    public void write() {
        writeVarInt(action.getId());
        writeBlockPosition(blockPosition);
        writeByte(blockFace.getFaceValue());

        writeVarInt(sequence);
    }

    @Override
    public void copy(WrapperPlayClientPlayerDigging wrapper) {
        action = wrapper.action;
        blockPosition = wrapper.blockPosition;
        blockFace = wrapper.blockFace;
        sequence = wrapper.sequence;
    }

    public DiggingAction getAction() {
        return action;
    }

    public void setAction(DiggingAction action) {
        this.action = action;
    }

    public Vector3i getBlockPosition() {
        return blockPosition;
    }

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    public BlockFace getBlockFace() {
        return blockFace;
    }

    public void setBlockFace(BlockFace blockFace) {
        this.blockFace = blockFace;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
