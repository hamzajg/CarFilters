# Gates: photo-notes

> Preconditions for phase transitions. Each `## G<n> — <title>` block is a
> gate. `ship_loop` refuses to advance past a phase while a blocking gate is
> pending. Definitions live here; status and evidence live in `LOOP.md`.
>
> Gate phases: `spec` (before writing the spec), `plan` (before planning
> waves), `wave:<n>` (before starting wave n), `validate` (before validation),
> `finish` (before closing the feature).
>
> Gate types: decision | artifact | external | technical | quality | data.
> Resolver: `human` (needs a person to decide/provide something) or `agent`
> (the agent can satisfy it). Check: `manual`, `file:<repo-relative-path>`, or
> `command:<exact command>` (the agent runs it and records the output as
> evidence — the oracle never executes it).

## G1 — On-device smoke test
- Before: finish
- Type: quality
- Owner: product-owner
- Resolver: human
- Blocking: yes
- Check: manual
- Why: All scenarios are UI/Room behaviour — JVM unit tests cannot cover
  Room or touch handling, and CI has no emulator. A human must install the
  debug APK, annotate a photo, save, re-open, edit and delete a note
  (EXAMPLES.feature @w1–@w3) before the feature is called done.
