/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.item.component;

import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StaticComponentMap implements IComponentMap {

    public static final StaticComponentMap EMPTY = new StaticComponentMap(Collections.emptyMap());

    private final boolean empty;
    private final Map<ComponentType<?>, ?> delegate;

    public StaticComponentMap(Map<ComponentType<?>, ?> delegate) {
        this.empty = delegate.isEmpty();
        this.delegate = this.empty ? Collections.emptyMap()
                : Collections.unmodifiableMap(new HashMap<>(delegate));
    }

    @Override
    public boolean has(ComponentType<?> type) {
        return !this.empty && this.delegate.containsKey(type);
    }

    @SuppressWarnings("unchecked") // no
    @Override
    public <T> @Nullable T get(ComponentType<T> type) {
        return this.empty ? null : (T) this.delegate.get(type);
    }

    @Override
    public <T> void set(ComponentType<T> type, Optional<T> value) {
        throw new UnsupportedOperationException();
    }

    public Map<ComponentType<?>, ?> getDelegate() {
        return this.delegate;
    }
}