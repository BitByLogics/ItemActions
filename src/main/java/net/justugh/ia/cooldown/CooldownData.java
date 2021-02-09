package net.justugh.ia.cooldown;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Getter
@Setter
public class CooldownData {

    private final UUID owner;
    private final String key;
    private final CompletableFuture<Void> finishTask;
    private final int length;

    private int taskID;

}
