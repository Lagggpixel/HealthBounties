package me.lagggpixel.healthbounties.modules.UUID;

import me.lagggpixel.healthbounties.Main;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UuidGenerator {

    @NotNull
    public static UUID generateVoucherUUID() {

        UUID uuid = null;

        while (uuid == null || Main.getInstance().getVoucherMap().containsKey(uuid)) {
            uuid = UUID.randomUUID();
        }
        return uuid;
    }

}
