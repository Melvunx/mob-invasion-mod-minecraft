package melvunx.mobinvasion.net.command;

import com.mojang.brigadier.CommandDispatcher;
import melvunx.mobinvasion.net.MobInvasion;
import melvunx.mobinvasion.net.portal.base.spawn.PortalSpawnManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class PortalCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("portal")
                        .requires(source -> source.hasPermission(2)) // OP niveau 2
                        .then(Commands.literal("spawn")
                                .executes(context -> {
                                    CommandSourceStack source = context.getSource();
                                    ServerLevel level = source.getLevel();

                                    // Position du joueur
                                    BlockPos pos = BlockPos.containing(source.getPosition());

                                    // Spawn le portail
                                    PortalSpawnManager manager = PortalSpawnManager.getInstance();
                                    manager.forceSpawnPortal(level, pos);

                                    source.sendSuccess(
                                            () -> Component.literal("§aPortail zombie spawné en " + pos),
                                            true
                                    );

                                    MobInvasion.LOGGER.info("Commande /portal spawn exécutée en {}", pos);
                                    return 1;
                                })
                        )
                        .then(Commands.literal("close")
                                .executes(context -> {
                                    CommandSourceStack source = context.getSource();
                                    ServerLevel level = source.getLevel();

                                    PortalSpawnManager manager = PortalSpawnManager.getInstance();
                                    int count = manager.closeAllPortals(level);

                                    source.sendSuccess(
                                            () -> Component.literal("§c" + count + " portail(s) fermé(s)"),
                                            true
                                    );

                                    return 1;
                                })
                        )
        );
    }
}
