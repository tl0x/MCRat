package com.version.a.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.net.Proxy;

@Mixin(MinecraftClient.class)
public interface MinecraftClientAccessor {

    @Accessor("session")
    Session getSession();

    @Accessor("networkProxy")
    Proxy getProxy();
}