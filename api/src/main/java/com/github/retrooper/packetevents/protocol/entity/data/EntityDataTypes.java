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

package com.github.retrooper.packetevents.protocol.entity.data;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.armadillo.ArmadilloState;
import com.github.retrooper.packetevents.protocol.entity.pose.EntityPose;
import com.github.retrooper.packetevents.protocol.entity.sniffer.SnifferState;
import com.github.retrooper.packetevents.protocol.entity.villager.VillagerData;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.particle.Particle;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.protocol.world.Direction;
import com.github.retrooper.packetevents.protocol.world.WorldBlockPosition;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.Quaternion4f;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public final class EntityDataTypes {

    //nbt was added in 1.12

    // there is no proper mojang registry for this yet, the registry name is just a guess
    private static final VersionedRegistry<IEntityDataType<?>> REGISTRY = new VersionedRegistry<>(
            "entity_data_serializer", "entity/entity_data_type_mappings");

    public static final IEntityDataType<Byte> BYTE_TYPE = define("byte",
            PacketWrapper::readByte, PacketWrapper::writeByte);
    public static final IEntityDataType<Integer> INT_TYPE = define("int",
            EntityDataTypes::readVersionedVarInt, EntityDataTypes::writeVersionedVarInt);
    /**
     * Added with 1.19.3
     */
    public static final IEntityDataType<Long> LONG_TYPE = define("int",
            PacketWrapper::readVarLong, PacketWrapper::writeVarLong);
    public static final IEntityDataType<Float> FLOAT_TYPE = define("float",
            PacketWrapper::readFloat, PacketWrapper::writeFloat);
    public static final IEntityDataType<String> STRING_TYPE = define("string",
            PacketWrapper::readString, PacketWrapper::writeString);
    public static final IEntityDataType<Component> COMPONENT_TYPE = define("component",
            PacketWrapper::readComponent, PacketWrapper::writeComponent);
    public static final IEntityDataType<Optional<Component>> OPTIONAL_COMPONENT_TYPE = define("optional_component",
            EntityDataTypes::readOptionalComponent, EntityDataTypes::writeOptionalComponent);
    public static final IEntityDataType<ItemStack> ITEM_STACK_TYPE = define("item_stack",
            PacketWrapper::readItemStack, PacketWrapper::writeItemStack);
    /**
     * Added with 1.9
     */
    public static final IEntityDataType<Boolean> BOOLEAN_TYPE = define("boolean",
            PacketWrapper::readBoolean, PacketWrapper::writeBoolean);
    public static final IEntityDataType<Vector3f> ROTATIONS_TYPE = define("rotations",
            Vector3f::read, Vector3f::write);
    public static final IEntityDataType<Vector3i> BLOCK_POS_TYPE = define("block_pos",
            EntityDataTypes::readVersionedBlockPosition, EntityDataTypes::writeVersionedBlockPosition);
    public static final IEntityDataType<Optional<Vector3i>> OPTIONAL_BLOCK_POS_TYPE = define("optional_block_pos",
            EntityDataTypes::readOptionalVersionedBlockPosition, EntityDataTypes::writeOptionalVersionedBlockPosition);
    public static final IEntityDataType<BlockFace> DIRECTION_TYPE = define("direction",
            wrapper -> BlockFace.getBlockFaceByValue(wrapper.readVarInt()), PacketWrapper::writeEnum);
    public static final IEntityDataType<Optional<UUID>> OPTIONAL_UUID_TYPE = define("optional_uuid") // TODO continue

    /**
     * Removed in 1.9
     */
    @ApiStatus.Obsolete
    public static final IEntityDataType<Short> SHORT_TYPE = define("short", PacketWrapper::readShort, PacketWrapper::writeShort);

    private static final GsonComponentSerializer GSON_SERIALIZER = GsonComponentSerializer.gson();
    @Deprecated
    public static final EntityDataType<Byte> BYTE = BYTE_TYPE.asLegacy();
    @Deprecated
    public static final EntityDataType<Integer> INT = INT_TYPE.asLegacy();
    @Deprecated
    public static final EntityDataType<Short> SHORT = SHORT_TYPE.asLegacy();
    @Deprecated
    public static final EntityDataType<Long> LONG = LONG_TYPE.asLegacy();
    @Deprecated
    public static final EntityDataType<Float> FLOAT = FLOAT_TYPE.asLegacy();
    @Deprecated
    public static final EntityDataType<String> STRING = STRING_TYPE.asLegacy();
    @Deprecated
    public static final EntityDataType<String> COMPONENT = COMPONENT_TYPE.map(
            GSON_SERIALIZER::serialize, GSON_SERIALIZER::deserialize).asLegacy();
    @Deprecated
    public static final EntityDataType<Component> ADV_COMPONENT = COMPONENT_TYPE.asLegacy();
    @Deprecated
    public static final EntityDataType<Optional<String>> OPTIONAL_COMPONENT = OPTIONAL_COMPONENT_TYPE.map(
            comp -> comp.map(GSON_SERIALIZER::serialize),
            string -> string.map(GSON_SERIALIZER::deserialize)).asLegacy();
    @Deprecated
    public static final EntityDataType<Optional<Component>> OPTIONAL_ADV_COMPONENT = OPTIONAL_COMPONENT_TYPE.asLegacy();
    @Deprecated
    public static final EntityDataType<ItemStack> ITEMSTACK = ITEM_STACK_TYPE.asLegacy();
    @Deprecated
    public static final EntityDataType<Optional<ItemStack>> OPTIONAL_ITEMSTACK = ITEM_STACK_TYPE.map(
            Optional::of, opt -> opt.orElse(ItemStack.EMPTY)).asLegacy();
    @Deprecated
    public static final EntityDataType<Boolean> BOOLEAN = BOOLEAN_TYPE.asLegacy();
    @Deprecated
    public static final EntityDataType<Vector3f> ROTATION = ROTATIONS_TYPE.asLegacy();
    @Deprecated
    public static final EntityDataType<Vector3i> BLOCK_POSITION = BLOCK_POS_TYPE.asLegacy();
    @Deprecated
    public static final EntityDataType<Optional<Vector3i>> OPTIONAL_BLOCK_POSITION = OPTIONAL_BLOCK_POS_TYPE.asLegacy();
    @Deprecated
    public static final EntityDataType<BlockFace> BLOCK_FACE = DIRECTION_TYPE.asLegacy();

    public static final EntityDataType<Optional<UUID>> OPTIONAL_UUID = define("optional_uuid",
            (PacketWrapper<?> wrapper) -> Optional.ofNullable(wrapper.readOptional(PacketWrapper::readUUID)),
            (PacketWrapper<?> wrapper, Optional<UUID> value) ->
                    wrapper.writeOptional(value.orElse(null), PacketWrapper::writeUUID));

    public static final EntityDataType<Integer> BLOCK_STATE = define("block_state",
            readVersionedVarInt(), buildVarIntWriter());

    public static final EntityDataType<Integer> OPTIONAL_BLOCK_STATE = define("optional_block_state", readVersionedVarInt(), buildVarIntWriter());

    public static final EntityDataType<NBTCompound> NBT = define("nbt", PacketWrapper::readNBT, PacketWrapper::writeNBT);

    public static final EntityDataType<Particle<?>> PARTICLE = define("particle", Particle::read, Particle::write);

    public static final EntityDataType<VillagerData> VILLAGER_DATA = define("villager_data", PacketWrapper::readVillagerData, PacketWrapper::writeVillagerData);

    public static final EntityDataType<Optional<Integer>> OPTIONAL_INT = define("optional_int", (PacketWrapper<?> wrapper) -> {
        int i = wrapper.readVarInt();
        return i == 0 ? Optional.empty() : Optional.of(i - 1);
    }, (PacketWrapper<?> wrapper, Optional<Integer> value) -> {
        wrapper.writeVarInt(value.orElse(-1) + 1);
    });

    public static final EntityDataType<EntityPose> ENTITY_POSE = define("entity_pose", (PacketWrapper<?> wrapper) -> {
        int id = wrapper.readVarInt();
        return EntityPose.getById(wrapper.getServerVersion().toClientVersion(), id);
    }, (PacketWrapper<?> wrapper, EntityPose value) -> wrapper.writeVarInt(value.getId(wrapper.getServerVersion().toClientVersion())));

    public static final EntityDataType<Integer> CAT_VARIANT = define("cat_variant_type", readVersionedVarInt(), buildVarIntWriter());

    public static final EntityDataType<Integer> FROG_VARIANT = define("frog_variant_type", readVersionedVarInt(), buildVarIntWriter());

    public static final EntityDataType<Optional<WorldBlockPosition>> OPTIONAL_GLOBAL_POSITION = define("optional_global_position",
            (PacketWrapper<?> wrapper) -> Optional.ofNullable(wrapper.readOptional(w -> new WorldBlockPosition(new ResourceLocation(w.readString(32767)), w.readBlockPosition()))),
            (PacketWrapper<?> wrapper, Optional<WorldBlockPosition> value) -> wrapper.writeOptional(value.orElse(null), (w, globalPos) -> {
                w.writeString(globalPos.getWorld().toString());
                w.writeBlockPosition(globalPos.getBlockPosition());
            }));

    public static final EntityDataType<Integer> PAINTING_VARIANT_TYPE = define("painting_variant_type", readVersionedVarInt(), buildVarIntWriter());

    public static final EntityDataType<SnifferState> SNIFFER_STATE = define("sniffer_state", (PacketWrapper<?> wrapper) -> {
        int id = wrapper.readVarInt();
        return SnifferState.values()[id];
    }, (PacketWrapper<?> wrapper, SnifferState value) -> wrapper.writeVarInt(value.ordinal()));

    public static final EntityDataType<Vector3f> VECTOR3F = define("vector3f",
            (PacketWrapper<?> wrapper) -> new Vector3f(wrapper.readFloat(), wrapper.readFloat(), wrapper.readFloat()),
            (PacketWrapper<?> wrapper, Vector3f value) -> {
                wrapper.writeFloat(value.x);
                wrapper.writeFloat(value.y);
                wrapper.writeFloat(value.z);
            });

    public static final EntityDataType<Quaternion4f> QUATERNION = define("quaternion",
            (PacketWrapper<?> wrapper) -> new Quaternion4f(wrapper.readFloat(), wrapper.readFloat(), wrapper.readFloat(), wrapper.readFloat()),
            (PacketWrapper<?> wrapper, Quaternion4f value) -> {
                wrapper.writeFloat(value.getX());
                wrapper.writeFloat(value.getY());
                wrapper.writeFloat(value.getZ());
                wrapper.writeFloat(value.getW());
            });

    // Added in 1.20.5
    public static final EntityDataType<ArmadilloState> ARMADILLO_STATE = define("armadillo_state",
            (PacketWrapper<?> wrapper) -> ArmadilloState.values()[wrapper.readVarInt()],
            (PacketWrapper<?> wrapper, ArmadilloState value) -> wrapper.writeVarInt(value.ordinal())
    );

    public static final EntityDataType<List<Particle<?>>> PARTICLES = define("particles",
            wrapper -> wrapper.readList(Particle::read),
            (wrapper, particles) -> wrapper.writeList(particles, Particle::write)
    );

    public static final EntityDataType<Integer> WOLF_VARIANT =
            define("wolf_variant_type", readVersionedVarInt(), buildVarIntWriter());

    @Deprecated
    private static final List<EntityDataType<?>> LEGACY_ENTRIES = REGISTRY.getEntries().stream()
            .map(IEntityDataType::asLegacy).collect(Collectors.toUnmodifiableList());

    public static VersionedRegistry<IEntityDataType<?>> getRegistry() {
        return REGISTRY;
    }

    /**
     * Returns an immutable view of the entity-data types.
     *
     * @return Entity-Data Types
     */
    @Deprecated
    public static Collection<EntityDataType<?>> values() {
        return LEGACY_ENTRIES;
    }

    @Deprecated
    public static @Nullable EntityDataType<?> getById(ClientVersion version, int id) {
        IEntityDataType<?> type = REGISTRY.getById(version, id);
        return type == null ? null : type.asLegacy();
    }

    @Deprecated
    public static @Nullable EntityDataType<?> getByName(String name) {
        IEntityDataType<?> type = REGISTRY.getByName(name);
        return type == null ? null : type.asLegacy();
    }

    private static <T> IEntityDataType<T> define(
            String name, PacketWrapper.Reader<T> reader, PacketWrapper.Writer<T> writer) {
        return REGISTRY.define(name, data ->
                new EntityDataTypeImpl<>(data, reader, writer));
    }

    private static int readVersionedVarInt(PacketWrapper<?> wrapper) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            return wrapper.readVarInt();
        }
        return wrapper.readInt();
    }

    private static void writeVersionedVarInt(PacketWrapper<?> wrapper, Number number) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            wrapper.writeVarInt(number.intValue());
        } else {
            wrapper.writeInt(number.intValue());
        }
    }

    @Deprecated
    private static Optional<String> readOptionalComponentJson(PacketWrapper<?> wrapper) {
        if (wrapper.readBoolean()) {
            return Optional.of(wrapper.readComponentJSON());
        } else {
            return Optional.empty();
        }
    }

    @Deprecated
    private static void writeOptionalComponentJson(PacketWrapper<?> wrapper, Optional<String> componentJson) {
        if (componentJson != null && componentJson.isPresent()) {
            wrapper.writeBoolean(true);
            wrapper.writeComponentJSON(componentJson.get());
        } else {
            wrapper.writeBoolean(false);
        }
    }

    private static Optional<Component> readOptionalComponent(PacketWrapper<?> wrapper) {
        if (wrapper.readBoolean()) {
            return Optional.of(wrapper.readComponent());
        }
        return Optional.empty();
    }

    private static void writeOptionalComponent(PacketWrapper<?> wrapper, Optional<Component> component) {
        if (component != null && component.isPresent()) {
            wrapper.writeBoolean(true);
            wrapper.writeComponent(component.get());
        } else {
            wrapper.writeBoolean(false);
        }
    }

    private static Vector3i readVersionedBlockPosition(PacketWrapper<?> wrapper) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            return wrapper.readBlockPosition();
        } else {
            return new Vector3i(wrapper.readInt(), wrapper.readInt(), wrapper.readInt());
        }
    }

    private static void writeVersionedBlockPosition(PacketWrapper<?> wrapper, Vector3i vector) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            wrapper.writeBlockPosition(vector);
        } else {
            wrapper.writeInt(vector.x);
            wrapper.writeInt(vector.y);
            wrapper.writeInt(vector.z);
        }
    }

    private static Optional<Vector3i> readOptionalVersionedBlockPosition(PacketWrapper<?> wrapper) {
        if (!wrapper.readBoolean()) {
            return Optional.empty();
        }
        return Optional.of(readVersionedBlockPosition(wrapper));
    }

    private static void writeOptionalVersionedBlockPosition(PacketWrapper<?> wrapper, Optional<Vector3i> value) {
        if (value == null || !value.isPresent()) {
            wrapper.writeBoolean(false);
        } else {
            wrapper.writeBoolean(true);
            writeVersionedBlockPosition(wrapper, value.get());
        }
    }

    static {
        REGISTRY.unloadMappings();
    }
}
