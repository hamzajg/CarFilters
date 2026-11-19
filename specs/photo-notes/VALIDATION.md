# Validation — photo-notes

## Traceability

| AC | Criterion | Scenarios | Wave(s) | Test evidence |
|----|-----------|-----------|---------|---------------|
| 1 | Saving the edit screen twice never duplicates note rows for… | Notes load before interaction, Save is idempotent | 1 | — |
| 2 | Existing notes load and render as markers without racing… | Notes load before interaction, Save is idempotent | 1 | — |
| 3 | Tapping a marker shows its text and offers edit/delete | Delete persists, Edit persists, Manage a note | 2 | — |
| 4 | Editing a note updates its text and survives reload | Delete persists, Edit persists, Manage a note | 2 | — |
| 5 | Deleting a note removes the marker and the DB row | Delete persists, Edit persists, Manage a note | 2 | — |
| 6 | Submitting an empty note is rejected | Empty note rejected | 3 | — |
| 7 | `sh gradlew test --no-daemon` + `assembleDebug` pass on JDK… | — | — | — |

## Scenario coverage

- Scenarios: 6 total, 0 with detected test evidence, 6 without
- ✗ `Save is idempotent` (@w1)
- ✗ `Notes load before interaction` (@w1)
- ✗ `Manage a note` (@w2)
- ✗ `Edit persists` (@w2)
- ✗ `Delete persists` (@w2)
- ✗ `Empty note rejected` (@w3)

## Demo script (manual walkthrough)

1. **Manage a note** — run the app, perform the scenario, observe the outcome.
2. **Edit persists** — run the app, perform the scenario, observe the outcome.
3. **Delete persists** — run the app, perform the scenario, observe the outcome.

## Gaps

- 6 scenario(s) lack detected test evidence — verify manually or add checks

Note: 'no evidence' means neither the scenario name nor a
token match was found in test files — verify by hand before
treating it as a real gap. '~' marks weak token-match evidence.

## Outcome follow-up

Optional; this is a post-release learning record, not a finish gate.
- Status: not yet measured
- Owner and review date: (record when known)
- Signal and observed result: (record after release; do not invent)
- Learning or next action: (record after review)