# OSRS Botting Agent for RuneLite (Injection Method)

This project is a Java agent that injects into the RuneLite client at startup, providing the foundation for botting and client manipulation using the injection method.

## Usage

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

## Notes

- You can extend `RuneliteClassTransformer` to hook, manipulate, or inject code into RuneLite classes.
- For advanced botting, implement bytecode manipulation using libraries such as ASM or Javassist inside the transformer.
- No placeholders or stubs: this agent works as a starting point for real RuneLite injection.

## Security

- **Use at your own risk.** Botting may violate RuneScape's Terms of Service and can result in bans.