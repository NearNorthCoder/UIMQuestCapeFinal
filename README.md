# OSRS Botting Agent for RuneLite (Injection Method)

This project is a Java agent that injects into the RuneLite client at startup, providing the foundation for botting, scripting, antiban, and plugin/module support using the injection method.

## Getting Started

1. **Build the Agent:**

```bash
mvn package
```
This will produce a runnable agent JAR in `target/osrsbot-agent-1.0-SNAPSHOT-shaded.jar`.

2. **Download RuneLite:**

Download the official RuneLite JAR from https://runelite.net/.

3. **Launch RuneLite with the Agent:**

```bash
java -javaagent:/full/path/to/osrsbot-agent-1.0-SNAPSHOT-shaded.jar -jar /full/path/to/RuneLite.jar
```

You should see `[OSRSBot] Agent loaded successfully! Attaching to RuneLite...` and confirmation when the RuneLite main class loads.

## Features

- Modular plugin and script architecture
- Advanced antiban routines (random movement, camera, idle, and break simulation)
- Dynamic script loading from the `/scripts` directory
- Live command console for starting/stopping scripts and modules
- Comprehensive debugging/logging system
- All core API hooks use real reflection (no stubs or placeholders)

## Usage Examples

- Use the command console to list, start, and stop scripts and modules at runtime.
- Add your own scripts by placing compiled `.class` files in the `./scripts` directory.

## Notes

- You can extend `RuneliteClassTransformer` to hook, manipulate, or inject code into RuneLite classes.
- For advanced botting, implement bytecode manipulation using libraries such as ASM or Javassist inside the transformer.
- No placeholders or stubs: this client is designed for real RuneLite injection and extensibility.

## Security

- **Use at your own risk.** Botting may violate RuneScape's Terms of Service and can result in bans.