package io.github.divios.jcommands;

import io.github.divios.jcommands.arguments.Argument;
import io.github.divios.jcommands.utils.Value;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class JCommand {

    private final String name;
    private final List<String> aliases = new ArrayList<>();
    private Permission permission = new Permission("");
    private Consumer<CommandSender> invalidPermission = player -> {};
    private final List<Argument> arguments = new ArrayList<>();
    private final List<JCommand> subCommands = new ArrayList<>();
    private String usage;
    private final List<Predicate<CommandSender>> requirements = new ArrayList<>();
    private BiConsumer<Player, List<Value>> playerExecutor = (player, args) -> {};
    private BiConsumer<ConsoleCommandSender, List<Value>> consoleExecutor = (sender, args) -> {};
    private BiConsumer<CommandSender, List<Value>> defaultExecutor = (commandSender, args) -> {};


    public static JCommand create(String name) {
        return new JCommand(name);
    }

    public JCommand(String name) {
        this.name = name;
        aliases.add(name);
    }

    public JCommand withAliases(String ...aliases) {
        this.aliases.addAll(Arrays.asList(aliases));
        return this;
    }

    public JCommand assertPermission(String permissionStr) {
        return assertPermission(new Permission(permissionStr));
    }

    public JCommand assertPermission(Permission permission) {
        return assertPermission(permission, sender -> {});
    }

    private JCommand assertPermission(String permissionStr, Consumer<CommandSender> invalidPermission) {
        return assertPermission(new Permission(permissionStr), invalidPermission);
    }

    private JCommand assertPermission(Permission permission, Consumer<CommandSender> invalidPermission) {
        this.permission = permission;
        this.invalidPermission = invalidPermission;
        return this;
    }

    public JCommand withArguments(Collection<? extends Argument> arguments) {
        this.arguments.addAll(arguments);
        return this;
    }

    public JCommand withArguments(Argument...arguments) {
        return withArguments(Arrays.asList(arguments));
    }

    public JCommand withSubcommands(Collection<? extends JCommand> commands) {
        this.subCommands.addAll(commands);
        return this;
    }

    public JCommand withSubcommands(JCommand ...commands) {
        return withSubcommands(Arrays.asList(commands));
    }

    public JCommand assertUsage(String usage) {
        this.usage = usage;
        return this;
    }

    public JCommand assertRequirements(Collection<Predicate<CommandSender>> requirements) {
        this.requirements.addAll(requirements);
        return this;
    }

    @SafeVarargs
    public final JCommand assertRequirements(Predicate<CommandSender>... requirements) {
        return assertRequirements(Arrays.asList(requirements));
    }

    public JCommand executesConsole(BiConsumer<ConsoleCommandSender, List<Value>> consoleExecutor) {
        this.consoleExecutor = consoleExecutor;
        return this;
    }

    public JCommand executesPlayer(BiConsumer<Player, List<Value>> playerExecutor) {
        this.playerExecutor = playerExecutor;
        return this;
    }

    public JCommand executes(BiConsumer<CommandSender, List<Value>> defaultExecutor) {
        this.defaultExecutor = defaultExecutor;
        return this;
    }

    public void register() {
        JCommands.registerCommand(this);
    }

    public String getName() {
        return name;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public Permission getPermission() {
        return permission;
    }

    public Consumer<CommandSender> getInvalidPermissionAction() {
        return invalidPermission == null ? sender -> {} : invalidPermission;
    }

    public List<Argument> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    public List<JCommand> getSubCommands() {
        return Collections.unmodifiableList(subCommands);
    }

    public String getUsage() {
        return usage;
    }

    public List<Predicate<CommandSender>> getRequirements() {
        return Collections.unmodifiableList(requirements);
    }

    public BiConsumer<Player, List<Value>> getPlayerExecutor() {
        return playerExecutor;
    }

    public BiConsumer<ConsoleCommandSender, List<Value>> getConsoleExecutor() {
        return consoleExecutor;
    }

    public BiConsumer<CommandSender, List<Value>> getDefaultExecutor() {
        return defaultExecutor;
    }
}