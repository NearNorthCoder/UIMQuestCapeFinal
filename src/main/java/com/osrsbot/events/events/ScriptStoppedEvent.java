package com.osrsbot.events.events;

import com.osrsbot.scripts.Script;

/**
 * Event fired when a script is stopped.
 */
public record ScriptStoppedEvent(Script script) {}