package net.aiirial.custommobgriefing.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.aiirial.custommobgriefing.config.CustomMobGriefingConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.Set;

public class CustomMobGriefingCommand {

    private static final Set<String> VALID_MOBS = Set.of(
            "creeper",
            "wither",
            "ender_dragon",
            "ghast",
            "enderman",
            "ravager",
            "silverfish",
            "zombie"
    );

    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(
                Commands.literal("custommobgriefing")
                        .requires(Commands.hasPermission(Commands.LEVEL_ADMINS))

                        // /custommobgriefing list
                        .then(Commands.literal("list")
                                .executes(ctx -> {
                                    ctx.getSource().sendSuccess(
                                            () -> Component.literal("§6Custom MobGriefing Status:"),
                                            false
                                    );

                                    CustomMobGriefingConfig.getAllSettings().forEach((mob, allowed) -> {
                                        ctx.getSource().sendSuccess(
                                                () -> Component.literal(
                                                        " §7- §e" + mob + ": " +
                                                                (allowed ? "§aTRUE" : "§cFALSE")
                                                ),
                                                false
                                        );
                                    });
                                    return 1;
                                })
                        )

                        // /custommobgriefing <mob> <true|false>
                        .then(Commands.argument("mob", StringArgumentType.word())
                                .suggests((ctx, builder) -> {
                                    VALID_MOBS.forEach(builder::suggest);
                                    return builder.buildFuture();
                                })
                                .then(Commands.argument("allowed", BoolArgumentType.bool())
                                        .executes(ctx -> {
                                            String mob = StringArgumentType.getString(ctx, "mob").toLowerCase();
                                            boolean allowed = BoolArgumentType.getBool(ctx, "allowed");

                                            if (!VALID_MOBS.contains(mob)) {
                                                ctx.getSource().sendFailure(
                                                        Component.literal("Unbekannter Mob: " + mob)
                                                );
                                                return 0;
                                            }

                                            CustomMobGriefingConfig.setGriefingAllowed(mob, allowed);

                                            ctx.getSource().sendSuccess(
                                                    () -> Component.literal(
                                                            "MobGriefing für '" + mob + "' = " + allowed
                                                    ),
                                                    true
                                            );
                                            return 1;
                                        })
                                )
                        )
        );
    }

}

