package net.aiirial.custommobgriefing;

import net.aiirial.custommobgriefing.command.CustomMobGriefingCommand;
import net.aiirial.custommobgriefing.events.CustomMobGriefingEvents;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod(CustomMobGriefing.MOD_ID)
public class CustomMobGriefing {

    public static final String MOD_ID = "custommobgriefing";

    public CustomMobGriefing() {
        NeoForge.EVENT_BUS.register(CustomMobGriefingEvents.class);
        NeoForge.EVENT_BUS.addListener(this::onRegisterCommands);
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        CustomMobGriefingCommand.register(event);
    }
}
