package pl.kuezese.core.manager.impl;

import lombok.Getter;
import lombok.Setter;

public @Getter @Setter class ChatManager {

    private boolean enabled = true;
    private boolean onlyVip = true;
    private boolean requireLvl = false;
}
