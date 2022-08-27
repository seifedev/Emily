package tech.seife.emily;

import tech.seife.emily.commands.Details;

import java.util.HashSet;
import java.util.Set;

public class Commands {

    private final Set<Details> commands;

    public Commands() {
        commands = new HashSet<>();
    }

    public void addCommand(Details command) {
        commands.add(command);
    }

    public Set<Details> getCommands() {
        return Set.copyOf(commands);
    }
}
