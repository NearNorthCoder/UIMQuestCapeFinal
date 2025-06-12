package com.osrsbot.events.events;

import com.osrsbot.scripts.Script;

/**
 * Event fired when a script is started.
 */
public record ScriptStartedEvent(Script script) {}